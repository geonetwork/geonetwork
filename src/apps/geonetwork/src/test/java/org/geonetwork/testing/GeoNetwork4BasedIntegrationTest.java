/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.testing;

import java.io.File;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.GeonetworkApplication;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;

/**
 * Integration test running with GeoNetwork 4.
 *
 * <p>Test containers are started on random port. Only service geonetwork is exposed on port 8082 but the port during
 * the test is random. Use getGeoNetworkCoreUrl() to get the container URL for GeoNetwork4.
 */
@SpringBootTest(classes = GeonetworkApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles(value = {"prod", "test"})
@Slf4j
@ContextConfiguration(initializers = GeoNetwork4BasedIntegrationTest.class)
public class GeoNetwork4BasedIntegrationTest implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final String GEONETWORK4_SERVICE = "geonetwork-1";
    public static final int GEONETWORK4_PORT = 8082;

    @Container
    public static ComposeContainer environment = new ComposeContainer(new File("src/test/resources/docker-compose.yml"))
            .withLogConsumer(GEONETWORK4_SERVICE, new Slf4jLogConsumer(log))
            .withExposedService("database-1", 5432)
            .withExposedService(
                    GEONETWORK4_SERVICE,
                    GEONETWORK4_PORT,
                    Wait.forHealthcheck().withStartupTimeout(Duration.ofMinutes(2)));

    @Override
    public void initialize(ConfigurableApplicationContext ctx) {
        TestPropertyValues.of(
                        "geonetwork.core.url=" + getGeoNetworkCoreUrl(),
                        "spring.datasource.url=" + getGeoNetworkDatabaseUrl(),
                        "spring.datasource.username=geonetwork",
                        "spring.datasource.password=geonetwork",
                        "spring.datasource.driverClassName=org.postgresql.Driver",
                        "spring.jpa. database-platform=org.hibernate.dialect.PostgreSQLDialect")
                .applyTo(ctx.getEnvironment());
    }

    protected String getGeoNetworkCoreUrl() {
        String serviceHost = environment.getServiceHost(GEONETWORK4_SERVICE, GEONETWORK4_PORT);
        Integer servicePort = environment.getServicePort(GEONETWORK4_SERVICE, GEONETWORK4_PORT);
        String geonetworkCoreUrl = "http://" + serviceHost + ":" + servicePort + "/geonetwork";
        log.atInfo().log("Running with GeoNetwork 4 at URL {}", geonetworkCoreUrl);
        return geonetworkCoreUrl;
    }

    protected String getGeoNetworkDatabaseUrl() {
        String dbHost = environment.getServiceHost("database-1", 5432);
        Integer dbPort = environment.getServicePort("database-1", 5432);
        return "jdbc:postgresql://" + dbHost + ":" + dbPort + "/geonetwork";
    }

    @BeforeAll
    static void setUp() {
        environment.start();
    }

    @BeforeEach
    void testIsContainerRunning() {}

    @AfterAll
    static void destroy() {
        environment.stop();
    }
}
