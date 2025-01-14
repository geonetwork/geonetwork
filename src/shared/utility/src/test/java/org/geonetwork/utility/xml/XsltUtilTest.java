/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
/*
 * (c) 2024 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.utility.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.util.HashMap;
import org.geonetwork.GeonetworkTestingApplication;
import org.junit.Test;

public class XsltUtilTest {

    /**
     * runs a simple test where is "executes" a trivial XSLT that xsl:includes another XSLT.
     *
     * <p>The XSLT must be inside a JAR (jar:file:...location of jar...!path/to/xslt.xsl)
     *
     * <p>XSLT comes from the "gn-testing" module.
     *
     * @throws Exception typically a parsing error
     */
    @Test
    public void test_load_from_url_with_includes() throws Exception {

        var myxsl = GeonetworkTestingApplication.class.getClassLoader().getResource("tests/simpleinclude/test.xsl");

        // this might fail if you are running inside your IDE.  Run from maven directly.
        // your IDE should support this.
        // ISSUE: ide will often include resources directly from the filesystem, instead of from
        //        inside the JAR.  When deploying GN5 in a real environment, the system will almost certainly
        //        be get getting the XSLTs from inside a JAR.  There were old problems where the assumption,
        //        incorrectly, was getting the XSLT from a file.
        assertEquals("jar", myxsl.getProtocol());
        myxsl = new URI(myxsl.toString()).toURL(); // be explicit about the URL

        var result = XsltUtil.transformXmlAsString("<dave>hi</dave>", myxsl, new HashMap<>());

        assertTrue(result.contains("from-test-include.xsl"));
        assertTrue(result.contains("from-test.xsl"));
    }
}
