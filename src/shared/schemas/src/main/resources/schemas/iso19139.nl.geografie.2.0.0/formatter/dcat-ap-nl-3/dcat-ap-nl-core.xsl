<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
                xmlns:mcc="http://standards.iso.org/iso/19115/-3/mcc/1.0"
                xmlns:gco="http://standards.iso.org/iso/19115/-3/gco/1.0"
                xmlns:mrl="http://standards.iso.org/iso/19115/-3/mrl/2.0"
                xmlns:mri="http://standards.iso.org/iso/19115/-3/mri/1.0"
                xmlns:cit="http://standards.iso.org/iso/19115/-3/cit/2.0"
                xmlns:mco="http://standards.iso.org/iso/19115/-3/mco/1.0"
                xmlns:lan="http://standards.iso.org/iso/19115/-3/lan/1.0"
                xmlns:mrs="http://standards.iso.org/iso/19115/-3/mrs/1.0"
                xmlns:gcx="http://standards.iso.org/iso/19115/-3/gcx/1.0"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:dct="http://purl.org/dc/terms/"
                xmlns:util="https://geonetwork-opensource.org/xsl-extension"
                exclude-result-prefixes="#all">

  <xsl:import href="./dcat-ap-nl-utils.xsl" />
  <xsl:import href="../../../iso19115-3.2018/formatter/eu-geodcat-ap/eu-geodcat-ap-core.xsl"/>
  <xsl:import href="../../../iso19115-3.2018/formatter/eu-dcat-ap-hvd/eu-dcat-ap-hvd-core.xsl"/>
  <xsl:import href="./dcat-ap-nl-core-dataservice.xsl"/>
  <xsl:import href="./dcat-ap-nl-core-access-and-use.xsl"/>
  <xsl:import href="./dcat-ap-nl-core-lineage.xsl"/>
  <xsl:import href="./dcat-ap-nl-core-distribution.xsl"/>
  <xsl:import href="./dcat-ap-nl-core-contact.xsl"/>
  <xsl:import href="./dcat-ap-nl-core-dataset.xsl"/>
  <xsl:import href="./dcat-ap-nl-core-resource.xsl"/>

  <!-- Check http://publications.europa.eu/resource/dataset/high-value-dataset-category values also -->
  <xsl:variable name="hvdCategoryThesaurusKey"
                select="('http://data.europa.eu/bna/asd487ae75', 'http://publications.europa.eu/resource/dataset/high-value-dataset-category')"/>

  <!-- Update mapping to creation: dct:modified instead dct:issued -->
  <xsl:variable name="isoDateTypeToDcatCommonNames"
                as="node()*">
    <entry key="dct:modified">creation</entry>
    <entry key="dct:issued">publication</entry>
    <entry key="dct:modified">revision</entry>
  </xsl:variable>

  <xsl:variable name="isoContactRoleToDcatCommonNames"
                as="node()*">
    <entry key="dct:creator" as="foaf">originator</entry>
    <entry key="dct:creator" as="foaf">author</entry>
    <!-- Add this? -->
    <!--<entry key="dct:creator" as="foaf">originator</entry>-->
    <entry key="dct:publisher" as="foaf">publisher</entry>
    <entry key="dcat:contactPoint" as="vcard">pointOfContact</entry>
    <!-- Add this? -->
    <!--<entry key="dcat:contactPoint" as="vcard">owner</entry>-->
    <!--<entry key="dct:rightsHolder" as="foaf">owner</entry>--> <!-- TODO: Check if dcat or only in profile -->
    <!-- Others are prov:qualifiedAttribution -->
  </xsl:variable>

  <xsl:variable name="isMappingResourceConstraintsToEuVocabulary"
                as="xs:boolean"
                select="true()"/>

  <xsl:variable name="languageMap"
                as="node()*">
    <entry key="dut">nld</entry>
  </xsl:variable>

  <!--
  Definition:	A language of the resource. This refers to the natural language used for textual metadata (i.e., titles, descriptions, etc.) of a cataloged resource (i.e., dataset or service) or the textual values of a dataset distribution

  Range:
  dcterms:LinguisticSystem
  Resources defined by the Library of Congress (ISO 639-1, ISO 639-2) SHOULD be used.

  If a ISO 639-1 (two-letter) code is defined for language, then its corresponding IRI SHOULD be used; if no ISO 639-1 code is defined, then IRI corresponding to the ISO 639-2 (three-letter) code SHOULD be used.

  Usage note:	Repeat this property if the resource is available in multiple languages.
  -->
  <!-- Map DUT to NLD -->
  <xsl:template mode="iso19115-3-to-dcat"
                match="mri:defaultLocale
                      |mri:otherLocale
                      |mdb:defaultLocale
                      |mdb:otherLocale">


    <xsl:variable name="languageValue"
                  as="xs:string?"
                  select="if ($languageMap[@key = current()/*/lan:language/*/@codeListValue])
                          then $languageMap[@key = current()/*/lan:language/*/@codeListValue]
                          else */lan:language/*/@codeListValue"/>

    <dct:language>
      <dct:LinguisticSystem rdf:about="{concat($europaPublicationLanguage, upper-case($languageValue))}"/>
    </dct:language>
  </xsl:template>


  <!-- Process all dataset dates and keep the first one -->
  <xsl:template mode="iso19115-3-to-dcat"
                match="cit:CI_Citation/cit:date[*/cit:date]">


    <!-- Process all dates when processing the first date -->
    <xsl:if test="count(preceding-sibling::cit:date) = 0">
      <xsl:variable name="issuedDateTypes" select="$isoDateTypeToDcatCommonNames[@key='dct:issued']" />

      <!-- issued dates -->
      <xsl:variable name="issuedDates">
        <xsl:for-each select="../cit:date/*/cit:date">
          <xsl:sort select="." order="descending" />

          <xsl:variable name="dateType"
                        as="xs:string?"
                        select="../cit:dateType/*/@codeListValue"/>
          <xsl:variable name="dcatElementName"
                        as="xs:string?"
                        select="$issuedDateTypes[. = $dateType]/@key"/>
          <xsl:if test="string($dcatElementName)">
            <xsl:call-template name="rdf-date">
              <xsl:with-param name="nodeName" select="$dcatElementName"/>
            </xsl:call-template>
          </xsl:if>
        </xsl:for-each>
      </xsl:variable>

      <xsl:copy-of select="$issuedDates/*[1]"/>

      <!-- modified dates -->
      <xsl:variable name="modifiedDateTypes" select="$isoDateTypeToDcatCommonNames[@key='dct:modified']" />

      <xsl:variable name="modifiedDates">
        <xsl:for-each select="../cit:date/*/cit:date">
          <xsl:sort select="." order="descending" />
          <xsl:variable name="dateType"
                        as="xs:string?"
                        select="../cit:dateType/*/@codeListValue"/>
          <xsl:variable name="dcatElementName"
                        as="xs:string?"
                        select="$modifiedDateTypes[. = $dateType]/@key"/>
          <xsl:if test="string($dcatElementName)">
            <xsl:call-template name="rdf-date">
              <xsl:with-param name="nodeName" select="$dcatElementName"/>
            </xsl:call-template>
          </xsl:if>
        </xsl:for-each>
      </xsl:variable>

      <xsl:copy-of select="$modifiedDates/*[1]"/>
    </xsl:if>

    <!--<xsl:choose>
      <xsl:when test="string($dcatElementName)">
        <xsl:call-template name="rdf-date">
          <xsl:with-param name="nodeName" select="$dcatElementName"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:message>WARNING: Unmatched date type <xsl:value-of select="$dateType"/>. If needed, add this type in dcat-variables.xsl and add the element to map to in isoDateTypeToDcatCommonNames.</xsl:message>
      </xsl:otherwise>
    </xsl:choose>-->
  </xsl:template>

  <!-- CatalogRecord template -->
  <xsl:template mode="iso19115-3-to-dcat-catalog-record"
                name="iso19115-3-to-dcat-ap-nl-catalog-record"
                match="mdb:MD_Metadata">
    <xsl:param name="additionalProperties"
               as="node()*"/>
    <xsl:variable name="properties" as="node()*">
      <xsl:variable name="resourcePrefix"
                    select="concat(util:getServerURL(), 'dut/catalog.search#/metadata/')" />

      <xsl:variable name="metadataIdentifier"
                    as="node()?">
        <xsl:apply-templates mode="iso19115-3-to-dcat"
                             select="mdb:metadataIdentifier"/>
      </xsl:variable>

      <dct:source rdf:resource="{concat($resourcePrefix, $metadataIdentifier)}"/>

      <xsl:copy-of select="$additionalProperties"/>
    </xsl:variable>
    <xsl:call-template name="iso19115-3-to-eu-dcat-ap-catalog-record">
      <xsl:with-param name="additionalProperties" select="$properties"/>
    </xsl:call-template>
  </xsl:template>

  <!-- Map spatialRepresentationType to dct:type -->
  <xsl:template match="mri:spatialRepresentationType/*/@codeListValue"
                mode="iso19115-3-to-dcat">
    <xsl:if test="current() != ''">
      <dct:type>
        <skos:Concept rdf:about="{concat($isoCodeListBaseUri, current())}">
          <skos:prefLabel><xsl:value-of select="current()"/></skos:prefLabel>
        </skos:Concept>
      </dct:type>
    </xsl:if>
  </xsl:template>

  <!-- Initial template to create a dcat:Resource: dcat:Dataset, dcat:DataService, dcat:DataSeries -->
  <xsl:template mode="iso19115-3-to-dcat-resource"
                name="iso19115-3-to-dcat-resource"
                match="mdb:MD_Metadata"
                priority="5">
    <xsl:call-template name="iso19115-3-to-dcat-ap-nl-resource"/>

    <xsl:apply-templates mode="iso19115-3-to-dcat"
                         select="mdb:referenceSystemInfo/*/mrs:referenceSystemIdentifier/*"/>

    <xsl:variable name="isSeriesMetadata" select="mdb:metadataScope/mdb:MD_MetadataScope/mdb:resourceScope/mcc:MD_ScopeCode/@codeListValue = 'series'" />

    <xsl:if test="not($isSeriesMetadata)">
      <xsl:apply-templates mode="iso19115-3-to-dcat"
                           select="mdb:identificationInfo/mri:MD_DataIdentification/mri:spatialRepresentationType/*/@codeListValue"/>
      
      <xsl:call-template name="rdf-eu-dcat-ap-nl-theme"/>
    </xsl:if>
  </xsl:template>

  <!-- Map reference system info to dct:conformsTo -->
  <xsl:template mode="iso19115-3-to-dcat"
                match="mdb:referenceSystemInfo/*/mrs:referenceSystemIdentifier/*">
    <xsl:variable name="code" select="mcc:code/(gco:CharacterString|gcx:Anchor)/text()"/>
    <xsl:variable name="link" select="mcc:code/gcx:Anchor/@xlink:href"/>

    <xsl:variable name="uri"
                  select="($link, $code[matches(., '^https?://')])[1]"/>
    <xsl:if test="$uri != ''">
      <dct:conformsTo>
        <dct:Standard rdf:about="{$uri}" />
      </dct:conformsTo>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
