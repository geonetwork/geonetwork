/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.formatter;

import java.util.List;
import lombok.AllArgsConstructor;
import org.geonetwork.ogcapi.service.configuration.ItemPageLinksConfiguration;
import org.geonetwork.schemas.model.schemaident.Formatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

/**
 * This chooses the correct formatter (official profile name) given the available formatters, user-requested media type,
 * user-requested profile(s) (if any), and the default (application.yaml) profile for that media type.
 */
@Configuration
@AllArgsConstructor
public class ProfileSelector {
    final ItemPageLinksConfiguration itemPageLinksConfiguration;

    /**
     * This chooses the correct formatter (official profile name) given the available formatters, user-requested media
     * type, user-requested profile(s) (if any), and the default (application.yaml) profile for that media type.
     *
     * <p>1. no formatters -> null (no formatters is an error)
     *
     * <p>2. If no requested profiles: + return default (if available) + return first possibleFormatters profile
     *
     * <p>3. if requested profile: go through the profiles: + is this a possible formatter profile -> return that
     *
     * <p>4. if there's default -> return that 5. return first possbileFormatter (guess)
     *
     * @param mediaType media type of request
     * @param requestedProfiles what profile(s) the user wanted
     * @param possibleFormatters formatters for that media type
     * @return best matching formatter
     */
    public String chooseProfile(
            MediaType mediaType, List<String> requestedProfiles, List<Formatter> possibleFormatters) {

        var defaultProfile = itemPageLinksConfiguration.getProfileDefaults().stream()
                .filter(x -> x.getMimetype().equals(mediaType.toString()))
                .findFirst();

        if (possibleFormatters.isEmpty()) {
            return null; // no formatters -> doesn't matter
        }
        // no requested profile - return either the default or choose 1st one from possible formatters
        if (requestedProfiles == null || requestedProfiles.isEmpty()) {
            if (defaultProfile.isPresent()) {
                return defaultProfile.get().getDefaultProfile();
            }
            return possibleFormatters.getFirst().getOfficialProfileName();
        }

        // have requested profile(s) and at least one formatter
        // try to match, if not return default.

        for (var requestedProfile : requestedProfiles) {
            var match = possibleFormatters.stream()
                    .filter(entry -> entry.getOfficialProfileName().equals(requestedProfile))
                    .findFirst();
            if (match.isPresent()) {
                return match.get().getOfficialProfileName();
            }
        }

        if (defaultProfile.isPresent()) {
            return defaultProfile.get().getDefaultProfile();
        }

        if (!possibleFormatters.isEmpty()) {
            return possibleFormatters.getFirst().getOfficialProfileName();
        }

        return null;
    }
}
