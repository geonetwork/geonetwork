/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.utility.schemas;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
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

    private final Map<CodelistKey, Codelists> codelists = new ConcurrentHashMap<>();
    private final XmlMapper xmlMapper = new XmlMapper();

    protected record CodelistKey(String schema, String language) {}

    /** Get the translation of a code. */
    @Cacheable(cacheNames = "schema-codelists")
    public String getTranslation(String schema, String codeListNameOrAlias, String code, String language) {
        Codelists cachedList =
                this.codelists.computeIfAbsent(new CodelistKey(schema, language), this::loadTranslations);

        return Optional.ofNullable(cachedList).map(Codelists::getCodelist).orElse(Collections.emptyList()).stream()
                .filter(codelist -> codeListNameOrAlias.equals(codelist.getName())
                        || codeListNameOrAlias.equals(codelist.getAlias()))
                .flatMap(codelist -> codelist.getEntry().stream())
                .filter(entry -> entry.getCode().equals(code))
                .findFirst()
                .map(Entry::getLabel)
                .orElse("");
    }

    private Codelists loadTranslations(CodelistKey key) {
        String path = String.format("schemas/%s/loc/%s/codelists.xml", key.schema(), key.language());

        try (InputStream is = new ClassPathResource(path).getInputStream()) {
            return xmlMapper.readValue(is, Codelists.class);
        } catch (IOException e) {
            // There is no translation and this will not change at runtime...
            return Codelists.builder().codelist(Collections.emptyList()).build();
        }
    }
}
