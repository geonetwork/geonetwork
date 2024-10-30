/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.server.mvc.common.MvcUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

/** Configuration for the HTTP proxy. */
@Configuration
public class HttpProxyConfiguration {
    private static final String X_METHOD = "X-METHOD";

    private final Set<HttpMethod> allowedHttpMethods = Set.of(HttpMethod.GET, HttpMethod.POST);

    private final Set<Integer> allowedHttpPorts = Set.of(-1, 80, 443);

    @Bean
    RouterFunction<ServerResponse> universalProxy() {
        return route("geonetwork_proxy")
                .route(path("/api/proxy"), http())
                .before((ServerRequest serverRequest) -> {
                    String method = serverRequest.headers().firstHeader(X_METHOD);
                    if (method == null) {
                        method = serverRequest.method().name();
                    }

                    HttpMethod httpMethod = HttpMethod.valueOf(method.toUpperCase(Locale.getDefault()));
                    if (!allowedHttpMethods.contains(httpMethod)) {
                        throw new HttpClientErrorException(
                                HttpStatus.BAD_REQUEST,
                                "Invalid method value: " + method + " in " + X_METHOD + " header.");
                    }

                    String uriString = serverRequest
                            .param("url")
                            .orElseThrow(() ->
                                    new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing url parameter"));
                    URI uri = URI.create(uriString);

                    if (!allowedHttpPorts.contains(uri.getPort())) {
                        throw new HttpClientErrorException(
                                HttpStatus.BAD_REQUEST, "Invalid port value: " + uri.getPort());
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
}
