/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
/**
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.querybuilder;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an ogcapi records query.
 *
 * <p>see https://docs.ogc.org/DRAFTS/20-004.htm
 */
@Getter
@Setter
public class OgcApiQuery {

  /**
   * A bounding box. If the spatial extent of the record intersects the specified bounding box, then the record shall
   * be presented in the response document.
   *
   * <p>The bounding box SHALL be provided as four or six numbers, depending on whether the coordinate reference
   * system includes a vertical axis (height or depth).
   *
   * <p>If the bounding box consists of four numbers, the coordinate reference system of the values SHALL be
   * interpreted as WGS 84 longitude/latitude (http://www.opengis.net/def/crs/OGC/1.3/CRS84) unless a different
   * coordinate reference system is specified in a parameter bbox-crs.
   *
   * <p>If the bounding box consists of six numbers, the coordinate reference system of the values SHALL be
   * interpreted as WGS 84 longitude/latitude/ellipsoidal height (http://www.opengis.net/def/crs/OGC/0/CRS84h) unless
   * a different coordinate reference system is specified in a parameter bbox-crs.
   */
  private List<Double> bbox;

  /**
   * CRS of the bbox.
   *
   * <p>NOTE: spec does NOT define this field, but it is implied (cf bbox).
   */
  private String bboxCrs;

  /**
   * A time instance or time period. If the temporal extent of the record intersects the specified date/time value,
   * then the record shall be presented in the response document.
   */
  private String datetime;

  /**
   * The number of records to be presented in a response document.
   *
   * <p>1-10,000 default=10 (spec says this may change)
   *
   * <p>If the value of the limit parameter is larger than the maximum value, this SHALL NOT result in an error
   * (instead use the maximum as the parameter value).
   *
   * <p>Only items are counted that are on the first level of the collection. Any nested objects contained within the
   * explicitly requested items SHALL not be counted.
   */
  private Integer limit = 10;

  /**
   * This is not in the specification, however, its implied in the examples and needed for paging.
   */
  private Integer startIndex = 0;

  /**
   * A comma-separated list of search terms. If any server-chosen text field in the record contains 1 or more of the
   * terms listed, then this record shall appear in the response set.
   *
   * <p>Search terms that may appear together (logical OR) in a record SHALL be separated by literal commas.
   *
   * <p>Search terms that must appear together, and in the order specified, in a record SHALL be separated by one or
   * more white space characters.
   *
   * <p>Keyword searches using the q parameter SHALL be case insensitive.
   *
   * <p>The specific set of text keys/fields/properties of a record to which the q operator is applied SHALL be left
   * to the discretion of the implementation.
   */
  @SuppressWarnings("checkstyle:membername") // single letter var name defined by OGCAPI query
  private List<String> q;

  /**
   * An equality predicate consisting of a comma-separated list of resource types. Only records of the listed type
   * shall appear in the response set.
   *
   * <p>The definition of the type parameter SHOULD be extended to enumerate the list of known record or resource
   * types.
   */
  private List<String> type;

  /**
   * An equality predicate consisting of a comma-separated list of record identifiers. Only records with the specified
   * identifiers shall appear in the response set.
   */
  private List<String> ids;

  /**
   * An equality predicate consisting of a comma-separated list of external resource identifiers. Only records with
   * the specified external identifiers shall appear in the response set.
   *
   * <p>pattern: ([^:]+:)?[^:]+
   *
   * <p>If the search value is qualified with a scheme then both the scheme and the value of the record’s external
   * identifier SHALL match in order for the record to be in the result set.
   *
   * <p>If the search value is not qualified with a scheme then only the value of the record’s external identifier
   * SHALL match in order for the record to be in the result set.
   *
   * <p>If the search value is only the scheme then the scheme component of the record’s external identified SHALL
   * match in order for the record to be in the result set.
   */
  private List<String> externalIds;

  /**
   * Equality predicates with any queryable not already listed in this table.
   *
   * <p>Implied is that the keys MUST BE listed in the Queryables
   */
  private Map<String, String> propValues;

  /**
   * This is NOT part of the ogcapi query specification. But, it is implied because you must do a query against a
   * collection.
   */
  private String collectionId;

  /**
   * How to order the results.
   *
   * <p>This is not in the query spec - cf. sorting
   */
  private List<String> sortBy;

  // ---------------------------------------------------------

  public List<String> getSortBy() {
    return sortBy == null ? null : Collections.unmodifiableList(sortBy);
  }

  public void setSortBy(List<String> sortBy) {
    this.sortBy = sortBy == null ? null : Collections.unmodifiableList(sortBy);
  }

  public List<Double> getBbox() {
    return bbox == null ? null : Collections.unmodifiableList(bbox);
  }

  public void setBbox(List<Double> bbox) {
    this.bbox = bbox == null ? null : Collections.unmodifiableList(bbox);
  }

  public List<String> getQ() {
    return q == null ? null : Collections.unmodifiableList(q);
  }

  public void setQ(List<String> q) {
    this.q = q == null ? null : Collections.unmodifiableList(q);
  }

  public List<String> getType() {
    return type == null ? null : Collections.unmodifiableList(type);
  }

  public void setType(List<String> type) {
    this.type = type == null ? null : Collections.unmodifiableList(type);
  }

  public List<String> getIds() {
    return ids == null ? null : Collections.unmodifiableList(ids);
  }

  public void setIds(List<String> ids) {
    this.ids = ids == null ? null : Collections.unmodifiableList(ids);
  }

  public List<String> getExternalIds() {
    return externalIds == null ? null : Collections.unmodifiableList(externalIds);
  }

  public void setExternalIds(List<String> externalIds) {
    this.externalIds = externalIds == null ? null : Collections.unmodifiableList(externalIds);
  }

  public void addProp(String key, String value) {
    if (propValues == null) {
      propValues = new HashMap<>();
    }
    propValues.put(key, value);
  }

  public Map<String, String> getPropValues() {
    return propValues == null ? null : new HashMap<>(propValues);
  }

  public void setPropValues(Map<String, String> propValues) {
    this.propValues = propValues == null ? null : new HashMap<>(propValues);
  }
}
