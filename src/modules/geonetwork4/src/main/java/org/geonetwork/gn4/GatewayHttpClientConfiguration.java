/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.gn4;

import java.time.Duration;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

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
}
