/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.utility.legacy.resolver;

/**
 * Represents a strategy for rewriting the href of a XSLT or XML import.
 *
 * @author Jesse on 11/28/2014.
 */
public interface ResolverRewriteDirective {
    /** Test if this should be applied to the href. */
    boolean appliesTo(String href);

    /** Modify the url for locating the referenced stylesheet. */
    String rewrite(String href);

    /**
     * Each directive must have a key that identifies the directive so that it is possible to find and
     * replace the directive if (for example) the resource moves. An example of this is in testing.
     * Each test might need the value to be different.
     */
    Object getKey();
}
