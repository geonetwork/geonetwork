/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */

package org.geonetwork.caching;

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
        // don't use  cacheManager.setCacheNames(List.of("mycache"));
        // this will put the cache manager in to `static` mode (instead of `dynamic`).
    }
}
