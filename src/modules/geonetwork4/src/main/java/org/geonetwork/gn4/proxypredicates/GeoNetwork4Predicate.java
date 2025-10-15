/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4.proxypredicates;

import io.micrometer.common.util.StringUtils;
import java.util.List;
import lombok.AllArgsConstructor;
import org.geonetwork.utility.ApplicationContextProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.common.Shortcut;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RequestPredicate;

/**
 * Looks at the incoming request and sees if it is a GN4 request to one of the endpoint or portals (as defined in
 * DB:`sources` table). Please see the uuid field in the `sources` table.
 *
 * <p>Example usage:
 *
 * <pre>
 *     cloud:
 *     gateway:
 *       mvc:
 *         routes:
 *           - id: geonetwork_route
 *             uri: ${geonetwork.4.url}
 *             predicates:
 *               - isGeoNetwork4Request=
 *             filters:
 *               - addSimpleGn4SecurityHeader=gn5.to.gn4.trusted.json.auth
 * </pre>
 *
 * <p>This will match `/srv/...` and `/srv` (default portal name) If you have a portal with ID (uuid) 'dave', then it
 * will also match `/dave/...` and `/dave`. If using a custom GeoNetwork 4 instance and additional path needs to be
 * added to the list of paths, then it can be added in the application.yml.
 *
 * <p>If there is no source called "home", then `/home/...` will NOT MATCH and be handled by spring boot application.
 */
@Component
@AllArgsConstructor
public class GeoNetwork4Predicate {

    static volatile CachingPortalIds cachingPortalIdsInstance = null;

    private static List<String> listOfPath;

    @Value("${geonetwork.4.path}")
    public void setListOfPath(List<String> listOfPath) {
        GeoNetwork4Predicate.listOfPath = listOfPath;
    }

    // double check pattern to ensure we only construct CachingPortalIds once
    static CachingPortalIds cachingPortalIds() {
        if (cachingPortalIdsInstance != null) {
            return cachingPortalIdsInstance;
        }
        synchronized (GeoNetwork4Predicate.class) {
            if (cachingPortalIdsInstance == null) {
                cachingPortalIdsInstance =
                        ApplicationContextProvider.getApplicationContext().getBean(CachingPortalIds.class);
            }
            return cachingPortalIdsInstance;
        }
    }

    /*
     * This does the matching to things like '/geonetwork/<portal uuid>/...'.
     * This also matches the default portal '/geonetwork/srv'.
     *
     * @param contextPath - typically "/geonetwork"
     * @return true if request path is like "/geonetwork/PORTAL-UUID/..."
     */
    @Shortcut
    public static RequestPredicate isGeoNetwork4Request() {
        return request -> {
            var path = request.path();
            if (StringUtils.isBlank(path)) {
                return false;
            }
            if (listOfPath.stream().anyMatch(path::startsWith)) {
                return true;
            }
            var portalName = path.substring(1);
            var slashIndex = portalName.indexOf("/");
            return cachingPortalIds()
                    .loadIdsFromDB()
                    .contains(slashIndex == -1 ? portalName : portalName.substring(0, slashIndex));
        };
    }
}
