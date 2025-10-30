/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application.ctrlreturntypes;

import java.util.List;

public interface IProfileResponse {
    List<String> getUserRequestedProfiles();

    void setUserRequestedProfiles(List<String> userRequestedProfiles);
}
