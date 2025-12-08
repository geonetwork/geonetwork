# OGCAPI-Records Dynamic Properties

Dynamic Properties are user-defined (cf. `application-ogcapi-records.yml`) set of "extra" properties (from the Elastic `IndexRecord`) that are added to the metadata records JSON `properties`.  This is a very powerful system for customizations.  

See `OgcElasticFieldMapperConfig` (cf. `application-ogcapi-records.yml`) for how the user can configure the Dynamic Properties:

1. `ogcProperty` - what the new property should be called in the OGCAPI-Records metadata record's `property`
2. `elasticProperty` - where in the Elastic Index do we get the information from
3. `indexRecordProperty` - where in the parsed `IndexRecord` is the Elastic information stored?
4. `isSortable` - can you use this property for sorting (`sortby`)?
5. `isQueryable` - can you use ths property in "Queryable" queries?
6. `addPropertyToOutput` - add the property to the OGCAPI-Records metadata record's `property`?
7. `typeOverride` - allows the user to override a type calculated from the Elastic Index metadata (useful for converting between single-value and list-value objects).
8. `sortFieldSuffix` - Some elastic index properties are not sortable (i.e. text fields), but there might be a sub-object that is sortable (i.e. `.keyword`) (optional) (see Elastic `resourceTitleObject`)
9. `facetsConfig` - configuration for multiple facets that this might produce

As you can see, adding a dynamic property has a big impact:

1. it will typically change the OpenAPI document as well as the actual metadata record JSON returned to the user
2. it will typically change `/collections/collectionID/queryables` endpoint as well as the `.../items` query functionality
3. it will typically change `/collections/collectionID/sortables` endpoint as well as the `.../items` `sortby` functionality
4. it will typically change the `facets` endpoint, as well as add facets to the `.../items` endpoint

So, this has a big impact on the system.
