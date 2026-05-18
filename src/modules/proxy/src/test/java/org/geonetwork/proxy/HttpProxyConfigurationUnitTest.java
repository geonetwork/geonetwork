/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.proxy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.util.List;
import org.geonetwork.domain.repository.LinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

class HttpProxyConfigurationUnitTest {

    private LinkRepository linkRepository;
    private HttpProxyConfiguration.HttpProxyProperties proxyProperties;

    @BeforeEach
    void setUp() {
        linkRepository = mock(LinkRepository.class);
        proxyProperties = new HttpProxyConfiguration.HttpProxyProperties();
        proxyProperties.setEnabled(true);
        proxyProperties.setAllowedPorts(List.of(-1, 80, 443));
        proxyProperties.setAllowedMethods(List.of("GET", "POST"));
        proxyProperties.setExcludeHosts(
                "^(localhost|127\\..*|0\\..*|255\\.255\\.255\\.255|.*\\.local|.*\\.localhost|0:0:0:0:0:0:1|::1)$");
        proxyProperties.setCheckInLinkTable(false);
    }

    private HttpProxyConfiguration createConfig() {
        return new HttpProxyConfiguration(proxyProperties, linkRepository);
    }

    @Test
    void testAllowedUrl_HappyPath() {
        HttpProxyConfiguration config = createConfig();
        URI uri = URI.create("https://example.com/api");
        assertTrue(config.isUrlAllowed(uri, HttpMethod.GET));
        assertTrue(config.isUrlAllowed(uri, HttpMethod.POST));
    }

    @Test
    void testAllowedUrl_DisallowedMethod() {
        HttpProxyConfiguration config = createConfig();
        URI uri = URI.create("https://example.com/api");

        HttpClientErrorException ex =
                assertThrows(HttpClientErrorException.class, () -> config.isUrlAllowed(uri, HttpMethod.DELETE));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertTrue(ex.getMessage().contains("Invalid method value"));
    }

    @Test
    void testAllowedUrl_DisallowedPort() {
        HttpProxyConfiguration config = createConfig();
        URI uri = URI.create("http://example.com:22/api");
        assertFalse(config.isUrlAllowed(uri, HttpMethod.GET));
    }

    @Test
    void testAllowedUrl_ExcludedHost() {
        HttpProxyConfiguration config = createConfig();

        URI uriLocalhost = URI.create("http://localhost/api");
        assertFalse(config.isUrlAllowed(uriLocalhost, HttpMethod.GET));

        URI uri127 = URI.create("http://127.0.0.1/api");
        assertFalse(config.isUrlAllowed(uri127, HttpMethod.GET));
    }

    @Test
    void testAllowedUrl_CheckInLinkTable_Found_ExactScheme() {
        proxyProperties.setCheckInLinkTable(true);
        // Simulating the DB finding a match when queried
        when(linkRepository.countByUrlStartingWith(anyString())).thenReturn(0L);
        when(linkRepository.countByUrlStartingWith("https://example.com")).thenReturn(1L);

        HttpProxyConfiguration config = createConfig();
        URI uri = URI.create("https://example.com/api");
        assertTrue(config.isUrlAllowed(uri, HttpMethod.GET));

        verify(linkRepository, atLeastOnce()).countByUrlStartingWith(anyString());
    }

    @Test
    void testAllowedUrl_CheckInLinkTable_NotFound() {
        proxyProperties.setCheckInLinkTable(true);
        // Simulating no match in DB
        when(linkRepository.countByUrlStartingWith(anyString())).thenReturn(0L);

        HttpProxyConfiguration config = createConfig();
        URI uri = URI.create("https://example.com/api");
        assertFalse(config.isUrlAllowed(uri, HttpMethod.GET));

        verify(linkRepository, atLeastOnce()).countByUrlStartingWith(anyString());
    }
}
