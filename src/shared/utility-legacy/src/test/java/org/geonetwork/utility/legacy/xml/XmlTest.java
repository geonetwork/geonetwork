/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.utility.legacy.xml;

import java.io.IOException;
import org.jdom.JDOMException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

public class XmlTest {
    @Test
    public void testSelectBoolean() throws JDOMException, IOException {
        String xml = "<root><node1>text</node1></root>";
        org.jdom.Element element = Xml.loadString(xml, false);

        boolean node1Exists = Xml.selectBoolean(element, "count(node1) = 1");
        Assertions.assertTrue(node1Exists);

        boolean node5Exists = Xml.selectBoolean(element, "count(node5) = 1");
        Assertions.assertFalse(node5Exists);

        // If the XPath expression is not a boolean expression, an exception is thrown
        org.jdom.Element finalElement = element;
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Xml.selectBoolean(finalElement, "node1");
        });
    }
}
