/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.gn4;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.Duration;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.cloud.gateway.server.mvc.common.MvcUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Sizes the HTTP client connection pool used to proxy requests to GN4.
 *
 * <p>Without this, the pool falls back to Apache HttpClient5's library defaults (5 connections per route, 25 total, 3
 * minute connection-request timeout). A single catalogue page opens far more than 5 concurrent connections to GN4 for
 * its assets, so the default pool is too small even without a leak: once it fills up, every further request queues for
 * the full 3 minutes before failing, making the whole GN4-proxied UI appear to hang.
 */
@Configuration
public class GatewayHttpClientConfiguration {

    @Bean
    public ClientHttpRequestFactoryBuilder<HttpComponentsClientHttpRequestFactory> clientHttpRequestFactoryBuilder(
            @Value("${geonetwork.4.http-client.max-total-connections:100}") int maxTotalConnections,
            @Value("${geonetwork.4.http-client.max-connections-per-route:50}") int maxConnectionsPerRoute,
            @Value("${geonetwork.4.http-client.connection-request-timeout:5s}") Duration connectionRequestTimeout) {
        return ClientHttpRequestFactoryBuilder.httpComponents()
                .withConnectionManagerCustomizer(connectionManager -> connectionManager
                        .setMaxConnTotal(maxTotalConnections)
                        .setMaxConnPerRoute(maxConnectionsPerRoute))
                .withDefaultRequestConfigCustomizer((RequestConfig.Builder requestConfig) ->
                        requestConfig.setConnectionRequestTimeout(Timeout.of(connectionRequestTimeout)));
    }

    /**
     * Closes the proxied GN4 response once the request completes. The gateway only closes it while writing the body,
     * which it skips when it answers 304 itself, leaking the pooled connection (spring-cloud-gateway#4259).
     */
    @Bean
    public WebMvcConfigurer cleanupWebMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new HandlerInterceptor() {
                    @Override
                    public void afterCompletion(
                            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                        Object clientResponse = request.getAttribute(MvcUtils.CLIENT_RESPONSE_ATTR);
                        if (clientResponse instanceof ClientHttpResponse clientHttpResponse) {
                            clientHttpResponse.close();
                        }
                    }
                });
            }
        };
    }
}
