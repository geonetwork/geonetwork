/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.utility.legacy.exceptions.NoSchemaMatchesException;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MetadataSchemaAutodetector {
    private final List<? extends SchemaPlugin> schemaPlugins;
    private final Deque<String> schemaPluginDependencies;

    public MetadataSchemaAutodetector(List<? extends SchemaPlugin> schemaPlugins) {
        this.schemaPlugins = schemaPlugins;
        this.schemaPluginDependencies = buildSchemaDependencies();
    }

    /**
     * Process the schema plugins to find the one that matches the metadata record.
     *
     * <p>Schema plugins are processed first the ones that depend on others and last the ones that doesn't have
     * dependencies.
     *
     * @param md Metadata to identify the schema that belongs to.
     * @return Schema idenfier for the metadata provided.
     * @throws JDOMException
     * @throws NoSchemaMatchesException
     */
    public String autodetectSchema(Element md) throws JDOMException, NoSchemaMatchesException {
        Iterator<String> pluginDependenciesIterator = schemaPluginDependencies.iterator();

        String schemaName = pluginDependenciesIterator.next();

        while (!schemaName.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug(String.format("	Checking schema: %s", schemaName));
            }
            Optional<? extends SchemaPlugin> schemaOpt = getSchemaPlugin(schemaName);

            if (schemaOpt.isPresent()) {
                SchemaPlugin schemaPlugin = schemaOpt.get();
                String autodetectConfiguration = schemaPlugin.getConfiguration().getAutodetect();

                boolean matchesSchema = Xml.selectBoolean(
                        md,
                        autodetectConfiguration,
                        schemaPlugin.getXsdSchemaDefinition().getNamespaces());
                if (matchesSchema) {
                    return schemaName;
                }
            }

            schemaName = pluginDependenciesIterator.next();
        }

        throw new NoSchemaMatchesException("Autodetecting schema failed for metadata record with root element "
                + md.getName()
                + " in namespace "
                + md.getNamespace()
                + ".");
    }

    /**
     * Analyses the dependencies of the metadata schema plugins, creating a stack of schema plugin identifiers, with
     * schemas that have no dependencies at the bottom of the stack and schemas that depend on other schemas at the top.
     * Each schema plugin depends on at most one other schema plugin. For example: - iso19139.nl.geografie.2.1.0 depends
     * on iso19139 - iso19139.ca.HNAP depends on iso19139 - iso19139 does not depend on other schema plugin -
     * iso19115-3.2018 does not depend on other schema plugin Returns a stack with the names of the schemas, with the
     * schemas that depend on others at the top and the names of the schemas without dependencies at the bottom (the
     * order in these two sets is not guaranteed): - iso19139.nl.geografie.2.1.0 - iso19139.ca.HNAP - iso19115-3.2018 -
     * iso19139
     *
     * @return Deque
     */
    private Deque<String> buildSchemaDependencies() {
        Deque<String> schemaDependencies = new ArrayDeque<>();

        // Stores for each schema plugin the number of dependencies it has.
        // Positions correspond to the schemas in the schema plugins list.
        List<Integer> schemaDependenciesCount =
                schemaPlugins.stream().map(s -> 0).collect(Collectors.toList());

        for (int i = 0; i < schemaPlugins.size(); i++) {
            String dependsOn = schemaPlugins.get(i).getConfiguration().getDepends();

            // Process the dependency of the current schema and follow all transitive dependencies
            while ((dependsOn != null) && !dependsOn.isEmpty()) {
                Optional<? extends SchemaPlugin> schemaPluginOpt = getSchemaPlugin(dependsOn);

                // Increment the dependency count for the schema plugin that depends on
                if (schemaPluginOpt.isPresent()) {
                    int pos = schemaPlugins.indexOf(schemaPluginOpt.get());
                    schemaDependenciesCount.set(pos, schemaDependenciesCount.get(pos) + 1);

                    dependsOn = schemaPluginOpt.get().getConfiguration().getDepends();
                } else {
                    dependsOn = "";
                }
            }
        }

        // Creates a map to store each schema plugin and its dependencies
        // The key is the schema name and the value is the schema plugin
        // number of dependencies in the original list
        Map<String, Integer> schemasWithNumberOfDependencies = new HashMap<>();
        int p = 0;
        for (Integer position : schemaDependenciesCount) {
            schemasWithNumberOfDependencies.put(schemaPlugins.get(p++).getIdentifier(), position);
        }

        // Sort the schema plugins based on the number of dependencies they have
        // First schemas with no dependencies and after the schemas with dependencies
        List<Map.Entry<String, Integer>> list = new ArrayList<>(schemasWithNumberOfDependencies.entrySet());
        list.sort(Map.Entry.comparingByValue());

        // Get the schema identifiers in the order to be processed
        list.stream().map(Map.Entry::getKey).forEach(schemaDependencies::push);

        return schemaDependencies;
    }

    /**
     * Retrieves a schema plugin by its identifier
     *
     * @param schemaIdentifier Schema identifier
     * @return Schema plugin that match the schema identifier
     */
    private Optional<? extends SchemaPlugin> getSchemaPlugin(String schemaIdentifier) {
        return schemaPlugins.stream()
                .filter(schemaPlugin -> schemaPlugin.getIdentifier().equals(schemaIdentifier))
                .findFirst();
    }
}
