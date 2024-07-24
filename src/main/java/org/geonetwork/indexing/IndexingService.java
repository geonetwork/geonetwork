package org.geonetwork.indexing;

import static java.util.stream.Collectors.groupingBy;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.OP_PREFIX;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.VALID;

import co.elastic.clients.elasticsearch._helpers.bulk.BulkIngester;
import co.elastic.clients.elasticsearch._helpers.bulk.BulkListener;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.util.VisibleForTesting;
import jakarta.persistence.EntityManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.Group;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.MetadataValidationStatus;
import org.geonetwork.domain.Metadatastatus;
import org.geonetwork.domain.Operationallowed;
import org.geonetwork.domain.ReservedGroup;
import org.geonetwork.domain.ReservedOperation;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Validation;
import org.geonetwork.index.IndexClient;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.index.model.record.IndexRecordFieldNames;
import org.geonetwork.index.model.record.IndexRecords;
import org.geonetwork.repository.GroupRepository;
import org.geonetwork.repository.GufUserfeedbackRepository;
import org.geonetwork.repository.MetadataDraftRepository;
import org.geonetwork.repository.MetadataRepository;
import org.geonetwork.repository.MetadatacategRepository;
import org.geonetwork.repository.MetadatastatusRepository;
import org.geonetwork.repository.OperationallowedRepository;
import org.geonetwork.repository.UserRepository;
import org.geonetwork.repository.UsersavedselectionRepository;
import org.geonetwork.repository.ValidationRepository;
import org.geonetwork.utility.xml.XsltUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Indexing service. */
@Component
@Slf4j(topic = "org.fao.geonet.indexing.tasks")
public class IndexingService {

  private final MetadataRepository metadataRepository;
  private final MetadataDraftRepository metadataDraftRepository;
  private final GufUserfeedbackRepository userfeedbackRepository;
  private final UserRepository userRepository;
  private final MetadatastatusRepository metadatastatusRepository;
  private final GroupRepository groupRepository;
  private final OperationallowedRepository operationallowedRepository;
  private final MetadatacategRepository metadatacategRepository;
  private final ValidationRepository validationRepository;
  private final UsersavedselectionRepository usersavedselectionRepository;

  private final EntityManager entityManager;

  private final IndexClient indexClient;
  private final boolean isPreferringGroupLogo;
  private final boolean isAsynchronousIndexing;
  private final int indexingChunkSize;

  private ExecutorService executor;
  private BulkIngester<Object> bulkIngester;

  /** Constructor. */
  public IndexingService(
      @Value("${geonetwork.settings.system.metadata.prefergrouplogo:'true'}")
          boolean isPreferringGroupLogo,
      @Value("${geonetwork.indexing.asynchronous:'false'}") boolean isAsynchronousIndexing,
      @Value("${geonetwork.indexing.chunksize:'1000'}") int indexingChunkSize,
      @Value("${geonetwork.indexing.poolsize:'2'}") int poolSize,
      MetadataRepository metadataRepository,
      MetadataDraftRepository metadataDraftRepository,
      UserRepository userRepository,
      GroupRepository groupRepository,
      OperationallowedRepository operationallowedRepository,
      MetadatacategRepository metadatacategRepository,
      GufUserfeedbackRepository userfeedbackRepository,
      ValidationRepository validationRepository,
      MetadatastatusRepository metadatastatusRepository,
      UsersavedselectionRepository usersavedselectionRepository,
      EntityManager entityManager,
      IndexClient indexClient) {
    this.isPreferringGroupLogo = isPreferringGroupLogo;
    this.isAsynchronousIndexing = isAsynchronousIndexing;
    this.indexingChunkSize = indexingChunkSize;

    this.executor = Executors.newFixedThreadPool(poolSize);

    this.metadataRepository = metadataRepository;
    this.metadataDraftRepository = metadataDraftRepository;
    this.userRepository = userRepository;
    this.groupRepository = groupRepository;
    this.operationallowedRepository = operationallowedRepository;
    this.metadatacategRepository = metadatacategRepository;
    this.userfeedbackRepository = userfeedbackRepository;
    this.validationRepository = validationRepository;
    this.metadatastatusRepository = metadatastatusRepository;
    this.usersavedselectionRepository = usersavedselectionRepository;

    this.entityManager = entityManager;

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
            log.info(String.format("Indexing operation took %d.", response.took()));
          }

