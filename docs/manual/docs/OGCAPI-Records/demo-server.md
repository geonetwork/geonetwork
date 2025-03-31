## Setting up a demo OGCAPI-Records

In order to run OGCAPI-Records, you will need:

1. PostgreSQL/PostGIS database
2. Elastic Search (recommend v8.14)
3. GN4
4. GN5

### Setting up PostgreSQL

This container (`kartoza/postgis:16-3.4`) is for an M1 mac - use a different docker container for an X64 machine.

```
docker run -p 5432:5432 -e POSTGRES_PASS=postgres -e POSTGRES_USER=postgres   kartoza/postgis:16-3.4
PGPASSWORD=postgres psql -h localhost -U postgres -d template1 -c "create database gn;"
PGPASSWORD=postgres psql -h localhost -U postgres -d gn -c "create extension postgis;"


PGPASSWORD=postgres psql -h localhost -U postgres -d gn -c "CREATE USER \"www-data\" WITH SUPERUSER PASSWORD 'www-data';"
```

This will:

1. run the `docker` PostgreSQL/PostGIS container (available on PORT 5432).  The admin user will be postgres/postgres.
2. create a database called `gn`
3. add the PostGIS extension to the database
4. add a super user with name `www-data` and password `www-data`.

These settings should match what is in your GN5 `application.yml` file.

To run a `psql` session:

```
PGPASSWORD=postgres psql -h localhost -U postgres -d gn 
```

### Setup Elastic Search (recommend v8.14)

This will run an elastic search docker container (v8.14.0) on port 9200.

```
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" -e "xpack.security.enabled=false" -e "xpack.security.enrollment.enabled=false" docker.elastic.co/elasticsearch/elasticsearch:8.14.0
```

This does not have any indexes in it - when you run GN4 (see below) it will add the indexes.

Here are some interesting URLs:

