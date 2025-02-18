/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4proxypredicates;

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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.function.ServerRequest;

/** Simple tests for GnPortalPredicate. */
@ContextConfiguration
@ExtendWith(SpringExtension.class)
public class GnPortalPredicateTest {

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
        ((CachingPortalIds) applicationContext.getBean("cachingPortalIds")).clear();
        predicate.test(createRequest("/zzz"));

        verify(sourcerepo, times(2)).findAll();

        // make 2 requests.  Should be 1 call to the SourceRepo (other is cached)
        clearInvocations(sourcerepo);
        ((CachingPortalIds) applicationContext.getBean("cachingPortalIds")).clear();

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
     * This setups up a STATIC class (GnPortalPredicate). Watch for side effects!!!
     *
     * @param allSourceUUID list of the uuids to put in the mocked source reposition
     * @return the underlying SourceRepository used
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

        // manually setup the application context
        var beanFactory = ((GenericApplicationContext) applicationContext).getBeanFactory();

        SourceRepository mockSourceRepository;

        // update sourceRepository so it returns the correct results.
        // 1. already registered?  -> just update its return value
        // 2. not registered? --> create, update return value, put in application context
        try {
            mockSourceRepository = (SourceRepository) applicationContext.getBean("sourceRepository");
            doReturn(sourceRepoResults).when(mockSourceRepository).findAll();
        } catch (Exception e) {
            mockSourceRepository = mock(SourceRepository.class);
            doReturn(sourceRepoResults).when(mockSourceRepository).findAll();
            beanFactory.registerSingleton("sourceRepository", mockSourceRepository);
        }

        // remove bean if it exists
        try {
            applicationContext.getBean("cachingPortalIds");
            ((DefaultListableBeanFactory) beanFactory).destroySingleton("cachingPortalIds");
        } catch (BeansException e) {
            // no action
        }

        // create CachingPortalIds class, make sure cache is cleared.
        var cachingPortalIds = beanFactory.createBean(CachingPortalIds.class);
        cachingPortalIds.clear(); // make sure the cache is cleared
        beanFactory.registerSingleton("cachingPortalIds", cachingPortalIds);

        var acp = new ApplicationContextProvider();
        acp.setApplicationContext(applicationContext);

        return mockSourceRepository;
    }

    // ---------------------------------------------------------------------------

    // setup the context - make sure caching is enabled.
    @EnableCaching
    @Configuration
    public static class CachingTestConfig {

        @Bean
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager("source-portal-ids");
        }
    }
}
