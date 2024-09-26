/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.model.xsd;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * @author heikki doeleman
 */
public abstract class BaseHandler {

  /** TODO Javadoc. */
  protected void handleSequence(
      Element elChild, ArrayList<ElementEntry> alElements, ElementInfo ei) {
    @SuppressWarnings("unchecked")
    List<Element> sequence = elChild.getChildren();

    for (Element elElem : sequence) {

      if (isChoiceOrElementOrGroupOrSequence(elElem)) {
        alElements.add(new ElementEntry(elElem, ei.file, ei.targetNS, ei.targetNSPrefix));
      }
    }
  }

  /** TODO Javadoc. */
  protected boolean isChoiceOrElementOrGroupOrSequence(Element elElem) {
    return elElem.getName().equals("choice")
        || elElem.getName().equals("element")
        || elElem.getName().equals("group")
        || elElem.getName().equals("sequence");
  }

  /** TODO Javadoc. */
  protected String handleAttribs(ElementInfo ei, String name) {
    @SuppressWarnings("unchecked")
    List<Attribute> attribs = ei.element.getAttributes();
    for (Attribute at : attribs) {

      String attrName = at.getName();
      if (attrName.equals("name")) {
        name = at.getValue();
        if ((name.indexOf(':') == -1) && (ei.targetNSPrefix != null)) {
          name = ei.targetNSPrefix + ":" + at.getValue();
        }
      }
    }
    return name;
  }
}
