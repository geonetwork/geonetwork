/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.formatting.processor.dcatap;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DCAT-AP 3.0 and OGC API Records Model classes. All classes are nested to avoid multiple file requirements. */
public class DcatModel {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MultilingualValue {

        @JsonIgnore
        private Map<String, String> languages = new HashMap<>();

        @JsonAnyGetter
        public Map<String, String> getLanguages() {
            return languages;
        }

        @JsonAnySetter
        public void setLanguage(String key, String value) {
            if (languages == null) {
                languages = new HashMap<>();
            }
            languages.put(key, value);
        }

        public static MultilingualValue of(Map<String, String> values) {
            if (values == null || values.isEmpty()) return null;
            MultilingualValue mv = new MultilingualValue();
            values.forEach((k, v) -> {
                if (v == null || v.isBlank()) return;
                if ("default".equalsIgnoreCase(k)) {
                    mv.setLanguage("@value", v);
                } else if ("lang".equalsIgnoreCase(k)) {
                    mv.setLanguage("@value", v);
                } else if (k.startsWith("lang") && k.length() >= 6) {
                    mv.setLanguage(k.substring(4, 6).toLowerCase(), v);
                } else if (k.length() == 2 || (k.length() == 3 && !k.equals("key"))) {
                    mv.setLanguage(k.toLowerCase(), v);
                } else {
                    mv.setLanguage(k, v);
                }
            });
            return mv;
        }

        public static MultilingualValueBuilder builder() {
            return new MultilingualValueBuilder();
        }

        public static class MultilingualValueBuilder {
            private Map<String, String> languages = new HashMap<>();

            public MultilingualValueBuilder value(String value) {
                languages.put("@value", value);
                return this;
            }

            public MultilingualValueBuilder language(String lang, String value) {
                languages.put(lang, value);
                return this;
            }

            public MultilingualValue build() {
                MultilingualValue mv = new MultilingualValue();
                mv.languages = this.languages;
                return mv;
            }
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Resource {

        @JsonProperty("@id")
        private String id;

        @JsonProperty("@type")
        private String type;

        public static Resource of(String uri) {
            return Resource.builder().id(uri).build();
        }
    }

    /** DCT LinguisticSystem. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LinguisticSystem {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "dct:LinguisticSystem";

        @JsonProperty("@id")
        private String id;

        public static LinguisticSystem of(String uri) {
            return LinguisticSystem.builder().id(uri).build();
        }
    }

    /** DCT Rights Statement. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RightsStatement {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "dct:RightsStatement";

        @JsonProperty("@id")
        private String id;

        @JsonProperty("rdfs:label")
        private MultilingualValue label;

        public static RightsStatement of(String uri) {
            return RightsStatement.builder().id(uri).build();
        }
    }

    /** SKOS Concept. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Concept {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "skos:Concept";

        @JsonProperty("@id")
        private String id;

        @JsonProperty("skos:prefLabel")
        private MultilingualValue prefLabel;

        @JsonProperty("skos:inScheme")
        private Resource inScheme;
    }

    /** ADMS Identifier. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Identifier {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "adms:Identifier";

        @JsonProperty("skos:notation")
        private String notation;

        @JsonProperty("adms:schemeAgency")
        private String schemeAgency;

        @JsonProperty("dct:creator")
        private Resource creator;

        @JsonProperty("dct:issued")
        private String issued;
    }

    /** GeoSPARQL Geometry. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Geometry {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "gsp:Geometry";

        @JsonProperty("gsp:asWKT")
        private String asWKT;

        @JsonProperty("gsp:asGeoJSON")
        private String asGeoJSON;
    }

    /** Custom serializer for Geometry to output as typed literal. */
    public static class GeometrySerializer extends com.fasterxml.jackson.databind.JsonSerializer<Geometry> {
        @Override
        public void serialize(
                Geometry value,
                com.fasterxml.jackson.core.JsonGenerator gen,
                com.fasterxml.jackson.databind.SerializerProvider serializers)
                throws java.io.IOException {
            if (value != null) {
                if (value.getAsGeoJSON() != null) {
                    gen.writeStartObject();
                    gen.writeStringField("@type", "http://www.opengis.net/ont/geosparql#geoJSONLiteral");
                    gen.writeStringField("@value", value.getAsGeoJSON());
                    gen.writeEndObject();
                } else if (value.getAsWKT() != null) {
                    gen.writeStartObject();
                    gen.writeStringField("@type", "http://www.opengis.net/ont/geosparql#wktLiteral");
                    gen.writeStringField("@value", value.getAsWKT());
                    gen.writeEndObject();
                } else {
                    gen.writeNull();
                }
            } else {
                gen.writeNull();
            }
        }
    }

