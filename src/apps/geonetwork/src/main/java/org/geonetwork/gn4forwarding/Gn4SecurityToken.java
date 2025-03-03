/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4forwarding;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * very simple class (for JSON) for the information being sent to GN4's JWT Header. Does some conversions for the
 * userGroupProfiles ("groupname:profile"). Determines the "max" profile for the generic user.profile.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Getter
@Setter
public class Gn4SecurityToken {
    @SuppressWarnings("UnusedVariable")
    @edu.umd.cs.findbugs.annotations.SuppressFBWarnings("URF_UNREAD_FIELD")
    @JsonProperty
    private String username;

    /**
     * just puts in the username
     *
     * @param username gn4/gn5 username (get from DB)
     */
    public Gn4SecurityToken(String username) {
        this.username = username;
    }
}
