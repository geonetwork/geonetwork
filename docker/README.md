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

