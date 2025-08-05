/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
// =============================================================================
// ===	Copyright (C) 2001-2007 Food and Agriculture Organization of the
// ===	United Nations (FAO-UN), United Nations World Food Programme (WFP)
// ===	and United Nations Environment Programme (UNEP)
// ===
// ===	This program is free software; you can redistribute it and/or modify
// ===	it under the terms of the GNU General Public License as published by
// ===	the Free Software Foundation; either version 2 of the License, or (at
// ===	your option) any later version.
// ===
// ===	This program is distributed in the hope that it will be useful, but
// ===	WITHOUT ANY WARRANTY; without even the implied warranty of
// ===	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
// ===	General Public License for more details.
// ===
// ===	You should have received a copy of the GNU General Public License
// ===	along with this program; if not, write to the Free Software
// ===	Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
// ===
// ===	Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
// ===	Rome - Italy. email: geonetwork@osgeo.org
// ==============================================================================

package org.geonetwork.metadata.editing;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.constants.Edit;
import org.geonetwork.constants.Geonet;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.indexing.IndexingMode;
import org.geonetwork.metadata.MetadataManager;
import org.geonetwork.metadata.editing.model.AddElemValue;
import org.geonetwork.schemas.SchemaManager;
import org.geonetwork.schemas.SchemaPlugin;
import org.geonetwork.schemas.iso19139.ISO19139Namespaces;
import org.geonetwork.schemas.plugin.MultilingualSchemaPlugin;
import org.geonetwork.utility.legacy.Pair;
import org.geonetwork.utility.legacy.exceptions.ResourceNotFoundException;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Attribute;
import org.jdom.Content;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.Text;
import org.jdom.filter.ElementFilter;
import org.jdom.filter.Filter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** Utilities. */
@Component
@Slf4j
@AllArgsConstructor
public class EditUtils {

    public static final String ID = "id";
    public static final String MINOREDIT = "minor";
    public static final String VERSION = "version";

    MetadataManager metadataManager;

    SchemaManager schemaManager;

    EditLib editLib;

    MetadataRepository metadataRepository;

    //    protected AccessManager accessMan;
    //    protected IMetadataValidator metadataValidator;

    /** Visit all descendants of an element and add an empty geonet:ref element for later use by the editor. */
    protected static void addMissingGeoNetRef(Element element) {
        Iterator<Object> descendants = element.getDescendants();
        List<Object> list = List.of(descendants);

        for (Object descendant : list) {
            if (descendant instanceof Element e) {
                if (e.getName() != Edit.RootChild.ELEMENT && e.getNamespace() != Edit.NAMESPACE) {
                    Element geonetRef = e.getChild(Edit.RootChild.ELEMENT, Edit.NAMESPACE);
                    if (geonetRef == null) {
                        geonetRef = new Element(Edit.RootChild.ELEMENT, Edit.NAMESPACE);
                        geonetRef.setAttribute(Edit.Element.Attr.REF, "");
                        e.addContent(geonetRef);
                    }
                }
            }
        }
    }

    /** Updates metadata content. */
    public void updateContent(Map<String, String> params, boolean validate) throws Exception {
        updateContent(params, validate, false, IndexingMode.full);
    }

    /** TODO javadoc. */
    public void updateContent(Map<String, String> params, boolean validate, boolean embedded, IndexingMode indexingMode)
            throws Exception {
        String id = params.get(ID);
        String version = params.get(VERSION);
        String minor = params.getOrDefault(MINOREDIT, "false");

        // --- build hashtable with changes
        // --- each change is a couple (pos, value)

        Map<String, String> htChanges = new HashMap<>();
        params.forEach((k, v) -> {
            if (k.startsWith("_")) {
                htChanges.put(k.substring(1), v);
            }
        });

        // update element and return status
        //        AbstractMetadata result = null;
        Metadata result = null;
        // whether to request automatic changes (update-fixed-info)
        boolean ufo = true;

        boolean updateDateStamp = !minor.equals("true");
        String changeDate = null;
        if (embedded) {
            Element updatedMetada = applyChangesEmbedded(id, htChanges, version);
            if (updatedMetada != null) {
                result = metadataManager.update(
                        Integer.parseInt(id), updatedMetada, false, ufo, changeDate, updateDateStamp);
            }
        } else {
            Element updatedMetada = applyChanges(id, htChanges, version);
            if (updatedMetada != null) {
                result = metadataManager.update(
                        Integer.parseInt(id), updatedMetada, validate, ufo, changeDate, updateDateStamp);
            }
        }
        if (result == null) {
            throw new ConcurrentRecordUpdateException(id);
        }
    }

