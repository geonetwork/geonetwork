/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.thesaurus.util;

import java.util.Locale;

public class LanguageCodeConverter {

    public static String toTwoLetterCode(String threeLetterCode) {

        return java.util.Arrays.stream(Locale.getAvailableLocales())
                .filter(locale -> locale.getISO3Language().equalsIgnoreCase(threeLetterCode))
                .map(Locale::getLanguage)
                .findFirst()
                .orElse("en"); // return default language as English.
    }
}