    /** DCT Location. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Location {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "dct:Location";

        @JsonProperty("@id")
        private String id;

        @JsonProperty("rdfs:label")
        private MultilingualValue label;

        @JsonProperty("locn:geometry")
        @JsonSerialize(using = GeometrySerializer.class)
        private Geometry geometry;

        @JsonProperty("dcat:bbox")
        private String bbox;

        @JsonProperty("dcat:centroid")
        private String centroid;
    }

    /** DCT Period of Time. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PeriodOfTime {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "dct:PeriodOfTime";

        @JsonProperty("dcat:startDate")
        private String startDate;

        @JsonProperty("dcat:endDate")
        private String endDate;
    }

    /** FOAF Agent. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Agent {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "foaf:Agent";

        @JsonProperty("@id")
        private String id;

        @JsonProperty("foaf:name")
        private MultilingualValue name;

        @JsonProperty("foaf:givenName")
        private String givenName;

        @JsonProperty("foaf:mbox")
        private String mbox;

        @JsonProperty("foaf:homepage")
        private Resource homepage;

        @JsonProperty("dct:type")
        private Resource agentType;
    }

    /** VCard Telephone. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Telephone {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "vcard:Voice";

        @JsonProperty("vcard:hasValue")
        private String hasValue;
    }

    /** VCard Address. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Address {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "vcard:Address";

        @JsonProperty("vcard:street-address")
        private String streetAddress;

        @JsonProperty("vcard:locality")
        private String locality;

        @JsonProperty("vcard:postal-code")
        private String postalCode;

        @JsonProperty("vcard:country-name")
        private String countryName;
    }

    /** VCard Contact Point. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ContactPoint {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "vcard:Organization";

        @JsonProperty("vcard:fn")
        private MultilingualValue fn;

        @JsonProperty("vcard:hasEmail")
        private String hasEmail;

        @JsonProperty("vcard:hasTelephone")
        private Telephone hasTelephone;

        @JsonProperty("vcard:hasAddress")
        private Address hasAddress;

        @JsonProperty("vcard:hasURL")
        private String hasURL;
    }

    /** DCT Standard. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Standard {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "dct:Standard";

        @JsonProperty("@id")
        private String id;

        @JsonProperty("dct:title")
        private MultilingualValue title;
    }

    /** DCT MediaType. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MediaType {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "dct:MediaType";

        @JsonProperty("@id")
        private String id;

        public static MediaType of(String uri) {
            return MediaType.builder().id(uri).build();
        }
    }

    /** DCT MediaTypeOrExtent. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MediaTypeOrExtent {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "dct:MediaTypeOrExtent";

        @JsonProperty("@id")
        private String id;

        public static MediaTypeOrExtent of(String uri) {
            return MediaTypeOrExtent.builder().id(uri).build();
        }
    }

    /** PROV Attribution for qualified attribution (DCAT-AP 3.0). */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Attribution {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "prov:Attribution";

        @JsonProperty("prov:agent")
        private Agent agent;

        @JsonProperty("dcat:hadRole")
        private Resource hadRole;
    }

    /** PROV Activity for provenance (DCAT-AP 3.0). */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Activity {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "prov:Activity";

        @JsonProperty("@id")
        private String id;

        @JsonProperty("prov:startedAtTime")
        private String startedAtTime;

        @JsonProperty("prov:endedAtTime")
        private String endedAtTime;

        @JsonProperty("prov:wasAssociatedWith")
        private Agent wasAssociatedWith;
    }

    /** DCAT Relationship for qualified relationships (DCAT-AP 3.0). */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Relationship {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "dcat:Relationship";

        @JsonProperty("dct:relation")
        private Resource relation;

        @JsonProperty("dcat:hadRole")
        private Resource hadRole;
    }

