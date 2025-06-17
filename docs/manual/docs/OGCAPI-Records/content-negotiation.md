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

http://localhost:7979/ogcapi-records/collections/%3CcollectionId%3E/items/%3CitemId%3E?f=application/rdf+xml&profile=%5Bgeonetwork:eu-dcat-ap%5D

We are requesting MimeType `application/rdf` (DCAT RDF) with the profile `[geonetwork:eu-dcat-ap]` (in safe CURIE format).

NOTE: use `%5B` ("`[`") and `%5D` ("`]`") because square brackets aren't valid in URLs.

### Default Profiles for Mime Types

You can set default Profiles for a Mime Types in the `application.yaml` file (`geonetwork`.`openapi-records`.`links`.`item`):

```
profileDefaults:
    - mimetype: application/geo+json
        defaultProfile: http://www.opengis.net/def/profile/OGC/0/ogc-record
    - mimetype: application/rdf+xml
        defaultProfile: '[geonetwork:dcat]'
    - mimetype: application/xml
        defaultProfile: '[geonetwork:raw-xml]'
    - mimetype: application/json
        defaultProfile: '[geonetwork:elastic-json-index]'
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
        "[geonetwork:raw-xml]",
        "[geonetwork:datacite]"
        ],
        "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/6fa96b57-7ae3-4b58-85eb-e957f54ec915?f=application/xml"
    },
    {
        "rel": "self",
        "type": "application/json",
        "hreflang": "eng",
        "profile": [
        "[geonetwork:elastic-json-index]",
        "[geonetwork:raw-json]"
        ],
        "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/6fa96b57-7ae3-4b58-85eb-e957f54ec915?f=application/json"
    },
    {
        "rel": "alternative",
        "type": "application/rdf+xml",
        "hreflang": "eng",
        "profile": [
        "[geonetwork:eu-dcat-ap-hvd]",
        "[geonetwork:eu-geodcat-ap]",
        "[geonetwork:dcat]",
        "[geonetwork:eu-dcat-ap]"
        ],
        "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/6fa96b57-7ae3-4b58-85eb-e957f54ec915?f=application/rdf+xml"
    },
    {
        "rel": "alternative",
        "type": "application/geo+json",
        "hreflang": "eng",
        "profile": [
        "http://www.opengis.net/def/profile/OGC/0/ogc-catalog"
        ],
        "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/6fa96b57-7ae3-4b58-85eb-e957f54ec915?f=application/geo+json"
    },
]
 ```




 Notice that the link's `href` does NOT have a `&profile...` part.  Its the user's responsibility to attach that to the `href` when requesting the document.  If the user does not do this, then the default profile (defined in `application.yaml`) will be used.

 Looking at the `RDF+XML` content type in the links, above:
 ```
 {
    "rel": "alternative",
    "type": "application/rdf+xml",
    "hreflang": "eng",
    "profile": [
    "[geonetwork:eu-dcat-ap-hvd]",
    "[geonetwork:eu-geodcat-ap]",
    "[geonetwork:dcat]",
    "[geonetwork:eu-dcat-ap]"
    ],
    "href": "http:/localhost:7979/ogcapi-records/collections/1402ea86-2900-4cf7-b58a-f8d78bc051e4/items/6fa96b57-7ae3-4b58-85eb-e957f54ec915?f=application/rdf+xml"
},
```

This shows that there are 4 profiles available (all in safe CURIE format):

*   `[geonetwork:eu-dcat-ap-hvd]`
*   `[geonetwork:eu-geodcat-ap]`
*   `[geonetwork:dcat]`
*   `[geonetwork:eu-dcat-ap]`


To get the `[geonetwork:eu-dcat-ap]` profile, use:

    http://localhost:7979/ogcapi-records/collections/<collectionId>/items/<itemId>?f=application/rdf+xml&profile=%5Bgeonetwork:eu-dcat-ap%5D

To get the default (defined in `application.yaml`) profile, use:

    http://localhost:7979/ogcapi-records/collections/<collectionId>/items/<itemId>?f=application/rdf+xml


NOTE: use `%5B` ("`[`") and `%5D` ("`]`") because square brackets aren't valid in URLs.

## Headers

When requesting a Record (`item` endpoint), a `Link` header will be set in this manner:

```
HTTP/1.1 200
Content-Type: application/rdf+xml
Link: <[geonetwork:eu-dcat-ap]>; rel="profile"
```

Or, for the standard OGCAPI-Records `ogcapi-record` output:

```
HTTP/1.1 200
Content-Type: application/json
Link: <[geonetwork:eu-dcat-ap]>; rel="profile"
```

## More Information

See the [OGCAPI-Records](https://ogcapi.ogc.org/records/#:~:text=OGC%20API%20%2D%20Records%20is%20a,resources%20(metadata)%20are%20exposed.) specification.