    private Element applyChanges(String id, Map<String, String> changes, String currVersion) throws Exception {
        Metadata metadataEntity = getMetadataEntity(id);
        Element md = Xml.loadString(
                metadataManager.findMetadataById(Integer.parseInt(id)).getData(), false);

        // --- check if the metadata has been deleted
        if (md == null) {
            return null;
        }

        String schema = metadataEntity.getSchemaid();
        editLib.expandElements(schema, md);
        editLib.enumerateTree(md);

        // --- check if the metadata has been modified from last time
        if (currVersion != null && !editLib.getVersion(id).equals(currVersion)) {
            return null;
        }

        // --- update elements
        for (Map.Entry<String, String> entry : changes.entrySet()) {
            String ref = entry.getKey().trim();
            String val = entry.getValue().trim();
            String attr = null;

            if (updatedLocalizedTextElement(md, schema, ref, val, editLib)) {
                continue;
            }

            int at = ref.indexOf('_');
            if (at != -1) {
                attr = ref.substring(at + 1);
                ref = ref.substring(0, at);
            }
            boolean xmlContent = false;
            if (ref.startsWith("X")) {
                ref = ref.substring(1);
                xmlContent = true;
            }
            Element el = editLib.findElement(md, ref);
            if (el == null) throw new IllegalStateException("Element not found at ref = " + ref);

            if (attr != null) {
                // The following work-around decodes any attribute name that has a COLON in it
                // The : is replaced by the word COLON in the xslt so that it can be processed
                // by the XML Serializer when an update is submitted - a better solution is
                // to modify the argument handler in Jeeves to store arguments with their name
                // as a value rather than as the element itself
                int indexColon = attr.indexOf("COLON");
                if (indexColon != -1) {
                    String prefix = attr.substring(0, indexColon);
                    String localname = attr.substring(indexColon + 5);
                    String namespace = editLib.getNamespace(
                            prefix + ":" + localname,
                            md,
                            schemaManager
                                    .getSchema(metadataEntity.getSchemaid())
                                    .getXSDSchemaDefinition());
                    Namespace attrNS = Namespace.getNamespace(prefix, namespace);
                    if (el.getAttribute(localname, attrNS) != null) {
                        el.setAttribute(new Attribute(localname, val, attrNS));
                    }
                    // End of work-around
                } else {
                    if (el.getAttribute(attr) != null) el.setAttribute(new Attribute(attr, val));
                }
            } else if (xmlContent) {
                log.debug("replacing XML content");
                el.removeContent();
                val = EditLib.addGmlNamespaceToFragment(val);
                el.addContent(Xml.loadString(val, false));
            } else {
                @SuppressWarnings("unchecked")
                List<Content> content = el.getContent();

                content.removeIf(Text.class::isInstance);
                el.addContent(val);
            }
        }
        // --- remove editing info added by previous call
        editLib.removeEditingInfo(md);

        editLib.contractElements(md);
        return md;
    }

