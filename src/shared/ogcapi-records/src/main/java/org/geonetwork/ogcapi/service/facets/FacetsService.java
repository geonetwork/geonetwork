/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.facets;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetsDto;
import org.geonetwork.ogcapi.service.queryables.QueryablesService;
import org.springframework.stereotype.Service;

@Service
@Slf4j(topic = "org.fao.geonet.ogcapi.records")
public class FacetsService {

    /** full data from facets.json */
    public static final OgcApiRecordsFacetsDto fullFacets = readFacets();

    /** partial data from facets.json (elastic info removed) */
    public OgcApiRecordsFacetsDto getTruncatedFacets() {
        var facets = readFacets();
        facets.setxOgcToElastic(null);
        if (facets != null && facets.getFacets() != null) {
            for (var f : facets.getFacets().values()) {
                try {
                    var method = f.getClass().getMethod("setxElasticProperty", String.class);
                    method.invoke(f, (Object) null);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return facets;
    }

    /**
     * helper method to read the "facets/facets.json" resource into a OgcApiRecordsFacetsDto.
     *
     * @return contents of "facets/facets.json"
     */
    public static OgcApiRecordsFacetsDto readFacets() {
        InputStream is = QueryablesService.class.getClassLoader().getResourceAsStream("facets/facets.json");

        try {
            String text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            text = text.replaceAll("(?m)^//.*", "");

            var objectMapper = new ObjectMapper();
            var result = objectMapper.readValue(text, OgcApiRecordsFacetsDto.class);
            return result;
        } catch (IOException e) {
            log.debug("problem reading in facets - is it mal-formed?", e);
        }

        return null;
    }

    public OgcApiRecordsFacetsDto getFullFacets(String collectionId) {
        return fullFacets;
    }

    public OgcApiRecordsFacetsDto buildFacets(String catalogId) {
        var result = getTruncatedFacets();
        result.setId(catalogId);
        result.setTitle("Facets for catalog " + catalogId);
        return result;
    }
}
