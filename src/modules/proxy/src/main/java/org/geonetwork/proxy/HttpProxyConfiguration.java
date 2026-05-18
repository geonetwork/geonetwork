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

import java.net.URI;
import java.util.AbstractMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.domain.repository.LinkRepository;
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
    private static final String X_METHOD = "X-METHOD";

    private final Set<HttpMethod> allowedHttpMethods;

    private final Set<Integer> allowedHttpPorts;

    private final HttpProxyProperties proxyProperties;

    private final LinkRepository linkRepository;

    private Pattern excludeHostsPattern;
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

        if (methodError != null && regexError != null) {
            this.configurationValidationError = methodError + " " + regexError;
        } else if (methodError != null) {
            this.configurationValidationError = methodError;
        } else {
            this.configurationValidationError = regexError;
        }
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

                    HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase(Locale.getDefault()));

                    String uriString = serverRequest
                            .param("url")
                            .orElseThrow(() ->
                                    new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing url parameter"));
                    URI uri = URI.create(uriString);

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

        /** Regex of hosts to exclude from proxying. */
        @Getter
        @Setter
        private String excludeHosts =
                "^(localhost|127\\..*|0\\..*|255\\.255\\.255\\.255|.*\\.local|.*\\.localhost|0:0:0:0:0:0:1|::1)$";

        /** Allowed ports for proxied URIs. */
        private List<Integer> allowedPorts = List.of(-1, 80, 443);

        /** Only allow proxy usage for authenticated users. */
        @Getter
        @Setter
        private boolean onlyForAuthenticatedUsers = false;

        /** If true, check the links table to see if any stored link.url starts with the requested host. */
        @Getter
        @Setter
        private boolean checkInLinkTable = false;
        /** Allowed HTTP methods for proxied requests (e.g. GET, POST). */
        private List<String> allowedMethods = List.of("GET", "POST");

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
     * HttpClientErrorException(HttpStatus.BAD_REQUEST) when the method is invalid. Returns false when the URI should be
     * blocked (either due to port, excluded host, or no matching link when checkInLinkTable is enabled).
     */
    boolean isUrlAllowed(URI uri, HttpMethod httpMethod) {
        // Validate method first and throw BAD_REQUEST if not allowed
        if (!allowedHttpMethods.contains(httpMethod)) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid method value: " + httpMethod.name() + " in " + X_METHOD + " header.");
        }

        if (!allowedHttpPorts.contains(uri.getPort())) {
            return false;
        }

        if (this.excludeHostsPattern != null) {
            Matcher matcher = this.excludeHostsPattern.matcher(uri.getHost());
            if (matcher.matches()) {
                return false;
            }
        }

        if (proxyProperties.isCheckInLinkTable()) {
            return isUrlInLinkTable(uri);
        }

        return true;
    }

    /**
     * Check if the given URI matches any stored link in the database. Returns true if at least one link's URL starts
     * with any candidate prefix derived from the URI.
     */
    private boolean isUrlInLinkTable(URI uri) {
        String host = uri.getHost();
        int port = uri.getPort();
        String scheme = uri.getScheme();

        java.util.List<String> prefixes = new java.util.ArrayList<>();
        if (scheme != null) {
            if (port == -1) {
                prefixes.add(scheme + "://" + host);
            } else {
                prefixes.add(scheme + "://" + host + ":" + port);
            }
        }
        if (port == -1) {
            prefixes.add(host);
            prefixes.add("//" + host);
        } else {
            prefixes.add(host + ":" + port);
            prefixes.add("//" + host + ":" + port);
        }

        long total = 0;
        for (String p : prefixes) {
            total += linkRepository.countByUrlStartingWith(p);
            if (total > 0) break;
        }

        return total > 0;
    }
}
