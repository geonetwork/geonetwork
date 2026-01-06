/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.thesaurus.util;

import java.util.Locale;

public class LanguageCodeConverter {

    /**
     * Convert ISO three letter language code to Java two letter Locale name.
     *
     * @param threeLetterCode ISO three letter language code
     * @return {@link Locale#getLanguage} two letter code, defaults to {@code en} if not matched.
     */
    public static String toTwoLetterCode(String threeLetterCode) {

        return java.util.Arrays.stream(Locale.getAvailableLocales())
                .filter(locale -> locale.getISO3Language().equalsIgnoreCase(threeLetterCode))
                .map(Locale::getLanguage)
                .findFirst()
                .orElse("en"); // return default language as English.
    }
}
