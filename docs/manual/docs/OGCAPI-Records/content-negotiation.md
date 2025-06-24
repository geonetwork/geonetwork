# Content Negotiation and Profiles

The GN5 OGCAPI-Records is integrated with the GN5 `FormatterApi`.  This allows outputting of various formats (like JSON, XML, and DCAT) for a record.

There are two types of Formatters available:

1. XSLT-Based Formatters

   Most formatters are of this type - they take the underlying XML and run a XSLT transformation to get the output.  These XSLTs need to be defined on a per-schema basis.  

   You can use the `FormatterApi` endpoints to see what is available (and for what schemas) - see the `FormatterController` and the `FormatterApi`.  A useful summary of available formatter is at http://localhost:7979/api/records/formatters.

2. Elastic JSON Index-Based Formatters

   There are a few formatters (most notably the `ogcapi-record` JSON output) use the information in the Elastic JSON Index to create output.  This index is a schema-independent simple representation of the underlying metadata record (which is usually XML).  This is **only** used for simple output formats as a lot of information in the underlying XML record is lost.


## Selecting the Content

In general, you can add `?f=<mime type>` to OGCAPI-Records requests to get content in a specific format.  For actual records, you can also request a Profile.

For example;

http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/51c6b2ac-3658-40b3-9a8f-0131ee74443f?f=application%2Frdf%2Bxml&profile=http%3A%2F%2Fgeonetwork.net%2Fdef%2Fprofile%2Feu-dcat-ap-hvd

We are requesting MimeType `application/rdf` (DCAT RDF) with the profile `http://geonetwork.net/def/profile/eu-geodcat-ap` (url encoded).

 

### Default Profiles for Mime Types

You can set default Profiles for a Mime Types in the `application.yaml` file (`geonetwork`.`openapi-records`.`links`.`item`):

```
profileDefaults:
    - mimetype: application/geo+json
        defaultProfile: http://www.opengis.net/def/profile/OGC/0/ogc-record
    - mimetype: application/rdf+xml
        defaultProfile: http://geonetwork.net/def/profile/dcat
    - mimetype: application/xml
        defaultProfile: http://geonetwork.net/def/profile/raw-xml
    - mimetype: application/json
        defaultProfile: http://geonetwork.net/def/profile/elastic-json-index
```

If someone doesn't put a Profile in the request, then this will choose the profile.

## Links

When requesting in the `ogcapi-record` JSON format, Record documents will have links like:

```
 "links": [
    {
      "rel": "alternative",
      "type": "application/xml",
      "hreflang": "eng",
      "profile": [
        "http://geonetwork.net/def/profile/raw-xml"
      ],
      "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/51c6b2ac-3658-40b3-9a8f-0131ee74443f?f=application%2Fxml&profile=http%3A%2F%2Fgeonetwork.net%2Fdef%2Fprofile%2Fraw-xml"
    },
    {
      "rel": "alternative",
      "type": "application/xml",
      "hreflang": "eng",
      "profile": [
        "http://geonetwork.net/def/profile/datacite"
      ],
      "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/51c6b2ac-3658-40b3-9a8f-0131ee74443f?f=application%2Fxml&profile=http%3A%2F%2Fgeonetwork.net%2Fdef%2Fprofile%2Fdatacite"
    },
    {
      "rel": "alternative",
      "type": "application/json",
      "hreflang": "eng",
      "profile": [
        "http://geonetwork.net/def/profile/elastic-json-index"
      ],
      "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/51c6b2ac-3658-40b3-9a8f-0131ee74443f?f=application%2Fjson&profile=http%3A%2F%2Fgeonetwork.net%2Fdef%2Fprofile%2Felastic-json-index"
    },
    {
      "rel": "alternative",
      "type": "application/rdf+xml",
      "hreflang": "eng",
      "profile": [
        "http://geonetwork.net/def/profile/eu-dcat-ap-hvd"
      ],
      "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/51c6b2ac-3658-40b3-9a8f-0131ee74443f?f=application%2Frdf%2Bxml&profile=http%3A%2F%2Fgeonetwork.net%2Fdef%2Fprofile%2Feu-dcat-ap-hvd"
    },
    {
      "rel": "alternative",
      "type": "application/rdf+xml",
      "hreflang": "eng",
      "profile": [
        "http://geonetwork.net/def/profile/eu-geodcat-ap"
      ],
      "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/51c6b2ac-3658-40b3-9a8f-0131ee74443f?f=application%2Frdf%2Bxml&profile=http%3A%2F%2Fgeonetwork.net%2Fdef%2Fprofile%2Feu-geodcat-ap"
    },
    ...
]
 ```

 Notice that the link's `href` has a `&profile...` part. If it is missing, the system will choose the default profile for that mime type (configured inside `application.yaml`)

 Looking at the `RDF+XML` content type in the links, above:
 ```
{
      "rel": "alternative",
      "type": "application/rdf+xml",
      "hreflang": "eng",
      "profile": [
        "http://geonetwork.net/def/profile/eu-dcat-ap-hvd"
      ],
      "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/51c6b2ac-3658-40b3-9a8f-0131ee74443f?f=application%2Frdf%2Bxml&profile=http%3A%2F%2Fgeonetwork.net%2Fdef%2Fprofile%2Feu-dcat-ap-hvd"
    },
    {
      "rel": "alternative",
      "type": "application/rdf+xml",
      "hreflang": "eng",
      "profile": [
        "http://geonetwork.net/def/profile/eu-geodcat-ap"
      ],
      "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/51c6b2ac-3658-40b3-9a8f-0131ee74443f?f=application%2Frdf%2Bxml&profile=http%3A%2F%2Fgeonetwork.net%2Fdef%2Fprofile%2Feu-geodcat-ap"
    },
    {
      "rel": "alternative",
      "type": "application/rdf+xml",
      "hreflang": "eng",
      "profile": [
        "http://geonetwork.net/def/profile/dcat"
      ],
      "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/51c6b2ac-3658-40b3-9a8f-0131ee74443f?f=application%2Frdf%2Bxml&profile=http%3A%2F%2Fgeonetwork.net%2Fdef%2Fprofile%2Fdcat"
    },
    {
      "rel": "alternative",
      "type": "application/rdf+xml",
      "hreflang": "eng",
      "profile": [
        "http://geonetwork.net/def/profile/eu-dcat-ap"
      ],
      "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/51c6b2ac-3658-40b3-9a8f-0131ee74443f?f=application%2Frdf%2Bxml&profile=http%3A%2F%2Fgeonetwork.net%2Fdef%2Fprofile%2Feu-dcat-ap"
    },
```

This shows that there are 4 profiles available (all in url format):

*   `http://geonetwork.net/def/profile/eu-dcat-ap-hvd`
*   `http://geonetwork.net/def/profile/eu-geodcat-ap`
*   `http://geonetwork.net/def/profile/dcat`
*   `http://geonetwork.net/def/profile/eu-dcat-ap`

NOTE: these will likely change to a more authoritative URL.

To get the `http://geonetwork.net/def/profile/eu-dcat-ap` profile, use:

    http://localhost:7979/ogcapi-records/collections/<collectionId>/items/<itemId>?f=application/rdf+xml&profile=http%3A%2F%2Fgeonetwork.net%2Fdef%2Fprofile%2Feu-geodcat-ap

NOTE: the profile name (url) has been url-encoded

To get the default (defined in `application.yaml`) profile, use:

    http://localhost:7979/ogcapi-records/collections/<collectionId>/items/<itemId>?f=application/rdf+xml


## Headers

When requesting a Record (`item` endpoint), a `Link` header will be set in this manner:

```
HTTP/1.1 200
Content-Type: application/rdf+xml
Link: <http://geonetwork.net/def/profile/eu-geodcat-ap>; rel="profile"
```

Or, for the standard OGCAPI-Records `ogcapi-record` output:

```
HTTP/1.1 200
Content-Type: application/json
Link: <http://www.opengis.net/def/profile/OGC/0/ogc-record>; rel="profile"
```

