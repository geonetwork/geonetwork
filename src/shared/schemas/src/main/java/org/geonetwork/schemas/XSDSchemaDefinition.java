/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jdom.Namespace;

@Slf4j
public class XSDSchemaDefinition {
    private Map<String, List<String>> hmElements = new HashMap<>();
    private Map<String, List<List<String>>> hmRestric = new HashMap<>();
    private Map<String, XSDTypeDefinition> hmTypes = new HashMap<>();
    private Map<String, List<String>> hmSubs = new HashMap<>();
    private Map<String, String> hmSubsLink = new HashMap<>();
    private Map<String, Namespace> hmNameSpaces = new HashMap<>();
    private Map<String, Namespace> hmPrefixes = new HashMap<>();

    private String[] schematronRules;

    XSDSchemaDefinition() {}

    public XSDTypeDefinition getTypeInfo(String type) {
        if (hmTypes.get(type) == null) return new XSDTypeDefinition();
        else return hmTypes.get(type);
    }

    public String getElementType(String elem, String parent) throws Exception {
        // two cases here - if we have just one element (or a substitute) with
        // this name then return its type

        List<String> childType = hmElements.get(elem);
        if (childType == null) {
            // Check and see whether we can substitute another element from the
            // substitution link
            String oldelem = elem;
            elem = hmSubsLink.get(elem);
            childType = hmElements.get(elem);
            if (childType == null) {
                log.warn("ERROR: Mismatch between schema and xml: No type for 'element' : "
                        + oldelem
                        + " with parent "
                        + parent
                        + ". Returning xs:string");
                return "xs:string";
            }
        }
        if (childType.size() == 1) return childType.get(0);

        // OTHERWISE get the type by examining the parent:
        // for each parent with that name parent
        // 1. retrieve its mdt
        List<String> exType = hmElements.get(parent);
        if (exType == null) return "xs:string";
        for (String type : exType) {
            // 2. search that mdt for the element names elem
            XSDTypeDefinition mdt = getTypeInfo(type);
            for (int k = 0; k < mdt.getElementCount(); k++) {
                String elemTest = mdt.getElementAt(k);
                // 3. return the type name of that element
                if (elem.equals(elemTest)) {
                    return mdt.getElementTypeAt(k);
                }
            }
        }

        return null;
    }

    /** A simple type is a type that has no children and no attributes (but can have restrictions on its value) */
    public boolean isSimpleElement(String elem, String parent) throws Exception {
        String type = getElementType(elem, parent);
        return type != null && !hmTypes.containsKey(type);
    }

    public List<String> getElementValues(String elem, String parent) throws Exception {

        String type = getElementType(elem, parent);
        String restricName = elem;
        if (type != null) restricName = restricName + "+" + type;

        // two cases here - if we have just one element with this name
        // then return its values
        List<List<String>> childValues = hmRestric.get(restricName);
        if (childValues == null) return null;
        if (childValues.size() == 1) return childValues.get(0);

        // OTHERWISE we don't know what to do so return the first one anyway! This
        // should not happen....
        return childValues.get(0);
    }

    void addElement(String name, String type, List<String> alValues, List<String> alSubs, String subLink) {
        // first just add the subs - because these are for global elements we
        // never have a clash because global elements are all in the same scope
        // and are thus unique
        if (alSubs != null && !alSubs.isEmpty()) hmSubs.put(name, alSubs);
        if (subLink != null && StringUtils.isNotBlank(subLink)) hmSubsLink.put(name, subLink);

        List<String> exType = hmElements.get(name);

        // it's already there but the type has been added already
        if (exType != null && exType.contains(type)) return;

        // it's already there but doesn't have this type
        if (exType != null && !exType.contains(type)) {

            // it's not there so add a new list
        } else {
            exType = new ArrayList<>();
            hmElements.put(name, exType);
        }
        exType.add(type);

        String restricName = name;
        if (type != null) restricName = name + "+" + type;

        // it's already there
        List<List<String>> exValues = hmRestric.get(restricName);
        if (exValues != null) {
            // it's not there so add a new list of lists
        } else {
            exValues = new ArrayList<>();
            hmRestric.put(restricName, exValues);
        }
        exValues.add(alValues);
    }

    public void addType(String name, XSDTypeDefinition mdt) {
        mdt.setName(name);
        hmTypes.put(name, mdt);
    }

    public void addNS(String targetNSPrefix, String targetNSUri) {

        Namespace ns = Namespace.getNamespace(targetNSPrefix, targetNSUri);
        hmNameSpaces.put(targetNSPrefix, ns);
        hmPrefixes.put(targetNSUri, ns);
    }

    @JsonIgnore
    public String getNS(String targetNSPrefix) {
        Namespace ns = hmNameSpaces.get(targetNSPrefix);
        if (ns != null) {
            return ns.getURI();
        } else {
            return null;
        }
    }

    /** Return the list of namespaces for the schema. */
    @JsonIgnore
    public List<Namespace> getNamespaces() {
        List<Namespace> list = new ArrayList<>(hmNameSpaces.size());
        for (Namespace ns : hmNameSpaces.values()) {
            list.add(ns);
        }
        return list;
    }

    public String getPrefix(String theNSUri) {
        Namespace ns = hmPrefixes.get(theNSUri);
        if (ns != null) {
            return ns.getPrefix();
        } else {
            return null;
        }
    }

    @JsonIgnore
    public List<Namespace> getSchemaNS() {
        return new ArrayList<>(hmPrefixes.values());
    }

    @JsonProperty(value = "namespaces")
    public Map<String, String> getSchemaNSWithPrefix() {
        Map<String, String> mapNs = new HashMap<>();
        List<Namespace> schemaNsList = getSchemaNS();

        for (Namespace ns : schemaNsList) {
            mapNs.put(ns.getPrefix(), ns.getURI());
        }
        return mapNs;
    }

    /** Return the list of schematron rules to applied for this schema */
    public String[] getSchematronRules() {
        if (schematronRules != null) {
            return this.schematronRules.clone();
        } else {
            return new String[] {};
        }
    }

    @SuppressWarnings("unused")
    private void setSchematronRules(String[] schematronRules) {
        if (schematronRules != null) {
            this.schematronRules = schematronRules.clone();
        }
    }
}