          @Override
          public void afterBulk(
              long executionId, BulkRequest request, List<Object> objects, Throwable failure) {
            log.info(String.format("Indexing operation took %d.", failure.getMessage()));
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

    AtomicInteger counter = new AtomicInteger();

    Sort sortBy =
        Sort.by(Sort.Direction.ASC, "schemaid").and(Sort.by(Sort.Direction.DESC, "changedate"));
    try (Stream<Metadata> metadataStream =
        uuids == null
            ? metadataRepository.streamAllBy(sortBy)
            : metadataRepository.streamAllByUuidIn(uuids, sortBy)) {

      metadataStream
          .collect(groupingBy(x -> counter.getAndIncrement() / indexingChunkSize))
          .forEach(
              (k, m) -> {
                executor.submit(
                    () -> {
                      log.info(String.format("Indexing chunk %s %s ", k, counter.get()));
                      IndexRecords indexRecords = collectProperties(m.getFirst().getSchemaid(), m);
                      if (indexRecords != null
                          && indexRecords.getIndexRecord() != null
                          && indexRecords.getIndexRecord().size() > 0) {

                        //              ObjectMapper objectMapper = new ObjectMapper();
                        //              try {
                        //                String jsonFromSerialization =
                        //                    objectMapper
                        //                        .writerWithDefaultPrettyPrinter()
                        //
                        // .writeValueAsString(indexRecords.getIndexRecord().getFirst());
                        //                log.info(jsonFromSerialization);
                        //              } catch (JsonProcessingException e) {
                        //                throw new RuntimeException(e);
                        //              }
                        sendToIndex(indexRecords);
                        m.forEach(entityManager::detach);
                      }
                    });
              });
    }

    //    log.info(String.format("Indexing %d records in batch", records.size()));
    //
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
    //
    //    Map<String, List<Metadata>> recordsBySchema =
    //        records.stream().collect(Collectors.groupingBy(record -> record.getSchemaid()));
    //
    //    recordsBySchema.forEach(
    //        (schema, schemaRecords) -> {
    //          log.info(String.format("Indexing %d records in schema %s", schemaRecords.size(),
    // schema));
    //          IndexRecords indexRecords = collectProperties(schema, schemaRecords);
    //          if (indexRecords != null
    //              && indexRecords.getIndexRecord() != null
    //              && indexRecords.getIndexRecord().size() > 0) {
    //            sendToIndex(indexRecords);
    //            //            log.info(indexRecords.toString());
    //
    //            //            ObjectMapper objectMapper = new ObjectMapper();
    //            //            try {
    //            //              String jsonFromSerialization =
    //            //                  objectMapper
    //            //                      .writerWithDefaultPrettyPrinter()
    //            //
    // .writeValueAsString(indexRecords.getIndexRecord().getFirst());
    //            //              log.info(jsonFromSerialization);
    //            //            } catch (JsonProcessingException e) {
    //            //              throw new RuntimeException(e);
    //            //            }
    //          }
    //        });
  }

  @VisibleForTesting
  protected IndexRecords collectProperties(String schema, List<Metadata> schemaRecords) {
    String indexingXsltFileName = String.format("xslt/indexing/%s.xsl", schema);
    try {
      File indexingXsltFile = new ClassPathResource(indexingXsltFileName).getFile();

      IndexRecords.IndexRecordsBuilder indexRecordsBuilder = IndexRecords.builder();

      schemaRecords.stream()
          .map(this::collectDbProperties)
          .forEach(indexRecordsBuilder::indexRecord);
      IndexRecords records = indexRecordsBuilder.build();

      //      System.out.println(
      //          XsltUtil.transformObjectToString(
      //              records, indexingXsltFile, IndexRecords.class, new HashMap<>()));

      return XsltUtil.transformObjectToObject(
          records, indexingXsltFile, IndexRecords.class, new HashMap<>());
    } catch (IOException ioException) {
      //      report.setNumberOfRecordsWithUnsupportedSchema(schemaRecords.size());
      log.error(
          String.format(
              "Schema %s used by records %s does not exist or does not provide indexing file %s.",
              schema,
              schemaRecords.stream()
                  .map(Metadata::getId)
                  .map(Objects::toString)
                  .collect(Collectors.joining(",")),
              indexingXsltFileName));
      //      if (!schemaManager.existsSchema(schema)) {
      //        fields.put(IndexFields.DRAFT, "n");
      //        fields.put(IndexFields.INDEXING_ERROR_FIELD, true);
      //        fields.put(IndexFields.INDEXING_ERROR_MSG,
      //          searchManager.createIndexingErrorMsgObject("indexingErrorMsg-schemaNotRegistered",
      //            "error",
      //            Map.of("record", metadataId, "schema", schema)));
      //        searchManager.index(null, md, indexKey, fields, metadataType,
      //          forceRefreshReaders, indexingMode);
      //        Log.error(Geonet.DATA_MANAGER, String.format(
      //          "Record %s / Schema '%s' is not registered in this catalog. Install it or remove
      // those records. Record is indexed indexing error flag.",
      //          metadataId, schema));
      //      }
    }
    return null;
  }

