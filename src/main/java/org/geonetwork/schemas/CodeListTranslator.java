/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.geonetwork.schemas.model.Codelists;
import org.geonetwork.schemas.model.Entry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Schema codelist translator.
 *
 * <p>Load translations from codelists.xml and provide a method to get the translation of a code.
 */
@Component
public class CodeListTranslator {
  private final Map<String, Codelists> codelists = new HashMap<>();

  /** Get the translation of a code. */
  @Cacheable(cacheNames = "schema-codelists")
  public String getTranslation(String codeListNameOrAlias, String code, String language) {
    loadTranslations("iso19115-3.2018", language);

    return Optional.ofNullable(this.codelists.get(language))
        .orElse(Codelists.builder().codelist(Collections.emptyList()).build())
        .getCodelist()
        .stream()
        .filter(
            codelist ->
                codeListNameOrAlias.equals(codelist.getName())
                    || codeListNameOrAlias.equals(codelist.getAlias()))
        .flatMap(codelist -> codelist.getEntry().stream())
        .filter(entry -> entry.getCode().equals(code))
        .findFirst()
        .map(Entry::getLabel)
        .orElse("");
  }

  private void loadTranslations(String schema, String language) {
    if (this.codelists.containsKey(language)) {
      return;
    }
    XmlMapper xmlMapper = new XmlMapper();
    try {
      File translationFile =
          new ClassPathResource(String.format("schemas/%s/loc/%s/codelists.xml", schema, language))
              .getFile();
      if (translationFile.exists()) {
        this.codelists.put(language, xmlMapper.readValue(translationFile, Codelists.class));
      }
    } catch (IOException e) {
      //      System.out.println(e.getMessage());
    }
  }
}
