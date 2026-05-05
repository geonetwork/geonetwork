/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.facets;

import java.util.ArrayList;
import java.util.List;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsAdvancedFacetDto;
import org.geonetwork.ogcapi.service.configuration.BucketSorting;
import org.geonetwork.ogcapi.service.configuration.BucketSortingDirection;
import org.geonetwork.ogcapi.service.configuration.OgcFacetConfig;
import org.springframework.stereotype.Component;

@Component
public class AdvancedFacetsService {

    /**
     * given all the configured facets, and what the user wanted, return a COPY of the original.
     *
     * @param facetsOriginal all configured facets
     * @param advancedFacets what the user requested
     * @return copy of the facets config that the user requested (0, N, or all)
     */
    public List<OgcFacetConfig> filterFacets(
            List<OgcFacetConfig> facetsOriginal, List<OgcApiRecordsAdvancedFacetDto> advancedFacets) {
        List<OgcFacetConfig> facets;
        if (advancedFacets == null) {
            facets = new ArrayList<>(facetsOriginal); // use all pre-configured facets
        } else {
            var requestedFacetNames =
                    advancedFacets.stream().map(f -> f.getFacetName()).toList();
            facets = facetsOriginal.stream()
                    .filter(f -> requestedFacetNames.contains(f.getFacetName()))
                    .map(ff -> new OgcFacetConfig(ff)) // deep copy for modification
                    .toList();
        }

        return facets;
    }

    public void copyInUserConfig(OgcFacetConfig originalFacet, OgcApiRecordsAdvancedFacetDto userRequestedFacetConfig) {
        if (userRequestedFacetConfig == null) {
            return;
        }
        if (userRequestedFacetConfig.getBucketSize() != null) {
            originalFacet.setBucketCount(userRequestedFacetConfig.getBucketSize());
        }
        if (userRequestedFacetConfig.getSorting() != null) {
            var userSorting = userRequestedFacetConfig.getSorting();
            if (userSorting.equals(OgcApiRecordsAdvancedFacetDto.SortingEnum.VALUE_ASC)
                    || userSorting.equals(OgcApiRecordsAdvancedFacetDto.SortingEnum.COUNT_ASC)) {
                originalFacet.setBucketSortingDirection(BucketSortingDirection.ASCENDING);
            } else {
                originalFacet.setBucketSortingDirection(BucketSortingDirection.DESCENDING);
            }

            if (userSorting.equals(OgcApiRecordsAdvancedFacetDto.SortingEnum.VALUE_ASC)
                    || userSorting.equals(OgcApiRecordsAdvancedFacetDto.SortingEnum.VALUE_DESC)) {
                originalFacet.setBucketSorting(BucketSorting.VALUE);
            } else {
                originalFacet.setBucketSorting(BucketSorting.COUNT);
            }
        }
    }

    public OgcApiRecordsAdvancedFacetDto findAdvancedFacet(
            List<OgcApiRecordsAdvancedFacetDto> advancedFacets, OgcFacetConfig facet) {
        if (advancedFacets == null) {
            return null;
        }
        for (var advancedFacet : advancedFacets) {
            if (advancedFacet.getFacetName().equals(facet.getFacetName())) {
                return advancedFacet;
            }
        }
        return null;
    }

    public List<OgcFacetConfig> determineFacets(
            List<OgcFacetConfig> facetsOriginal, List<OgcApiRecordsAdvancedFacetDto> advancedFacets) {
        // filter unwanted facets
        List<OgcFacetConfig> facets = filterFacets(facetsOriginal, advancedFacets);
        facets.stream().forEach(f -> copyInUserConfig(f, findAdvancedFacet(advancedFacets, f)));
        return facets;
    }
}
