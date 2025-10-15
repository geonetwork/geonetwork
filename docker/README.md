# 🐳 Docker Compose for GeoNetwork 5 Development & Testing
This project provides Docker Compose configurations to simplify local development and testing of GeoNetwork 5. It helps set up all necessary services without dealing with infrastructure dependencies manually.

> [!IMPORTANT]
> These configurations are created for the sole purpose of testing GeoNetwork 5 locally. In the development setup Geonetwork 4 is accessible on port 8080, and this should be avoided in a production environment. For more details, please check [JWT Headers](https://github.com/geonetwork/geonetwork/blob/main/docs/manual/docs/GN4-Integration/index.md#setting-up-gn5-and-gn4).



## ⚙️ Development Setup (docker-compose-dev.yml)
This composition includes:


|Service|Functionality|Port|
|:-----------|:-------------------------|:-----|
|geonetwork4|GeoNetwork 4 "sidecar" for GN5 (used for harvesting etc.)|8080|
|database|PostgreSQL — Database backend for GeoNetwork|5432|
|elasticsearch|Elasticsearch — Search engine backend|9200|

This setup is ideal for developers working on GN5 who need a quick and clean environment.

▶️ Start the stack
From the /docker directory:

```bash
docker compose -f docker-compose-base.yml -f docker-compose-dev.yml up -d
```

⏹️ Stop the stack

```bash
docker compose -f docker-compose-base.yml -f docker-compose-dev.yml down
```

Access Elastic at http://localhost:9200/

Access GN4 at http://localhost:8080/geonetwork

Other Configurations
--------------------


### GN4 Proxied by GN5

This will run GN4 configured to accept GN5 logins.  When you login to GN5 and then access GN4 (via GN5's proxy) you will be logged in.

If you run this configuration, you can **only** login to GN4 via GN5 and GN4 will **not** have any login buttons.
Ensure that your GN5 configuration is setup to proxy to GN4 (including authentication).

```bash
docker compose -f docker-compose-base.yml -f docker-compose-dev.yml -f  docker-compose-proxy.yml up -d
```

### Just GN4

*(advanced only)*

If you just want to quickly create a GN4 that will connect to an already running PostgreSQL and Elastic:

```bash
docker compose   -f  docker-compose-gn4-only.yml up -d
```

PostgreSQL should be running on the local (docker host) machine on port 5432, database `geonetwork`, username `geonetwork`, and password `geonetwork`.

Elastic should be running on the local (docker host) machine on port 9200. 


## Helpful Commands

### Remove volumes
 
This will remove the volumes associated with the containers.  This is useful to reset your postgresql and elastic databases.

```bash
docker compose -f docker-compose-base.yml -f docker-compose-dev.yml down --volumes 
```

### Connect to Postgresql via `psql`:

```bash
docker exec -it development-database-1  psql -h localhost -U geonetwork
```

## 🚀 Full Stack Setup
This composition includes also GeoNetwork 5:


|Service|Functionality|Port|
|:-----------|:-------------------------|:-----|
|geonetwork5|GeoNetwork 5|7979|
|geonetwork4|GeoNetwork 4 "sidecar" for GN5|8080|
|database|PostgreSQL — Database backend|5432|
|elasticsearch|Elasticsearch — Search engine backend|9200|

This setup is intended for full-feature testing of GN5 in an environment that mimics real-world dependencies.

▶️ Start the stack

```bash
docker compose -f docker-compose-base.yml -f docker-compose-full.yml up -d
```

⏹️ Stop the stack

```bash
docker compose -f docker-compose-base.yml -f docker-compose-full.yml down
```

