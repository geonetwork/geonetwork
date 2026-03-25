# REST API Design Guidelines

This section defines the routing design principles for the GeoNetwork 5 REST API. Our primary goal is to build on the [OGC API record specification](https://github.com/opengeospatial/OGC-Web-API-Guidelines?tab=readme-ov-file#design-principles), extending it with intuitive, predictable, and highly readable interfaces, without compromising standard compatibility.


1. A well-designed URL should allow a developer to locate related endpoints, easily edit paths manually, and share readily understandable links.
2. No redundant prefixes, technology-exposing file extensions, or unnecessary verbosity.
3. All new endpoints must either adhere directly to the OGC API structure (*/collections/{collectionId}/items*) or mirror its structural rhythm for custom extensions.
4. Paths should use simple, clear nouns. Avoid heavy symbol usage or unnecessary hex encoding where possible.
5. Identity via Path, Not Query Parameters
* **Anti-pattern**: */api/records/search?collection=maps&id=54896*
* **GN5 Standard**: */api/collections/maps/items/54896*
6. Navigate down the resource tree logically. The resource nesting should match the OGC API resource tree, extending it cleanly for custom GeoNetwork features.

    For example:

* **List collections**: */api/collections*
* **Specific collection**: */api/collections/{collectionId}*
* **Items in collection**: */api/collections/{collectionId}/items*
* **Specific item**: */api/collections/{collectionId}/items/{itemId}*
7. When a resource exists within a user's specific context or multiple relationships, express this cleanly in the path as an extension of the base OGC model. It may be useful, just for example, when a workflow is active and user copies of documents exist.
* **User Spaces**: */api/users/{username}/collections/{collectionId}/items*


## Conformance classes

To maintain strict consistency with the OGC API architecture, the advertisement of implemented API features, custom extensions, and supported data schemas must be handled at the [conformance classes](https://docs.ogc.org/is/17-069r3/17-069r3.html#_declaration_of_conformance_classes) level.

Rather than relying on custom endpoints or ad-hoc queries to expose what the API can do, all capabilities should be declared as URIs within the global */conformance* endpoint. This ensures that any client interacting with the GeoNetwork 5 API can systematically discover both standard OGC functionalities and GN5-specific features (such as supported metadata schemas, specific output formats, or custom operations) in a predictable, standard-compliant manner.

For conformance classes that are specific to GeoNetwork (which do not exist in any official standard) it is possible to manage custom ones in an internal namespace.
