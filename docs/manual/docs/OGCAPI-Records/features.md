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
