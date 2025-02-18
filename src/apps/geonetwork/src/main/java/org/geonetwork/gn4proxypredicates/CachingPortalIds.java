/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4proxypredicates;

import java.util.List;
import org.geonetwork.domain.repository.SourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * very simple cache for the portal IDs. This is likely to be called on every request, so its worth caching to cut down
 * on DB traffic.
 *
 * <p>See the CacheLoader.load() function. This actually makes the request to the DB to get the list of portal UUIDs.
 *
 * <p>The cache is set to expire this after CACHE_TIMEOUT_SECONDS seconds. After this time, it will re-execute the db
 * request for the portal uuids.
 */
@Component
public class CachingPortalIds {

    @Autowired
    SourceRepository sourceRepository;

    /**
     * get the portal UUIDs from the cache.
     *
     * @return list of portal uuids (might be old due to cache).
     */
    @Cacheable("source-portal-ids")
    public List<String> loadIdsFromDB() {
        var portalUUIDs =
                sourceRepository.findAll().stream().map(x -> x.getUuid()).toList();
        return portalUUIDs;
    }

    //    @Autowired
    //    CacheManager cacheManager;
    //
    //    /** Clean cache NOW. */
    //    public void clear() {
    //        cacheManager.getCache("source-portal-ids").invalidate();
    //    }

    /** Clean cache NOW. */
    @CacheEvict("source-portal-ids")
    public void clear() {}
}
