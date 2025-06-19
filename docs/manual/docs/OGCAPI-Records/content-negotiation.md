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