    /**
     * Adds a localised character string to an element for an ISO19139 record.
     *
     * <pre>
     * <gmd:title xsi:type="gmd:PT_FreeText_PropertyType">
     *    <gco:CharacterString>Template for Vector data in ISO19139 (multilingual)</gco:CharacterString>
     *    <gmd:PT_FreeText>
     *        <gmd:textGroup>
     *            <gmd:LocalisedCharacterString locale="#FRE">Modèle de données vectorielles en
     * ISO19139 (multilingue)</gmd:LocalisedCharacterString>
     *        </gmd:textGroup>
     * </pre>
     *
     * @param md metadata record
     * @param ref current ref of element. All _lang_AB_123 element will be processed.
     */
    protected boolean updatedLocalizedTextElement(Element md, String schema, String ref, String val, EditLib editLib) {
        if (ref.startsWith("lang")) {
            if (!val.isEmpty()) {

                SchemaPlugin schemaPlugin = SchemaManager.getSchemaPlugin(schema);
                if (schemaPlugin instanceof MultilingualSchemaPlugin msp) {
                    String[] ids = ref.split("_");
                    // --- search element in current parent
                    Element parent = editLib.findElement(md, ids[2]);
                    String language = ids[1];
                    List<Element> elems = msp.getTranslationForElement(parent, language);

                    // Element exists, set the value
                    if (elems != null && !elems.isEmpty()) {
                        elems.getFirst().setText(val);
                    } else {
                        ((MultilingualSchemaPlugin) schemaPlugin).addTranslationToElement(parent, language, val);
                        addMissingGeoNetRef(parent);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public void updateContent(Map<String, String> params) throws Exception {
        updateContent(params, false);
    }

    /** Used for editing : swaps 2 elements. */
    protected void swapElements(Element el1, Element el2) {

        Element parent = el1.getParentElement();
        if (parent == null) {
            throw new IllegalArgumentException("No parent element for swapping");
        }

        int index1 = parent.indexOf(el1);
        if (index1 == -1) {
            throw new IllegalArgumentException("Element 1 not found for swapping");
        }
        int index2 = parent.indexOf(el2);
        if (index2 == -1) {
            throw new IllegalArgumentException("Element 2 not found for swapping");
        }

        Element el1Spare = (Element) el1.clone();

        parent.setContent(index1, (Element) el2.clone());
        parent.setContent(index2, el1Spare);
    }

    /**
     * TODO: Move elsewhere probably In GN4, UserSession contains HttpSession and store properties in a map. Storing
     * directly in HttpSession for now.
     */
    public static HttpSession getHttpSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

    private Metadata getMetadataEntity(String id) {
        Optional<Metadata> metadataEntity = metadataRepository.findById(Integer.parseInt(id));
        if (metadataEntity.isEmpty()) {
            throw new IllegalStateException("Metadata not found for id = " + id);
        }
        // TODO: Check privileges or consider it is already checked?
        return metadataEntity.get();
    }

    private void setMetadataIntoSession(Element md, String id) {
        if (log.isDebugEnabled()) {
            log.debug("Storing metadata in session {}", getHttpSession().getId());
        }
        getHttpSession().setAttribute(Geonet.Session.METADATA_EDITING + id, md);
    }

    protected static Element getMetadataFromSession(String id) throws ResourceNotFoundException {
        if (log.isDebugEnabled()) {
            log.debug("Retrieving metadata from session {}", getHttpSession().getId());
        }
        Element md = (Element) getHttpSession().getAttribute(Geonet.Session.METADATA_EDITING + id);
        if (md == null) {
            throw new ResourceNotFoundException(String.format(
                    "Requested metadata with id '%s' is not available in current session. "
                            + "Open an editing session on this record first.",
                    id));
        }
        md.detach();
        return md;
    }

    /**
     * Apply a list of changes to the metadata record in current editing session.
     *
     * <p>The changes are a list of KVP. A key contains at least the element identifier from the meta-document. A key
     * starting with an "X" should contain an XML fragment for the value.
     *
     * <p>The following KVP combinations are allowed:
     *
     * <ul>
     *   <li>ElementId=ElementValue
     *   <li>ElementId_AttributeName=AttributeValue
     *   <li>ElementId_AttributeNamespacePrefixCOLONAttributeName=AttributeValue
     *   <li>XElementId=ElementValue
     *   <li>XElementId_replace=ElementValue
     *   <li>XElementId_ElementName=ElementValue
     *   <li>XElementId_ElementName_replace=ElementValue
     *   <li>P{key}=xpath with P{key}_xml=XML snippet
     * </ul>
     *
     * <p>ElementName MUST contain "{@value EditLib#COLON_SEPARATOR}" instead of ":" for prefixed elements.
     *
     * <p>When using X key ElementValue could contains many XML fragments (eg. &lt;gmd:keywords
     * .../&gt;{@value EditLib#XML_FRAGMENT_SEPARATOR}&lt;gmd:keywords .../&gt;) separated by
     * {@link EditLib#XML_FRAGMENT_SEPARATOR}. All those fragments are inserted to the last element of this type in its
     * parent if ElementName is set. If not, the element with ElementId is replaced. If _replace suffix is used, then
     * all elements having the same type than elementId are removed before insertion.
     *
     * <p>
     *
     * <pre>
     *  _Pd2295e223:/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/
     *              gmd:citation/gmd:CI_Citation/
     *              gmd:date[gmd:CI_Date/gmd:dateType/gmd:CI_DateTypeCode/@codeListValue =
     * 'creation']
     *
     *  _Pd2295e223_xml:&lt;gmd:date/&gt; ... &lt;/gmd:date&gt;
     * </pre>
     *
     * @param id Metadata internal identifier.
     * @param changes List of changes to apply.
     * @param currVersion Editing version which is checked against current editing version.
     * @return The update metadata record
     */
    protected Element applyChangesEmbedded(String id, Map<String, String> changes, String currVersion)
            throws Exception {
        Metadata metadataEntity = getMetadataEntity(id);
        String schema = metadataEntity.getSchemaid();
        SchemaPlugin schemaPlugin = schemaManager.getSchema(schema);

        // --- check if the metadata has been modified from last time
        if (currVersion != null && !editLib.getVersion(id).equals(currVersion)) {
            log.error("Version mismatch: had " + currVersion + " but expected " + editLib.getVersion(id));
            return null;
        }

        // --- get metadata from session
        Element md = getMetadataFromSession(id);

        // Store XML fragments to be handled after other elements update
        Map<String, String> xmlInputs = new HashMap<>();
        LinkedHashMap<String, AddElemValue> xmlAndXpathInputs = new LinkedHashMap<>();

        // Preprocess
        for (Map.Entry<String, String> entry : changes.entrySet()) {
            String originalRef = entry.getKey().trim();
            String ref = null;
            String value = entry.getValue().trim();
            String originalAttributeName = null;
            String parsedAttributeName = null;

            // Avoid empty key
            if (originalRef.isEmpty()) {
                continue;
            }

            // No pre processes for ref starting
            // with "P" (for XPath mode)
            // or "X" (for XML mode)
            // or "lang_" (for multilingual fields)
            // Updates for these refs are handled in next step
            if (originalRef.startsWith("X") || originalRef.startsWith("P") || originalRef.startsWith("lang_")) {
                continue;
            }

            if (refIsAttribute(originalRef)) {
                originalAttributeName = parseRefAndGetAttribute(originalRef);
                ref = parseRefAndGetNewRef(originalRef);
                Pair<Namespace, String> attributePair =
                        parseAttributeName(originalAttributeName, EditLib.COLON_SEPARATOR, id, md, editLib);
                parsedAttributeName = attributePair.one().getPrefix() + ":" + attributePair.two();
            } else {
                continue;
            }

            String actualRef = ref != null ? ref : originalRef;
            Element el = editLib.findElement(md, actualRef);
            if (el == null) {
                log.error(EditLib.MSG_ELEMENT_NOT_FOUND_AT_REF + originalRef);
                continue;
            }
            schemaPlugin.processElement(el, originalRef, parsedAttributeName, value);
        }

        // --- update elements
        for (Map.Entry<String, String> entry : changes.entrySet()) {
            String ref = entry.getKey().trim();
            String value = entry.getValue().trim();
            String attribute = null;

            // Avoid empty key
            if (ref.isEmpty()) {
                continue;
            }

            // Catch element starting with a X to replace XML fragments
            if (ref.startsWith("X")) {
                ref = ref.substring(1);
                xmlInputs.put(ref, value);
            } else if (ref.startsWith("P") && ref.endsWith("_xml")) {
                // P{key}=xpath works with P{key}_xml=XML snippet, see next condition
            } else if (ref.startsWith("P") && !ref.endsWith("_xml")) {
                // Catch element starting with a P for xpath update mode
                String snippet = changes.get(ref + "_xml");

                if (log.isDebugEnabled()) {
                    log.debug("Add element by XPath: " + value);
                    log.debug("  Snippet is : " + snippet);
                }

                if (snippet != null && !snippet.isEmpty()) {
                    xmlAndXpathInputs.put(value, new AddElemValue(snippet));
                } else {
                    log.warn("No XML snippet or value found for xpath " + value + " and element ref " + ref);
                }
            } else if (ref.startsWith("lang_")) {
                updatedLocalizedTextElement(md, schema, ref, value, editLib);
            } else {
                int at = ref.indexOf('_');
                if (at != -1) {
                    attribute = ref.substring(at + 1);
                    ref = ref.substring(0, at);
                }

                Element el = editLib.findElement(md, ref);
                if (el == null) {
                    log.error(EditLib.MSG_ELEMENT_NOT_FOUND_AT_REF + ref);
                    continue;
                }

                // Process attribute
                if (attribute != null) {
                    Pair<Namespace, String> attInfo =
                            parseAttributeName(attribute, EditLib.COLON_SEPARATOR, id, md, editLib);
                    String localname = attInfo.two();
                    Namespace attrNS = attInfo.one();
                    if (el.getAttribute(localname, attrNS) != null) {
                        el.setAttribute(new Attribute(localname, value, attrNS));
                    }
                } else {
                    // Process element value
                    @SuppressWarnings("unchecked")
                    List<Content> content = el.getContent();

                    for (Iterator<Content> iterator = content.iterator(); iterator.hasNext(); ) {
                        Content content2 = iterator.next();
                        if (content2 instanceof Text) {
                            iterator.remove();
                        }
                    }
                    el.addContent(value);
                }
            }
        }

        // Deals with XML fragments to insert or update
        if (!xmlInputs.isEmpty()) {
            editLib.addXMLFragments(schema, md, xmlInputs);
        }

        // Deals with XML fragments and XPath to insert or update
        if (!xmlAndXpathInputs.isEmpty()) {
            editLib.addElementOrFragmentFromXpaths(md, xmlAndXpathInputs, schemaPlugin, true);
        }

        setMetadataIntoSession((Element) md.clone(), id);

        // --- remove editing info
        editLib.removeEditingInfo(md);
        editLib.contractElements(md);

        return (Element) md.detach();
    }

    /**
     * Reads a ref and extract the ID part from it.
     *
     * @param ref the ref to check.
     * @return the ID part.
     */
    private String parseRefAndGetNewRef(String ref) {
        String newRef = ref;
        int underscorePosition = ref.indexOf('_');
        if (underscorePosition != -1) {
            newRef = ref.substring(0, underscorePosition);
        }
        return newRef;
    }

    /**
     * Reads a ref and extract the attribute part from it.
     *
     * @param ref the ref to check.
     * @return the attribute part or null if ref doesn't contain an attribute name.
     */
    private String parseRefAndGetAttribute(String ref) {
        String attribute = null;
        int underscorePosition = ref.indexOf('_');
        if (underscorePosition != -1) {
            attribute = ref.substring(underscorePosition + 1);
        }
        return attribute;
    }

    /**
     * Checks if a ref name represents an attribute. This kind of ref is like <code>ID_ATTRIBUTENAME</code>.
     *
     * @param ref a ref element.
     * @return true ref is an attribute, false in other case.
     */
    private boolean refIsAttribute(String ref) {
        return ref.indexOf('_') != -1;
    }

    /** For Ajax Editing : removes metadata from session. */
    public void removeMetadataEmbedded(String id) {
        log.debug("Removing metadata from session " + getHttpSession().getId());
        getHttpSession().removeAttribute(Geonet.Session.METADATA_EDITING + id);
        getHttpSession().removeAttribute(Geonet.Session.VALIDATION_REPORT + id);
        getHttpSession().removeAttribute(Geonet.Session.METADATA_BEFORE_ANY_CHANGES + id);

        editLib.clearVersion(id);
    }

    /** For Ajax Editing : gets Metadata from database and places it in session. */
    public Element getMetadataEmbedded(String id, boolean withValidationErrors) throws Exception {
        boolean keepXlinkAttributes = false;
        Element md = metadataManager.getMetadataDocumentForEditing(id, withValidationErrors, keepXlinkAttributes);
        setMetadataIntoSession(md, id);
        return md;
    }

    /** For Ajax Editing : adds an element or an attribute to a metadata element ([add] link). */
    public synchronized Element addElementEmbedded(String id, String ref, String name, String childName)
            throws Exception {
        Metadata metadataEntity = getMetadataEntity(id);
        String schema = metadataEntity.getSchemaid();

        Element md = getMetadataFromSession(id);

        // --- ref is parent element so find it
        Element el = editLib.findElement(md, ref);
        if (el == null) {
            throw new IllegalStateException(EditLib.MSG_ELEMENT_NOT_FOUND_AT_REF + ref);
        }

        // --- locate the geonet:element and geonet:info elements and clone for
        // --- later re-use
        Element info = null;

        if (md.getChild(Edit.RootChild.INFO, Edit.NAMESPACE) != null) {
            info = (Element) (md.getChild(Edit.RootChild.INFO, Edit.NAMESPACE)).clone();
            md.removeChild(Edit.RootChild.INFO, Edit.NAMESPACE);
        }

        Element child = null;
        SchemaPlugin schemaPlugin = schemaManager.getSchema(schema);
        if (childName != null) {
            if (childName.equals("geonet:attribute")) {
                String defaultValue = "";
                @SuppressWarnings("unchecked")
                List<Element> attributeDefs = el.getChildren(Edit.RootChild.ATTRIBUTE, Edit.NAMESPACE);
                for (Element attributeDef : attributeDefs) {
                    if (attributeDef != null
                            && attributeDef
                                    .getAttributeValue(Edit.Attribute.Attr.NAME)
                                    .equals(name)) {
                        Element defaultChild = attributeDef.getChild(Edit.Attribute.Child.DEFAULT, Edit.NAMESPACE);
                        if (defaultChild != null) {
                            defaultValue = defaultChild.getAttributeValue(Edit.Attribute.Attr.VALUE);
                        }
                        attributeDef.removeAttribute(Edit.Attribute.Attr.ADD);
                        attributeDef.setAttribute(new Attribute(Edit.Attribute.Attr.DEL, "true"));
                    }
                }

                Pair<Namespace, String> attInfo = parseAttributeName(name, ":", id, md, editLib);
                // --- Add new attribute with default value
                el.setAttribute(new Attribute(attInfo.two(), defaultValue, attInfo.one()));

                child = el;
            } else {
                // --- normal element
                child = editLib.addElement(schemaPlugin, el, name);
                if (!childName.isEmpty()) {
                    // --- or element
                    String uChildName = editLib.getUnqualifiedName(childName);
                    String prefix = editLib.getPrefix(childName);
                    String ns = editLib.getNamespace(childName, md, schemaPlugin.getXSDSchemaDefinition());
                    if (prefix.isEmpty()) {
                        prefix = editLib.getPrefix(el.getName());
                        ns = editLib.getNamespace(el.getName(), md, schemaPlugin.getXSDSchemaDefinition());
                    }
                    Element orChild = new Element(uChildName, prefix, ns);
                    child.addContent(orChild);

                    // --- add mandatory sub-tags
                    editLib.fillElement(schema, child, orChild);
                }
            }
        } else {
            child = editLib.addElement(schemaPlugin, el, name);
        }
        // --- now enumerate the new child (if not a simple attribute)
        if (childName == null || !childName.equals("geonet:attribute")) {
            int iRef = editLib.findMaximumRef(md);
            editLib.enumerateTreeStartingAt(child, iRef + 1, Integer.parseInt(ref));
            editLib.expandTree(schemaPlugin, child);
        }
        if (info != null) {
            // --- remove and re-attach the info element to the child
            child.removeChild(Edit.RootChild.INFO, Edit.NAMESPACE);
            child.addContent(info);
        }

        /* When adding an gmx:Anchor to an element, due to the following code gets also a gco:CharacterString in EditLib.

           Remove the gco:CharacterString subelement in this case.

          } else if (isISOPlugin &&
            type.getElementList().contains(
                isoPlugin.getBasicTypeCharacterStringName()) &&
            !hasSuggestion) {
            // expand element which have no suggestion
            // and have a gco:CharacterString substitute.
            // gco:CharacterString is the default.
            if (Log.isDebugEnabled(Geonet.EDITORFILLELEMENT)) {
                Log.debug(Geonet.EDITORFILLELEMENT, "####   - Requested expansion of an OR element having gco:CharacterString substitute and no suggestion: " + element.getName());
            }
            Element child = isoPlugin.createBasicTypeCharacterString();
            element.addContent(child);
        */
        if (childName != null && childName.equals("gmx:Anchor")) {
            if (child.getChild("CharacterString", ISO19139Namespaces.GCO) != null) {
                child.removeChild("CharacterString", ISO19139Namespaces.GCO);
            }
        }

        if (info != null) {
            // --- attach the info element to the metadata root)
            md.addContent((Element) info.clone());
        }

        setMetadataIntoSession((Element) md.clone(), id);

        return child;
    }

    /** For Ajax Editing : removes an element from a metadata ([del] link). */
    public synchronized Element deleteElementEmbedded(String id, String ref, String parentRef) throws Exception {
        Metadata metadataEntity = getMetadataEntity(id);
        String schema = metadataEntity.getSchemaid();

        // --- get metadata from session
        Element md = getMetadataFromSession(id);

        // --- locate the geonet:info element and clone for later re-use
        Element info = (Element) (md.getChild(Edit.RootChild.INFO, Edit.NAMESPACE)).clone();
        md.removeChild(Edit.RootChild.INFO, Edit.NAMESPACE);

        // --- get element to remove
        Element el = editLib.findElement(md, ref);

        if (el == null) {
            throw new IllegalStateException(EditLib.MSG_ELEMENT_NOT_FOUND_AT_REF + ref);
        }

        String uName = el.getName();
        Namespace ns = el.getNamespace();
        Element parent = el.getParentElement();
        Element result = null;
        if (parent != null) {
            int me = parent.indexOf(el);

            // --- check and see whether the element to be deleted is the last one of its kind
            Filter elFilter = new ElementFilter(uName, ns);
            if (parent.getContent(elFilter).size() == 1) {

                // --- get geonet child element with attribute name = unqualified name
                Filter chFilter = new ElementFilter(Edit.RootChild.CHILD, Edit.NAMESPACE);
                @SuppressWarnings("unchecked")
                List<Element> children = parent.getContent(chFilter);

                for (Element ch : children) {
                    String name = ch.getAttributeValue("name");
                    if (name != null && name.equals(uName)) {
                        result = (Element) ch.clone();
                        break;
                    }
                }

                // -- now delete the element as requested
                parent.removeContent(me);

                // --- existing geonet child element not present so create it and insert it
                // --- where the last element was deleted
                if (result == null) {
                    result = editLib.createElement(schema, el, parent);
                    parent.addContent(me, result);
                }

                result.setAttribute(Edit.ChildElem.Attr.PARENT, parentRef);
                result.addContent(info);
            }
            // --- if not the last one then just delete it
            else {
                parent.removeContent(me);
            }
        } else {
            throw new IllegalStateException("Element at ref = " + ref + " doesn't have a parent");
        }

        // if we don't need a child then create a geonet:null element
        if (result == null) {
            result = new Element(Edit.RootChild.NULL, Edit.NAMESPACE);
            result.addContent(info);
        }

        // --- reattach the info element to the metadata
        md.addContent((Element) info.clone());

        // --- store the metadata in the session again
        setMetadataIntoSession((Element) md.clone(), id);

        return result;
    }

    /**
     * Removes attribute in embedded mode.
     *
     * @param ref Attribute identifier (eg. _169_uom).
     */
    public synchronized Element deleteAttributeEmbedded(String id, String ref) throws Exception {

        String[] token = ref.split("_");
        String elementId = token[1];
        String attributeName = token[2];
        Element result = new Element(Edit.RootChild.NULL, Edit.NAMESPACE);

        // --- get metadata from session
        Element md = getMetadataFromSession(id);

        // --- get element to remove
        Element el = editLib.findElement(md, elementId);

        if (el != null) {
            Pair<Namespace, String> attInfo = parseAttributeName(attributeName, ":", id, md, editLib);
            el.removeAttribute(attInfo.two(), attInfo.one());
        }

        // --- store the metadata in the session again
        setMetadataIntoSession((Element) md.clone(), id);

        return result;
    }

    private Pair<Namespace, String> parseAttributeName(
            String attributeName, String separator, String id, Element md, EditLib editLib) throws Exception {
        Metadata metadataEntity = getMetadataEntity(id);
        Integer indexColon = attributeName.indexOf(separator);
        String localname = attributeName;
        Namespace attrNS = Namespace.NO_NAMESPACE;
        // ... with qualified name
        if (indexColon != -1) {
            String prefix = attributeName.substring(0, indexColon);
            localname = attributeName.substring(indexColon + separator.length());
            String schema = metadataEntity.getSchemaid();
            String namespace = editLib.getNamespace(
                    prefix + ":" + localname,
                    md,
                    schemaManager.getSchema(schema).getXSDSchemaDefinition());
            attrNS = Namespace.getNamespace(prefix, namespace);
        }
        return Pair.write(attrNS, localname);
    }

    /** For Ajax Editing : swap element with sibling ([up] and [down] links). */
    public synchronized void swapElementEmbedded(String id, String ref, boolean down) throws Exception {
        // --- get metadata from session
        Element md = getMetadataFromSession(id);

        // --- get element to swap
        Element elSwap = editLib.findElement(md, ref);

        if (elSwap == null) {
            throw new IllegalStateException(EditLib.MSG_ELEMENT_NOT_FOUND_AT_REF + ref);
        }

        // --- swap the elements
        int iSwapIndex = -1;

        @SuppressWarnings("unchecked")
        List<Element> list = elSwap.getParentElement().getChildren(elSwap.getName(), elSwap.getNamespace());

        int i = -1;
        for (Element element : list) {
            i++;
            if (element == elSwap) {
                iSwapIndex = i;
                break;
            }
        }

        if (iSwapIndex == -1) throw new IllegalStateException("Index not found for element --> " + elSwap);

        if (down) swapElements(elSwap, list.get(iSwapIndex + 1));
        else swapElements(elSwap, list.get(iSwapIndex - 1));

        // --- store the metadata in the session again
        setMetadataIntoSession((Element) md.clone(), id);
    }

    /** For Ajax Editing : retrieves metadata from session and validates it. */
    public Element validateMetadataEmbedded(String id, String lang) throws Exception {
        Metadata metadataEntity = getMetadataEntity(id);
        String schema = metadataEntity.getSchemaid();

        // --- get metadata from session and clone it for validation
        Element realMd = getMetadataFromSession(id);
        Element md = (Element) realMd.clone();

        // --- remove editing info
        editLib.removeEditingInfo(md);
        editLib.contractElements(md);
        md = metadataManager.updateFixedInfo(schema, Optional.of(Integer.valueOf(id)), null, md, false);
        // TODO?    Processor.processXLink(md, this.context);

        // --- do the validation on the metadata
        // TODO    return metadataValidator.doValidate(session, schema, id, md, lang, false).one();
        return null;
    }

    /** For Editing : adds an attribute from a metadata ([add] link). FIXME: Modify and use within Ajax controls */
    public synchronized boolean addAttribute(String id, String ref, String name, String currVersion) throws Exception {
        Element md = Xml.loadString(
                metadataManager.findMetadataById(Integer.parseInt(id)).getData(), false);

        // --- check if the metadata has been deleted
        if (md == null) return false;

        Metadata metadataEntity = getMetadataEntity(id);
        String schema = metadataEntity.getSchemaid();
        editLib.expandElements(schema, md);
        editLib.enumerateTree(md);

        // --- check if the metadata has been modified from last time
        if (currVersion != null && !editLib.getVersion(id).equals(currVersion)) return false;

        // --- get element to add
        Element el = editLib.findElement(md, ref);

        if (el == null) {
            log.error(EditLib.MSG_ELEMENT_NOT_FOUND_AT_REF + ref);
        }

        // --- remove editing info added by previous call
        editLib.removeEditingInfo(md);

        if (el != null) {
            el.setAttribute(new Attribute(name, ""));
        }

        editLib.contractElements(md);
        md = metadataManager.updateFixedInfo(schema, Optional.of(Integer.valueOf(id)), null, md, false);
        String changeDate = null;
        metadataManager.update(Integer.parseInt(id), md, false, false, changeDate, false);
        // TODO dataManager.indexMetadata(id, true);

        return true;
    }

    /** For Editing : removes an attribute from a metadata ([del] link). FIXME: Modify and use within Ajax controls */
    public synchronized boolean deleteAttribute(String id, String ref, String name, String currVersion)
            throws Exception {
        Element md = Xml.loadString(
                metadataManager.findMetadataById(Integer.parseInt(id)).getData(), false);
        Metadata metadataEntity = getMetadataEntity(id);
        // --- check if the metadata has been deleted
        if (md == null) {
            return false;
        }

        String schema = metadataEntity.getSchemaid();
        editLib.expandElements(schema, md);
        editLib.enumerateTree(md);

        // --- check if the metadata has been modified from last time
        if (currVersion != null && !editLib.getVersion(id).equals(currVersion)) return false;

        // --- get element to remove
        Element el = editLib.findElement(md, ref);

        if (el == null) {
            throw new IllegalStateException(EditLib.MSG_ELEMENT_NOT_FOUND_AT_REF + ref);
        }

        // --- remove editing info added by previous call
        editLib.removeEditingInfo(md);

        el.removeAttribute(name);

        editLib.contractElements(md);
        md = metadataManager.updateFixedInfo(schema, Optional.of(Integer.valueOf(id)), null, md, false);

        String changeDate = null;
        metadataManager.update(Integer.parseInt(id), md, false, false, changeDate, false);

        //   TODO dataManager.indexMetadata(id, true);

        return true;
    }
}