  /**
   * Initialize an {@link IndexRecord} with all properties from the database of an {@link Metadata}
   * and return its XML representation as string.
   */
  protected IndexRecord collectDbProperties(Metadata r) {
    IndexRecord.IndexRecordBuilder indexRecord =
        IndexRecord.builder()
            .id(r.getId())
            .uuid(r.getUuid())
            .documentStandard(r.getSchemaid())
            .isTemplate(r.getIstemplate().charAt(0))
            .harvested(r.getIsharvested().equals("y"))
            .popularity(r.getPopularity())
            .rating(r.getRating())
            .changeDate(r.getChangedate())
            .createDate(r.getCreatedate())
            .groupOwner(r.getGroupowner())
            .sourceCatalogue(r.getSource())
            .displayOrder(r.getDisplayorder())
            .document(r.getData())
            .draft("n");

    boolean hasDraft = metadataDraftRepository.existsByApprovedversion_Id(r.getId());
    if (hasDraft) {
      // TODO:
      //  indexKey += "-draft";
      //   draft field e exists / y is a draft
      log.info(String.format("Record %s has a draft. TODO", r.getId()));
    }

    setOwnerFields(r, indexRecord);
    setCategoryFields(r, indexRecord);
    setPrivilegesFields(r, indexRecord);
    setStatusFields(r, indexRecord);
    setPopularityFields(r, indexRecord);
    processValidationInfo(validationRepository.findAllById_metadataid(r.getId()), indexRecord);

    return indexRecord.build();
  }

  private void setPopularityFields(Metadata r, IndexRecord.IndexRecordBuilder indexRecord) {
    indexRecord.feedbackCount(this.userfeedbackRepository.countByMetadataUuid_Uuid(r.getUuid()));
    int preferredRecordSelectionId = 0;
    indexRecord.userSavedCount(
        this.usersavedselectionRepository.countByIdMetadatauuidAndIdSelectionid(
            r.getUuid(), preferredRecordSelectionId));
  }

  private void setStatusFields(Metadata r, IndexRecord.IndexRecordBuilder indexRecord) {
    Optional<Metadatastatus> lastWorkflowStatus =
        this.metadatastatusRepository.findFirstByMetadataidAndStatusidTypeOrderByChangedateDesc(
            r.getId(), "workflow");
    lastWorkflowStatus.ifPresent(
        metadatastatus ->
            indexRecord.lastWorkflowStatus(String.valueOf(metadatastatus.getStatusid().getId())));
  }

  /** Validation status -1 : not evaluated 0 : invalid 1 : valid. */
  private void processValidationInfo(
      List<Validation> validationInfo, IndexRecord.IndexRecordBuilder indexRecord) {
    if (validationInfo.isEmpty()) {
      indexRecord.valid(-1);
      return;
    }

    int isValid = determineValidationStatus(validationInfo);
    indexRecord.valid(isValid);

    Map<String, String> validationStatus = new HashMap<>();
    validationInfo.stream()
        .filter(v -> !v.getId().getValtype().equalsIgnoreCase(IndexRecordFieldNames.INSPIRE))
        .forEach(
            vi ->
                validationStatus.put(
                    VALID + "_" + vi.getId().getValtype(), String.valueOf(vi.getStatus())));
    indexRecord.validationByType(validationStatus);

    boolean hasInspireValidation = processInspireValidation(validationInfo, indexRecord);
    if (!hasInspireValidation) {
      indexRecord.inspireValid(-1);
    }
  }

