/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.iso19115_3;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.schemas.MetadataSchemaConfiguration;
import org.geonetwork.schemas.SchemaPlugin;
import org.geonetwork.schemas.plugin.AssociatedResource;
import org.geonetwork.schemas.plugin.AssociatedResourcesSchemaPlugin;
import org.geonetwork.schemas.plugin.ExportablePlugin;
import org.geonetwork.schemas.plugin.ISOPlugin;
import org.geonetwork.schemas.plugin.MultilingualSchemaPlugin;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import org.jdom.xpath.XPath;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ISO19115_3SchemaPlugin extends SchemaPlugin
        implements AssociatedResourcesSchemaPlugin, MultilingualSchemaPlugin, ExportablePlugin, ISOPlugin {
    //    LinkAwareSchemaPlugin {
    public static final String IDENTIFIER = "iso19115-3";
    private static final Pattern RECORD_ID_PATTERN = Pattern.compile(".*[i|I][d|D]=([_\\w\\-\\.\\{{\\}}]*).*");
    public static Set<Namespace> allNamespaces;
    private static Map<String, Namespace> allTypenames;
    private static Map<String, String> allExportFormats;

    private String parentAssociatedResourceType = null;

    static {
        allNamespaces = Set.of(
                ISO19115_3Namespaces.GCO,
                ISO19115_3Namespaces.MDB,
                ISO19115_3Namespaces.GEX,
                ISO19115_3Namespaces.MRC,
                ISO19115_3Namespaces.MRL,
                ISO19115_3Namespaces.LAN,
                ISO19115_3Namespaces.MRI,
                ISO19115_3Namespaces.CIT,
                ISO19115_3Namespaces.SRV,
                ISO19115_3Namespaces.XLINK);

        allTypenames = Map.of(
                "csw:Record", Namespace.getNamespace("csw", "http://www.opengis.net/cat/csw/2.0.2"),
                "mdb:MD_Metadata", Namespace.getNamespace("mdb", "http://standards.iso.org/iso/19115/-3/mdb/2.0"),
                "gmd:MD_Metadata", Namespace.getNamespace("gmd", "http://www.isotc211.org/2005/gmd"));

        allExportFormats = Map.of("convert/ISO19139/toISO19139.xsl", "metadata-iso19139.xml");
    }

    private MetadataSchemaConfiguration configuration;

    public ISO19115_3SchemaPlugin(ISO19115_3Configuration configuration) {
        super(IDENTIFIER, allNamespaces);
        this.setConfiguration(configuration);
    }

    @Override
    public String getSchemasResourcePath() {
        return "/schemas/iso19115-3.2018";
    }

    @Override
    public Set<AssociatedResource> getAssociatedResourcesUUIDs(Element metadata) {
        String xpathForAggregationInfo = "*//mri:associatedResource/*" + "[mri:metadataReference/@uuidref " + "and %s]";
        Set<AssociatedResource> listOfResources = new HashSet<>();
        List<?> sibs = null;
        try {
            sibs = Xml.selectNodes(
                    metadata,
                    String.format(
                            xpathForAggregationInfo,
                            StringUtils.isNotEmpty(parentAssociatedResourceType)
                                    ? String.format(
                                            "mri:associationType/*/@codeListValue != '%s'",
                                            parentAssociatedResourceType)
                                    : "mri:associationType/mri:DS_AssociationTypeCode/@codeListValue != ''"),
                    new ArrayList<>(allNamespaces));

            for (Object o : sibs) {
                if (o instanceof Element sib) {
                    AssociatedResource resource = metadataRefAsAssociatedResource(sib);
                    listOfResources.add(resource);
                }
            }
        } catch (JDOMException e) {
            log.debug("getAssociatedResourcesUUIDs error {}", e.getMessage());
        }
        return listOfResources;
    }

    @Override
    public Set<String> getAssociatedParentUUIDs(Element metadata) {
        return getAssociatedParents(metadata).stream()
                .map(AssociatedResource::getUuid)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<AssociatedResource> getAssociatedParents(Element metadata) {
        Set<AssociatedResource> associatedResources = collectAssociatedResources(metadata, "mdb:parentMetadata");

        if (StringUtils.isNotEmpty(parentAssociatedResourceType)) {
            associatedResources.forEach(parent -> parent.setAssociationType(parentAssociatedResourceType));

            try {
                String XPATH_FOR_PARENT_IN_AGGRGATIONINFO =
                        "*//mri:associatedResource/*" + "[mri:associationType/*/@codeListValue = '%s']";
                final List<?> associatedParents = Xml.selectNodes(
                        metadata,
                        String.format(XPATH_FOR_PARENT_IN_AGGRGATIONINFO, parentAssociatedResourceType),
                        new ArrayList<>(allNamespaces));
                for (Object o : associatedParents) {
                    Element sib = (Element) o;
                    AssociatedResource resource = metadataRefAsAssociatedResource(sib);
                    associatedResources.add(resource);
                }
            } catch (JDOMException e) {
                // Ignore exception
            }
        }
        return associatedResources;
    }

    @Override
    public Set<String> getAssociatedDatasetUUIDs(Element metadata) {
        return getAssociatedDatasets(metadata).stream()
                .map(AssociatedResource::getUuid)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<AssociatedResource> getAssociatedDatasets(Element metadata) {
        return collectAssociatedResources(metadata, "*//srv:operatesOn", true);
    }

    @Override
    public Set<String> getAssociatedFeatureCatalogueUUIDs(Element metadata) {
        // Feature catalog may also be embedded into the document
        // Or the citation of the feature catalog may contains a reference to it
        return getAssociatedFeatureCatalogues(metadata).stream()
                .map(AssociatedResource::getUuid)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<AssociatedResource> getAssociatedFeatureCatalogues(Element metadata) {
        return collectAssociatedResources(metadata, "*//mrc:featureCatalogueCitation[@uuidref]");
    }

    @Override
    public Set<String> getAssociatedSourceUUIDs(Element metadata) {
        return getAssociatedSources(metadata).stream()
                .map(AssociatedResource::getUuid)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<AssociatedResource> getAssociatedSources(Element metadata) {
        return collectAssociatedResources(metadata, "*//mrl:source");
    }

    private Set<AssociatedResource> collectAssociatedResources(Element metadata, String xpath) {
        return collectAssociatedResources(metadata, xpath, false);
    }

    private Set<AssociatedResource> collectAssociatedResources(
            Element metadata, String xpath, boolean checkUuidInHref) {
        Set<AssociatedResource> associatedResources = new HashSet<>();
        try {
            final List<?> parentMetadata = Xml.selectNodes(metadata, xpath, new ArrayList<>(allNamespaces));
            for (Object o : parentMetadata) {
                Element sib = (Element) o;
                AssociatedResource resource = elementAsAssociatedResource(sib, checkUuidInHref);
                associatedResources.add(resource);
            }
        } catch (JDOMException e) {
            log.debug("collectAssociatedResources error {}", e.getMessage());
        }
        return associatedResources;
    }

    private AssociatedResource elementAsAssociatedResource(Element ref, boolean checkUuidInHref) {
        String title = ref.getAttributeValue("title", ISO19115_3Namespaces.XLINK, "");
        String url = ref.getAttributeValue("href", ISO19115_3Namespaces.XLINK, "");

        String sibUuid = ref.getAttributeValue("uuidref");
        if (StringUtils.isEmpty(sibUuid)) {
            sibUuid = ref.getTextNormalize();
        }

        // For srv:operatesOn, check if the xlink:href has a url with a parameter named 'id'
        // (aligned with the index.xsl process), as that element usually contains the reference to
        // the
        // metadata.
        // GeoNetwork uses uuidref to store this reference, but that's just a GeoNetwork convention.
        if (checkUuidInHref) {
            Matcher matcher = RECORD_ID_PATTERN.matcher(url);

            if (matcher.matches()) {
                sibUuid = matcher.group(1);
            }
        }

        return new AssociatedResource(sibUuid, "", "", url, title);
    }

    private AssociatedResource metadataRefAsAssociatedResource(Element sibling) {
        Element ref = sibling.getChild("metadataReference", ISO19115_3Namespaces.MRI);
        String sibUuid = ref.getAttributeValue("uuidref");

        String associationType = sibling.getChild("associationType", ISO19115_3Namespaces.MRI)
                .getChild("DS_AssociationTypeCode", ISO19115_3Namespaces.MRI)
                .getAttributeValue("codeListValue");

        String initType = "";
        final Element initiativeTypeEl = sibling.getChild("initiativeType", ISO19115_3Namespaces.MRI);
        if (initiativeTypeEl != null) {
            initType = initiativeTypeEl
                    .getChild("DS_InitiativeTypeCode", ISO19115_3Namespaces.MRI)
                    .getAttributeValue("codeListValue");
        }

        String title = ref.getAttributeValue("title", ISO19115_3Namespaces.XLINK);
        String url = ref.getAttributeValue("href", ISO19115_3Namespaces.XLINK);
        return new AssociatedResource(sibUuid, initType, associationType, url, title);
    }

    @Override
    public List<Element> getTranslationForElement(Element element, String languageIdentifier) {
        final String path = ".//lan:LocalisedCharacterString" + "[@locale='#" + languageIdentifier + "']";
        try {
            XPath xpath = XPath.newInstance(path);
            @SuppressWarnings("unchecked")
            List<Element> matches = xpath.selectNodes(element);
            return matches;
        } catch (Exception e) {
            log.debug(getIdentifier()
                    + ": getTranslationForElement failed "
                    + "on element "
                    + Xml.getString(element)
                    + " using XPath '"
                    + path
                    + " Exception: "
                    + e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * Add a LocalisedCharacterString to an element. In ISO19139, the translation are stored
     * gmd:PT_FreeText/gmd:textGroup/gmd:LocalisedCharacterString.
     *
     * <pre>
     * <cit:title xsi:type="lan:PT_FreeText_PropertyType">
     *    <gco:CharacterString>Template for Vector data</gco:CharacterString>
     *    <lan:PT_FreeText>
     *        <lan:textGroup>
     *            <lan:LocalisedCharacterString locale="#FRE">Modèle de données vectorielles en ISO19139 (multilingue)</lan:LocalisedCharacterString>
     *        </lan:textGroup>
     * </pre>
     */
    @Override
    public void addTranslationToElement(Element element, String languageIdentifier, String value) {
        element.setAttribute(
                "type",
                "lan:PT_FreeText_PropertyType",
                Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"));

        // Create a new translation for the language
        Element langElem = new Element("LocalisedCharacterString", ISO19115_3Namespaces.LAN);
        langElem.setAttribute("locale", "#" + languageIdentifier);
        langElem.setText(value);
        Element textGroupElement = new Element("textGroup", ISO19115_3Namespaces.LAN);
        textGroupElement.addContent(langElem);

        // Get the PT_FreeText node where to insert the translation into
        Element freeTextElement = element.getChild("PT_FreeText", ISO19115_3Namespaces.LAN);
        if (freeTextElement == null) {
            freeTextElement = new Element("PT_FreeText", ISO19115_3Namespaces.LAN);
            element.addContent(freeTextElement);
        }
        freeTextElement.addContent(textGroupElement);
    }

    /**
     * Remove all multingual aspect of an element. Keep the md language localized strings as default gco:CharacterString
     * for the element.
     *
     * @param langs Metadata languages. The main language MUST be the first one.
     */
    @Override
    public Element removeTranslationFromElement(Element element, List<String> langs) throws JDOMException {
        String mainLanguage = langs != null && !langs.isEmpty() ? langs.getFirst() : "#EN";

        @SuppressWarnings("unchecked")
        List<Element> nodesWithStrings = (List<Element>)
                Xml.selectNodes(element, "*//lan:PT_FreeText", Collections.singletonList(ISO19115_3Namespaces.LAN));

        for (Element e : nodesWithStrings) {
            // Retrieve or create the main language element
            Element mainCharacterString =
                    ((Element) e.getParent()).getChild("CharacterString", ISO19115_3Namespaces.GCO);
            if (mainCharacterString == null) {
                // create it if it does not exist
                mainCharacterString = new Element("CharacterString", ISO19115_3Namespaces.GCO);
                ((Element) e.getParent()).addContent(0, mainCharacterString);
            }

            // Retrieve the main language value if exist
            @SuppressWarnings("unchecked")
            List<Element> mainLangElement = (List<Element>) Xml.selectNodes(
                    e,
                    "*//lan:LocalisedCharacterString[@locale='" + mainLanguage + "']",
                    Collections.singletonList(ISO19115_3Namespaces.LAN));

            // Set the main language value
            if (mainLangElement.size() == 1) {
                String mainLangString = mainLangElement.getFirst().getText();

                if (StringUtils.isNotEmpty(mainLangString)) {
                    mainCharacterString.setText(mainLangString);
                } else if (mainCharacterString.getAttribute("nilReason", ISO19115_3Namespaces.GCO) == null) {
                    ((Element) mainCharacterString.getParent())
                            .setAttribute("nilReason", "missing", ISO19115_3Namespaces.GCO);
                }
            } else if (StringUtils.isEmpty(mainCharacterString.getText())) {
                ((Element) mainCharacterString.getParent())
                        .setAttribute("nilReason", "missing", ISO19115_3Namespaces.GCO);
            }
        }

        // Remove unused lang entries
        @SuppressWarnings("unchecked")
        List<Element> translationNodes = (List<Element>) Xml.selectNodes(element, "*//node()[@locale]");
        for (Element el : translationNodes) {
            // Remove all translations if there is no or only one language requested
            if (langs.size() <= 1 || !langs.contains(el.getAttribute("locale").getValue())) {
                Element parent = (Element) el.getParent();
                parent.detach();
            }
        }

        // Remove PT_FreeText which might be emptied by above processing
        for (Element el : nodesWithStrings) {
            if (el.getChildren().isEmpty()) {
                el.detach();
            }
        }

        return element;
    }

    @Override
    public String getBasicTypeCharacterStringName() {
        return "gco:CharacterString";
    }

    @Override
    public Element createBasicTypeCharacterString() {
        return new Element("CharacterString", ISO19115_3Namespaces.GCO);
    }

    @Override
    public Element addOperatesOn(
            Element serviceRecord, Map<String, String> layers, String serviceType, String baseUrl) {

        Element root = serviceRecord
                .getChild("identificationInfo", ISO19115_3Namespaces.MDB)
                .getChild("SV_ServiceIdentification", ISO19115_3Namespaces.SRV);

        if (root != null) {

            // Coupling type MUST be present as it is the insertion point
            // for coupledResource
            Element couplingType = root.getChild("couplingType", ISO19115_3Namespaces.SRV);
            int coupledResourceIdx = root.indexOf(couplingType);

            layers.keySet().forEach(uuid -> {
                String layerName = layers.get(uuid);

                // Create coupled resources elements to register all layername
                // in service metadata. This information could be used to add
                // interactive map button when viewing service metadata.
                Element coupledResource = new Element("coupledResource", ISO19115_3Namespaces.SRV);
                coupledResource.setAttribute("nilReason", "synchronizedFromOGC", ISO19115_3Namespaces.GCO);
                Element scr = new Element("SV_CoupledResource", ISO19115_3Namespaces.SRV);

                // Create operation according to service type
                Element operation = new Element("operation", ISO19115_3Namespaces.SRV);
                Element operationMetadata = new Element("SV_OperationMetadata", ISO19115_3Namespaces.SRV);
                Element operationName = new Element("operationName", ISO19115_3Namespaces.SRV);
                Element operationValue = new Element("CharacterString", ISO19115_3Namespaces.GCO);

                if (serviceType.startsWith("WMS")) operationValue.setText("GetMap");
                else if (serviceType.startsWith("WFS")) operationValue.setText("GetFeature");
                else if (serviceType.startsWith("WCS")) operationValue.setText("GetCoverage");
                else if (serviceType.startsWith("WPS")) operationValue.setText("DescribeProcess");
                else if (serviceType.startsWith("SOS")) operationValue.setText("GetObservation");
                operationName.addContent(operationValue);
                operationMetadata.addContent(operationName);
                operation.addContent(operationMetadata);

                // Create identifier (which is the metadata identifier)
                Element id = new Element("resourceReference", ISO19115_3Namespaces.SRV);
                id.setAttribute("uuidref", uuid);

                // Create scoped name element as defined in CSW 2.0.2 ISO profil
                // specification to link service metadata to a layer in a service.
                Element scopedName = new Element("scopedName", ISO19115_3Namespaces.SRV);
                Element scopedNameValue = new Element("ScopedName", ISO19115_3Namespaces.GCO);
                scopedNameValue.setText(layerName);
                scopedName.addContent(scopedNameValue);

                scr.addContent(scopedName);
                scr.addContent(id);
                // TODO: Add missing DCP here
                //                scr.addContent(operation);
                coupledResource.addContent(scr);

                // Add coupled resource before coupling type element
                if (coupledResourceIdx != -1) {
                    root.addContent(coupledResourceIdx, coupledResource);
                }

                // Add operatesOn element at the end of identification section.
                Element op = new Element("operatesOn", ISO19115_3Namespaces.SRV);
                op.setAttribute("nilReason", "synchronizedFromOGC", ISO19115_3Namespaces.GCO);
                op.setAttribute("uuidref", uuid);

                String hRefLink = baseUrl + "api/records/" + uuid + "/formatters/xml";
                op.setAttribute("href", hRefLink, ISO19115_3Namespaces.XLINK);

                root.addContent(op);
            });
        }

        return serviceRecord;
    }

    @Override
    public List<Extent> getExtents(Element metadataRecord) {
        List<Extent> extents = new ArrayList<>();

        ElementFilter bboxFinder = new ElementFilter("EX_GeographicBoundingBox", ISO19115_3Namespaces.GEX);
        @SuppressWarnings("unchecked")
        Iterator<Element> bboxes = metadataRecord.getDescendants(bboxFinder);
        while (bboxes.hasNext()) {
            Element box = bboxes.next();
            try {
                extents.add(new Extent(
                        Double.valueOf(box.getChild("westBoundLongitude", ISO19115_3Namespaces.GEX)
                                .getChild("Decimal", ISO19115_3Namespaces.GCO)
                                .getText()),
                        Double.valueOf(box.getChild("eastBoundLongitude", ISO19115_3Namespaces.GEX)
                                .getChild("Decimal", ISO19115_3Namespaces.GCO)
                                .getText()),
                        Double.valueOf(box.getChild("southBoundLatitude", ISO19115_3Namespaces.GEX)
                                .getChild("Decimal", ISO19115_3Namespaces.GCO)
                                .getText()),
                        Double.valueOf(box.getChild("northBoundLatitude", ISO19115_3Namespaces.GEX)
                                .getChild("Decimal", ISO19115_3Namespaces.GCO)
                                .getText())));
            } catch (NullPointerException e) {
                // Ignore exception
            }
        }
        return extents;
    }

    @Override
    public Map<String, Namespace> getCswTypeNames() {
        return allTypenames;
    }

    @Override
    public Map<String, String> getExportFormats() {
        return allExportFormats;
    }

    /**
     * Process some of the ISO elements which can have substitute.
     *
     * <p>For example, a CharacterString can have a gmx:Anchor as a substitute to encode a text value + an extra URL. To
     * make the transition between CharacterString and Anchor transparent, this method takes care of creating the
     * appropriate element depending on the presence of an xlink:href attribute. If the attribute is empty, then a
     * CharacterString is used, if a value is set, an Anchor is created.
     *
     * @param el element to process.
     * @param attributeRef the attribute reference
     * @param parsedAttributeName the name of the attribute, for example <code>xlink:href</code>
     * @param attributeValue the attribute value
     */
    @Override
    public Element processElement(Element el, String attributeRef, String parsedAttributeName, String attributeValue) {
        if (log.isDebugEnabled()) {
            log.debug(String.format(
                    "Processing element %s, attribute %s with attributeValue %s.", el, attributeRef, attributeValue));
        }

        boolean elementToProcess = isElementToProcess(el);

        if (elementToProcess && parsedAttributeName.equals("xlink:href")) {
            boolean isEmptyLink = StringUtils.isEmpty(attributeValue);
            boolean isMultilingualElement = el.getName().equals("LocalisedCharacterString");

            if (isMultilingualElement) {
                // The attribute provided relates to the CharacterString and not to the
                // LocalisedCharacterString
                Element targetElement = el.getParentElement()
                        .getParentElement()
                        .getParentElement()
                        .getChild("CharacterString", ISO19115_3Namespaces.GCO);
                if (targetElement != null) {
                    el = targetElement;
                }
            }

            if (isEmptyLink) {
                el.setNamespace(ISO19115_3Namespaces.GCO).setName("CharacterString");
                el.removeAttribute("href", ISO19115_3Namespaces.XLINK);
                return el;
            } else {
                el.setNamespace(ISO19115_3Namespaces.GCX).setName("Anchor");
                el.setAttribute("href", "", ISO19115_3Namespaces.XLINK);
                return el;
            }
        } else if (elementToProcess
                && StringUtils.isNotEmpty(parsedAttributeName)
                && parsedAttributeName.startsWith(":")) {
            // eg. :codeSpace
            el.setAttribute(parsedAttributeName.substring(1), attributeValue);
            return el;
        } else {
            return super.processElement(el, attributeRef, parsedAttributeName, attributeValue);
        }
    }

    /**
     * Checks if an element requires processing in {@link #processElement(Element, String, String, String)}.
     *
     * @param el Element to check.
     * @return boolean indicating if the element requires processing or not.
     */
    protected boolean isElementToProcess(Element el) {
        if (el == null) return false;

        return elementsToProcess.contains(el.getQualifiedName());
    }

    //  public <L, M> RawLinkPatternStreamer<L, M> createLinkStreamer(
    //      ILinkBuilder<L, M> linkbuilder, String excludePattern) {
    //    RawLinkPatternStreamer patternStreamer =
    //        new RawLinkPatternStreamer(linkbuilder, excludePattern);
    //    patternStreamer.setNamespaces(ISO19115_3SchemaPlugin.allNamespaces.asList());
    //    // TODO: Add xlink:href ?
    //    patternStreamer.setRawTextXPath(
    //        ".//*[name() = 'gco:CharacterString' or name() = 'lan:LocalisedCharacterString']");
    //    return patternStreamer;
    //  }

    /**
     * If not empty defined if parent metadata reference should also be searched in associated resources. Define the
     * value of associationType to use.
     */
    public String getParentAssociatedResourceType() {
        return parentAssociatedResourceType;
    }

    public void setParentAssociatedResourceType(String parentAssociatedResourceType) {
        this.parentAssociatedResourceType = parentAssociatedResourceType;
    }
}
