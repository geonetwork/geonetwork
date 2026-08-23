/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.proxy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geonetwork.domain.repository.LinkRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

class HttpProxyConfigurationUnitTest {

    private LinkRepository linkRepository;
    private HttpProxyConfiguration.HttpProxyProperties proxyProperties;

    /** Offline DNS stub: maps host -> literal IP. Literal IPs parse via InetAddress without network access. */
    private final Map<String, String> dns = new HashMap<>();

    @BeforeEach
    void setUp() {
        linkRepository = mock(LinkRepository.class);
        proxyProperties = new HttpProxyConfiguration.HttpProxyProperties();
        proxyProperties.setEnabled(true);
        proxyProperties.setAllowedPorts(List.of(-1, 80, 443));
        proxyProperties.setAllowedMethods(List.of("GET", "POST"));
        // Extended stopgap denylist (matches the shipped default).
        proxyProperties.setExcludeHosts("^(localhost|127\\..*|10\\..*|172\\.(1[6-9]|2[0-9]|3[01])\\..*"
                + "|192\\.168\\..*|169\\.254\\..*|100\\.(6[4-9]|[7-9][0-9]|1[01][0-9]|12[0-7])\\..*"
                + "|0\\..*|255\\.255\\.255\\.255|.*\\.local|.*\\.localhost|::1|0:0:0:0:0:0:0:1|::ffff:.*)$");
        proxyProperties.setCheckInLinkTable(false);
        dns.clear();
        dns.put("example.com", "93.184.216.34");
        dns.put("data.europa.eu", "93.184.216.34");
    }