  private int determineValidationStatus(List<Validation> validationInfo) {
    int isValid = 1;
    for (Validation vi : validationInfo) {
      if (!vi.getId().getValtype().equalsIgnoreCase(IndexRecordFieldNames.INSPIRE)) {
        if (vi.getStatus() == MetadataValidationStatus.NEVER_CALCULATED.getCode()
            && vi.getRequired()) {
          return -1;
        }
        if (vi.getStatus() == MetadataValidationStatus.INVALID.getCode() && vi.getRequired()) {
          isValid = 0;
        }
      }
    }
    return isValid;
  }

  private boolean processInspireValidation(
      List<Validation> validationInfo, IndexRecord.IndexRecordBuilder indexRecord) {
    for (Validation vi : validationInfo) {
      if (vi.getId().getValtype().equalsIgnoreCase(IndexRecordFieldNames.INSPIRE)) {
        indexRecord.inspireReportUrl(vi.getReporturl());
        indexRecord.inspireValidationDate(vi.getValdate());
        indexRecord.validation(
            VALID + "_" + vi.getId().getValtype(), String.valueOf(vi.getStatus()));
        return true;
      }
    }
    return false;
  }

  private void setPrivilegesFields(Metadata r, IndexRecord.IndexRecordBuilder indexRecord) {
    List<Operationallowed> operationsAllowed =
        operationallowedRepository.findAllByIdMetadataid(r.getId());
    boolean isPublishedToAll = false;
    boolean isPublishedToIntranet = false;
    boolean isPublishedToGuest = false;

    Map<String, ArrayList<Integer>> privileges = new HashMap<>();

    for (Operationallowed operationAllowed : operationsAllowed) {
      int groupId = operationAllowed.getId().getGroupid();
      int operationId = operationAllowed.getId().getOperationid();
      String fieldName = OP_PREFIX + operationId;

      ArrayList<Integer> operationField =
          privileges.computeIfAbsent(fieldName, k -> new ArrayList<>());
      operationField.add(groupId);

      if (operationId == ReservedOperation.view.getId()) {
        Optional<Group> g = groupRepository.findById(groupId);
        if (g.isPresent()) {
          indexRecord.groupPublished(g.get().getName());
          indexRecord.groupPublishedId(g.get().getId());

          if (g.get().getId() == ReservedGroup.all.getId()) {
            isPublishedToAll = true;
          } else if (g.get().getId() == ReservedGroup.intranet.getId()) {
            isPublishedToIntranet = true;
          } else if (g.get().getId() == ReservedGroup.guest.getId()) {
            isPublishedToGuest = true;
          }
        }
      }
    }
    indexRecord.operations(privileges);
    indexRecord.publishedToAll(isPublishedToAll);
    indexRecord.publishedToIntranet(isPublishedToIntranet);
    indexRecord.publishedToGuest(isPublishedToGuest);
  }

  private void setCategoryFields(Metadata r, IndexRecord.IndexRecordBuilder indexRecord) {
    this.metadatacategRepository
        .findAllByIdMetadataid(r.getId())
        .forEach(
            c -> {
              indexRecord.category(c.getCategoryid().getName());
            });
  }

  private void setOwnerFields(Metadata r, IndexRecord.IndexRecordBuilder indexRecord) {
    if (r.getOwner() != null) {
      Optional<User> userOpt = userRepository.findById(r.getOwner());
      if (userOpt.isPresent()) {
        User user = userOpt.get();
        indexRecord.userinfo(
            user.getUsername()
                + "|"
                + user.getSurname()
                + "|"
                + user.getName()
                + "|"
                + user.getProfile());
        indexRecord.recordOwner(user.getName() + " " + user.getSurname());
      }
    }
  }

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
        log.info(String.format("Indexing operation took %d.", bulkItemResponses.took()));
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
          log.info(String.format("Indexing operation has failures %d.", failureCount));
        }
      }
    } catch (Exception esException) {
      log.error(
          String.format(
              "Error while sending records to index. Error is: %s.", esException.getMessage()));
    }
  }
}
