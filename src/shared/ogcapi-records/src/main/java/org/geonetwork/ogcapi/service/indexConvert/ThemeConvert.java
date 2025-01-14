/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.indexConvert;

import org.geonetwork.index.model.record.Keyword;
import org.geonetwork.index.model.record.Thesaurus;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsThemeConceptsInnerDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsThemeDto;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.geonetwork.ogcapi.service.indexConvert.ElasticIndex2Catalog.getLangString;

/**
 * converts elastic json index records (`allKeywords`) to ogcapi theme
 */
public class ThemeConvert {

  /**
   * converts the elastic index record (`allKeywords`) to an ogcapi-record theme.
   *
   * @param map      elastic index record (`allKeywords` node)
   * @param lang3iso language (From request) - 3 letter iso lang value (i.e. 'eng')
   * @return ogcapi-record theme
   * @throws URISyntaxException bad uri in the thesaurus
   */
  public static List<OgcApiRecordsThemeDto> convert(Map<String, Thesaurus> map, String lang3iso)
    throws URISyntaxException {
    if (map == null) {
      return null;
    }
    List<OgcApiRecordsThemeDto> result = new ArrayList<>();
    for (var theme : map.values()) {
      var themeSchema = theme.getLink();
      // at least put something here!
      if (!StringUtils.hasText(themeSchema)
        && theme.getMultilingualTitle() != null
        && !theme.getMultilingualTitle().isEmpty()) {
        var multilingualTitle = theme.getMultilingualTitle();
        var multilingualTitleLink = multilingualTitle.get("link");

        if (StringUtils.hasText(multilingualTitleLink)) {
          themeSchema = multilingualTitleLink;
        } else {
          themeSchema = getLangString(multilingualTitle, lang3iso);
        }
      }
      if (!StringUtils.hasText(themeSchema) && theme.getTitle() != null) {
        themeSchema = theme.getTitle();
      }
      if (!StringUtils.hasText(themeSchema) && theme.getTheme() != null) {
        themeSchema = theme.getTheme();
      }
      if (!StringUtils.hasText(themeSchema) && theme.getId() != null) {
        themeSchema = theme.getId();
      }
      var theme2 = new OgcApiRecordsThemeDto();
      theme2.setScheme(themeSchema);

      List<Keyword> conceptsList = theme.getKeywords();
      for (var anConcept : conceptsList) {
        var link = anConcept.getProperties().get("link");
        var ogcConcept = new OgcApiRecordsThemeConceptsInnerDto();
        ogcConcept.setId(getLangString(anConcept.getProperties(), lang3iso));
        if (link != null) {
          ogcConcept.setUrl(new URI(link));
        }
        theme2.getConcepts().add(ogcConcept);
      }
      if (!theme2.getConcepts().isEmpty()) {
        result.add(theme2);
      }
    }
    return result;
  }
}
