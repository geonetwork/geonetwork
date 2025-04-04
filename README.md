# GeoNetwork

Architecture experiment for GeoNetwork 5 development setting up clean Spring Boot application for Java 21 development.

* GeoNetwork 5: spring boot application using fresh dependencies for Java 21 development
* Database: H2 database used for local development, PostgreSQL recommended for production
* Elasticsearch: index and search engine
* GeoNetwork 4: maintain functionality during architecture transition, using Java 11 and Jetty / Tomcat environment

# Get started

**Requirements**:
- Java 21

To run GeoNetwork 5 in dev mode, you first need to run GeoNetwork 4.

1. **Run GeoNetwork 4.4** in your favorite way ( [see GeoNetwork 4 documentation](https://github.com/geonetwork/core-geonetwork/tree/main/software_development#quickstart)) and remind the options to connect to the database and Elasticsearch.


2. **Configure GeoNetwork 5** search application accordingly. For that, edit the `config/application.yml` file. Check the port of Elasticsearch and the database connection settings. 

  Also change the index setting to the following:
```yaml
indexRecordName: gn-records
```
3. **Run GeoNetwork 5** with the following command:
```shell
cd src/app/geonetwork
mvn spring-boot:run
```

Access via localhost:7979 – login as admin/admin.

