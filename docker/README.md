# 🐳 Docker Compose for GeoNetwork 5 Development & Testing
This project provides Docker Compose configurations to simplify local development and testing of GeoNetwork 5. It helps set up all necessary services without dealing with infrastructure dependencies manually.

## ⚙️ Development Setup (docker-compose-dev.yml)
This composition includes:


|Service|Functionality|Port|
|:-----------|:-------------------------|:-----|
|geonetwork|GeoNetwork 4 "sidecar" for GN5 (used for harvesting etc.)|8080|
|database|PostgreSQL — Database backend for GeoNetwork|5432|
|elasticsearch|Elasticsearch — Search engine backend|9200|

This setup is ideal for developers working on GN5 who need a quick and clean environment.

▶️ Start the stack
From the /docker directory:

```bash
docker compose -f docker-compose-dev.yml up -d
```

⏹️ Stop the stack

```bash
docker compose -f docker-compose-dev.yml down
```

## 🚀 Full Stack Setup (docker-compose-full.yml)
Before running this setup, make sure to build the GeoNetwork 5 Docker image (instructions below).

This composition includes:


|Service|Functionality|Port|
|:-----------|:-------------------------|:-----|
|geonetwork5|GeoNetwork 5|7979|
|geonetwork4|GeoNetwork 4 "sidecar" for GN5|8080|
|database|PostgreSQL — Database backend|5432|
|elasticsearch|Elasticsearch — Search engine backend|9200|

This setup is intended for full-feature testing of GN5 in an environment that mimics real-world dependencies.

▶️ Start the stack

```bash
docker compose -f docker-compose-full.yml up -d
```

⏹️ Stop the stack

```bash
docker compose -f docker-compose-full.yml down
```

## 🛠️ Build the GeoNetwork 5 Docker Image
To build the GN5 image using Spring Boot:

Navigate to the project root:
/src/app/geonetwork

Run the following command:

```bash
mvn spring-boot:build-image
```

This creates a Docker image that will be used by the geonetwork5 service in the full stack.
