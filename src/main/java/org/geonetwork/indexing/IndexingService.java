/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.indexing;

import static java.util.stream.Collectors.groupingBy;

import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkListener;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
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
  private final String bulktimeout;

  private BulkIngester<Object> bulkIngester;

  /** Constructor. */
  public IndexingService(
      @Value("${geonetwork.indexing.asynchronous:'false'}") boolean isAsynchronousIndexing,
      @Value("${geonetwork.indexing.chunksize:'1000'}") int indexingChunkSize,
      @Value("${geonetwork.indexing.poolsize:'2'}") int poolSize,
      @Value("${geonetwork.indexing.bulktimeout:'45s'}") String bulktimeout,
      IndexingRecordService indexingRecordService,
      MetadataRepository metadataRepository,
      EntityManager entityManager,
      IndexClient indexClient) {
    this.isAsynchronousIndexing = isAsynchronousIndexing;
    this.indexingChunkSize = indexingChunkSize;
    this.bulktimeout = bulktimeout;

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
                    .globalSettings(s -> s.timeout(Time.of(builder -> builder.time(bulktimeout))))
                    .listener(bulkListener)
                    .flushInterval(10, TimeUnit.SECONDS)
                    .maxOperations(indexingChunkSize));
  }

  /** Index a list of records. */
  @Transactional(readOnly = true)
  public List<Future<?>> index(List<String> uuids) {
    AtomicInteger counter = new AtomicInteger(0);
    List<Future<?>> futures = new ArrayList<>();

    Sort sortBy =
        Sort.by(Sort.Direction.ASC, "schemaid").and(Sort.by(Sort.Direction.DESC, "changedate"));

    long nbRecords = uuids == null ? metadataRepository.count() : uuids.size();
    long nbOfChunck = nbRecords / indexingChunkSize;

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
                var submission =
                    executor.submit(
                        () -> {
                          log.atInfo().log(
                              "Indexing chunk #{}/{} of size {} over {} records",
                              k,
                              nbOfChunck,
                              indexingChunkSize,
                              nbRecords);
                          m.stream()
                              .collect(groupingBy(Metadata::getSchemaid))
                              .forEach(
                                  (schema, recordsForSchema) -> {
                                    IndexRecords indexRecords =
                                        indexingRecordService.collectProperties(
                                            schema, recordsForSchema);
                                    if (indexRecords != null
                                        && indexRecords.getIndexRecord() != null
                                        && !indexRecords.getIndexRecord().isEmpty()) {

                                      log.atDebug().log(
                                          "Indexing {} records for schema {}",
                                          recordsForSchema.size(),
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
                futures.add(submission);
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
    return futures;
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

        BulkResponse bulkItemResponses =
            indexClient
                .getEsClient()
                .bulk(br.timeout(Time.of(builder -> builder.time(bulktimeout))).build());
        log.atDebug().log(
            "Indexing {} documents in {}ms.",
            indexRecords.getIndexRecord().size(),
            bulkItemResponses.took());
        if (bulkItemResponses.errors()) {
          AtomicInteger failureCount = new AtomicInteger();
          bulkItemResponses
              .items()
              .forEach(
                  item -> {
                    if (item.status() != 200 && item.status() != 201) {
                      failureCount.getAndIncrement();
                      log.atError()
                          .log("{} (status {}). {}", item.id(), item.status(), item.result());
                      // TODO: Index error document
                    }
                  });
          //        report.setNumberOfRecordsWithIndexingErrors(failureCount.intValue());
          log.atError()
              .log(
                  "Indexing operation has failures {}. Records are {}",
                  failureCount,
                  indexRecords.getIndexRecord().stream()
                      .map(IndexRecord::getUuid)
                      .map(Objects::toString)
                      .collect(Collectors.joining(",")));
        }
      }
    } catch (Exception esException) {
      log.atError()
          .log(
              "Error while sending records to index. Error is: {}. If error is of type \"30,000"
                  + " milliseconds timeout on connection\" then reduce chunk size or increase"
                  + " timeout. Records are: {}",
              esException.getMessage(),
              indexRecords.getIndexRecord().stream()
                  .map(IndexRecord::getUuid)
                  .map(Objects::toString)
                  .collect(Collectors.joining(",")));
    }
  }
}
