/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4forwarding;

import lombok.Getter;
import lombok.Setter;
import org.geonetwork.domain.Profile;

/** simple DTO to keep track of a group/profile the user has been assigned. */
@Getter
@Setter
public class UserGroupProfile {

    private Profile profile;
    private String groupName;

    public UserGroupProfile(String groupName, int profileId) {
        this.groupName = groupName;
        this.profile = Profile.values()[profileId];
    }
}
