/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas.iso19139;

import static org.geonetwork.schemas.iso19139.ISO19139Namespaces.GCO;
import static org.geonetwork.schemas.iso19139.ISO19139Namespaces.GMD;
import static org.geonetwork.schemas.iso19139.ISO19139Namespaces.GMX;
import static org.geonetwork.schemas.iso19139.ISO19139Namespaces.SRV;
import static org.geonetwork.schemas.iso19139.ISO19139Namespaces.XLINK;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.schemas.SchemaPlugin;
import org.geonetwork.schemas.plugin.AssociatedResource;
import org.geonetwork.schemas.plugin.AssociatedResourcesSchemaPlugin;
import org.geonetwork.schemas.plugin.ExportablePlugin;
import org.geonetwork.schemas.plugin.ISOPlugin;
import org.geonetwork.schemas.plugin.MultilingualSchemaPlugin;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;
import org.jdom.xpath.XPath;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ISO19139SchemaPlugin extends SchemaPlugin
    implements AssociatedResourcesSchemaPlugin,
        MultilingualSchemaPlugin,
        ExportablePlugin,
        ISOPlugin
//        LinkAwareSchemaPlugin
{
  public static final String IDENTIFIER = "iso19139";

  private static final Pattern RECORD_ID_PATTERN =
      Pattern.compile(".*[i|I][d|D]=([_\\w\\-\\.\\{{\\}}]*).*");
  public static Set<Namespace> allNamespaces;
  private static Map<String, Namespace> allTypenames;
  private static Map<String, String> allExportFormats;

  static {
    allNamespaces = Set.of(GCO, GMD, SRV);
    allTypenames =
        Map.of(
            "csw:Record", Namespace.getNamespace("csw", "http://www.opengis.net/cat/csw/2.0.2"),
            "gmd:MD_Metadata", GMD,
            "dcat", Namespace.getNamespace("dcat", "http://www.w3.org/ns/dcat#"));

    allExportFormats =
        Map.of(
            // This is more for all basic iso19139 profiles using this bean as default
            // The conversion is not available in regular iso19139 plugin.
            // This is for backward compatibility.
            "convert/to19139.xsl", "metadata-iso19139.xml");
  }

  public ISO19139SchemaPlugin() {
    super(IDENTIFIER, allNamespaces);
  }

  /** Return sibling relation defined in aggregationInfo. */
  @Override
  public Set<AssociatedResource> getAssociatedResourcesUUIDs(Element metadata) {

    String xpathForAggregationInfo =
        "*//gmd:aggregationInfo/*"
            + "[gmd:aggregateDataSetIdentifier/*/gmd:code "
            + "and gmd:associationType/gmd:DS_AssociationTypeCode/@codeListValue!='']";
    Set<AssociatedResource> listOfResources = new HashSet<>();
    List<?> sibs = null;

    try {
      sibs = Xml.selectNodes(metadata, xpathForAggregationInfo, new ArrayList<>(allNamespaces));

      for (Object o : sibs) {
        try {
          if (o instanceof Element sib) {
            Element agId =
                (Element) sib.getChild("aggregateDataSetIdentifier", GMD).getChildren().get(0);
            List children = getChild(agId, "code", GMD).getChildren();
            String sibUuid = "";
            String title = "";
            String url = "";
            if (children.size() == 1) {
              Element charStringOrAnchor = ((Element) children.get(0));
              sibUuid = charStringOrAnchor.getText();
              title = charStringOrAnchor.getAttributeValue("title", XLINK);
              url = charStringOrAnchor.getAttributeValue("href", XLINK);
            }
            final Element associationTypeEl = getChild(sib, "associationType", GMD);
            String associationType =
                getChild(associationTypeEl, "DS_AssociationTypeCode", GMD)
                    .getAttributeValue("codeListValue");
            final Element initiativeTypeEl = getChild(sib, "initiativeType", GMD);
            String initiativeType = "";
            if (initiativeTypeEl != null) {
              initiativeType =
                  getChild(initiativeTypeEl, "DS_InitiativeTypeCode", GMD)
                      .getAttributeValue("codeListValue");
            }

            AssociatedResource resource =
                new AssociatedResource(sibUuid, initiativeType, associationType, url, title);
            listOfResources.add(resource);
          }
        } catch (Exception e) {
          log.error("Error getting resources UUIDs", e);
        }
      }
    } catch (Exception e) {
      log.error("Error getting resources UUIDs", e);
    }
    return listOfResources;
  }

  private Element getChild(Element el, String name, Namespace namespace) {
    final Element child = el.getChild(name, namespace);
    if (child == null) {
      return new Element(name, namespace);
    }
    return child;
  }

  @Override
  public Set<String> getAssociatedParentUUIDs(Element metadata) {
    return getAssociatedParents(metadata).stream()
        .map(AssociatedResource::getUuid)
        .collect(Collectors.toSet());
  }

  @Override
  public Set<AssociatedResource> getAssociatedParents(Element metadata) {
    Set<AssociatedResource> associatedResources = new HashSet<>();

    Element parentIdentifier = metadata.getChild("parentIdentifier", GMD);
    if (parentIdentifier != null) {
      Element characterString = parentIdentifier.getChild("CharacterString", GCO);
      if (characterString != null) {
        associatedResources.add(new AssociatedResource(characterString.getText(), "", ""));
      }
      Element anchor = parentIdentifier.getChild("Anchor", GMX);
      if (anchor != null) {
        associatedResources.add(elementAsAssociatedResource(anchor, false));
      }
    }
    // Parent relation is also frequently encoded using
    // aggregation. See parentAssociatedResourceType in ISO19115-3
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
    return collectAssociatedResources(metadata, "*//gmd:featureCatalogueCitation[@uuidref]");
  }

  @Override
  public Set<String> getAssociatedSourceUUIDs(Element metadata) {
    return getAssociatedSources(metadata).stream()
        .map(AssociatedResource::getUuid)
        .collect(Collectors.toSet());
  }

  @Override
  public Set<AssociatedResource> getAssociatedSources(Element metadata) {
    return collectAssociatedResources(metadata, "*//gmd:source");
  }

  private Set<AssociatedResource> collectAssociatedResources(Element metadata, String xpath) {
    return collectAssociatedResources(metadata, xpath, false);
  }

  private Set<AssociatedResource> collectAssociatedResources(
      Element metadata, String xpath, boolean checkUuidInHref) {
    Set<AssociatedResource> associatedResources = new HashSet<>();
    try {
      final List<?> parentMetadata =
          Xml.selectNodes(metadata, xpath, new ArrayList<>(allNamespaces));
      for (Object o : parentMetadata) {
        Element sib = (Element) o;
        AssociatedResource resource = elementAsAssociatedResource(sib, checkUuidInHref);
        associatedResources.add(resource);
      }
    } catch (JDOMException e) {
      log.debug("Error getting associated resources {}", e.getMessage());
    }
    return associatedResources;
  }

  private AssociatedResource elementAsAssociatedResource(Element ref, boolean checkUuidInHref) {
    String title = ref.getAttributeValue("title", XLINK, "");
    String url = ref.getAttributeValue("href", XLINK, "");

    String sibUuid = ref.getAttributeValue("uuidref");
    if (StringUtils.isEmpty(sibUuid)) {
      sibUuid = ref.getTextNormalize();
    }

    // For srv:operatesOn, check if the xlink:href has a url with a parameter named 'id'
    // (aligned with the index.xsl process), as that element usually contains the reference to the
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

  @Override
  public List<Element> getTranslationForElement(Element element, String languageIdentifier) {
    final String path =
        ".//gmd:LocalisedCharacterString" + "[@locale='#" + languageIdentifier + "']";
    try {
      XPath xpath = XPath.newInstance(path);
      @SuppressWarnings("unchecked")
      List<Element> matches = xpath.selectNodes(element);
      return matches;
    } catch (Exception e) {
      log.debug(
          getIdentifier()
              + ": getTranslationForElement failed "
              + "on element "
              + Xml.getString(element)
              + " using XPath '"
              + path
              + "updatedLocalizedTextElement exception "
              + e.getMessage());
    }
    return Collections.emptyList();
  }

  /**
   * Add a LocalisedCharacterString to an element. In ISO19139, the translation are stored
   * gmd:PT_FreeText/gmd:textGroup/gmd:LocalisedCharacterString.
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
   */
  @Override
  public void addTranslationToElement(Element element, String languageIdentifier, String value) {
    // An ISO19139 element containing translation has an xsi:type attribute
    element.setAttribute(
        "type",
        "gmd:PT_FreeText_PropertyType",
        Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"));

    // Create a new translation for the language
    Element langElem = new Element("LocalisedCharacterString", GMD);
    langElem.setAttribute("locale", "#" + languageIdentifier);
    langElem.setText(value);
    Element textGroupElement = new Element("textGroup", GMD);
    textGroupElement.addContent(langElem);

    // Get the PT_FreeText node where to insert the translation into
    Element freeTextElement = element.getChild("PT_FreeText", GMD);
    if (freeTextElement == null) {
      freeTextElement = new Element("PT_FreeText", GMD);
      element.addContent(freeTextElement);
    }
    freeTextElement.addContent(textGroupElement);
  }

  /**
   * Remove all multilingual aspect of an element. Keep the md language localized strings as default
   * gco:CharacterString for the element.
   *
   * @param langs Metadata languages. The main language MUST be the first one.
   */
  @Override
  public Element removeTranslationFromElement(Element element, List<String> langs)
      throws JDOMException {
    String mainLanguage = langs != null && !langs.isEmpty() ? langs.get(0) : "#EN";

    @SuppressWarnings("unchecked")
    List<Element> nodesWithStrings =
        (List<Element>) Xml.selectNodes(element, "*//gmd:PT_FreeText", Arrays.asList(GMD));

    for (Element e : nodesWithStrings) {
      // Retrieve or create the main language element
      Element mainCharacterString = ((Element) e.getParent()).getChild("CharacterString", GCO);
      if (mainCharacterString == null) {
        // create it if it does not exist
        mainCharacterString = new Element("CharacterString", GCO);
        ((Element) e.getParent()).addContent(0, mainCharacterString);
      }

      // Retrieve the main language value if exist
      @SuppressWarnings("unchecked")
      List<Element> mainLangElement =
          (List<Element>)
              Xml.selectNodes(
                  e,
                  "*//gmd:LocalisedCharacterString[@locale='" + mainLanguage + "']",
                  Arrays.asList(GMD));

      // Set the main language value
      if (mainLangElement.size() == 1) {
        String mainLangString = mainLangElement.get(0).getText();

        if (StringUtils.isNotEmpty(mainLangString)) {
          mainCharacterString.setText(mainLangString);
        } else if (mainCharacterString.getAttribute("nilReason", GCO) == null) {
          ((Element) mainCharacterString.getParent()).setAttribute("nilReason", "missing", GCO);
        }
      } else if (StringUtils.isEmpty(mainCharacterString.getText())) {
        ((Element) mainCharacterString.getParent()).setAttribute("nilReason", "missing", GCO);
      }
    }

    // Remove unused lang entries
    // eg. the directory entry contains more languages than requested.
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

    // Sort all children elements translation
    // according to the language list.
    // When a directory entry is added as an xlink, the URL
    // contains an ordered list of language and this ordre must
    // be preserved in order to display fields in the editor in the same
    // order as other element in the record.
    if (langs.size() > 1) {
      @SuppressWarnings("unchecked")
      List<Element> elementList =
          (List<Element>) Xml.selectNodes(element, ".//*[gmd:PT_FreeText]", Arrays.asList(GMD));
      for (Element el : elementList) {
        final Element ptFreeText = el.getChild("PT_FreeText", GMD);
        List<Element> orderedTextGroup = new ArrayList<>();
        for (String l : langs) {
          @SuppressWarnings("unchecked")
          List<Element> node =
              (List<Element>)
                  Xml.selectNodes(
                      ptFreeText, "gmd:textGroup[*/@locale='" + l + "']", Arrays.asList(GMD));
          if (node != null && node.size() == 1) {
            orderedTextGroup.add((Element) node.get(0).clone());
          }
        }
        ptFreeText.removeContent();
        ptFreeText.addContent(orderedTextGroup);
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
    return new Element("CharacterString", GCO);
  }

  @Override
  public Element addOperatesOn(
      Element serviceRecord, Map<String, String> layers, String serviceType, String baseUrl) {

    Element root =
        serviceRecord.getChild("identificationInfo", GMD).getChild("SV_ServiceIdentification", SRV);

    if (root != null) {

      // Coupling type MUST be present as it is the insertion point
      // for coupledResource
      Element couplingType = root.getChild("couplingType", SRV);
      int coupledResourceIdx = root.indexOf(couplingType);

      layers
          .keySet()
          .forEach(
              uuid -> {
                String layerName = layers.get(uuid);

                // Create coupled resources elements to register all layername
                // in service metadata. This information could be used to add
                // interactive map button when viewing service metadata.
                Element coupledResource = new Element("coupledResource", SRV);
                coupledResource.setAttribute("nilReason", "synchronizedFromOGC", GCO);
                Element scr = new Element("SV_CoupledResource", SRV);

                // Create operation according to service type
                Element operation = new Element("operationName", SRV);
                Element operationValue = new Element("CharacterString", GCO);

                if (serviceType.startsWith("WMS")) operationValue.setText("GetMap");
                else if (serviceType.startsWith("WFS")) operationValue.setText("GetFeature");
                else if (serviceType.startsWith("WCS")) operationValue.setText("GetCoverage");
                else if (serviceType.startsWith("SOS")) operationValue.setText("GetObservation");
                operation.addContent(operationValue);

                // Create identifier (which is the metadata identifier)
                Element id = new Element("identifier", SRV);
                Element idValue = new Element("CharacterString", GCO);
                idValue.setText(uuid);
                id.addContent(idValue);

                // Create scoped name element as defined in CSW 2.0.2 ISO profil
                // specification to link service metadata to a layer in a service.
                Element scopedName = new Element("ScopedName", GCO);
                scopedName.setText(layerName);

                scr.addContent(operation);
                scr.addContent(id);
                scr.addContent(scopedName);
                coupledResource.addContent(scr);

                // Add coupled resource before coupling type element
                if (coupledResourceIdx != -1) {
                  root.addContent(coupledResourceIdx, coupledResource);
                }

                // Add operatesOn element at the end of identification section.
                Element op = new Element("operatesOn", SRV);
                op.setAttribute("nilReason", "synchronizedFromOGC", GCO);
                op.setAttribute("uuidref", uuid);

                String hRefLink = baseUrl + "api/records/" + uuid + "/formatters/xml";
                op.setAttribute("href", hRefLink, XLINK);

                root.addContent(op);
              });
    }

    return serviceRecord;
  }

  @Override
  public List<Extent> getExtents(Element metadataRecord) {
    List<Extent> extents = new ArrayList<>();

    ElementFilter bboxFinder = new ElementFilter("EX_GeographicBoundingBox", GMD);
    @SuppressWarnings("unchecked")
    Iterator<Element> bboxes = metadataRecord.getDescendants(bboxFinder);
    while (bboxes.hasNext()) {
      Element box = bboxes.next();
      try {
        extents.add(
            new Extent(
                Double.valueOf(
                    box.getChild("westBoundLongitude", GMD).getChild("Decimal", GCO).getText()),
                Double.valueOf(
                    box.getChild("eastBoundLongitude", GMD).getChild("Decimal", GCO).getText()),
                Double.valueOf(
                    box.getChild("southBoundLatitude", GMD).getChild("Decimal", GCO).getText()),
                Double.valueOf(
                    box.getChild("northBoundLatitude", GMD).getChild("Decimal", GCO).getText())));
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
   * <p>For example, a CharacterString can have a gmx:Anchor as a substitute to encode a text value
   * + an extra URL. To make the transition between CharacterString and Anchor transparent, this
   * method takes care of creating the appropriate element depending on the presence of an
   * xlink:href attribute. If the attribute is empty, then a CharacterString is used, if a value is
   * set, an Anchor is created.
   *
   * @param el element to process.
   * @param attributeRef the attribute reference
   * @param parsedAttributeName the name of the attribute, for example <code>xlink:href</code>
   * @param attributeValue the attribute value
   */
  @Override
  public Element processElement(
      Element el, String attributeRef, String parsedAttributeName, String attributeValue) {
    if (log.isDebugEnabled()) {
      log.debug(
          String.format(
              "Processing element %s, attribute %s with attributeValue %s.",
              el, attributeRef, attributeValue));
    }

    boolean elementToProcess = isElementToProcess(el);

    if (elementToProcess && parsedAttributeName.equals("xlink:href")) {
      boolean isEmptyLink = StringUtils.isEmpty(attributeValue);
      boolean isMultilingualElement = el.getName().equals("LocalisedCharacterString");

      if (isMultilingualElement) {
        // The attribute provided relates to the CharacterString and not to the
        // LocalisedCharacterString
        Element targetElement =
            el.getParentElement()
                .getParentElement()
                .getParentElement()
                .getChild("CharacterString", GCO);
        if (targetElement != null) {
          el = targetElement;
        }
      }

      if (isEmptyLink) {
        el.setNamespace(GCO).setName("CharacterString");
        el.removeAttribute("href", XLINK);
        return el;
      } else {
        el.setNamespace(GMX).setName("Anchor");
        el.setAttribute("href", "", XLINK);
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
   * Checks if an element requires processing in {@link #processElement(Element, String, String,
   * String)}.
   *
   * @param el Element to check.
   * @return boolean indicating if the element requires processing or not.
   */
  protected boolean isElementToProcess(Element el) {
    if (el == null) return false;

    return elementsToProcess.contains(el.getQualifiedName());
  }

  /**
   * Return an ordered list of record languages. The main language is the first.
   *
   * <p>This may be used when substituting elements by their matching XLinks. In such case, the
   * XLink must contains an ordered list of language codes.
   *
   * @param md The record to analyze
   * @return An ordered list of ISO 3 letters codes
   */
  public static List<String> getLanguages(Element md) {
    List<String> languages = new ArrayList<>();
    try {
      String mainLanguage = getMainLanguage(md);

      if (!mainLanguage.isEmpty()) {
        languages.add(mainLanguage);
      }

      // Append all other locales as ordered in the locale section
      @SuppressWarnings("unchecked")
      List<Attribute> locales =
          (List<Attribute>)
              Xml.selectNodes(
                  md,
                  "gmd:locale/*/gmd:languageCode/*/@codeListValue",
                  new ArrayList<>(ISO19139SchemaPlugin.allNamespaces));
      if (locales != null) {
        locales.stream()
            .map(Attribute::getValue)
            .filter(Objects::nonNull)
            .filter(l -> !l.equalsIgnoreCase(mainLanguage))
            .forEach(languages::add);
      }
    } catch (JDOMException e) {
      // Ignore exception
    }
    return languages;
  }

  //  public <L, M> RawLinkPatternStreamer<L, M> createLinkStreamer(
  //      ILinkBuilder<L, M> linkbuilder, String excludePattern) {
  //    RawLinkPatternStreamer patternStreamer =
  //        new RawLinkPatternStreamer(linkbuilder, excludePattern);
  //    patternStreamer.setNamespaces(ISO19139SchemaPlugin.allNamespaces.asList());
  //    patternStreamer.setRawTextXPath(".//*[name() = 'gco:CharacterString' or name() =
  // 'gmd:URL']");
  //    return patternStreamer;
  //  }

  private static String getMainLanguage(Element md) throws JDOMException {
    Attribute mainLanguageAttribute =
        (Attribute)
            Xml.selectSingle(
                md,
                "gmd:language/*/@codeListValue",
                new ArrayList<>(ISO19139SchemaPlugin.allNamespaces));
    if (mainLanguageAttribute != null && StringUtils.isNotEmpty(mainLanguageAttribute.getValue())) {
      return mainLanguageAttribute.getValue();
    }

    Element mainLanguageElem =
        (Element)
            Xml.selectSingle(
                md, "gmd:language/*", new ArrayList<>(ISO19139SchemaPlugin.allNamespaces));
    if (mainLanguageElem != null && StringUtils.isNotEmpty(mainLanguageElem.getText())) {
      return mainLanguageElem.getText();
    }
    return "";
  }
}
