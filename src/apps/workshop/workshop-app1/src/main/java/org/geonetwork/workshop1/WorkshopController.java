/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.workshop1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.schemas.SchemaManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Part 1 of the workshop. This is a bit messy - we will clean it up in Part 2!
 *
 * <p>Endpoints:
 *
 * <p>workshop1 - returns hardcoded trivial string <br>
 * workshop1b - returns hardcoded Map<String,Long> <br>
 * workshop1c - returns hardcoded GeoNetworkMetadataSummary <br>
 * workshop1d - returns with actual DB results <br>
 * workshop1e - same as workshop1d, but with openapi information <br>
 *
 * <p>endpoints:
 *
 * <p>http://localhost:7979/geonetwork/workshop1 <br>
 * http://localhost:7979/geonetwork/workshop1b <br>
 * http://localhost:7979/geonetwork/workshop1c <br>
 * http://localhost:7979/geonetwork/workshop1d <br>
 * http://localhost:7979/geonetwork/workshop1e <br>
 *
 * <p>openAPI (swagger) endpoints:
 *
 * <p>http://localhost:7979/geonetwork/v3/api-docs?f=json <br>
 * http://localhost:7979/geonetwork/swagger-ui/index.html
 */
@Controller
public class WorkshopController {

    private final SchemaManager schemaManager;
    private final EntityManager entityManager;

    public WorkshopController(SchemaManager schemaManager, EntityManager entityManager) {
        this.schemaManager = schemaManager;
        this.entityManager = entityManager;
    }

    /** Just returns the message "hello workshop" as a JSON string. */
    @GetMapping(value = "/workshop1", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> workshop1() {
        return new ResponseEntity<>("hello workshop", HttpStatus.OK);
    }

    /**
     * Returns a hard-coded JSON result showing the schema name and the number of records in that schema.
     *
     * <p>{ "iso19115-3.2018": 4, "iso19139": 39 }
     */
    @GetMapping(value = "/workshop1b", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Long>> workshop1b() {
        Map<String, Long> result = new HashMap<>();
        result.put("iso19139", 39L);
        result.put("iso19115-3.2018", 4L);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * This returns a hard-coded JSON object with some summary info about the records. See GeoNetworkMetadataSummary.
     */
    @GetMapping(value = "/workshop1c", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeoNetworkMetadataSummary> workshop1c() {
        var result = new GeoNetworkMetadataSummary();

        Map<String, Long> schemaCounts = new HashMap<>();
        schemaCounts.put("iso19139", 3L);
        schemaCounts.put("iso19115-3.2018", 4L);

        result.setRecordsPerSchema(schemaCounts);
        result.setAllSchemaIds(List.of("iso19139", "iso19115-3.2018"));
        result.setTotalNumberOfRecords(43L);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * This returns a DB-calculated GeoNetworkMetadataSummary json object.
     *
     * <p>There are security concerns with doing this, but this is just an example query for the workshop.
     *
     * <p>{ "totalNumberOfRecords": 43, "allSchemaIds": [ "iso19115-3.2018", "iso19139" ], "recordsPerSchema": {
     * "iso19115-3.2018": 4, "iso19139": 39 } }
     */
    @GetMapping(value = "/workshop1d", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeoNetworkMetadataSummary> workshop1d() {
        var result = new GeoNetworkMetadataSummary();

        // use the schema manager to get the set of supported schemas
        List<String> allSchemaIds = this.schemaManager.getSchemas().stream().toList();

        // calculate the number of records in each schema from the DB `metadata` table
        Map<String, Long> schemaCounts = calculateSchemaCounts();

        // sum the counts inside schemaCounts
        // Alternatively, we could have counted the number of records in the `metadata` table
        long nRecords = schemaCounts.values().stream().mapToLong(x -> x).sum();

        result.setRecordsPerSchema(schemaCounts);
        result.setAllSchemaIds(allSchemaIds);
        result.setTotalNumberOfRecords(nRecords);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Operation(
            operationId = "getMetadataSummary",
            summary = "Gets summary information about the available Records",
            description =
                    "Lists the schemas this GeoNetwork is configured with, the total number of metadata records, and how records of each schema there are.",
            responses = {
                @ApiResponse(
                        responseCode = "200",
                        description =
                                "Lists the schemas this GeoNetwork is configured with, the total number of metadata records, and how records of each schema there are.",
                        content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GeoNetworkMetadataSummary.class))
                        })
            })
    @GetMapping(value = "/workshop1e", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GeoNetworkMetadataSummary> workshop1e() {
        return workshop1d();
    }
    /**
     * This is the complicated way to do this. Ideally, we would just add this to the metadata repository
     * ({@link MetadataRepository}) as a custom query. We do it here just for the workshop.
     *
     * <p>SELECT schemaId, count(*) FROM metadata GROUP BY schemaId;
     */
    private Map<String, Long> calculateSchemaCounts() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Metadata> root = cq.from(Metadata.class);
        cq.multiselect(root.get("schemaid"), cb.count(root));
        cq.groupBy(root.get("schemaid"));

        var dbResults = entityManager.createQuery(cq).getResultList();

        var result = new HashMap<String, Long>();
        dbResults.forEach(dbResult -> {
            result.put((String) dbResult.get(0), (Long) dbResult.get(1));
        });
        return result;
    }

    /** Simple object to capture some summary information about the GN node. */
    @Getter
    @Setter
    @NoArgsConstructor
    @Schema(
            description =
                    "Summary information about this GeoNetwork's Metadata schemas and number of records in each schema.")
    public static class GeoNetworkMetadataSummary {

        @Schema(description = "total number of metadata records in this GeoNetwork system", example = "123")
        /* total number of records in the DB. SELECT count(*) FROM metatdata; */
        private Long totalNumberOfRecords;

        @Schema(
                description = " list of all the supported schemas in this GeoNetwork system",
                example = "[\"iso19115-3.2018\", \"iso19139\"]")
        /* Names of all the supported schemas in GN (via the SchemaManager). */
        private List<String> allSchemaIds;

        @Schema(
                description =
                        "map from the schemaId (schema name) to the number of records of that schema type in this GeoNetwork system",
                example = "{\"iso19115-3.2018\": 4, \"iso19139\": 39 }")
        /* Maps scheamId -> number of records in that schema.
         * SELECT schemaId, count(*) FROM metadata GROUP BY schemaId; */
        private Map<String, Long> recordsPerSchema;
    }
}
