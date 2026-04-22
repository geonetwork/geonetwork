# GeoNetwork

GeoNetwork's primary goal is to provide a platform for discovering and managing geospatial metadata on the web. GeoNetwork serves as a data catalog in a Spatial Data Infrastructure (SDI) and integration hub in geospatial federated ecosystems.

Main quality goals:

**Interoperability:** The system must act as a node of a larger network, strictly adhering to the OGC CSW, OGC API Records, OAI-PMH, Z39.50 protocols.

**Extensibility:** The ability for developers to introduce new metadata profiles (e.g., ISO 19115 national extensions) or new harvesting tasks without having to modify core aspects of the software, and if possible, at runtime.

**Search Scalability:** The ability to process and query indexes containing millions of geospatial records with sub-second response times, offloading the load to optimized engines like Elasticsearch.

Additional capabilities that are important:

**Asynchronous Harvesting Engine:** The capacity to schedule and execute robust, background ingestion tasks from heterogeneous external sources (e.g., CSW, WMS/WFS, WebDAV, Thredds) utilizing dedicated background threads or job queues without degrading the performance of concurrent read/write API operations.

**Rules-Driven Validation & Transformation Pipeline:** The ability to automatically process incoming metadata payloads through a multi-stage pipeline of validation and transformation processes prior to database persistence and indexing.

**Granular Security Framework:** The strict enforcement of record-level and operation-level access controls directly at the API layer, backed by seamless integration with different identity tools/frameworks (e.g., LDAP, OIDC).

**Lifecycle & State Management:** The underlying capability to govern the metadata publication workflow through an internal state machine (e.g., draft, pending, approved), while maintaining strict data provenance and historical versioning.
