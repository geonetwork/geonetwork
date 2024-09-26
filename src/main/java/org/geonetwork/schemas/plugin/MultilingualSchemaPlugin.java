/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
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
  public abstract List<Element> getTranslationForElement(
      Element element, String languageIdentifier);

  public abstract void addTranslationToElement(
      Element element, String languageIdentifier, String value);

  public abstract Element removeTranslationFromElement(Element element, List<String> mdLang)
      throws JDOMException;
}