    private HttpProxyConfiguration createConfig() {
        HttpProxyConfiguration config = new HttpProxyConfiguration(proxyProperties, linkRepository);
        // Stubbed resolver: mapped names -> literal IP; otherwise parse as a literal (offline) so
        // tests for IPv6 / IP-literal handling exercise the real InetAddress normalization.
        config.setHostResolver(host -> {
            String mapped = dns.getOrDefault(host, host);
            return new InetAddress[] {InetAddress.getByName(mapped)};
        });
        return config;
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
    void testExcludedHost_LoopbackAndLocalhost() {
        HttpProxyConfiguration config = createConfig();
        assertFalse(config.isUrlAllowed(URI.create("http://localhost/api"), HttpMethod.GET));
        assertFalse(config.isUrlAllowed(URI.create("http://127.0.0.1/api"), HttpMethod.GET));
    }

    @Test
    void testExcludedHost_RegexCoversRfc1918AndMetadataAndCgnat() {
        HttpProxyConfiguration config = createConfig();
        assertFalse(config.isUrlAllowed(URI.create("http://10.1.2.3/x"), HttpMethod.GET));
        assertFalse(config.isUrlAllowed(URI.create("http://172.16.5.5/x"), HttpMethod.GET));
        assertFalse(config.isUrlAllowed(URI.create("http://192.168.1.1/x"), HttpMethod.GET));
        assertFalse(config.isUrlAllowed(URI.create("http://169.254.169.254/latest/meta-data/"), HttpMethod.GET));
        assertFalse(config.isUrlAllowed(URI.create("http://100.64.0.1/x"), HttpMethod.GET));
    }

    @Test
    void testDnsNameResolvingToInternalIpIsBlocked() {
        // The string regex never sees the IP; only resolve-and-validate catches this.
        dns.put("rebind.attacker.test", "10.0.0.5");
        HttpProxyConfiguration config = createConfig();
        assertFalse(config.isUrlAllowed(URI.create("https://rebind.attacker.test/x"), HttpMethod.GET));
    }

    @Test
    void testDnsNameResolvingToCloudMetadataIsBlocked() {
        dns.put("meta.attacker.test", "169.254.169.254");
        HttpProxyConfiguration config = createConfig();
        assertFalse(config.isUrlAllowed(URI.create("https://meta.attacker.test/x"), HttpMethod.GET));
    }

    @Test
    void testDnsNameResolvingToCgnatIsBlocked() {
        dns.put("cgnat.attacker.test", "100.100.0.1");
        HttpProxyConfiguration config = createConfig();
        assertFalse(config.isUrlAllowed(URI.create("https://cgnat.attacker.test/x"), HttpMethod.GET));
    }

    @Test
    void testIpv6BracketedLoopbackIsBlocked() {
        // URI.getHost() returns "[::1]" (RFC 2732). Brackets must be stripped before resolution.
        HttpProxyConfiguration config = createConfig();
        assertFalse(config.isUrlAllowed(URI.create("http://[::1]/x"), HttpMethod.GET));
    }

    @Test
    void testIpv6UniqueLocalIsBlocked() {
        HttpProxyConfiguration config = createConfig();
        assertFalse(config.isUrlAllowed(URI.create("http://[fc00::1]/x"), HttpMethod.GET));
    }

    @Test
    void testNullHostIsDenied() {
        HttpProxyConfiguration config = createConfig();
        URI uri = URI.create("http:///path");
        assertNull(uri.getHost());
        assertFalse(config.isUrlAllowed(uri, HttpMethod.GET));
    }

    @Test
    void testUnresolvableHostIsDenied() {
        HttpProxyConfiguration config = new HttpProxyConfiguration(proxyProperties, linkRepository);
        config.setHostResolver(host -> {
            throw new UnknownHostException(host);
        });
        assertFalse(config.isUrlAllowed(URI.create("https://does-not-resolve.test/x"), HttpMethod.GET));
    }

    @Test
    void testAllowlistIsPrimaryControl() {
        proxyProperties.setAllowedHosts(List.of(".*\\.europa\\.eu"));
        HttpProxyConfiguration config = createConfig();

        assertTrue(config.isUrlAllowed(URI.create("https://data.europa.eu/x"), HttpMethod.GET));
        assertFalse(config.isUrlAllowed(URI.create("https://example.com/x"), HttpMethod.GET));
    }

    @Test
    void testAllowlistAndLinkTableAreBothRequired() {
        proxyProperties.setAllowedHosts(List.of(".*\\.europa\\.eu"));
        proxyProperties.setCheckInLinkTable(true);
        when(linkRepository.countByUrlStartingWith(anyString())).thenReturn(0L);

        HttpProxyConfiguration config = createConfig();
        // Matches allowlist but absent from links table -> denied.
        assertFalse(config.isUrlAllowed(URI.create("https://data.europa.eu/x"), HttpMethod.GET));

        when(linkRepository.countByUrlStartingWith("https://data.europa.eu/")).thenReturn(1L);
        assertTrue(config.isUrlAllowed(URI.create("https://data.europa.eu/x"), HttpMethod.GET));
    }

    @Test
    void testLinkTable_Found() {
        proxyProperties.setCheckInLinkTable(true);
        when(linkRepository.countByUrlStartingWith(anyString())).thenReturn(0L);
        when(linkRepository.countByUrlStartingWith("https://example.com/")).thenReturn(1L);

        HttpProxyConfiguration config = createConfig();
        assertTrue(config.isUrlAllowed(URI.create("https://example.com/api"), HttpMethod.GET));
    }

    @Test
    void testLinkTable_NotFound() {
        proxyProperties.setCheckInLinkTable(true);
        when(linkRepository.countByUrlStartingWith(anyString())).thenReturn(0L);

        HttpProxyConfiguration config = createConfig();
        assertFalse(config.isUrlAllowed(URI.create("https://example.com/api"), HttpMethod.GET));
    }

    @Test
    void testLinkTable_HostBoundaryIsAnchored() {
        proxyProperties.setCheckInLinkTable(true);
        // A stored look-alike host must NOT satisfy a request for example.com.
        when(linkRepository.countByUrlStartingWith(anyString())).thenReturn(0L);
        when(linkRepository.countByUrlStartingWith("https://example.com.attacker.org/x"))
                .thenReturn(1L);

        HttpProxyConfiguration config = createConfig();
        assertFalse(config.isUrlAllowed(URI.create("https://example.com/api"), HttpMethod.GET));

        ArgumentCaptor<String> prefix = ArgumentCaptor.forClass(String.class);
        verify(linkRepository, atLeastOnce()).countByUrlStartingWith(prefix.capture());
        assertTrue(
                prefix.getAllValues().stream().allMatch(p -> p.endsWith("/")),
                "every link-table prefix must be host-boundary anchored with a trailing slash");
    }

    @Test
    void testSecureDefaults() {
        HttpProxyConfiguration.HttpProxyProperties defaults = new HttpProxyConfiguration.HttpProxyProperties();
        assertTrue(defaults.isCheckInLinkTable(), "checkInLinkTable must default to true (secure-by-default)");
        assertFalse(defaults.isEnabled(), "proxy must default to disabled");
    }
}
