/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.index.model.record;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/** List of values. */
@XmlAccessorType(XmlAccessType.FIELD)
public class ListOfValues {

  @XmlElement private final List<Values> values = new ArrayList<>();

  public List<Values> getValues() {
    return values;
  }
}
