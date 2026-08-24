/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.proxy;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.domain.repository.LinkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.server.mvc.common.MvcUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
@EnableConfigurationProperties(HttpProxyConfiguration.HttpProxyProperties.class)
@ConditionalOnProperty(prefix = "geonetwork.proxy", name = "enabled", havingValue = "true")
public class HttpProxyConfiguration {
    private static final Logger log = LoggerFactory.getLogger(HttpProxyConfiguration.class);

    private static final String X_METHOD = "X-METHOD";

    private final Set<HttpMethod> allowedHttpMethods;

    private final Set<Integer> allowedHttpPorts;

    private final HttpProxyProperties proxyProperties;

    private final LinkRepository linkRepository;

    private Pattern excludeHostsPattern;

    /** Compiled allowlist host patterns. Empty means the allowlist is not enforced. */
    private final List<Pattern> allowedHostPatterns = new ArrayList<>();

    private final String configurationValidationError;

    public HttpProxyConfiguration(HttpProxyProperties proxyProperties, LinkRepository linkRepository) {
        this.proxyProperties = proxyProperties;
        this.linkRepository = linkRepository;
        if (proxyProperties.getAllowedPorts() == null
                || proxyProperties.getAllowedPorts().isEmpty()) {
            this.allowedHttpPorts = Set.of(-1, 80, 443);
        } else {
            this.allowedHttpPorts = Set.copyOf(proxyProperties.getAllowedPorts());
        }

        String methodError = null;
        Set<HttpMethod> parsedAllowedHttpMethods;
        // Initialize allowed HTTP methods from configuration (defaults to GET, POST)
        if (proxyProperties.getAllowedMethods() == null
                || proxyProperties.getAllowedMethods().isEmpty()) {
            parsedAllowedHttpMethods = Set.of(HttpMethod.GET, HttpMethod.POST);
        } else {
            try {
                parsedAllowedHttpMethods = Set.copyOf(proxyProperties.getAllowedMethods().stream()
                        .map(m -> HttpMethod.valueOf(m.toUpperCase(Locale.getDefault())))
                        .collect(java.util.stream.Collectors.toSet()));
            } catch (IllegalArgumentException ex) {
                parsedAllowedHttpMethods = Set.of(HttpMethod.GET, HttpMethod.POST);
                methodError = "Invalid HTTP method in geonetwork.proxy.allowedMethods: " + ex.getMessage();
            }
        }
        this.allowedHttpMethods = parsedAllowedHttpMethods;

        String regexError = null;
        // Compile the exclude hosts regex pattern
        String excludeHosts = proxyProperties.getExcludeHosts();
        if (StringUtils.isNotBlank(excludeHosts)) {
            try {
                this.excludeHostsPattern = Pattern.compile(excludeHosts);
            } catch (PatternSyntaxException ex) {
                regexError = String.format("'%s' is not a valid regular expression. %s", excludeHosts, ex.getMessage());
            }
        }

        String allowedHostsError = null;
        // Compile the optional allowlist host patterns (case-insensitive).
        if (proxyProperties.getAllowedHosts() != null) {
            for (String allowed : proxyProperties.getAllowedHosts()) {
                if (StringUtils.isBlank(allowed)) {
                    continue;
                }
                try {
                    this.allowedHostPatterns.add(Pattern.compile(allowed, Pattern.CASE_INSENSITIVE));
                } catch (PatternSyntaxException ex) {
                    allowedHostsError = String.format(
                            "'%s' in geonetwork.proxy.allowedHosts is not a valid regular expression. %s",
                            allowed, ex.getMessage());
                    break;
                }
            }
        }

        String aggregatedError = Stream.of(methodError, regexError, allowedHostsError)
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.joining(" "));
        this.configurationValidationError = aggregatedError.isEmpty() ? null : aggregatedError;
    }

    @Bean
    RouterFunction<ServerResponse> universalProxy() {
        if (this.configurationValidationError != null) {
            throw new IllegalArgumentException(this.configurationValidationError);
        }

        return route("geonetwork_proxy")
                .route(path("/proxy"), http())
                .before((ServerRequest serverRequest) -> {
                    String method = serverRequest.headers().firstHeader(X_METHOD);

                    if (proxyProperties.isOnlyForAuthenticatedUsers()) {
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
                            throw new HttpClientErrorException(
                                    HttpStatus.UNAUTHORIZED, "Proxy usage requires authentication");
                        }
                    }
                    if (method == null) {
                        method = serverRequest.method().name();
                    }

                    HttpMethod httpMethod;
                    try {
                        httpMethod = HttpMethod.valueOf(method.toUpperCase(Locale.getDefault()));
                    } catch (IllegalArgumentException ex) {
                        throw new HttpClientErrorException(
                                HttpStatus.BAD_REQUEST,
                                "Invalid method value: " + method + " in " + X_METHOD + " header.");
                    }

                    String uriString = serverRequest
                            .param("url")
                            .orElseThrow(() ->
                                    new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing url parameter"));
                    URI uri;
                    try {
                        uri = URI.create(uriString);
                    } catch (IllegalArgumentException ex) {
                        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Malformed url parameter.");
                    }

                    if (!isUrlAllowed(uri, httpMethod)) {
                        throw new HttpClientErrorException(HttpStatus.FORBIDDEN, "URL is not allowed.");
                    }

                    ServerRequest request = ServerRequest.from(serverRequest)
                            .params(stringStringMultiValueMap -> {
                                stringStringMultiValueMap.remove("url");
                                if (uri.getQuery() != null) {
                                    stringStringMultiValueMap.putAll(Stream.of(StringUtils.split(uri.getQuery(), "&"))
                                            .map(param -> {
                                                String[] parts = StringUtils.split(param, "=");
                                                return new AbstractMap.SimpleEntry<>(parts[0], parts[1]);
                                            })
                                            .collect(groupingBy(
                                                    Map.Entry::getKey, mapping(Map.Entry::getValue, toList()))));
                                }
                            })
                            .method(httpMethod)
                            .uri(uri)
                            .build();
                    MvcUtils.setRequestUrl(request, uri);
                    return request;
                })
                .build();
    }

    @ConfigurationProperties(prefix = "geonetwork.proxy")
    public static class HttpProxyProperties {
        /** Enable the proxy configuration (controls the conditional on this configuration). */
        @Getter
        @Setter
        private boolean enabled = false;

        /**
         * Regex of hosts to exclude from proxying, used as a fast pre-filter. Applied to the host string after
         * stripping any IPv6 brackets. Host validation also resolves the name and checks the resolved address(es) (see
         * {@link HttpProxyConfiguration#isInternalAddress}). Covers loopback, private, link-local, broadcast,
         * {@code *.local} and IPv6 loopback / mapped literals.
         */
        @Getter
        @Setter
        private String excludeHosts = "^(localhost|127\\..*|10\\..*|172\\.(1[6-9]|2[0-9]|3[01])\\..*"
                + "|192\\.168\\..*|169\\.254\\..*|100\\.(6[4-9]|[7-9][0-9]|1[01][0-9]|12[0-7])\\..*"
                + "|0\\..*|255\\.255\\.255\\.255|.*\\.local|.*\\.localhost"
                + "|::1|0:0:0:0:0:0:0:1|::ffff:.*)$";

        /**
         * Optional allowlist of permitted host regexes (case-insensitive). When non-empty it is the primary control:
         * the requested host must match at least one pattern. {@link #checkInLinkTable} then applies as an additional
         * constraint. Empty means the allowlist is not enforced.
         */
        private List<String> allowedHosts = List.of();

        /** Allowed ports for proxied URIs. */
        private List<Integer> allowedPorts = List.of(-1, 80, 443);

        /** Only allow proxy usage for authenticated users. */
        @Getter
        @Setter
        private boolean onlyForAuthenticatedUsers = false;

        /**
         * If true, only proxy hosts that already appear in the GeoNetwork {@code links} table. Secure-by-default: the
         * code default is {@code true} so an absent/overridden {@code application.yml} cannot silently reopen the
         * proxy.
         */
        @Getter
        @Setter
        private boolean checkInLinkTable = true;
        /** Allowed HTTP methods for proxied requests (e.g. GET, POST). */
        private List<String> allowedMethods = List.of("GET", "POST");

        public List<String> getAllowedHosts() {
            return allowedHosts == null ? null : List.copyOf(allowedHosts);
        }

        public void setAllowedHosts(List<String> allowedHosts) {
            this.allowedHosts = allowedHosts == null ? null : List.copyOf(allowedHosts);
        }

        public List<Integer> getAllowedPorts() {
            return allowedPorts == null ? null : List.copyOf(allowedPorts);
        }

        public void setAllowedPorts(List<Integer> allowedPorts) {
            this.allowedPorts = allowedPorts == null ? null : List.copyOf(allowedPorts);
        }

        public List<String> getAllowedMethods() {
            return allowedMethods == null ? null : List.copyOf(allowedMethods);
        }

        public void setAllowedMethods(List<String> allowedMethods) {
            this.allowedMethods = allowedMethods == null ? null : List.copyOf(allowedMethods);
        }
    }

    /**
     * Check whether the given URI and HTTP method are allowed to be proxied. Throws
     * HttpClientErrorException(HttpStatus.BAD_REQUEST) when the method is invalid. Returns false (deny) for any URL
     * that is malformed, targets a disallowed port, matches the exclude denylist, resolves to an internal/special IP
     * address, fails the allowlist, or is absent from the links table when those controls are enabled.
     *
     * <p>The host is validated by resolving it and checking the resolved address(es). Connection-time behaviour of the
     * underlying gateway client is a known limitation tracked separately.
     */
    boolean isUrlAllowed(URI uri, HttpMethod httpMethod) {
        // Validate method first and throw BAD_REQUEST if not allowed
        if (!allowedHttpMethods.contains(httpMethod)) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid method value: " + httpMethod.name() + " in " + X_METHOD + " header.");
        }

        // Reject malformed authorities up front (null host) rather than NPE/500 later.
        String host = stripIpv6Brackets(uri.getHost());
        if (StringUtils.isBlank(host)) {
            return false;
        }

        if (!allowedHttpPorts.contains(uri.getPort())) {
            return false;
        }

        // Stopgap string denylist (fast pre-filter only).
        if (this.excludeHostsPattern != null
                && this.excludeHostsPattern.matcher(host).matches()) {
            return false;
        }

        // Authoritative SSRF defense: resolve the host and reject if ANY resolved
        // address is internal/special. Normalizes decimal/hex/octal/IPv6 encodings.
        if (isInternalAddress(host)) {
            return false;
        }

        // Allowlist is the primary control when configured.
        if (!allowedHostPatterns.isEmpty()) {
            boolean matched =
                    allowedHostPatterns.stream().anyMatch(p -> p.matcher(host).matches());
            if (!matched) {
                return false;
            }
        }

        // Links table is an additional constraint, not the primary control.
        if (proxyProperties.isCheckInLinkTable()) {
            return isUrlInLinkTable(uri);
        }

        return true;
    }

    /** Seam over {@link InetAddress#getAllByName(String)} so SSRF validation can be unit-tested without DNS. */
    @FunctionalInterface
    interface HostResolver {
        InetAddress[] resolve(String host) throws UnknownHostException;
    }

    private HostResolver hostResolver = InetAddress::getAllByName;

    void setHostResolver(HostResolver hostResolver) {
        this.hostResolver = hostResolver;
    }

    /** Strip the RFC 2732 brackets that {@link URI#getHost()} keeps around IPv6 literals (e.g. {@code [::1]}). */
    private static String stripIpv6Brackets(String host) {
        if (host != null && host.length() > 1 && host.charAt(0) == '[' && host.charAt(host.length() - 1) == ']') {
            return host.substring(1, host.length() - 1);
        }
        return host;
    }

    /**
     * Resolve {@code host} and return true if it is unresolvable or ANY resolved address is loopback, any-local
     * (0.0.0.0/8 and ::), link-local (169.254.0.0/16, fe80::/10), site-local (RFC 1918, deprecated IPv6 fec0::/10),
     * multicast, CGNAT (100.64.0.0/10) or IPv6 unique-local (fc00::/7). Resolving via {@link InetAddress} also
     * normalizes alternate IP encodings (decimal/hex/octal) and IPv6 literals, closing those bypass classes.
     */
    boolean isInternalAddress(String host) {
        try {
            InetAddress[] addresses = hostResolver.resolve(host);
            if (addresses.length == 0) {
                return true;
            }
            for (InetAddress address : addresses) {
                if (isAddressBlocked(address)) {
                    log.warn(
                            "Proxy blocked SSRF attempt: host '{}' resolves to internal/special address {}",
                            host,
                            address.getHostAddress());
                    return true;
                }
            }
            return false;
        } catch (UnknownHostException ex) {
            // Cannot validate -> cannot safely proxy.
            return true;
        }
    }

    private static boolean isAddressBlocked(InetAddress address) {
        if (address.isLoopbackAddress()
                || address.isAnyLocalAddress()
                || address.isLinkLocalAddress()
                || address.isSiteLocalAddress()
                || address.isMulticastAddress()) {
            return true;
        }
        byte[] b = address.getAddress();
        if (b.length == 4) {
            int o1 = b[0] & 0xFF;
            int o2 = b[1] & 0xFF;
            // 0.0.0.0/8 ("this network") and CGNAT 100.64.0.0/10.
            return o1 == 0 || (o1 == 100 && o2 >= 64 && o2 <= 127);
        }
        // IPv6 unique-local addresses fc00::/7 (first 7 bits = 1111110).
        return b.length == 16 && (b[0] & 0xFE) == 0xFC;
    }

    /**
     * Check if the given URI matches any stored link in the database. Returns true if at least one link's URL starts
     * with any candidate prefix derived from the URI.
     */
    private boolean isUrlInLinkTable(URI uri) {
        String host = uri.getHost();
        int port = uri.getPort();
        String scheme = uri.getScheme();

        // A trailing "/" anchors the host boundary so a loose prefix LIKE cannot be
        // satisfied by a stored look-alike host such as "https://example.com.attacker.org/".
        String authority = port == -1 ? host : host + ":" + port;
        List<String> prefixes = new ArrayList<>();
        if (scheme != null) {
            prefixes.add(scheme + "://" + authority + "/");
        }
        prefixes.add(authority + "/");
        prefixes.add("//" + authority + "/");

        long total = 0;
        for (String p : prefixes) {
            total += linkRepository.countByUrlStartingWith(p);
            if (total > 0) break;
        }

        return total > 0;
    }
}
