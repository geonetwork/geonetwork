/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.thesaurus.model;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Thesaurus {

    private String key;
    private String dname;
    private Object description;
    private String filename;
    private String title;

    private List<MultilingualTitle> multilingualTitles;
    private List<DublinCoreMultilingual> dublinCoreMultilinguals;
    private List<MultilingualDescription> multilingualDescriptions;

    private String date;
    private Object createdDate;
    private Object issuedDate;
    private Object modifiedDate;

    private String url;
    private String defaultNamespace;
    private String type;
}
