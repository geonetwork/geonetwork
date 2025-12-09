RUNNING THE DOCKER CONTAINERS
=============================

This provides four groups of docker containers:

* Elastic Search and PostgreSQL databases, with sample data (`docker-compose-dbs.yml`)
* GeoNetwork 4, connected to the above DBS (`docker-compose-gn4.yml`)
* Simple OGCAPI-Records Web Interface (`docker-compose-web.yml`)
* GeoNetwork 5, connected to the above DBS (`docker-compose-gn5.yml`).  This uses the one built from the GIT branch `main`.

In general, most people will only use `docker-compose-dbs.yml` and run GN5 from either the command line or from and IDE.
If you want to look at the data in GN4 (or import more data), use `docker-compose-gn4.yml`.

The OGCAPI-Records Web Interface (`docker-compose-web.yml`) container is just for a nicer (but very very simple) HTML application for OGCAPI-Records.
The GN5 (`docker-compose-gn5.yml`) is just to be able to quickly run a GN5.  Most people will not use this.

To run Elastic Search and PostgreSQL databases:
```bash
docker-compose -f docker-compose-dbs.yml up -d
```

To run GN4:
```bash
docker-compose -f docker-compose-gn4.yml up -d
```

To run the OGCAPI-Records Web Interface (connected to an already running GN5):
```bash
docker-compose -f docker-compose-web.yml up -d
```

To run GN5:
```bash
docker-compose -f docker-compose-gn5.yml up -d
```
 
## RUNNING GN5 FROM THE COMMAND LINE OR IDE


You will also also need to run GN5 (in your IDE or with maven).  

USE JAVA 21.

```
git clone https://github.com/geonetwork/geonetwork.git

cd geonetwork
mvn clean install -Drelax

cd src/apps/ogcapi-records
mvn spring-boot:run
```

For use in Intellij:
1. checkout GN5 and build (see above)
2. In Intellij; <br>
    a. load the GN5 pom  <br>
    b. create a new spring-boot run/debug configuration <br>
    c. run the configuration

## NOTES
 

`docker-compose-dbs.yml` will run:
1. postgresql (with sample data)<br>
    PORT: 5432<br>
    USER: postgres<br>
    PASS: postgres<br>
    DB: geonetwork
2. elastic (with sample data)<br>
    PORT: 9200<br>
    no user/pass<br>
    INDEX: gn-records

`docker-compose-gn4.yml` will run:
1. GN4 running at http://localhost:8080/geonetwork
2. It will connect to the above database (running on the docker host)

`docker-compose-web.yml` will run:
1. Angular application one port 80  (http://localhost)
2. Connecting to a GN5 running OGCAPI-Records on at http://localhost:7979/geonetwork/ogcapi-records  (running on the docker host)

`docker-compose-gn5.yml` will run:
1. GN5 running at http://localhost:7979/geonetwork
2. It will connect to the above database (running on the docker host)



To delete any changes to PostgreSQL and Elastic, erase their volumes:

```
 docker-compose -f docker-compose-dbs.yml down --volume
```
 

Helpful URLs
============

OGCAPI-RECORDS
--------------

NOTE: its really helpful to have a browser extension to view formatted JSON data!

landing page: 
http://localhost:7979/geonetwork/ogcapi-records

list collections:
http://localhost:7979/geonetwork/ogcapi-records/collections

info about the "main" collection: 
http://localhost:7979/geonetwork/ogcapi-records/collections/3bef299d-cf82-4033-871b-875f6936b2e2


query endpoint for  "main" collection:
http://localhost:7979/geonetwork/ogcapi-records/collections/3bef299d-cf82-4033-871b-875f6936b2e2/items

example record in "main" collection (ogcapi-record json):
http://localhost:7979/geonetwork/ogcapi-records/collections/3bef299d-cf82-4033-871b-875f6936b2e2/items/004571b9-4649-42b3-9c28-a8cdc2bf53c7


example record in "main" collection (dcat: eu-dcat-ap):
http://localhost:7979/geonetwork/ogcapi-records/collections/3bef299d-cf82-4033-871b-875f6936b2e2/items/004571b9-4649-42b3-9c28-a8cdc2bf53c7?f=application%2Frdf%2Bxml&profile=http%3A%2F%2Fgeonetwork.net%2Fdef%2Fprofile%2Feu-dcat-ap


List queryables:
http://localhost:7979/geonetwork/ogcapi-records/collections/3bef299d-cf82-4033-871b-875f6936b2e2/queryables


list facets:
http://localhost:7979/geonetwork/ogcapi-records/collections/3bef299d-cf82-4033-871b-875f6936b2e2/facets

list sortables:
http://localhost:7979/geonetwork/ogcapi-records/collections/3bef299d-cf82-4033-871b-875f6936b2e2/sortables


OpenAPI (swagger) doc:
http://localhost:7979/v3/api-docs


Web App
-------

http://localhost

ELASTIC
-------

Index Definition:

http://localhost:9200/gn-records

View actual elastic json index:

[http://localhost:9200/gn-records/_search?pretty=true&q=\*:\*&size=500](http://localhost:9200/gn-records/_search?pretty=true&q=*:*&size=500)

## CONNECTING TO POSTGRESQL
 
```
  psql -h localhost -U postgres -p 5432 -d geonetwork
```

password is `geonetwork`



