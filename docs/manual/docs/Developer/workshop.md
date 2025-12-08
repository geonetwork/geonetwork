# GN5 Workshops

There are two workshops - workshop1 and workshop2.  Both of the workshops are quite simple.

| Workshop | Purpose |
| -------- | ------- |
| `workshop1` | This is a very simple (hard-coded) `WorkshopController` that produces some information about the metadata schemas and number of records associated with each schema.  Most developers should be able to easily understand this.  It builds up complexity from the `workshop1` to `workshop1e` endpoints. |
| `workshop2` | This accomplishes the same things as `workshop1`, however, it does this is more complicated architecture.  It autogenerates controller definitions and controller response objects from a OpenAPI document.  It lays out code in three modules - `app`, `module`, and `shared`.  This is how most of the larger GN5 modules (like OGCAPI-Records) are created. | 

## Pre Setup for Workshops

In order to run the workshops, you will need a running PostgreSQL and Elastic Search database filled with some data.  There are two ways to do this:

1. Using PostgreSQL and Elastic Search Containers Pre-Filled with Data
2. Using GN4 to Populate Metadata Records

### Using Pre-Populated PostgreSQL and Elastic Search Containers

To run a pre-populated PostgreSQL and Elastic Search databases:
```bash
cd docker
docker-compose -f docker-compose-dbs.yml up -d
# wait for containers to fully startup
```

This will contain about 40 sample metadata records - taken from the GN-UI project.

### Use GN4 to Populate Empty PostgreSQL and Elastic Search Containers

This open will create an empty PostgreSQL and Elastic Search databases.  You can then use GN4 to populate metadata.

To run empty PostgreSQL and Elastic Search databases:
```bash
cd docker
docker-compose -f docker-compose-dbs-empty.yml up -d
# wait for containers to fully startup
```

To run GN4 connected to those database:

```bash
docker-compose -f docker-compose-gn4.yml up -d
# wait for GN4 to startup
```

After the GN4 container has started, go to http://localhost:8080/geonetwork.  From there, you can do one of these:

* Login to GN4 as admin, import the `sample-gn-data-for-import.zip` data (30 records from the `GN-UI` project)
* Login to GN4 as admin, import the ISO19193 sample data
* Login to GN4 as admin, harvest from another server
* Login as a user, create your own sample data

### Docker Containers Summary

| Filename | Purpose |
| -------- | ------- |
| `docker-compose-dbs-empty.yml` | Runs **EMPTY** PostgreSQL and Elastic |
| `docker-compose-dbs.yml` | Runs **PRE-POPULATED** PostgreSQL and Elastic  |
| `docker-compose-gn4.yml` | Runs GN4 (requires running PostgreSQL and Elastic) |
| `docker-compose-gn5.yml` | Runs GN5 (requires running PostgreSQL and Elastic) |
| `docker-compose-web.yml` | Runs Angular application for OGCAPI-RECORDS GUI (requires running GN5) |




## Workshop 1

Before starting Workshop 1, ensure that you have PostgreSQL and Elastic running, and have some sample data loaded.

```bash
git clone https://github.com/geonetwork/geonetwork.git
cd geonetwork
git checkout dev # might not be necessary
mvn clean install -Drelax
cd src/app/workshop/workshop1
```

From here you can:

1. Run GN5 from the command line

    ```bash
    mvn spring-boot:run
    ```

2. OR, run it from your IDE

### Endpoints

* [http://localhost:7979/geonetwork/workshop1](http://localhost:7979/geonetwork/workshop1)
* [http://localhost:7979/geonetwork/workshop1b](http://localhost:7979/geonetwork/workshop1b)
* [http://localhost:7979/geonetwork/workshop1c](http://localhost:7979/geonetwork/workshop1c)
* [http://localhost:7979/geonetwork/workshop1d](http://localhost:7979/geonetwork/workshop1d)
* [http://localhost:7979/geonetwork/workshop1e](http://localhost:7979/geonetwork/workshop1e)

OpenAPI (swagger) endpoints (for use with the `workshop1e` endpoint):

 * [http://localhost:7979/geonetwork/v3/api-docs?f=json](http://localhost:7979/geonetwork/v3/api-docs?f=json) JSON version of the OpenAPI document
 * [http://localhost:7979/geonetwork/swagger-ui/index.html](http://localhost:7979/geonetwork/swagger-ui/index.html) HTML version of teh OpenAPI document

| Endpoint | What it does |
| -------- | ------------ |
| `workshop1` | returns hardcoded trivial hello-world `String` |
| `workshop1b` | returns hardcoded `Map<String,Long>` |
| `workshop1c` | returns hardcoded `GeoNetworkMetadataSummary ` |
| `workshop1d` | returns with real DB results |
| `workshop1e` |same as workshop1d, but with openapi information |

### Code

All the code for this is in the `WorkshopController` class.  

## Workshop 2

The main differences between `workshop1_app` and `workshop2_app` are:

1. Instead of hand-coding our controllers and the controllers response object, we autogenerate them from OpenAPI (swagger) `.yml` files
2. We use the full `src/app`, `src/module`, and `src/shared` source layout
3. We use more spring-oriented Repository access (DB)


### Source code layout (`src/app`, `src/module`, and `src/shared`)

| Location | Content |
| -------- | ------- |
| `src/app` | This is the application - typically, it only contains a `pom.xml` to manage "pulling in" dependencies |
| `src/module` | This is where the spring controllers are defined.  In this example it contains two modules - one that auto-generates controller interface and response objects from OpenAPI `.yml` documents.  It also contains a trivial implementation of the controller API that "hands off" the work to a class in `src/shared`  |
| `src/shared`| This is where the actual code goes.  In this case, it accesses the database and creates the actual response object.  It can use other GN5 code by depending on other `/src/shared` modules.  |

# See Other Areas 

* [New Application Development](new-app.md)