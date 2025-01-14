/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * simple configuration for searching in ogcapi-records. From gn-microservices.
 */
@Configuration
@ConfigurationProperties(prefix = "geonetwork.openapi-records.search")
public class OgcApiSearchConfiguration {

  String queryFilter;

  String queryBase;

  Boolean trackTotalHits;

  List<String> sources = new ArrayList<>();

  public String getQueryFilter() {
    return queryFilter;
  }

  public void setQueryFilter(String queryFilter) {
    this.queryFilter = queryFilter;
  }

  public String getQueryBase() {
    return queryBase;
  }

  public void setQueryBase(String queryBase) {
    this.queryBase = queryBase;
  }

  public Boolean getTrackTotalHits() {
    return trackTotalHits;
  }

  public void setTrackTotalHits(Boolean trackTotalHits) {
    this.trackTotalHits = trackTotalHits;
  }

  public List<String> getSources() {
    return sources == null ? null : Collections.unmodifiableList(sources);
  }

  public void setSources(List<String> sources) {
    this.sources = sources == null ? null : Collections.unmodifiableList(sources);
  }
}
