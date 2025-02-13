/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4proxypredicates;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import org.geonetwork.domain.Source;
import org.geonetwork.domain.repository.SourceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.function.ServerRequest;

/** Simple tests for GnPortalPredicate. */
public class GnPortalPredicateTest {

    /** this tests a Path that isn't to "/geonetwork" */
    @Test
    public void testNotGN() {
        setupGnPortalPredicate(Arrays.asList());

        var predicate = GnPortalPredicate.isGnPortalRequest();

        assertEquals(false, predicate.test(createRequest("/abc")));
        assertEquals(false, predicate.test(createRequest("/abc/")));
    }

    /** tests the "main" portal (`srv`). */
    @Test
    public void testSrv() {
        setupGnPortalPredicate(Arrays.asList());

        var predicate = GnPortalPredicate.isGnPortalRequest();

        assertEquals(true, predicate.test(createRequest("/srv")));
        assertEquals(true, predicate.test(createRequest("/srv/")));
    }

    /** Tests a good source (portal uuid) - one that exists. */
    @Test
    public void testGoodSource() {
        setupGnPortalPredicate(Arrays.asList("abc", "def", "ghi"));

        var predicate = GnPortalPredicate.isGnPortalRequest();

        assertEquals(true, predicate.test(createRequest("/def")));
        assertEquals(true, predicate.test(createRequest("/def/")));
    }

    /** Tests a bad source (portal uuid) - one that does NOT exists. */
    @Test
    public void testBadSource() {
        setupGnPortalPredicate(Arrays.asList("abc", "def", "ghi"));

        var predicate = GnPortalPredicate.isGnPortalRequest();

        assertEquals(false, predicate.test(createRequest("/zzz")));
        assertEquals(false, predicate.test(createRequest("/zzz/")));
    }

    public ServerRequest createRequest(String path) {
        ServerRequest request = mock(ServerRequest.class);
        when(request.path()).thenReturn(path);
        return request;
    }

    /**
     * This setups up a STATIC class (GnPortalPredicate). Watch for side effects!!!
     *
     * <p>TODO: make the class non-static.
     *
     * @param allSourceUUID list of the uuids to put in the mocked source reposition
     */
    public void setupGnPortalPredicate(List<String> allSourceUUID) {
        GnPortalPredicate gnPortalPredicate = new GnPortalPredicate();
        gnPortalPredicate.setListOfPath(List.of("/srv"));

        var mockSourceRepository = mock(SourceRepository.class);
        when(mockSourceRepository.findAll())
                .thenReturn(allSourceUUID.stream()
                        .map(x -> {
                            var result = new Source();
                            result.setUuid(x);
                            return result;
                        })
                        .toList());

        GnPortalPredicate.applicationContext = mock(ApplicationContext.class);
        when(GnPortalPredicate.applicationContext.getBean(SourceRepository.class))
                .thenReturn(mockSourceRepository);
    }
}
