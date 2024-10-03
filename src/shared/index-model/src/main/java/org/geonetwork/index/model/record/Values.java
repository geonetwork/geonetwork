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
import lombok.Data;

/** Values. */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Values {

  @XmlElement private final List<String> values = new ArrayList<>();
  @XmlElement private String key;
}
