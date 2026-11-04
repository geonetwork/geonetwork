# Content Negotiation in GeoNetwork 5

This document explains the Content Negotiation architecture in GeoNetwork 5, focusing on OGC API - Records endpoints, profile negotiation, dynamic schema formatters, and OpenAPI/Link integration.

For a developer it is useful to read, because explains some differences with Spring Boot way of managing content negotiation.

---

## 1. Why Standard Spring Boot Content Negotiation Isn't Enough

In a standard Spring Boot application:
1. A controller method returns a Java POJO.
2. Spring inspects the requested format via the `Accept` header or `?f=` query parameter.
3. Spring matches a `HttpMessageConverter` (e.g., Jackson) to serialize the POJO into JSON or XML.

### The OGC API - Records Challenge: "Profiles"
OGC API - Records introduces the concept of **profiles**. For example:
- `application/rdf+xml` (DCAT) supports profiles like `eu-dcat-ap`, `eu-dcat-ap-hvd`, `eu-geodcat-ap`, and `dcat`.
- `application/xml` can output CSW XML or ISO 19139 XML.
- Profiles are **dynamic**: they change as schema plugins and XSLT transformations are updated.
- Different profiles and representations are **not simple re-encodings of the same Java POJO**; they represent structurally distinct documents.

Therefore, GeoNetwork 5 decouples controller execution from payload formatting using **intermediate carrier objects** and **profile-aware message converters**.

---

## 2. Architecture Overview

```
                      Incoming Request (e.g. ?f=rdfxml&profile=eu-dcat-ap)
                                           │
                                           ▼
                            OgcapiCollectionsApiController
                  (RequestMediaTypeAndProfileBuilder resolves MIME + profile)
                                           │
                                           ▼
                      Returns IControllerResponseObject Envelope
                  (Carries query params, index data, RequestMediaTypeAndProfile)
                                           │
                                           ▼
                            Spring HttpMessageConverter Chain
                       (Ordered: specialized converters check canWrite() first)
                                           │
         ┌─────────────────────────────────┼─────────────────────────────────┐
         ▼                                 ▼                                 ▼
   Dynamic Formatter              Regular Formatter                    HTML Formatter
(IControllerResultFormatter)  (e.g., CswCollectionMessageWriter)   (OgcApiRecordsHtmlMessageWriter)
   Via FormatterApi / XSLT             Direct Java write                  Thymeleaf Template
```

---

## 3. Controller Carrier Objects (`IControllerResponseObject`)

Instead of building a large response POJO directly, controllers return lightweight envelopes implementing `IControllerResponseObject` (and `IMediaTypeAndProfile`). These carry query parameters, index search results, and negotiated profile information:

| Response Envelope Class | OGC API Endpoint | Description |
|---|---|---|
| `OgcApiLandingPageResponse` | `/` | Root landing page |
| `OgcApiRecordsCollectionsResponse` | `/collections` | Collections list |
| `OgcApiCollectionResponse` | `/collections/{collectionId}` | Single collection description |
| `OgcApiRecordsMultiRecordResponse` | `/collections/{collectionId}/items` | Multiple record search results |
| `OgcApiRecordsSingleRecordResponse` | `/collections/{collectionId}/items/{recordId}` | Single record item |

---

## 4. Profile & Format Negotiation (`RequestMediaTypeAndProfileBuilder`)

Each controller endpoint uses `RequestMediaTypeAndProfileBuilder` to resolve a `RequestMediaTypeAndProfile` before returning the response:

1. **Format Resolution**: Resolves MIME type using Spring's `ContentNegotiationManager` (`?f=` parameter takes precedence over `Accept` header; defaults to `application/json`).
2. **Profile Resolution**:
   - Reads the `?profile=` parameter (supports comma-separated preferences).
   - Matches requested profiles against available profiles for the target MIME type using `MimeAndProfilesForResponseType`.
   - **Default Profile**: If no profile is requested, falls back to the default configured in `ProfileDefaultsConfiguration` (from `application-ogcapi-records.yml`).
3. **Envelope Attachment**: The resolved `RequestMediaTypeAndProfile` is set directly on the `IControllerResponseObject`.

---

## 5. Message Converters: Dynamic vs Regular

GeoNetwork 5 uses two main types of message converters:

### 1. Dynamic Converters (`IContentNegotiationInitializable`)
- Factory interface for runtime-generated converters returning `IControllerResultFormatter` instances.
- Used for dynamic schema formatters (e.g., GeoNetwork XSLT and `IndexRecord` formatters managed by `SchemaManager` and `FormatterApi`).
- **Example**: `SingleRecordFormatterApiContentNegotiationInitializable` queries `FormatterApi` on startup and dynamically instantiates an `OgcApiRecordsSingleRecordResponseFormatter` for every supported MIME type and schema profile.

### 2. Regular Converters (`HttpMessageConverter`)
- Static converters implementing Spring's `HttpMessageConverter<T>`.
- Typically handle one MIME type and one `IControllerResponseObject` target.
- Target response type is detected via reflection by inspecting the `#write(TargetType, MediaType, ...)` signature or generic superclass parameters.
- **Examples**:
  - `CswCollectionMessageWriter`: Directly streams CSW-compatible XML for `OgcApiRecordsMultiRecordResponse` (`application/xml`).
  - `OgcApiCollectionResponseFormatter`: Formats `OgcApiCollectionResponse` for JSON, XML, or HTML.
  - `OgcApiRecordsHtmlMessageWriter`: Renders Thymeleaf templates (`templates/ogcapi/*.html`) for `text/html`.

### Converter Precedence ("First Match Wins")
Spring MVC evaluates converters in order. Specialized converters must precede generic ones so that Jackson's generic JSON/XML converters do not intercept the response. In `WebConfig`:
- `configureMessageConverters`: Dynamic and specialized formatters are inserted at index `0`.
- Generic serializers remain at the end of the converter chain.

---

## 6. Central Metadata Registry: `MimeAndProfilesForResponseType`

`MessageWriterUtil` coordinates converters on startup and captures all registered converters via `WebConfig.extendMessageConverters()`.

`MimeAndProfilesForResponseType` queries this list and provides `ResponseTypeInfo` for any `IControllerResponseObject` class:

```java
public static class ResponseTypeInfo {
    MediaType mimeType;
    List<String> profiles;
    String defaultProfile;
    List<String> formatProviders;
}
```

`ResponseTypeInfo` powers two core subsystems:

1. **OpenAPI Customization (`OgcApiRecordsOpenApiConfigMimeTypes`)**:
   - Inspects available formatters for `/items` and `/items/{recordId}`.
   - Enriches the OpenAPI specification with supported MIME types, `x-profiles`, `x-profile-default`, and `x-format-providers`.
2. **Hypermedia Link Generation (`BasicLinks`)**:
   - Generates navigation links (`rel="alternate"`, `rel="self"`) with proper `?f=` and profile attributes for each supported representation.


