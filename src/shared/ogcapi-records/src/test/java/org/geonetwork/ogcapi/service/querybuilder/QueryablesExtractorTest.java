/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.querybuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.geonetwork.ogcapi.service.queryables.QueryablesService;
import org.junit.jupiter.api.Test;

public class QueryablesExtractorTest {

    @Test
    public void testExtractFromQ_simple() {
        QueryablesExtractor extractor = new QueryablesExtractor();
        extractor.queryablesService = new QueryablesService();

        var result = extractor.queryableInString("aa id:abc def", "id");

        assertNotNull(result);
        assertEquals("id", result.getQueryableName());
        assertEquals("abc", result.getQueryableText());
        assertEquals("id:abc", result.getFullText());
    }

    @Test
    public void testExtractFromQ_quoted() {
        QueryablesExtractor extractor = new QueryablesExtractor();
        extractor.queryablesService = new QueryablesService();

        var result = extractor.queryableInString("aaa id:\"abc def\" xyz", "id");

        assertNotNull(result);
        assertEquals("id", result.getQueryableName());
        assertEquals("abc def", result.getQueryableText());
        assertEquals("id:\"abc def\"", result.getFullText());
    }

    @Test
    public void testfull1() {
        QueryablesExtractor extractor = new QueryablesExtractor();
        extractor.queryablesService = new QueryablesService();
        var result = extractor.parseWithQueryables(Arrays.asList("id", "contacts"), Arrays.asList("id:abc"));
        assertNotNull(result);
        assertEquals(1, result.qs.size());
        assertEquals("", result.qs.get(0));
        assertEquals(1, result.queryables.size());
        assertEquals("abc", result.queryables.get("id"));
    }

    @Test
    public void testfull2() {
        QueryablesExtractor extractor = new QueryablesExtractor();
        extractor.queryablesService = new QueryablesService();
        var result =
                extractor.parseWithQueryables(Arrays.asList("id", "contacts"), Arrays.asList("id:abc contacts:dave"));
        assertNotNull(result);
        assertEquals(1, result.qs.size());
        assertEquals("", result.qs.get(0));
        assertEquals(2, result.queryables.size());
        assertEquals("abc", result.queryables.get("id"));
    }

    @Test
    public void testProcess1() {
        QueryablesExtractor extractor = new QueryablesExtractor();
        var queryablesService = mock(QueryablesService.class);
        when(queryablesService.queryableProperties(null)).thenReturn(Arrays.asList("id", "contacts"));
        extractor.queryablesService = queryablesService;

        OgcApiQuery query = new OgcApiQuery();
        query.setQ(Arrays.asList("dave contacts:jeroen"));
        extractor.extractQueryables(query);

        assertEquals(1, query.getQ().size());
        assertEquals("dave", query.getQ().get(0));
        assertEquals(1, query.getPropValues().size());
        assertEquals("jeroen", query.getPropValues().get("contacts"));
    }

    @Test
    public void testProcess2() {
        QueryablesExtractor extractor = new QueryablesExtractor();
        var queryablesService = mock(QueryablesService.class);
        when(queryablesService.queryableProperties(null)).thenReturn(Arrays.asList("id", "contacts"));
        extractor.queryablesService = queryablesService;

        OgcApiQuery query = new OgcApiQuery();
        query.setQ(Arrays.asList("dave contacts:\"jeroen ticheler\" id:abc"));
        extractor.extractQueryables(query);

        assertEquals(1, query.getQ().size());
        assertEquals("dave", query.getQ().get(0));
        assertEquals(2, query.getPropValues().size());
        assertEquals("jeroen ticheler", query.getPropValues().get("contacts"));
        assertEquals("abc", query.getPropValues().get("id"));
    }
}
