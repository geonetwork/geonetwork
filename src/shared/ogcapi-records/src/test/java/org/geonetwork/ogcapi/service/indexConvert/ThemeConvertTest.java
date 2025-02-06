/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.geonetwork.index.model.record.Keyword;
import org.geonetwork.index.model.record.Thesaurus;
import org.junit.jupiter.api.Test;

public class ThemeConvertTest {

    @Test
    public void testConvert() throws URISyntaxException {
        var allKeywords = create();
        var result = ThemeConvert.convert(allKeywords, null);

        assertEquals(1, result.size());
        assertEquals("title", result.get(0).getScheme());

        assertEquals(2, result.get(0).getConcepts().size());
        assertEquals("k1 default", result.get(0).getConcepts().get(0).getId());
        assertEquals("k2 default", result.get(0).getConcepts().get(1).getId());
    }

    @Test
    public void testConvertLang() throws URISyntaxException {
        var allKeywords = create();

        var result = ThemeConvert.convert(allKeywords, "eng");
        assertEquals("k1 eng", result.get(0).getConcepts().get(0).getId());
    }

    public Map<String, Thesaurus> create() {
        Map<String, Thesaurus> map = new HashMap<String, Thesaurus>();
        var thesaurus = new Thesaurus();
        thesaurus.setTitle("title");
        var keywords = new ArrayList<Keyword>();
        var k1 = new Keyword(Map.of(
                "default", "k1 default",
                "langeng", "k1 eng"));
        var k2 = new Keyword(Map.of(
                "default", "k2 default",
                "langeng", "k2 eng"));
        keywords.add(k1);
        keywords.add(k2);
        thesaurus.setKeywords(keywords);
        map.put("th_otherKeywords-theme", thesaurus);

        return map;
    }
}
