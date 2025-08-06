/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.schemas.plugin.CSWPlugin;
import org.geonetwork.schemas.plugin.HttpLink;
import org.geonetwork.schemas.plugin.SavedQuery;
import org.geonetwork.utility.legacy.exceptions.ResourceNotFoundException;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;

@Data
@Slf4j
public abstract class SchemaPlugin implements CSWPlugin {

    protected SchemaPluginConfiguration configuration;

    XSDSchemaDefinition xsdSchemaDefinition;

    Path directory;

    protected SchemaPlugin(String identifier, Set<Namespace> allNamespaces) {
        this.identifier = identifier;
        this.allNamespaces = Collections.unmodifiableSet(allNamespaces);
    }

    public final String identifier;

    // TODO: GN5 i18n file access
    //    private Map<String, XmlFile> schemaInfo = new HashMap<String, XmlFile>();

    // TODO: GN5 conversion are in convert/from* and convert/to* files
    //    private List<Element> conversionElements = new ArrayList<>();

    private List<SavedQuery> savedQueries = new ArrayList<>();

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

    private List<String> xpathTitle;

    protected List<String> elementsToProcess = new ArrayList<>();

    /** Links to analyze in a metadata record */
    protected List<HttpLink> analyzedLinks;

    /**
     * Processes the passed element. This base class just return the same element without modifications but can be
     * overridden in a schema plugin in order to modify an element by one of its substitutes.
     *
     * @param el element to process.
     * @param parsedAttributeName the name of the attribute, for example <code>xlink:href</code>
     * @return the processed element.
     */
    public Element processElement(Element el, String attributeName, String parsedAttributeName, String attributeValue) {
        return el;
    }

    public abstract String getSchemasResourcePath();

    /**
     * Query XML document with one of the saved query to retrieve a simple string value.
     *
     * @param savedQuery {@link SavedQuery}
     * @param xml XML document to query
     */
    @SuppressWarnings("unused")
    public String queryString(String savedQuery, Element xml) throws ResourceNotFoundException, JDOMException {
        SavedQuery query = getSavedQuery(savedQuery);
        if (query == null) {
            throw new ResourceNotFoundException(String.format(
                    "Saved query '%s' for schema '%s' not found. Available queries are '%s'.",
                    savedQuery,
                    getIdentifier(),
                    getSavedQueries().stream().map(SavedQuery::getId).collect(Collectors.joining(", "))));
        }

        String xpath = query.getXpath();
        if (log.isDebugEnabled()) {
            log.error(String.format("Saved query XPath: %s", xpath));
        }

        return Xml.selectString(xml, xpath, getAllNamespaces().stream().toList());
    }
}
