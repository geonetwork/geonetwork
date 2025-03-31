

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
