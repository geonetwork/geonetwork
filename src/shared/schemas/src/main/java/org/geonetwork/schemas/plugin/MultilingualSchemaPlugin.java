/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.schemas.plugin;

import java.util.List;
import org.jdom.Element;
import org.jdom.JDOMException;

/** Created by francois on 8/20/14. */
public interface MultilingualSchemaPlugin {
    /**
     * Return the sub element matching the requested language.
     *
     * @param element The element to search in
     * @param languageIdentifier The translation language to search for
     */
    public abstract List<Element> getTranslationForElement(Element element, String languageIdentifier);

    public abstract void addTranslationToElement(Element element, String languageIdentifier, String value);

    public abstract Element removeTranslationFromElement(Element element, List<String> mdLang) throws JDOMException;
}
