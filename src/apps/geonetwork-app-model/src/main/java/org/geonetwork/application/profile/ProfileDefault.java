/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Configuration for the default profile for a MimeType. */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProfileDefault {
    public String mimetype;
    public String defaultProfile;
    public String responseType;
}
