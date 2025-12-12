/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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

    @XmlElement
    private final List<String> values = new ArrayList<>();

    @XmlElement
    private String key;
}
