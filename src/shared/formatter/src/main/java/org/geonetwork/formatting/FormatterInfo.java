/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.formatting;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormatterInfo {

    String mimeType;
    Set<String> schemas;
    ProfileInfo profile;

    public FormatterInfo(String mimeType, ProfileInfo profile) {
        this.mimeType = mimeType;
        this.profile = profile;
        schemas = new HashSet<>();
    }
}
