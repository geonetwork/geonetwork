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

## Connection Details
- **PostgreSQL:** `localhost:5432` (User/Pass: `postgres`/`postgres`)
- **Elasticsearch:** `localhost:9200` (Index: `gn-records`)

> [!NOTE]
> **Linux Docker Users:** `host.docker.internal` is used to allow containers to connect back to the host machine. While this works out-of-the-box on Docker Desktop (Windows/Mac), on native Linux hosts it is resolved using the `extra_hosts` mapping (`host.docker.internal:host-gateway`) specified in the compose files. Ensure you use Docker Compose (v20.10+) to deploy these.

---
Back to [Main README](../README.md)
