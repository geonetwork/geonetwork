/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.gn4;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * Checks that conditional requests do not exhaust the GN4 proxy connection pool. When GN4 ignores If-Modified-Since and
 * answers 200 with a body, the gateway answers 304 itself and skips writing the body, which is where it normally closes
 * the upstream response (spring-cloud-gateway#4259). Without {@link GatewayHttpClientConfiguration}'s cleanup
 * interceptor the pooled connection is never released.
 */
class GatewayConditionalRequestLeakTest {

    private static final String LAST_MODIFIED = "Fri, 16 Oct 2026 10:52:37 GMT";
    private static final int POOL_SIZE = 3;

    @SpringBootConfiguration
    @EnableAutoConfiguration(
            excludeName = {
                "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
                "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
                "org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration",
                "org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration",
                "org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration",
                "org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration",
                "org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration",
                "org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration"
            })
    @Import(GatewayHttpClientConfiguration.class)
    static class App {}

    private HttpServer upstream;
    private ConfigurableApplicationContext gateway;
    private final HttpClient client =
            HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();

    @BeforeEach
    void start() throws Exception {
        // Behaves like GN4's /srv/api/sources/{id}/logo: sends Last-Modified, ignores If-Modified-Since.
        upstream = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        upstream.createContext("/logo", exchange -> {
            byte[] body = "x".repeat(1024 * 1024).getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Last-Modified", LAST_MODIFIED);
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        });
        upstream.start();

        gateway = new SpringApplicationBuilder(App.class)
                .properties(
                        "server.port=0",
                        "geonetwork.4.http-client.max-total-connections=" + POOL_SIZE,
                        "geonetwork.4.http-client.max-connections-per-route=" + POOL_SIZE,
                        "geonetwork.4.http-client.connection-request-timeout=1s",
                        "spring.cloud.gateway.server.webmvc.routes[0].id=gn4",
                        "spring.cloud.gateway.server.webmvc.routes[0].uri=http://localhost:"
                                + upstream.getAddress().getPort(),
                        "spring.cloud.gateway.server.webmvc.routes[0].predicates[0]=Path=/logo")
                .run();
    }

    @AfterEach
    void stop() {
        if (gateway != null) gateway.close();
        if (upstream != null) upstream.stop(0);
    }

    private int get(boolean conditional) throws Exception {
        int port = Integer.parseInt(gateway.getEnvironment().getProperty("local.server.port"));
        var builder = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/logo"))
                .timeout(Duration.ofSeconds(10));
        if (conditional) builder.header("If-Modified-Since", LAST_MODIFIED);
        return client.send(builder.build(), HttpResponse.BodyHandlers.discarding())
                .statusCode();
    }

    @Test
    void plainRequestsDoNotExhaustThePool() throws Exception {
        for (int i = 0; i < POOL_SIZE * 5; i++) {
            assertEquals(200, get(false), "plain request #" + i);
        }
    }

    @Test
    void conditionalRequestsAnsweredWith304ByTheGatewayLeakConnections() throws Exception {
        for (int i = 0; i < POOL_SIZE * 5; i++) {
            assertEquals(304, get(true), "conditional request #" + i);
        }
        // If the 304s leaked their upstream connections, the pool is now empty and this fails after 1s.
        assertEquals(200, get(false), "plain request after " + POOL_SIZE * 5 + " conditional ones");
    }
}
