/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.application.ctrlreturntypes;

import java.util.List;

public interface IProfileResponse {
    List<String> getUserRequestedProfiles();

    String getResolvedProfile();

    void setUserRequestedProfiles(List<String> userRequestedProfiles);

    void setResolvedProfile(String resolvedProfile);
}
