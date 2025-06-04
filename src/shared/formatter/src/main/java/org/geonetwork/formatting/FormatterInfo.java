/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
public class FormatterInfo {

    String mimeType;
    List<String> schemas;

    public FormatterInfo() {
        schemas = new ArrayList<>();
    }
}
