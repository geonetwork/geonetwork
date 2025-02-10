/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4proxyprediates;

import io.micrometer.common.util.StringUtils;
import java.util.List;
import org.geonetwork.domain.repository.SourceRepository;
import org.geonetwork.utility.ApplicationContextProvider;
import org.springframework.cloud.gateway.server.mvc.common.Shortcut;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.function.RequestPredicate;

/**
 * Looks at the incoming request and sees if it looks like a GN4 request to one of the GN portals (as defined in
 * DB:`sources` table). Please see the uuid field in the `sources` table.
 *
 * <p>routes: - id: geonetwork_route uri: ${geonetwork.core.url} predicates: - isGnPortalRequest=/geonetwork filters: -
 * addSimpleGn4SecurityHeader=gn5.to.gn4.trusted.json.auth
 *
 * <p>This will match `/geonetwork/srv/...` and `/geonetwork/srv` (default portal name) If you have a portal with ID
 * (uuid) 'dave', then it will also match `/geonetwork/dave/...` and `/geonetwork/dave`.
 *
 * <p>If there is no source called "home", then `/geonetwork/home/...` will NOT MATCH.
 */
public class GnPortalPredicate {

    static ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();

    /*
     * This does the matching to things like '/geonetwork/<portal uuid>/...'.
     * This also matches the default portal '/geonetwork/srv'.
     *
     * @param contextPath - typically "/geonetwork"
     * @return true if request path is like "/geonetwork/PORTAL-UUID/..."
     */
    @Shortcut
    public static RequestPredicate isGnPortalRequest(String contextPath) {
        return request -> {
            if (!request.path().startsWith(contextPath + "/")) {
                return false; // not to "/geonetwork/..."
            }
            // remove "/geonetwork" from start
            var path = request.path().substring(contextPath.length());
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            if (StringUtils.isBlank(path)) {
                return false;
            }
            // portalName: "/geonetwork/srv/..." --> "srv"
            var portalName = path;
            if (portalName.contains("/")) {
                portalName = portalName.substring(0, portalName.indexOf("/"));
            }
            if (portalName.equals("srv")) {
                return true; // this is the default portal
            }
            var portalIds = portalIds();
            return portalIds.contains(portalName);
        };
    }

    /**
     * Get the IDs (UUID) of all the portals (DB: `sources` tables).
     *
     * <p>Note, in general, the UUID of the main portal will be GUID.
     *
     * @return the IDs (UUID) of the all portal (DB: `sources` tables)
     */
    static List<String> portalIds() {
        var sourceRepository = applicationContext.getBean(SourceRepository.class);
        var portalNames =
                sourceRepository.findAll().stream().map(x -> x.getUuid()).toList();
        return portalNames;
    }
}
