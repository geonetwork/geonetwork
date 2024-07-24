package org.geonetwork.indexing;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import org.geonetwork.index.IndexClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** Indexing controller. */
@RestController
@RequestMapping("/api/indexing")
public class IndexingController {

  private final IndexClient client;
  private final Counter indexingCounter;
  private final IndexingService indexingService;
  private final IndexClient indexClient;

  /** Constructor. */
  @Autowired
  public IndexingController(
      IndexClient client,
      IndexingService indexingService,
      MeterRegistry meterRegistry,
      IndexClient indexClient) {
    this.client = client;
    this.indexingService = indexingService;
    indexingCounter = meterRegistry.counter("gn.indexing.count");
    this.indexClient = indexClient;
  }

  /** Create index. */
  @GetMapping(path = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
  public String createIndex() {

    indexClient.createIndex(true);
    return "Indexing";
  }

  /** Index all records. */
  @GetMapping(path = "/indexAll", produces = MediaType.APPLICATION_JSON_VALUE)
  public String indexRecords(@RequestParam(defaultValue = "", required = false) String[] uuid) {

    indexingService.index(uuid.length > 0 ? List.of(uuid) : null);
    return "Indexing";
  }
}
