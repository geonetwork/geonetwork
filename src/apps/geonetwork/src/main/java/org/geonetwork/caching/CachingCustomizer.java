/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.caching;

import java.util.List;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** Caching customizer. */
@Component
@EnableScheduling
public class CachingCustomizer implements CacheManagerCustomizer<ConcurrentMapCacheManager> {

    /** Clean the "source-portal-ids" cache every 60 seconds. */
    @CacheEvict(cacheNames = "source-portal-ids", allEntries = true)
    @Scheduled(fixedRateString = "60000")
    public void emptyCachePeriodically() {
        // do nothing
    }

    @Override
    public void customize(ConcurrentMapCacheManager cacheManager) {
        cacheManager.setCacheNames(List.of("schema-codelists", "source-portal-ids"));
    }
}
