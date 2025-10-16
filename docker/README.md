# 🐳 Docker Compose for GeoNetwork 5 Development & Testing
This project provides Docker Compose configurations to simplify local development and testing of GeoNetwork 5. It helps set up all necessary services without dealing with infrastructure dependencies manually.

You will, typically, run GN5 from the command line:

```bash
cd src/apps/geonetwork
mvn spring-boot:run
```

There are three typical setups:

1. Main Development Setup (recommended for development).  This runs PostgreSQL, Elastic, and GeoNetwork 4.  GeoNetwork 4 runs independently of GN5 - you can access GN4 [directly](http://localhost:8080/geonetwork) and login normally.  PostgreSQL and Elastic are shared between GN4 and GN5.
2. GN4 Proxied by GN5 (recommended for actual deployments).  This also runs PostgreSQL, Elastic, and GeoNetwork 4.  However, GN4 is proxied behind GN5 and you **must** (a) login to GN5 (b) access GN4 via GN5 (http://localhost:7979/geonetwork/svr).  You cannot logon directly to GN4.
3. Just GN4 (recommended for testing and development).  In this case, you will need to run PostgreSQL and Elastic yourself.  This only runs GN4, and is useful to quickly "spin-up" GN4 to view/edit the settings/records (i.e. PostgreSQL and Elastic).

Most people will want to use the first ("Main Development Setup").


 
## ⚙️ Main Development Setup (docker-compose-dev.yml)
This composition includes:

| Service       |Functionality| Port                                     |
|:--------------|:-------------------------|:-----------------------------------------|
| GeoNetwork 4  |GeoNetwork 4 "sidecar" for GN5 (used for harvesting etc.)| [8080](http://localhost:8080/geonetwork) |
| database      |PostgreSQL — Database backend for GeoNetwork| 5432                                     |
| elasticsearch |Elasticsearch — Search engine backend| [9200](http://localhost:9200)            |


This setup is ideal for developers working on GN5 who need a quick and clean environment.

Typically, you will run GeoNetwork 5 - locally - from the command line on port [7979](http://localhost:7979/geonetwork/home).

This will not have any 

▶️ Start the stack
From the /docker directory:

```bash
docker compose -f docker-compose-base.yml -f docker-compose-dev.yml up -d
```

⏹️ Stop the stack

```bash
docker compose -f docker-compose-base.yml -f docker-compose-dev.yml down
```

🚮 Clean up the stack

This will delete the volumes associated with the container - deleting all data in PostgreSQL and Elastic.

```bash
docker compose -f docker-compose-base.yml -f docker-compose-dev.yml down --volumes
```

Access Elastic at http://localhost:9200/

Access GN4 at http://localhost:8080/geonetwork

When running GN5 locally (via the command line), it will likely be at http://localhost:7979/geonetwork/home

Other Configurations
--------------------


### GN4 Proxied by GN5

This will run GN4 configured to accept GN5 logins.  When you login to GN5 and then access GN4 (via GN5's proxy) you will be logged in.

If you run this configuration, you can **only** login to GN4 via GN5 and GN4 will **not** have any login buttons.
Ensure that your GN5 configuration is setup to proxy to GN4 (including authentication).

> [!IMPORTANT]
> These configurations are created for the sole purpose of testing GeoNetwork 5 locally. In the development setup Geonetwork 4 is accessible on port 8080, and this should be avoided in a production environment. For more details, please check [JWT Headers](https://github.com/geonetwork/geonetwork/blob/main/docs/manual/docs/GN4-Integration/index.md#setting-up-gn5-and-gn4).


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

### ElasticSearch

Here are some useful elastic search commands.

| URL                                                                                                                                            | Meaning                                                 |
|:-----------------------------------------------------------------------------------------------------------------------------------------------|:--------------------------------------------------------|
| http://localhost:9200/gn-records/?pretty                                                                                                       | See the Elastic JSON Index Properties and Configuration |
| [http://localhost:9200/gn-records/_search?pretty=true&q=*:&ast;&size=999](http://localhost:9200/gn-records/_search?pretty=true&q=*:*&size=999) | See first 999 records in the Index                      |
| http://localhost:9200/gn-records/_search?pretty=true&q=uuid:c186ad88-a47f-4391-8fb5-a721e01a36a1                                               | Search for a record by UUID                             |
| http://localhost:9200/_cluster/settings?include_defaults=true                                                                                  | Cluster Settings                                        |
| http://localhost:9200/_cluster/health?pretty                                                                                                   | Cluster Health                                          |


