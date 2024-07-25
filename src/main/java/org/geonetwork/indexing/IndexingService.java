package org.geonetwork.indexing;

import static java.util.stream.Collectors.groupingBy;

import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkListener;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.Metadata;
import org.geonetwork.index.IndexClient;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.index.model.record.IndexRecords;
import org.geonetwork.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Indexing service. */
@Component
@Slf4j(topic = "org.geonetwork.tasks.indexing")
public class IndexingService {

  private final IndexClient indexClient;
  private final boolean isAsynchronousIndexing;
  private final int indexingChunkSize;

  private final EntityManager entityManager;

  private final MetadataRepository metadataRepository;
  private final IndexingRecordService indexingRecordService;

  private final ExecutorService executor;

  private BulkIngester<Object> bulkIngester;

  private ObjectMapper objectMapper = new ObjectMapper();

  /** Constructor. */
  public IndexingService(
      @Value("${geonetwork.indexing.asynchronous:'false'}") boolean isAsynchronousIndexing,
      @Value("${geonetwork.indexing.chunksize:'1000'}") int indexingChunkSize,
      @Value("${geonetwork.indexing.poolsize:'2'}") int poolSize,
      IndexingRecordService indexingRecordService,
      MetadataRepository metadataRepository,
      EntityManager entityManager,
      IndexClient indexClient) {
    this.isAsynchronousIndexing = isAsynchronousIndexing;
    this.indexingChunkSize = indexingChunkSize;

    this.indexingRecordService = indexingRecordService;
    this.metadataRepository = metadataRepository;
    this.entityManager = entityManager;
    this.executor = Executors.newFixedThreadPool(poolSize);
    this.indexClient = indexClient;

    setupBulkIngester();
  }

  private void setupBulkIngester() {
    if (!this.isAsynchronousIndexing) {
      return;
    }
    BulkListener<Object> bulkListener =
        new BulkListener<>() {
          @Override
          public void beforeBulk(long executionId, BulkRequest request, List<Object> objects) {}

          @Override
          public void afterBulk(
              long executionId, BulkRequest request, List<Object> objects, BulkResponse response) {
            log.info(
                String.format(
                    "Indexing operation took %d. Errors: %b.", response.took(), response.errors()));
          }

          @Override
          public void afterBulk(
              long executionId, BulkRequest request, List<Object> objects, Throwable failure) {
            log.info(
                String.format(
                    "Indexing operation with %d failures. %s.",
                    objects.size(), failure.getMessage()));
          }
        };
    this.bulkIngester =
        BulkIngester.of(
            b ->
                b.client(indexClient.getEsAsynchClient())
                    .listener(bulkListener)
                    .flushInterval(10, TimeUnit.SECONDS)
                    .maxOperations(indexingChunkSize));
  }

  /** Index a list of records. */
  @Transactional(readOnly = true)
  public void index(List<String> uuids) {
    AtomicInteger counter = new AtomicInteger(0);

    Sort sortBy =
        Sort.by(Sort.Direction.ASC, "schemaid").and(Sort.by(Sort.Direction.DESC, "changedate"));

    long nbRecords = uuids == null ? metadataRepository.count() : uuids.size();

    try (Stream<Metadata> metadataStream =
        uuids == null
            ? metadataRepository.streamAllBy(sortBy)
            : metadataRepository.streamAllByUuidIn(uuids, sortBy)) {

      metadataStream
          .collect(
              groupingBy(
                  x ->
                      nbRecords < indexingChunkSize
                          ? 0L
                          : counter.getAndIncrement() / indexingChunkSize))
          .forEach(
              (k, m) -> {
                executor.submit(
                    () -> {
                      log.atInfo().log(
                          "Indexing chunk #{} of {} over {} records",
                          k,
                          indexingChunkSize,
                          nbRecords);
                      m.stream()
                          .collect(groupingBy(Metadata::getSchemaid))
                          .forEach(
                              (schema, records) -> {
                                IndexRecords indexRecords =
                                    indexingRecordService.collectProperties(schema, m);
                                if (indexRecords != null
                                    && indexRecords.getIndexRecord() != null
                                    && !indexRecords.getIndexRecord().isEmpty()) {

                                  log.atDebug().log(
                                      "Indexing {} records for schema {}",
                                      indexRecords.getIndexRecord().size(),
                                      schema);
                                  // log.atDebug().log(
                                  //   objectMapper
                                  //     .writerWithDefaultPrettyPrinter()
                                  //     .writeValueAsString(indexRecords.getIndexRecord())
                                  // );
                                  sendToIndex(indexRecords);
                                  m.forEach(entityManager::detach);
                                }
                              });
                    });
              });
    }

    // TODO: What happens if records are deleted while indexing?
    //    if (uuids != null && uuids.size() != records.size()) {
    //      List<String> listOfUuids = records.stream().map(Metadata::getUuid).toList();
    //      List<String> ghost = new ArrayList<>(uuids);
    //      ghost.removeAll(listOfUuids);
    //      log.warn(
    //          String.format(
    //              "Error while retrieving records from database. "
    //                  + "%d record(s) missing. Records are %s."
    //                  + "Records may have been deleted since we started this indexing task.",
    //              ghost.size(), ghost.toString()));
    //      //      report.setNumberOfGhostRecords(ghost.size());
    //      //      e.getIn().setHeader("NUMBER_OF_GHOST", report.getNumberOfGhostRecords());
    //    }
  }

  /** 2 operations per record: delete and then index. */
  private void sendToIndex(IndexRecords indexRecords) {
    try {
      if (isAsynchronousIndexing) {
        for (IndexRecord indexRecord : indexRecords.getIndexRecord()) {
          bulkIngester.add(
              op ->
                  op.delete(
                      idx ->
                          idx.index(indexClient.getIndexRecordName()).id(indexRecord.getUuid())));
          bulkIngester.add(
              op ->
                  op.index(
                      idx ->
                          idx.index(indexClient.getIndexRecordName())
                              .id(indexRecord.getUuid())
                              .document(indexRecord)));
        }
      } else {
        BulkRequest.Builder br = new BulkRequest.Builder();

        for (IndexRecord indexRecord : indexRecords.getIndexRecord()) {
          br.operations(
              op ->
                  op.delete(
                      idx ->
                          idx.index(indexClient.getIndexRecordName()).id(indexRecord.getUuid())));
          br.operations(
              op ->
                  op.index(
                      idx ->
                          idx.index(indexClient.getIndexRecordName())
                              .id(indexRecord.getUuid())
                              .document(indexRecord)));
        }

        BulkResponse bulkItemResponses = indexClient.getEsClient().bulk(br.build());
        log.atInfo().log("Indexing operation took {}.", bulkItemResponses.took());
        if (bulkItemResponses.errors()) {
          AtomicInteger failureCount = new AtomicInteger();
          bulkItemResponses
              .items()
              .forEach(
                  item -> {
                    if (item.status() != 200 && item.status() != 201) {
                      failureCount.getAndIncrement();
                      // TODO: Index error document
                    }
                  });
          //        report.setNumberOfRecordsWithIndexingErrors(failureCount.intValue());
          log.atInfo().log("Indexing operation has failures {}.", failureCount);
        }
      }
    } catch (Exception esException) {
      log.atError()
          .log("Error while sending records to index. Error is: {}.", esException.getMessage());
    }
  }
}
