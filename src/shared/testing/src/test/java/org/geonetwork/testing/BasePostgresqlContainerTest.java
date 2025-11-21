package org.geonetwork.testing;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;
@Testcontainers
public class BasePostgresqlContainerTest {

  protected DBConnectionProvider connectionProvider;
  @Container
  static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
    "database-1"
    //need to check
    ).withExposedPorts(5432)
    .withStartupTimeout(Duration.ofSeconds(90));

  @BeforeAll
  static void beforeAll() {
    postgresContainer.start();
  }

  @AfterAll
  static void afterAll() {
    postgresContainer.stop();
  }

  @BeforeEach
  void setUp() {

    connectionProvider = new DBConnectionProvider(
      postgresContainer.getJdbcUrl(),
      postgresContainer.getUsername(),
      postgresContainer.getPassword()
    );
  }
}
