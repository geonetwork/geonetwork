/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
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

  private final Counter indexingCounter;
  private final IndexingService indexingService;
  private final IndexClient indexClient;

  /** Constructor. */
  @Autowired
  public IndexingController(
      IndexingService indexingService, MeterRegistry meterRegistry, IndexClient indexClient) {
    this.indexingService = indexingService;
    indexingCounter = meterRegistry.counter("gn.indexing.count");
    this.indexClient = indexClient;
  }

  /** Create index. */
  @GetMapping(path = "/setup", produces = MediaType.APPLICATION_JSON_VALUE)
  public String setupIndex(@RequestParam(defaultValue = "true") boolean dropIfExists) {
    indexClient.setupIndex(dropIfExists);
    return "Index created.";
  }

  /** Index all records. */
  @GetMapping(path = "/index", produces = MediaType.APPLICATION_JSON_VALUE)
  public String indexRecords(@RequestParam(defaultValue = "", required = false) String[] uuid) {

    indexingService.index(uuid.length > 0 ? List.of(uuid) : null);
    return "Indexing task started.";
  }
}
