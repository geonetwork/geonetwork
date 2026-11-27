# Infrastructure (Docker)

This directory provides Docker Compose configurations for the GeoNetwork 5 environment.

## Core Dependencies
These files provide the database (PostGIS) and search engine (Elasticsearch).

- **`docker-compose-dbs.yml`**: PostgreSQL with sample data (`dump.gn.sql`) and clean Elasticsearch (index is automatically initialized and populated on startup by GN5 when `GN5_INDEX_CREATE_IF_EMPTY=true`).
- **`docker-compose-dbs-empty.yml`**: PostgreSQL and Elasticsearch **empty** (clean start).

### Usage
```bash
# Start
docker compose -f docker-compose-dbs.yml up -d

# Stop and remove volumes (reset data)
docker compose -f docker-compose-dbs.yml down -v
```

## Legacy & Integration
- **`docker-compose-gn4.yml`**: Runs GeoNetwork 4.4, connected to the databases.
- **`docker-compose-gn5.yml`**: Runs a pre-built GeoNetwork 5 image.
- **`docker-compose-web.yml`**: A simple Angular frontend for OGC API Records.
- **`keycloak/docker-compose.yml`**: Keycloak for testing OAuth2/OIDC login (see below).

## Keycloak (OAuth2/OIDC login)

`keycloak/docker-compose.yml` starts Keycloak 18.0.2 on <http://localhost:8180> and imports the `geonetwork` realm from `keycloak/realm/geonetwork-realm.json` on first start.

```bash
# From the repository root: start Keycloak
docker compose -f docker/keycloak/docker-compose.yml up -d

# Run GN5 from the repository root with the Keycloak login enabled
SPRING_CONFIG_ADDITIONAL_LOCATION=file:$PWD/docker/keycloak/application-keycloak.yml \
  ./mvnw spring-boot:run -pl src/apps/geonetwork

# Stop (no volume: this discards all Keycloak changes, the realm is imported again on the next start)
docker compose -f docker/keycloak/docker-compose.yml down
```

Log in at <http://localhost:7979/geonetwork/oauth2/authorization/keycloak>. On the first login, the user is created in the GN database.

- Keycloak admin console: <http://localhost:8180/admin/> (`admin`/`admin`), realm `geonetwork`.
- Client `geonetwork`, secret `geonetwork-secret`. Its client roles are sent in the `roles` claim: `Profile` gives a global profile, `group:Profile` a profile in that group.

| User (password = username) | Client role | GN result |
|---|---|---|
| `gnadmin` | `Administrator` | Administrator |
| `gnuseradmin` | `sample:UserAdmin` | UserAdmin in group `sample` |
| `gneditor` | `sample:Editor` | Editor in group `sample` |

## Connection Details
- **PostgreSQL:** `localhost:5432` (User/Pass: `postgres`/`postgres`)
- **Elasticsearch:** `localhost:9200` (Index: `gn-records`)

> [!NOTE]
> **Linux Docker Users:** `host.docker.internal` is used to allow containers to connect back to the host machine. While this works out-of-the-box on Docker Desktop (Windows/Mac), on native Linux hosts it is resolved using the `extra_hosts` mapping (`host.docker.internal:host-gateway`) specified in the compose files. Ensure you use Docker Compose (v20.10+) to deploy these.

---
Back to [Main README](../README.md)
