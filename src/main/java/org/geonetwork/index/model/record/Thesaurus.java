/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */


package org.geonetwork.index.model.record;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Thesaurus {
    String id;
    String title;
    String theme;
    String link;
    ArrayList<Keyword> keywords = new ArrayList<>();

    @JsonAnySetter
    public void ignored(String name, Object value) {
        // Ignore class fields.
        try {
            IndexRecord.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
//            if (name.startsWith(IndexRecordFieldNames.Codelists.prefix)) {
//                ArrayList<Codelist> codelist = codelists.computeIfAbsent(name, k -> new ArrayList<Codelist>());
//                if (value instanceof List) {
//                    codelist.addAll(((List<HashMap>) value).stream().map(c ->
//                            new Codelist(c)).toList());
//                }
//            }
        }
    }
}
