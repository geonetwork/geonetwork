/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.utility.legacy.resolver;

import java.util.Objects;

/**
 * if the url starts with the provided text then write the url with a new prefix.
 *
 * @author Jesse on 11/28/2014.
 */
public class PrefixUrlRewrite implements ResolverRewriteDirective {
    private final String prefix;
    private final String replacement;

    public PrefixUrlRewrite(String prefix, String replacement) {
        Objects.requireNonNull(prefix);
        Objects.requireNonNull(replacement);
        this.prefix = prefix;
        this.replacement = replacement;
    }

    @Override
    public boolean appliesTo(String href) {
        return href.startsWith(prefix);
    }

    @Override
    public String rewrite(String href) {
        return replacement + href.substring(prefix.length());
    }

    @Override
    public Object getKey() {
        return prefix;
    }
}
