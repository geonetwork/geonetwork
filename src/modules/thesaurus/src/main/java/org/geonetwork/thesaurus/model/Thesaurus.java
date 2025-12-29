/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
