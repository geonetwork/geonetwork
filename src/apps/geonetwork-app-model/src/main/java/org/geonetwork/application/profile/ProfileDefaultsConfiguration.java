/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application.profile;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

/** simple configuration for links in ogcapi-records = /collections/{collectionId}/items/{itemid} */
@Configuration
@ConfigurationProperties(prefix = "geonetwork.openapi-records.defaultprofiles")
@Getter
@Setter
public class ProfileDefaultsConfiguration {
    List<ProfileDefault> profileDefaults = new ArrayList<>();

    public String getDefaultProfile(String mimeType, Class<?> clazz) {
        var result = profileDefaults.stream()
                .filter(x ->
                        x.getMimetype().equals(mimeType) && x.getResponseType().equals(clazz.getSimpleName()))
                .map(ProfileDefault::getDefaultProfile)
                .findFirst();
        return result.orElse(null);
    }

    public String getDefaultProfile(MediaType mimeType, Class<?> clazz) {
        return getDefaultProfile(mimeType.toString(), clazz);
    }
}
