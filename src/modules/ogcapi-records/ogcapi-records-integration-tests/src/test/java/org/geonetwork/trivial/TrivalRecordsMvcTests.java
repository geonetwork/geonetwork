/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.trivial;

import static org.junit.jupiter.api.Assertions.*;

import org.geonetwork.infrastructure.ElasticPgMvcBaseTest;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetCollections200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.junit.jupiter.api.Test;

/** some trivial tests to verify that the containers have spun up and there is data available */
public class TrivalRecordsMvcTests extends ElasticPgMvcBaseTest {

    /**
     * checks that there are 2 named (title) collections. This will fail if Postgresql didn't come up properly!
     *
     * @throws Exception bad response (usually unparseable or bad MVC or bad PGSQL)
     */
    @Test
    void test_simple_collections_test() throws Exception {
        var result = retrieveUrlJson("ogcapi-records/collections", OgcApiRecordsGetCollections200ResponseDto.class);

        assertEquals(2, result.getCollections().size());
        var collectionAll = result.getCollections().stream()
                .filter(x -> x.getTitle().equals("Test GeoNetwork-UI instance"))
                .findFirst()
                .get();
        var collectionMetawal = result.getCollections().stream()
                .filter(x -> x.getTitle().equals("Metawal"))
                .findFirst()
                .get();

        assertNotNull(collectionAll);
        assertNotNull(collectionMetawal);

        assertEquals(MAIN_COLLECTION_ID, collectionAll.getId());
        assertEquals(METAWAL_COLLECTION_ID, collectionMetawal.getId());
    }

    @Test
    void test_query() throws Exception {
        var items = retrieveUrlJson(
                "ogcapi-records/collections/" + MAIN_COLLECTION_ID + "/items",
                OgcApiRecordsGetRecords200ResponseDto.class);

        assertNotNull(items);
        assertEquals(10, items.getFeatures().size());
        assertEquals(28, items.getNumberMatched());
    }
}
