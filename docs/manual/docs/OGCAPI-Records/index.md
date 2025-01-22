# GeoNetwork 5 OGCAPI-Records {#toc}

GeoNetwork 5 has an [OGCAPI-Records](https://ogcapi.ogc.org/records/#:~:text=OGC%20API%20%2D%20Records%20is%20a,resources%20(metadata)%20are%20exposed.) implementation.

## Run the OGCAPI-Records Demo Server

The best way to learn how to setup an OGCAPI-Records Server is to run the demo server.  This tutorial explains how to setup everything - PostgreSQL, Elastic, GeoNetwork 4, and GeoNetwork 5.

[Demo Server Tutorial](demo-server.md)

## OGCAPI-Records Features

The GeoNetwork 5 OGCAPI-Records module has several features that aren't standard in [OGCAPI-Records Specification](https://ogcapi.ogc.org/records/#:~:text=OGC%20API%20%2D%20Records%20is%20a,resources%20(metadata)%20are%20exposed.).

### Extra Features

1. If you connect the main portal to a [service metadata record](./demo-server.md#setup-portals), then the landing page will have a `catalogInfo` property with a `catalog.yaml` (as defined by OGCAPI-Records) object describing the catalog.
2. When a catalog (collection) is described (for example, the `landing page`, `/collections`, or `/collections/<collectionID>` pages) and the collection is attached to a [service metadata record](./demo-server.md#setup-portals), then there will be a `geoNetworkElasticIndexRecord` property with the underlying Elastic Index Record for that metadata record.
3. When you view a metadata record (for example, the `/collections/<collectionID>/items` or  `/collections/<collectionID>/items/<itemID>` pages), then;
    * There will be a `metadataRecordText` property with the text of the metadata record
    * There will be a `geoNetworkElasticIndexRecord` property with the underlying Elastic Index Record for that metadata record.
4. When doing a text search ("`q`"), you can add `<queryable Property Name>:<search value>` and it will convert that into a queryables search
    * For example, `http://localhost:7979/ogcapi-records/collections/subportal/items?q=portal%20contacts:geocat` is equivalent to  `http://localhost:7979/ogcapi-records/collections/subportal/items?q=portal&contacts=geocat`
    * This was done because most OGCAPI-Records clients do **not** support queryables. 
5. Security.  The logged in user can only see certain records (see `ElasticWithUserPermissions.java`).

### Differences with the Specification

There are some minor differences with the specification - these are mostly due to difficulties in the OGCAPI-Records OpenAPI .yaml (especially with `oneOf` OpenAI definitions).

Please see the comments in the README.md and .yaml files in the code autogeneration module (`src/modules/ogcapi-records/ogcapi-records-openapi-autogen/src/ogc-openapi-schema`).


1. The specification allows `ID`s to be either integers or strings.  We support incoming strings or integers, but they are always outputted as strings.
2. `bbox.yaml` slightly simplified
3. `time.yaml` slightly simplified by only having one time regex instead of several in a `oneOf`.
4. The `itemtype` property can either be a list of a single value.  This was simplified to be only a simple string property (no list).  This was originally simplified to be a list (most compatible), however, some clients do not accepts an `itemType` list so it was changed to be a single value.

There are other, minor changes.  Please report an issue if you see one.

Some of the conversions from the Elastic JSON Index Record to a GeoJSON record (or `catalog.yaml` object) are simplified and will need ongoing community discussion to ensure they are the best possible outcome.

## OGCAPI-Records Configuration and Deployment

Most of the configuration for the OGCAPI-Records module are in the `application.yaml` (key: `geonetwork: openapi-records:`).  There are two sections - `links` and `search`.

### Links Section

The links section defines how the different OGCAPI-Records object have links in them.  Its unlikely you will need to modify this, however, there are two properties that will change for [deployments](#ogcapi-records-configuration-and-deployment).


| property | meaning |
| -------- | ------- |
|ogcApiRecordsBaseUrl|This the base url of the GN5 OGCAPI-Records installation.  It defaults to `http://localhost:7979/ogcapi-records/`. <br>This is useful so links point to the correct location even if your OGCAPI-Records server is behind a reverse proxy.|
| gnBaseUrl |This is the base URL used for getting icons from GeoNetwork.  It defaults to `http://localhost:7979/geonetwork`.  You can either point to the [GN5 proxied GN4](../GN4-Integration/index.md) or directly to a running GN4.|

 

### Search Section

The search section defines some configuration for how he GN5 OGCAPI-Record interacts with Elastic.

| property | meaning |
| -------- | ------- |
| queryFilter  | This query filter is always added to all queries.  The default value is `+isTemplate:n` (do not return metadata record templates). |
|  queryBase | When executing a normal `q=...` query, this defines how we do an elastic search.  The default value is `'(any.\*:(${any}) OR resourceTitleObject.\*:(${any})^2)'` (search `any` and `title`.  Give priority to `title` matches).| 
| trackTotalHits | Should the elastic query track total hits?  Tracking total hits is expensive, however, is needed for paging. <br>The default value is `true`.| 
| sources|What elastic index json record sources (properties) should be retrieved during a query?   The default value is `[*]` (return all sources).|

## OGCAPI-Records Development

In order to make deployments easier, a few environment variables have been defined:

|    Var            |    default                      |     meaning|
| -------- | ------- |  ------- |
|  GN5_BASE_URL     |  http://localhost:7979/         |   Where is GN5 deployed.| 
|   GN4_BASE_URL      |  http://localhost:8080/geonetwork |  Where is GN4?| 
|  GN4_PROXY_BASE_URL|  http://localhost:7979/geonetwork |  Where is proxied GN4?| 
|   ELASTIC_URL       |  http://localhost:9200            |  Where is elastic?| 