    /** DCAT Data Service (expanded for DCAT-AP 3.0). */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DataService {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "dcat:DataService";

        @JsonProperty("@id")
        private String id;

        @JsonProperty("dct:title")
        private MultilingualValue title;

        @JsonProperty("dct:description")
        private MultilingualValue description;

        @JsonProperty("dcat:endpointURL")
        private Resource endpointURL;

        @JsonProperty("dcat:endpointDescription")
        private String endpointDescription;

        @JsonProperty("dct:conformsTo")
        private List<Standard> conformsTo;

        @JsonProperty("dcat:servesDataset")
        private List<Resource> servesDataset;

        @JsonProperty("dct:accessRights")
        private RightsStatement accessRights;

        @JsonProperty("dct:license")
        private Resource license;

        @JsonProperty("dcat:landingPage")
        private List<Resource> landingPage;

        @JsonProperty("dcat:keyword")
        private List<Object> keyword;

        @JsonProperty("dcat:theme")
        private List<Concept> theme;

        @JsonProperty("dcat:contactPoint")
        private List<ContactPoint> contactPoint;

        @JsonProperty("dct:spatial")
        private List<Location> spatial;

        @JsonProperty("dct:temporal")
        private List<PeriodOfTime> temporal;
    }

    /** DCAT Distribution (updated for DCAT-AP 3.0). */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Distribution {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "dcat:Distribution";

        @JsonProperty("@id")
        private String id;

        @JsonProperty("dct:title")
        private MultilingualValue title;

        @JsonProperty("dct:description")
        private MultilingualValue description;

        @JsonProperty("dcat:accessURL")
        private Resource accessURL;

        @JsonProperty("dcat:downloadURL")
        private Resource downloadURL;

        @JsonProperty("dct:format")
        private MediaTypeOrExtent format;

        @JsonProperty("dcat:mediaType")
        private MediaType mediaType;

        @JsonProperty("dcat:packageFormat")
        private MediaType packageFormat;

        @JsonProperty("dcat:compressFormat")
        private MediaType compressFormat;

        @JsonProperty("dcat:byteSize")
        private Long byteSize;

        @JsonProperty("dct:license")
        private Resource license;

        @JsonProperty("dct:rights")
        private RightsStatement rights;

        @JsonProperty("adms:status")
        private Resource status;

        @JsonProperty("dcat:accessService")
        private DataService accessService;

        @JsonProperty("dct:conformsTo")
        private List<Standard> conformsTo;

        @JsonProperty("adms:representationTechnique")
        private Resource representationTechnique;

        @JsonProperty("dcat:temporalResolution")
        private String temporalResolution;

        @JsonProperty("dcat:spatialResolutionInMeters")
        private Double spatialResolutionInMeters;

        @JsonProperty("dct:issued")
        private String issued;

        @JsonProperty("dct:modified")
        private String modified;
    }

    /** DQV Quality Measurement. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class QualityMeasurement {

        @JsonProperty("@type")
        @Builder.Default
        private String type = "dqv:QualityMeasurement";

        @JsonProperty("dqv:isMeasurementOf")
        private Resource isMeasurementOf;

        @JsonProperty("dqv:value")
        private Object value;
    }

    // ============ OGC API Records specific classes ============

    /** OGC API Records Link object. Used for links array in OGC API Records. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OGCLink {
        private String href;
        private String rel;
        private String type;
        private String title;
        private String hreflang;
        private Long length;
    }

    /** OGC API Records extent object. Combines spatial and temporal extents. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OGCExtent {
        private OGCSpatialExtent spatial;
        private OGCTemporalExtent temporal;
    }

    /** OGC API Records spatial extent. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OGCSpatialExtent {
        private List<List<Double>> bbox; // [[minX, minY, maxX, maxY], ...]
        private String crs;
    }

    /** OGC API Records temporal extent. */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class OGCTemporalExtent {
        private List<List<String>> interval; // [["start", "end"], ...]
        private String trs;
    }

