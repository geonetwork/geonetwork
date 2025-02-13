/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4proxyprediates;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.geonetwork.domain.repository.SourceRepository;
import org.geonetwork.utility.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

/**
 * very simple cache for the portal IDs. This is likely to be called on every request, so its worth caching to cut down
 * on DB traffic.
 *
 * <p>See the CacheLoader.load() function. This actually makes the request to the DB to get the list of portal UUIDs.
 *
 * <p>The cache is set to expire this after CACHE_TIMEOUT_SECONDS seconds. After this time, it will re-execute the db
 * request for the portal uuids.
 */
public class CachingPortalIds {
    static ApplicationContext applicationContext = ApplicationContextProvider.getApplicationContext();
    static LoadingCache<String, List<String>> cache;

    static int CACHE_TIMEOUT_SECONDS = 60;

    // create the cache - with the load() method to do the DB query.
    static {
        var cacheLoader = new CacheLoader<String, List<String>>() {

            /**
             * Get the IDs (UUID) of all the portals (DB: `sources` tables).
             *
             * <p>Note, in general, the UUID of the main portal will be GUID.
             *
             * @return the IDs (UUID) of the all portal (DB: `sources` tables)
             */
            @Override
            public List<String> load(String key) {
                var sourceRepository = applicationContext.getBean(SourceRepository.class);
                var portalUUIDs = sourceRepository.findAll().stream()
                        .map(x -> x.getUuid())
                        .toList();
                return portalUUIDs;
            }
        };
        // build cache and expire after CACHE_TIMEOUT_SECONDS seconds
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(CACHE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build(cacheLoader);
    }

    /**
     * get the portal UUIDs from the cache.
     *
     * @return list of portal uuids (might be up to CACHE_TIMEOUT_SECONDS old).
     */
    public List<String> portalIds() {
        return cache.getUnchecked("portalIds");
    }
}
