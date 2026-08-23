# GeoNetwork 5

GeoNetwork 5 is an architecture experiment setting up a clean Spring Boot application for Java 21 development.

## Quick Start

### 1. Start Dependencies (Docker)
Ensure you have Docker installed, then start the database and search engine:

```bash
# Start with sample data
docker compose -f docker/docker-compose-dbs.yml up -d

# OR start with an empty database
docker compose -f docker/docker-compose-dbs-empty.yml up -d
```
See [docker/README.md](docker/README.md) for more infrastructure details.

### 2. Build and Run
Build the project and start the main application:

```bash
# Build (skipping slow tests/QA for speed)
./mvnw clean install -DskipTests

# Run the main application
./mvnw spring-boot:run -pl src/apps/geonetwork
```

### 3. Access the Application
- **Main App:** [http://localhost:7979/geonetwork](http://localhost:7979/geonetwork) (Login: `admin`/`admin`)
- **API Docs:** [http://localhost:7979/v3/api-docs](http://localhost:7979/v3/api-docs)

## Project Structure
- `src/apps`: Main application entry points.
- `src/modules`: Functional catalog modules.
- `src/shared`: Core domain and utilities.
- `docs/`: Technical documentation.