1. [http://localhost:9200/](http://localhost:9200/) Cluster URL
2. [http://localhost:9200/gn-records/_search?pretty=true&q=*:*](http://localhost:9200/gn-records/_search?pretty=true&q=*:*) All records in the index
3. [http://localhost:9200/gn-records/_search?pretty=true&q=_id:ce6dc729-7ec0-4ba9-baf8-4bc1525120c8](http://localhost:9200/gn-records/_search?pretty=true&q=_id:ce6dc729-7ec0-4ba9-baf8-4bc1525120c8) Show a particular record (by metadata ID)
4. [http://localhost:9200/gn-records](http://localhost:9200/gn-records) The index definition (advanced users only!)

### Setup GN4


Run GeoNetwork 5 connected to the PostgreSQL/PostGIS database.  The easiest way to do this is to checkout and build GN4 from source.

```
cd web
mvn jetty:run -Dgeonetwork.db.type=postgres-postgis -Djdbc.database=gn -Djdbc.username=www-data -Djdbc.password=www-data -Djdbc.host=localhost -Ddb.port=5432 -Djdbc.port=5432
```

#### DEMO GN4 setup

Here are some steps to take to setup some data in GN4.  This demo will include the 5 iso19139 sample records supplied with GN4 and two extra ones we will use to annotate geonetwork portals (Also called `sources` in GeoNetwork and `collections` or `catalogs` in OGCAPI-Records).

We then create a sub-portal (called "subportal") and attach the `subportal.xml` record to it.

We then attach the `main.xml` record to the main portal.

##### Setup Records

1. Login as admin [http://localhost:8080/geonetwork](http://localhost:8080/geonetwork)
2. Go to `Admin console` -> `Metadata and Templates`
    * Tick `Geographic information -- Metadata (iso19139:2007) (iso19139)` and press `Load samples for selected standards`
3. Download [main.xml](files/main.xml) and [subportal.xml](files/subportal.xml) which are two extra sample metadata records.
4. Go to `Contribute` -> `Import New Records`
    * Tick `Publish`
    * Choose the `main.xml` you downloaded (above) (use `Choose or drop resource here`)
    * Press `Import`
5. Go to `Contribute` -> `Import New Records`
    * Tick `Publish`
    * Choose the `subportal.xml` you downloaded (above) (use `Choose or drop resource here`)
    * Press `Import`

At this point you should have 7 records in your [GN4 installation](http://localhost:8080/geonetwork/srv/eng/catalog.search#/search).

##### Setup Portals

1. Login as admin [http://localhost:8080/geonetwork](http://localhost:8080/geonetwork)
2. Go to `Admin console` -> `Settings` and choose `Sources` on the left column.
3. Press "Add portal"
    * `Identifier` - subportal
    * `Name` - SubPortal
    * `Search Filter` - leave this blank, you can modify this later if you want
    * `Records to use for GetCapabilities` - type "sub" and then choose the "GeoCat Demo OGCIAPI sub-portal" record
    * Choose a logo from the `Logo Selection`
    * Press "Save"
4. On the Left hand column, choose `Settings`
    * Go down to the "Catalog Services for the Web"
    * In the `Record to use for GetCapabilities`, type "4e2e361b-02cf-499f-b16e-7eff87925e40"
    * Press `Save Settings` (very top)


NOTE: There's no way to edit the GeoNetwork Source for the main-portal, so we use the CSW GetCapabilities link instead!


### Setup GN5

You should be able to just run GeoNetwork 5 (from source).  

1. Checkout GN5 from [GitHub](https://github.com/geonetwork/geonetwork)
2. Verify that the `application.xml` (in `config/`)
2. Build (with java21): `mvn clean install -DskipTests`
3. Run with:

```
cd src/app/geonetwork
mvn spring-boot:run
```

##### Running the DEMO

0. Go to [http://localhost:7979/v3/api-docs?f=json](http://localhost:7979/v3/api-docs?f=json) to see the [OpenAPI](https://www.openapis.org/) (swagger) documentation.
1. Go to [http://localhost:7979/ogcapi-records/?f=json](http://localhost:7979/ogcapi-records/?f=json)
    * This is the OGCAPI-Records Landing Page
    * The title should be "GeoCat Demo OGCIAPI Server"
    * Notice that it has an extra `catalogInfo` which contains information taken from `main.xml`
2. Go to [http://localhost:7979/ogcapi-records/collections?f=json](http://localhost:7979/ogcapi-records/collections?f=json)
    * This is the OGCAPI-Records Collections ("Catalogs") page
    * There should be two collections - "GeoCat Demo OGCIAPI Server" and "GeoCat Demo OGCIAPI sub-portal"
    * Notice that the title (and contact) information is coming from the linked metadata records (`main.xml` and `subportal.xml`)
3. Go to [http://localhost:7979/ogcapi-records/collections/subportal?f=json](http://localhost:7979/ogcapi-records/collections/subportal?f=json)
    * This is the OGCAPI-Records Collection ("Catalog") page for the sub-portal collection
    * The metadata about this is coming from the attached `subportal.xml` record
4. Go to [http://localhost:7979/ogcapi-records/collections/subportal/queryables?f=json](http://localhost:7979/ogcapi-records/collections/subportal/queryables?f=json)
    * This is the OGCAPI-Records Queryables page for the sub-portal collection
    * This are all the "extra" ways you can query the collection
    * Advanced users can see how this is configured [in queryables.json](https://github.com/geonetwork/geonetwork/pull/74/files#diff-9185e89c379fe0a8287daf7c7b09389dcf35bb8667e885fc607326eb48dd660a).
5. Go to [http://localhost:7979/ogcapi-records/collections/subportal/items?f=json](http://localhost:7979/ogcapi-records/collections/subportal/items?f=json)
    * This is the OGCAPI-Records Items page for the sub-portal collection
    * This returns a [GeoJSON](https://geojson.org/) feature collection JSON document
    * Since we haven't put a filter on the sub-portal, all 7 metadata records in the GN4 catalog will be shown
6. Go to [http://localhost:7979/ogcapi-records/collections/subportal/items/da165110-88fd-11da-a88f-000d939bc5d8?f=json](http://localhost:7979/ogcapi-records/collections/subportal/items/da165110-88fd-11da-a88f-000d939bc5d8?f=json)
    * This is the OGCAPI-Records Items page for single record in the sub-portal collection
    * This is the JSON representation of the ISO19193 record (in the format specified by the OGCAPI-Records specification and in GeoJSON format)
    * It also contains the underlying record, in XML, in the the `metadataRecordText` property



Here are some queries you can run (see the [queryables](https://github.com/geonetwork/geonetwork/pull/74/files#diff-9185e89c379fe0a8287daf7c7b09389dcf35bb8667e885fc607326eb48dd660a)):

* Find `contacts` that contain "jody" [http://localhost:7979/ogcapi-records/collections/subportal/items?contacts=jody&f=json](http://localhost:7979/ogcapi-records/collections/subportal/items?contacts=jody&f=json)
    * or [jeroen](http://localhost:7979/ogcapi-records/collections/subportal/items?contacts=jeroen&f=json)
    * or by area code [250](http://localhost:7979/ogcapi-records/collections/subportal/items?contacts=250&f=json)
    * or by city [victoria](http://localhost:7979/ogcapi-records/collections/subportal/items?contacts=victoria&f=json)
* Find by `id` [http://localhost:7979/ogcapi-records/collections/subportal/items?id=9bac358b-11ec-4293-aeef-5a077b778412&f=json](http://localhost:7979/ogcapi-records/collections/subportal/items?id=9bac358b-11ec-4293-aeef-5a077b778412&f=json)
* Find by `organization` [http://localhost:7979/ogcapi-records/collections/subportal/items?organization=geocat&f=json](http://localhost:7979/ogcapi-records/collections/subportal/items?organization=geocat&f=json)
* Find by `keyword` [http://localhost:7979/ogcapi-records/collections/subportal/items?keywords=africa&f=json](http://localhost:7979/ogcapi-records/collections/subportal/items?keywords=africa&f=json)
* Find records created in 2008 [http://localhost:7979/ogcapi-records/collections/subportal/items?created=2007-01-01/2007-12-31&f=json](http://localhost:7979/ogcapi-records/collections/subportal/items?created=2007-01-01/2007-12-31&f=json)

NOTE: See the [OGCAPI-Records Specification](https://ogcapi.ogc.org/records/#:~:text=OGC%20API%20%2D%20Records%20is%20a,resources%20(metadata)%20are%20exposed.) for more info on querying.

NOTE:  You can change the above URLs to have 'f=html`.  However, the current HTML page is trivial - just showing the JSON result.