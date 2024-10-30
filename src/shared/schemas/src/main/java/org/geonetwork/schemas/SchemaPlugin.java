/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.geonetwork.schemas.plugin.CSWPlugin;
import org.geonetwork.schemas.plugin.HttpLink;
import org.geonetwork.schemas.plugin.SavedQuery;
import org.jdom.Element;
import org.jdom.Namespace;

public abstract class SchemaPlugin implements CSWPlugin {

    protected SchemaPlugin(String identifier, Set<Namespace> allNamespaces) {
        this.identifier = identifier;
        this.allNamespaces = Collections.unmodifiableSet(allNamespaces);
    }

    public final String identifier;

    public String getIdentifier() {
        return identifier;
    }

    private List<SavedQuery> savedQueries = new ArrayList<>();

    public List<SavedQuery> getSavedQueries() {
        return savedQueries;
    }

    public void setSavedQueries(List<SavedQuery> savedQueries) {
        this.savedQueries = savedQueries;
    }

    public @Nullable SavedQuery getSavedQuery(@Nonnull String queryKey) {
        Iterator<SavedQuery> iterator = this.getSavedQueries().iterator();
        while (iterator.hasNext()) {
            SavedQuery query = iterator.next();
            if (queryKey.equals(query.getId())) {
                return query;
            }
        }
        return null;
    }

    private Set<Namespace> allNamespaces;

    public Set<Namespace> getNamespaces() {
        return allNamespaces;
    }

    private List<String> xpathTitle;

    public void setXpathTitle(List<String> xpathTitle) {
        this.xpathTitle = xpathTitle;
    }

    public List<String> getXpathTitle() {
        return xpathTitle;
    }

    protected List<String> elementsToProcess = new ArrayList<>();

    public void setElementsToProcess(List<String> elementsToProcess) {
        this.elementsToProcess = elementsToProcess;
    }

    /** Links to analyze in a metadata record */
    protected List<HttpLink> analyzedLinks;

    public void setAnalyzedLinks(List<HttpLink> analyzedLinks) {
        this.analyzedLinks = analyzedLinks;
    }

    public List<HttpLink> getAnalyzedLinks() {
        return analyzedLinks;
    }

    /**
     * Processes the passed element. This base class just return the same element without
     * modifications but can be overridden in a schema plugin in order to modify an element by one of
     * its substitutes.
     *
     * @param el element to process.
     * @param parsedAttributeName the name of the attribute, for example <code>xlink:href</code>
     * @return the processed element.
     */
    public Element processElement(Element el, String attributeName, String parsedAttributeName, String attributeValue) {
        return el;
    }
}
