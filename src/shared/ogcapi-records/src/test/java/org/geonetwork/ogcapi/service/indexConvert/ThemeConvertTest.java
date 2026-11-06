/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.indexConvert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.geonetwork.index.model.record.Keyword;
import org.geonetwork.index.model.record.Thesaurus;
import org.junit.jupiter.api.Test;

public class ThemeConvertTest {

    @Test
    public void testConvert() {
        var allKeywords = create();
        var result = ThemeConvert.convert(allKeywords, null);

        assertEquals(1, result.size());
        assertEquals("title", result.get(0).getScheme());

        assertEquals(2, result.get(0).getConcepts().size());
        assertEquals("k1 default", result.get(0).getConcepts().get(0).getId());
        assertEquals("k2 default", result.get(0).getConcepts().get(1).getId());
    }

    @Test
    public void testConvertLang() {
        var allKeywords = create();

        var result = ThemeConvert.convert(allKeywords, "eng");
        assertEquals("k1 eng", result.get(0).getConcepts().get(0).getId());
    }

    @Test
    public void testConvertInvalidLinkKeepsConcepts() {
        var allKeywords = create();
        var keywords = allKeywords.get("th_otherKeywords-theme").getKeywords();
        keywords.set(
                0,
                new Keyword(Map.of(
                        "default", "k1 default",
                        "link", "https://applications.eea.europa.eu/ManagementPlan/default.aspx#2019 1.5.2")));
        keywords.set(
                1,
                new Keyword(Map.of(
                        "default", "k2 default",
                        "link", "https://example.com/k2")));

        var result = ThemeConvert.convert(allKeywords, null);

        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getConcepts().size());
        assertEquals("k1 default", result.get(0).getConcepts().get(0).getId());
        assertNull(result.get(0).getConcepts().get(0).getUrl());
        assertEquals(
                URI.create("https://example.com/k2"),
                result.get(0).getConcepts().get(1).getUrl());
    }

    @Test
    public void testConvertLinkWithSurroundingSpacesAndBlankLink() {
        var allKeywords = create();
        var keywords = allKeywords.get("th_otherKeywords-theme").getKeywords();
        keywords.set(0, new Keyword(Map.of("default", "k1 default", "link", "  https://example.com/k1 ")));
        keywords.set(1, new Keyword(Map.of("default", "k2 default", "link", "")));

        var result = ThemeConvert.convert(allKeywords, null);

        assertEquals(
                URI.create("https://example.com/k1"),
                result.get(0).getConcepts().get(0).getUrl());
        assertNull(result.get(0).getConcepts().get(1).getUrl());
    }

    @Test
    public void testConvertThesaurusWithoutKeywords() {
        var allKeywords = create();
        allKeywords.get("th_otherKeywords-theme").setKeywords(null);

        var result = ThemeConvert.convert(allKeywords, null);

        assertEquals(0, result.size());
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
