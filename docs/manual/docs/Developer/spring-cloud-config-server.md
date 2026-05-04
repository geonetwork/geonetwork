# Spring Cloud Config Server

## Objectives
GeoNetwork application is based on multiple apps, that need configuration to operate. The same configuration must be shared between different components/apps.

**Spring Cloud Config Server** provides a unified server to manage properties for all applications, ensuring consistency and providing an update mechanism to notify other modules/apps.

While Spring Cloud Config supports Git as a back-end, GeoNetwork 5 uses **JDBC** by default to store configurations. This avoids the extra dependency on a Git repository while maintaining the flexibility of a database-backed solution.

### Infrastructure
The Config Server is implemented as an additional microservice (`spring-cloud-config-server`). While this adds a component to the infrastructure, it is consistent with our multi-app architecture. To ensure a smooth developer experience, applications are configured to use `optional:configserver`, meaning they can still function using local configuration if the server is unavailable.

Currently, GeoNetwork 5 configuration is transitioning from static YAML files to the Config Server. 

### Configuration Precedence
1. **Config Server (JDBC):** Highest precedence. Values stored in the `APP_CONFIGS` table override local settings.
2. **Local YAML Files:** Used for environment-specific bootstrap settings (database credentials, Config Server location) or as defaults when the Config Server is not used or available.

It is recommended to keep default values for relevant configurations in the local `application.yml` and move dynamic or operational settings to the database via the Config Server.

## Database Structure
The configuration is stored in the `APP_CONFIGS` table with the following structure:

```sql
CREATE TABLE APP_CONFIGS (
  APP VARCHAR(255),           -- Application name (e.g., 'GeoNetwork')
  PROFILE VARCHAR(255),       -- Spring profile (e.g., 'default', 'prod')
  LABEL VARCHAR(255),         -- Optional label (e.g., 'master')
  CONFIG_PARAM VARCHAR(255),  -- Configuration key (e.g., 'geonetwork.index.indexPrefix')
  CONFIG_VALUE VARCHAR(255)   -- Configuration value
);
```

## Developer Guide

### Using Variables in Code
From a developer's perspective, accessing configurations remains transparent and standard Spring style. You use standard Spring annotations regardless of whether the value comes from a YAML file or the Config Server:

```java
@Value("${my.config.param}")
private String param;
```

Or using `@ConfigurationProperties`:

```java
@ConfigurationProperties(prefix = "geonetwork.index")
public class IndexProperties {
    private String indexPrefix;
}
```

### Enabling Dynamic Updates
To allow a component to pick up changes at runtime without a restart, you must annotate the class with `@RefreshScope`:

```java
@Component
@RefreshScope
public class MyDynamicService {
    @Value("${dynamic.setting}")
    private String setting;
}
```

When a configuration is updated via the API, a `ContextRefresher.refresh()` call is triggered, and all beans in `@RefreshScope` are re-instantiated with the new values.

### Runtime Configuration API
Since the Config Server is read-only by design, GeoNetwork 5 provides a dedicated module (`gn-configuration-management-api`) that exposes a REST API to manage these settings:

* **Endpoint:** `/api/configuration`
* **GET `?key=...`**: Retrieve a value from the current environment.
* **GET `/set?key=...&value=...`**: Persist a value to the database and trigger a refresh.
* **POST `/`**: Persist a value via JSON body.

## Possible usages
The Spring Cloud Config Server architecture is also being evaluated as a potential solution for managing:
* **Internationalization (i18n):** Storing and updating translations for both backend and UI dynamically.
* **Feature Toggles:** Managing the activation of specific features across the infrastructure at runtime.