    /**
     * OGC API Records GeoJSON Feature/Record. This is the hybrid model that combines OGC API Records, GeoJSON, and
     * DCAT-AP 3.0.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
        "@context",
        "@type",
        "type",
        "id",
        "@id",
        "dct:title",
        "dct:description",
        "geometry",
        "time",
        "properties"
    })
    public static class OGCRecord {

        @JsonProperty("@context")
        private Map<String, Object> context;

        // For JSON-LD/DCAT-AP compatibility
        @JsonProperty("@type")
        private List<String> atType; // ["geo:Feature", "dcat:Dataset"]

        // For GeoJSON/OGC API Records compatibility
        @JsonProperty("type")
        @Builder.Default
        private String type = "Feature";

        // Record identifier (both formats)
        @JsonProperty("id")
        private String id;

        @JsonProperty("@id")
        private String atId; // URI for linked data

        // DCAT-AP 3.0 core properties at root level (for proper DCAT-AP compliance)
        @JsonProperty("dct:title")
        private MultilingualValue dctTitle;

        @JsonProperty("dct:description")
        private MultilingualValue dctDescription;

        // Additional DCAT-AP root-level properties for full compliance
        @JsonProperty("dct:identifier")
        private List<Identifier> dctIdentifier;

        @JsonProperty("dcat:keyword")
        private List<Object> dcatKeyword;

        @JsonProperty("dcat:theme")
        private List<Concept> dcatTheme;

        @JsonProperty("dct:issued")
        private String dctIssued;

        @JsonProperty("dct:modified")
        private String dctModified;

        @JsonProperty("dct:language")
        private List<LinguisticSystem> dctLanguage;

        @JsonProperty("dct:publisher")
        private Agent dctPublisher;

        @JsonProperty("dcat:contactPoint")
        private List<ContactPoint> dcatContactPoint;

        @JsonProperty("dcat:distribution")
        private List<Distribution> dcatDistribution;

        @JsonProperty("dct:accessRights")
        private RightsStatement dctAccessRights;

        @JsonProperty("dct:spatial")
        private List<Location> dctSpatial;

        @JsonProperty("dct:temporal")
        private List<PeriodOfTime> dctTemporal;

        // GeoJSON geometry
        @JsonProperty("geometry")
        private Object geometry; // GeoJSON geometry object

        // OGC API Records temporal extent
        @JsonProperty("time")
        private OGCTemporalExtent time;

        // OGC API Records properties (simplified metadata)
        @JsonProperty("properties")
        private OGCRecordProperties properties;

        // OGC API Records links
        @JsonProperty("links")
        private List<OGCLink> links;
    }

    /**
     * OGC API Records properties object. Contains OGC API Records core metadata fields only. DCAT-AP properties are at
     * the root level for proper compliance.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({"recordCreated", "recordUpdated", "type", "title", "description"})
    public static class OGCRecordProperties {

        // OGC API Records core properties
        @JsonProperty("recordCreated")
        private String recordCreated;

        @JsonProperty("recordUpdated")
        private String recordUpdated;

        @JsonProperty("type")
        private String type; // Resource type

        @JsonProperty("title")
        private String title; // Simple string for OGC

        @JsonProperty("description")
        private String description; // Simple string for OGC

        @JsonProperty("keywords")
        private List<String> keywords; // Simple string array for OGC

        @JsonProperty("language")
        private Object language; // String or array

        @JsonProperty("languages")
        private List<String> languages;

        @JsonProperty("resourceLanguages")
        private List<String> resourceLanguages;

        @JsonProperty("externalIds")
        private List<Object> externalIds;

        @JsonProperty("formats")
        private List<String> formats;

        @JsonProperty("contacts")
        private List<Object> contacts;

        @JsonProperty("license")
        private String license; // Simple string or URI for OGC

        @JsonProperty("rights")
        private String rights; // Simple string for OGC

        @JsonProperty("extent")
        private OGCExtent extent;

        @JsonProperty("created")
        private String created;

        @JsonProperty("updated")
        private String updated;

        @JsonProperty("conformsTo")
        private List<String> conformsTo; // Simple URI array for OGC

        // Additional metadata that doesn't fit core OGC properties
        @JsonProperty("themes")
        private List<String> themes; // Theme URIs

        @JsonProperty("temporalResolution")
        private String temporalResolution;

        @JsonProperty("spatialResolution")
        private Double spatialResolution;
    }

    /** DCAT-AP 3.0 Dataset model (kept for backward compatibility). */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({"@context", "@type", "@id"})
    public static class Dataset {

        @JsonProperty("@context")
        private Map<String, Object> context;

        @JsonProperty("@type")
        @Builder.Default
        private String type = "dcat:Dataset";

        @JsonProperty("@id")
        private String id;

        @JsonProperty("dct:title")
        private MultilingualValue title;

        @JsonProperty("dct:description")
        private MultilingualValue description;

        @JsonProperty("dct:identifier")
        private List<Identifier> identifier;

        @JsonProperty("dcat:keyword")
        private List<Object> keyword;

        @JsonProperty("dcat:theme")
        private List<Concept> theme;

        @JsonProperty("dct:subject")
        private List<Concept> subject;

        @JsonProperty("dct:spatial")
        private List<Location> spatial;

        @JsonProperty("dct:temporal")
        private List<PeriodOfTime> temporal;

        @JsonProperty("dct:issued")
        private String issued;

