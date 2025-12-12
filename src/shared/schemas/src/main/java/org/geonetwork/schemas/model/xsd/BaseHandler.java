/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.schemas.model.xsd;

import java.util.List;
import org.jdom.Attribute;
import org.jdom.Element;

/**
 * Base handler.
 *
 * @author heikki doeleman
 */
public abstract class BaseHandler {

    /** TODO Javadoc. */
    protected void handleSequence(Element elChild, List<ElementEntry> alElements, ElementInfo ei) {
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