## More Information

See the [OGCAPI-Records](https://ogcapi.ogc.org/records/#:~:text=OGC%20API%20%2D%20Records%20is%20a,resources%20(metadata)%20are%20exposed.) specification.

See [this discussion](https://github.com/opengeospatial/ogcapi-records/issues/481) on OGCAPI-Records and Profiles.


# Technical Discussion of Content Negotiation

This is a technical discussion of how the Content Negotiation is working in the OGCAPI-Records and how it integrates with Spring Boot.

One of the difficulties is that there are multiple output "profiles" for a single mime type.  For example, `application/rdf+xml` (i.e. DCAT) has several "flavours" of output (i.e. GEODCAT, DCAT, and country-specific DCAT-AP).  This is a bit more advanced for how spring boot does its Content Negotiation.

## Simple Spring Boot Content Negotiation

In a typical spring boot application, there is a 1:1 correlation between the mime type in the request (i.e. either an `Accepts: ...` header or a `?f=<mime type>` parameter). 

This is setup with the `ContentNegotiationConfigurer` (cf. `WebConfig.java`) and registering a set of `HttpMessageConverter`.

`ContentNegotiationConfigurer` - This sets up how spring processes incoming requests and determines what Mime Type to use.  For example, it converts `&f=json` into `application/json`.

`HttpMessageConverter` - These convert an object (i.e. result from the controller endpoint) into the corresponding Mime Type format.  Typically, for an `application/json` request, it will use the Jackson `ObjectMapper` to convert to an actual JSON string.

In summary, the typical spring boot content negotiation is:

 a) controller's result object - this is the main work of the OGCAPI-Records codebase <br>
 b) user's requested Mime Type - the `ContentNegotiationConfigurer` determines this <br>
 c) respond with the converted result object - this is done with a `HttpMessageConverter` 

Spring boot will take the incoming request, determines the requested MimeType (`ContentNegotiationConfigurer`), then execute the controller endpoint.  The result object is then passed to the correct `HttpMessageConverter` which will write the output to the Http Response.

NOTE:

If you look at the `HttpMessageConverter`, its only really given the controllers result object and the Mime Type.  Is very disconnected from the original request.

## GN5's OGCAPI-Records Content Negotiation

There are a few complexities with OGCAPI-Records and how its integrated into the GN5 `FormatterApi`.  

1. The output configuration is dynamic - OGCAPI-Records doesn't *apriori* know the MimeType (and profiles) that it supports.
2. The output requires both the MimeType (i.e. `application/rdf+xml`) and a *profile* (i.e. `dcat-ap-nl`).

The OGCAPI team recently added support for profiles (and profile negotiation).

Internally, on startup, the `FormatterApi` is queried for the supported MimeTypes (cf. `WebConfig.java`) and the `FormatterApiMessageWriter` is created.

`FormatterApiMessageWriter` is configured (by querying the `FormatterApi`) with what Mime Types it supports.  It, also, only knows how to process `OgcApiRecordsRecordGeoJSONDto` objects.  This object is a **single** metadata record (i.e. from the `/collections/<CollectionId>/items/<ItemId>` endpoint).

The main problem is that the `FormatterApiMessageWriter` doesn't have access to the user's request - just the expected Mime Type and the `OgcApiRecordsRecordGeoJSONDto` object.  The main issue is that it doesn't know what profile the user requested.

Currently, I've modified the Controller and have it add information to the output headers (which is available both to the controller and the `HttpMessageConverter`).  This is a bit of a hack.

Alternative Hacks:

1. add a `"requestedProfiles"` field to the `OgcApiRecordsRecordGeoJSONDto` object
2. try to put the information into a thread local or try to get access to the current http request
3. try to set "strange" mime types that could be parsed by the `FormatterApiMessageWriter`

None of these solutions is great - please let us know if there are other, better, options.

NOTE:  The implementation uses spring's `BeanFactory` to break some implied circular dependencies.  This is mostly because of the `WebConfig -> Formatter -> Index Formatter - > Elastic Indexing -> WebConfig` chain.