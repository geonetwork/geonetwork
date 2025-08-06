# GeoNetwork

Architecture experiment for GeoNetwork 5 development setting up clean Spring Boot application for Java 21 development.

* GeoNetwork 5: spring boot application using fresh dependencies for Java 21 development
* Database: shared PostgreSQL database
* Elasticsearch: index and search engine
* GeoNetwork 4: maintain functionality during architecture transition, using Java 11 and Jetty / Tomcat environment

# Get started

**Requirements**:

- Java 21 / Maven Development Environment ([sdkman](https://sdkman.io) recommended)
- PostgreSQL Database
- Elasticsearch
- GeoNetwork 4, which requires Java 11 Enviornment

To run GeoNetwork 5 in dev mode, you first need to run GeoNetwork 4 with shared data base:

1. **Run GeoNetwork 4.4** in your favorite way ( [see GeoNetwork 4 documentation](https://github.com/geonetwork/core-geonetwork/tree/main/software_development#quickstart)) and remind the options to connect to the database and Elasticsearch.
   
   Example Development Environment:
   
   ```bash
   cd docker
   docker compose -f docker-compose-base.yml -f docker-compose-dev.yml up -d
   ```
   
   Access GN4: http://localhost:8080/geonetwork/ (login `admin`/`admin`)
   

2. **Configure GeoNetwork 5** search application by editing the file `config/application.yml`.
   
   * Check the port of Elasticsearch and the database connection settings. 

   * Check the index setting:
  
     ```yaml
     indexRecordName: gn-records
     ```
   
   The example development environment does not require any configuration changes.

3. **Build GeoNetwork 5** with following command:
   
   ```bash
   mvn clean install
   ```
   
   
3. **Run GeoNetwork 5** with the following command:

   ```bash
   cd src/apps/geonetwork
   mvn spring-boot:run
   ```
   
   Access GN5: http://localhost:7979 (login `admin`/`admin`)

