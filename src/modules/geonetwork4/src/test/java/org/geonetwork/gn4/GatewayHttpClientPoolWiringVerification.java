/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.gn4;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.http.client.HttpClientAutoConfiguration;
import org.springframework.boot.autoconfigure.web.client.RestClientAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestClient;

/**
 * Ad-hoc proof that {@link GatewayHttpClientConfiguration}'s bean is actually picked up by Spring Boot's own
 * autoconfiguration (not just directly callable), by firing more concurrent requests than Apache HttpClient5's
 * unconfigured per-route default (5) allows and confirming they don't serialize.
 */
class GatewayHttpClientPoolWiringVerification {

    private static final int REQUEST_COUNT = 10;
    private static final int SERVER_DELAY_MS = 400;

    private HttpServer server;

    @BeforeEach
    void startSlowServer() throws Exception {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/slow", exchange -> {
            try {
                Thread.sleep(SERVER_DELAY_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            exchange.sendResponseHeaders(200, 0);
            exchange.close();
        });
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    @AfterEach
    void stopSlowServer() {
        server.stop(0);
    }

    @Test
    void configuredPoolSizeIsHonoredByRestClientAutoConfiguration() throws Exception {
        try (ConfigurableApplicationContext context = new SpringApplicationBuilder(
                        GatewayHttpClientConfiguration.class,
                        HttpClientAutoConfiguration.class,
                        RestClientAutoConfiguration.class)
                .web(WebApplicationType.NONE)
                .properties(
                        "geonetwork.4.http-client.max-total-connections=20",
                        "geonetwork.4.http-client.max-connections-per-route=20")
                .run()) {
            RestClient restClient = context.getBean(RestClient.Builder.class).build();
            String url = "http://localhost:" + server.getAddress().getPort() + "/slow";

            var futures = new CompletableFuture[REQUEST_COUNT];
            long start = System.nanoTime();
            for (int i = 0; i < REQUEST_COUNT; i++) {
                futures[i] = CompletableFuture.runAsync(
                        () -> restClient.get().uri(url).retrieve().toBodilessEntity());
            }
            CompletableFuture.allOf(futures).get(10, TimeUnit.SECONDS);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;

            // With the library's unconfigured default (5 connections/route), 10 requests need 2
            // sequential batches: ~2*SERVER_DELAY_MS. Our pool (20/route) should do it in ~1 batch.
            assertTrue(
                    elapsedMs < (long) (SERVER_DELAY_MS * 1.5),
                    "expected all " + REQUEST_COUNT + " requests to run concurrently (~" + SERVER_DELAY_MS
                            + "ms), took " + elapsedMs + "ms - looks like the default pool size (5/route) was used"
                            + " instead of our configured bean");
        }
    }
}
