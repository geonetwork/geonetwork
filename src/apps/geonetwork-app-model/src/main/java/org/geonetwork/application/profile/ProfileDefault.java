/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
