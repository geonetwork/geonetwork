/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.querybuilder;

import com.google.common.base.Ascii;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.common.Strings;
import org.geonetwork.application.LowLoggingRuntimeException;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsAdvancedFacetDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetsDto;
import org.geonetwork.ogcapi.service.facets.FacetsJsonService;
import org.springframework.stereotype.Component;

/**
 * This parses the "&facets=..." parameter of the /collections/{collectionId}/items endpoint, and validates that the
 * facet names are valid.
 *
 * <p>See OGCAPI-RECORDS specification Part 2: Facets ("Advanced Facets").
 *
 * <p>See OgcApiRecordsAdvancedFacetDto.java and OpenAPI definition.
 */
@AllArgsConstructor
@Component
public class AdvancedFacetsBuilder {

    private final FacetsJsonService facetsService;

    public static final int MAX_BUCKETS = 50;

    /**
     * Given a set of facets from the request, parse and validate them, and return a list of
     * OgcApiRecordsAdvancedFacetDto.
     *
     * <p>facetsFromRequest == null (i.e. when there's no "&facets=..." in request) --> do all facets (this returns
     * null) <br>
     * facetsFromRequest == empty list (i.e. when there's "&facets=" in request, but no value) --> no facets in response
     * (this returns empty list)
     *
     * @param catalogId what catalog is this for (we ensure that the facet names are legal, and the are, technically,
     *     catalog-scoped).
     * @param facetsFromRequest requested facets (i.e. "keywords:20:value_asc","organization:10")
     * @return parsed objects
     * @throws Exception invalid facet definition from user
     */
    public List<OgcApiRecordsAdvancedFacetDto> buildAdvancedFacets(String catalogId, List<String> facetsFromRequest)
            throws Exception {
        if (facetsFromRequest == null) {
            return null;
        }
        if (facetsFromRequest.isEmpty()) {
            return new ArrayList<>(); // empty
        }

        var result = parse(facetsFromRequest);
        validate(result, catalogId);
        return result;
    }

    /**
     * Validate that the facets are legal:
     *
     * <p>1. facetName exist in the ../{catalogId}/facets list of facets
     *
     * @param requestedFacets parsed from user
     * @param catalogId what catalog is this for
     * @throws Exception facet is illegal
     */
    public void validate(List<OgcApiRecordsAdvancedFacetDto> requestedFacets, String catalogId) throws Exception {
        var currentFacets = facetsService.buildFacets(catalogId);

        requestedFacets.forEach(requestedFacet -> {
            var facetName = requestedFacet.getFacetName();
            validateFacetNameExists(facetName, currentFacets);
        });
    }

    /**
     * checks that the facetName is configured in the current set of facets. Will throw if invalid.
     *
     * @param facetName name of facet (from user)
     * @param currentFacets set of defined facets
     */
    private void validateFacetNameExists(String facetName, OgcApiRecordsFacetsDto currentFacets) {
        if (!currentFacets.getFacets().containsKey(facetName)) {
            throw new LowLoggingRuntimeException("facet does not exist: " + facetName + ", all facet names: "
                    + String.join(",", currentFacets.getFacets().keySet()));
        }
    }

    /**
     * parses the information from the user.
     *
     * <p>Example input: "keywords:20:value_asc"
     *
     * <p>Output: { . <br>
     * facetName: "keywords", <br>
     * bucketSize: 20, <br>
     * sorting: VALUE_ASC <br>
     * }
     *
     * @param facetsFromRequest set of facets in the "&facets=..." from user
     * @return parsed in OgcApiRecordsAdvancedFacetDto
     */
    public List<OgcApiRecordsAdvancedFacetDto> parse(List<String> facetsFromRequest) {
        return facetsFromRequest.stream()
                .map(f -> {
                    try {
                        return parseFacet(f);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }

    /**
     * parses a single facet definition from the user ("keywords:20:value_asc") into an OgcApiRecordsAdvancedFacetDto.
     *
     * @param userFacetDefinition from user's request
     * @return parsed version.
     * @throws Exception invalid facet definition
     */
    private OgcApiRecordsAdvancedFacetDto parseFacet(String userFacetDefinition) throws Exception {
        var subStrs = StringUtils.splitByWholeSeparatorPreserveAllTokens(userFacetDefinition, ":");
        if (subStrs.length < 1 || subStrs.length > 3) {
            throw new Exception("Invalid facet format: " + userFacetDefinition);
        }
        var result = new OgcApiRecordsAdvancedFacetDto();
        result.facetName(subStrs[0]);
        if (Strings.isEmpty(result.getFacetName())) {
            throw new Exception("facetName is blank: " + userFacetDefinition);
        }
        if (subStrs.length > 1) {
            if (!Strings.isEmpty(subStrs[1])) {
                if (!StringUtils.isNumeric(subStrs[1])) {
                    throw new Exception("Bucket size must be a number: " + userFacetDefinition);
                }
                result.bucketSize(Integer.parseInt(subStrs[1]));
                if (result.getBucketSize() < 0) {
                    throw new Exception("Bucket size must be zero or positive: " + userFacetDefinition);
                }
                if (result.getBucketSize() > MAX_BUCKETS) {
                    throw new Exception(
                            "Bucket size exceeds maximum: " + userFacetDefinition + ", max is " + MAX_BUCKETS);
                }
            }
        }
        if (subStrs.length > 2) {
            var sortBy = Ascii.toLowerCase(subStrs[2]);
            if (!Strings.isEmpty(sortBy)) {
                result.sorting(OgcApiRecordsAdvancedFacetDto.SortingEnum.fromValue(sortBy));
            }
        }
        return result;
    }
}
