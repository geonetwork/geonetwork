/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4.proxypredicates;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.geonetwork.domain.Source;
import org.geonetwork.domain.repository.SourceRepository;
import org.geonetwork.utility.ApplicationContextProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.function.ServerRequest;

/** Simple tests for GnPortalPredicate. */
@ContextConfiguration
@ExtendWith(SpringExtension.class)
public class GeoNetwork4PredicateTest {

    @Autowired
    ApplicationContext applicationContext;

    /** this tests a Path that isn't to "/" */
    @Test
    public void testNotGN() {
        setupGnPortalPredicate(List.of());

        var predicate = GeoNetwork4Predicate.isGeoNetwork4Request();

        assertFalse(predicate.test(createRequest("/abc")));
        assertFalse(predicate.test(createRequest("/abc/")));
    }

    /** tests the "main" portal (`srv`). */
    @Test
    public void testSrv() {
        setupGnPortalPredicate(List.of());

        var predicate = GeoNetwork4Predicate.isGeoNetwork4Request();

        assertTrue(predicate.test(createRequest("/srv")));
        assertTrue(predicate.test(createRequest("/srv/")));
    }

    /** Tests a good source (portal uuid) - one that exists. */
    @Test
    public void testGoodSource() {
        setupGnPortalPredicate(Arrays.asList("abc", "def", "ghi"));

        var predicate = GeoNetwork4Predicate.isGeoNetwork4Request();

        assertTrue(predicate.test(createRequest("/def")));
        assertTrue(predicate.test(createRequest("/def/")));
    }

    /** Tests a bad source (portal uuid) - one that does NOT exists. */
    @Test
    public void testBadSource() {
        setupGnPortalPredicate(Arrays.asList("abc", "def", "ghi"));

        var predicate = GeoNetwork4Predicate.isGeoNetwork4Request();

        assertFalse(predicate.test(createRequest("/zzz")));
        assertFalse(predicate.test(createRequest("/zzz/")));
    }

    /** tests caching is working */
    @Test
    public void testCaching() {
        var sourcerepo = setupGnPortalPredicate(List.of("abc"));
        var predicate = GeoNetwork4Predicate.isGeoNetwork4Request();

        // start counting now
        clearInvocations(sourcerepo);

        // make 2 requests, clear cache between each request.  Should be 2 calls to the SourceRepo
        predicate.test(createRequest("/zzz"));
        applicationContext.getBean(CachingPortalIds.class).clear();
        predicate.test(createRequest("/zzz"));

        verify(sourcerepo, times(2)).findAll();

        // make 2 requests.  Should be 1 call to the SourceRepo (other is cached)
        clearInvocations(sourcerepo);
        applicationContext.getBean(CachingPortalIds.class).clear();

        predicate.test(createRequest("/zzz"));
        predicate.test(createRequest("/zzz"));

        verify(sourcerepo, times(1)).findAll();
    }

    // -----------------------------------------------------------------------

    public ServerRequest createRequest(String path) {
        ServerRequest request = mock(ServerRequest.class);
        when(request.path()).thenReturn(path);
        return request;
    }

    /**
     * This setups up the source repository (mock) so it returns the given list of UUIDs.
     *
     * <p>Config: 1. set "/srv" as a path 2. clear the CachingPortalIds cache 3. sets up the source repository mock so
     * it returns the given list of UUIDs.
     *
     * @param allSourceUUID list of the uuids to put in the mocked source reposition
     * @return the underlying SourceRepository used (from spring)
     */
    public SourceRepository setupGnPortalPredicate(List<String> allSourceUUID) {
        GeoNetwork4Predicate geoNetwork4Predicate = new GeoNetwork4Predicate();
        geoNetwork4Predicate.setListOfPath(List.of("/srv"));

        // create the `Source` objects to return (with the correct UUID)
        var sourceRepoResults = allSourceUUID.stream()
                .map(x -> {
                    var result = new Source();
                    result.setUuid(x);
                    return result;
                })
                .toList();

        var cachingPortalIds = applicationContext.getBean(CachingPortalIds.class);
        cachingPortalIds.clear();

        var mockSourceRepository = applicationContext.getBean(SourceRepository.class);
        doReturn(sourceRepoResults).when(mockSourceRepository).findAll();
        return mockSourceRepository;
    }

    // ----------------------------  Spring Context  -------------------------

    // setup the context - make sure caching is enabled.
    @EnableCaching
    @Configuration
    public static class CachingTestConfig {

        // required for getting beans from a static context
        @Bean
        public ApplicationContextProvider applicationContextProvider(ApplicationContext applicationContext) {
            var result = new ApplicationContextProvider();
            result.setApplicationContext(applicationContext);
            return result;
        }

        // make sure that CachingPortalIds uses our mocked SourceRepository
        @Bean
        public CachingPortalIds cachingPortalIds(SourceRepository sourceRepository) {
            return new CachingPortalIds(sourceRepository);
        }

        // SourceRepository is a mock (so we can change the answer)
        @Bean
        public SourceRepository sourceRepositoryMock() {
            return mock(SourceRepository.class);
        }

        // setup the cache (NO TTL)
        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("source-portal-ids");
        }
    }
}