        @JsonProperty("dct:modified")
        private String modified;

        @JsonProperty("dct:dateSubmitted")
        private String dateSubmitted;

        @JsonProperty("dct:language")
        private List<LinguisticSystem> language;

        @JsonProperty("dct:publisher")
        private Agent publisher;

        @JsonProperty("dct:creator")
        private List<Agent> creator;

        @JsonProperty("dcat:contactPoint")
        private List<ContactPoint> contactPoint;

        @JsonProperty("dcat:distribution")
        private List<Distribution> distribution;

        @JsonProperty("dct:accessRights")
        private RightsStatement accessRights;

        @JsonProperty("dct:conformsTo")
        private List<Standard> conformsTo;

        @JsonProperty("dct:license")
        private Object license;

        @JsonProperty("dct:rights")
        private List<RightsStatement> rights;

        @JsonProperty("dct:relation")
        private List<Resource> relation;

        @JsonProperty("dqv:hasQualityMeasurement")
        private List<QualityMeasurement> qualityMeasurement;

        @JsonProperty("dcat:landingPage")
        private List<Resource> landingPage;

        @JsonProperty("foaf:page")
        private List<Resource> page;

        @JsonProperty("dct:accrualPeriodicity")
        private Resource accrualPeriodicity;

        @JsonProperty("dct:type")
        private Resource datasetType;

        @JsonProperty("owl:versionInfo")
        private String versionInfo;

        @JsonProperty("adms:versionNotes")
        private MultilingualValue versionNotes;

        @JsonProperty("dcat:granularity")
        private Resource granularity;

        // DCAT-AP 3.0 additions
        @JsonProperty("dcat:version")
        private String version;

        @JsonProperty("dcat:hasVersion")
        private List<Resource> hasVersion;

        @JsonProperty("dcat:isVersionOf")
        private Resource isVersionOf;

        @JsonProperty("dcat:previousVersion")
        private Resource previousVersion;

        @JsonProperty("dcat:spatialResolutionInMeters")
        private Double spatialResolutionInMeters;

        @JsonProperty("dcat:temporalResolution")
        private String temporalResolution;

        @JsonProperty("dct:isReferencedBy")
        private List<Resource> isReferencedBy;

        @JsonProperty("prov:qualifiedAttribution")
        private List<Attribution> qualifiedAttribution;

        @JsonProperty("prov:wasGeneratedBy")
        private Activity wasGeneratedBy;

        @JsonProperty("dcat:qualifiedRelation")
        private List<Relationship> qualifiedRelation;

        @JsonProperty("adms:identifier")
        private List<Identifier> admsIdentifier;

        @JsonProperty("dct:provenance")
        private List<Resource> provenance;

        @JsonProperty("adms:sample")
        private List<Resource> sample;

        @JsonProperty("dct:source")
        private List<Resource> source;

        @JsonProperty("dcat:hasCurrentVersion")
        private Resource hasCurrentVersion;
    }

    /** DCAT Catalog wrapper (updated for DCAT-AP 3.0). */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Catalog {

        @JsonProperty("@context")
        private Map<String, Object> context;

        @JsonProperty("@type")
        @Builder.Default
        private String type = "dcat:Catalog";

        @JsonProperty("@id")
        private String id;

        @JsonProperty("dct:title")
        private MultilingualValue title;

        @JsonProperty("dct:description")
        private MultilingualValue description;

        @JsonProperty("dcat:dataset")
        private List<Dataset> dataset;

        @JsonProperty("dcat:service")
        private List<DataService> service;

        @JsonProperty("dcat:catalog")
        private List<Catalog> catalog;

        @JsonProperty("dct:publisher")
        private Agent publisher;

        @JsonProperty("dct:issued")
        private String issued;

        @JsonProperty("dct:modified")
        private String modified;

        @JsonProperty("dct:language")
        private List<Resource> language;

        @JsonProperty("dcat:themeTaxonomy")
        private List<Resource> themeTaxonomy;

        @JsonProperty("dct:rights")
        private RightsStatement rights;

        @JsonProperty("foaf:homepage")
        private Resource homepage;

        @JsonProperty("dct:spatial")
        private List<Location> spatial;

        @JsonProperty("dct:license")
        private Resource license;

        @JsonProperty("dcat:contactPoint")
        private List<ContactPoint> contactPoint;
    }

    /** Private constructor to prevent instantiation. */
    private DcatModel() {
        // Utility class with inner classes only
    }
}
