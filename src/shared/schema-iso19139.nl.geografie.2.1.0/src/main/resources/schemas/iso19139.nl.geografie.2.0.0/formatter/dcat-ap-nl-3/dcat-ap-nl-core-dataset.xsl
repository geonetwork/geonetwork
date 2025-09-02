<?xml version="1.0" encoding="UTF-8"?>
<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet version="3.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
                xmlns:mcc="http://standards.iso.org/iso/19115/-3/mcc/1.0"
                xmlns:gco="http://standards.iso.org/iso/19115/-3/gco/1.0"
                xmlns:mri="http://standards.iso.org/iso/19115/-3/mri/1.0"
                xmlns:cit="http://standards.iso.org/iso/19115/-3/cit/2.0"
                xmlns:gcx="http://standards.iso.org/iso/19115/-3/gcx/1.0"
                xmlns:mco="http://standards.iso.org/iso/19115/-3/mco/1.0"
                xmlns:mmi="http://standards.iso.org/iso/19115/-3/mmi/1.0"
                xmlns:mrl="http://standards.iso.org/iso/19115/-3/mrl/2.0"
                xmlns:gex="http://standards.iso.org/iso/19115/-3/gex/1.0"
                xmlns:srv="http://standards.iso.org/iso/19115/-3/srv/2.0"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                xmlns:dcat="http://www.w3.org/ns/dcat#"
                xmlns:adms="http://www.w3.org/ns/adms#"
                xmlns:dcatap="http://data.europa.eu/r5r/"
                xmlns:dct="http://purl.org/dc/terms/"
                exclude-result-prefixes="#all">

  <!-- Used for metadata that does not have ISO topic categories (for example, service metadata) and does not have also INSPIRE GEMET Themes keywords -->
  <xsl:variable name="fallbackDcatApThemes" as="node()*">
    <!-- <entry key="http://publications.europa.eu/resource/authority/data-theme/GOVE" /> -->
  </xsl:variable>

  <!--
Theme mapping
https://joinup.ec.europa.eu/collection/semantic-interoperability-community-semic/solution/dcat-application-profile-implementation-guidelines/discussion/di1-tools-dcat-ap
https://github.com/SEMICeu/iso-19139-to-dcat-ap/blob/master/alignments/iso-topic-categories-to-inspire-themes.rdf
-->
  <xsl:variable name="isoTopicToEuDcatApThemes"
                as="node()*">
    <entry key="http://publications.europa.eu/resource/authority/data-theme/AGRI">
      <inspire>http://inspire.ec.europa.eu/theme/af</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/af</inspire>
      <iso>farming</iso>
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/ECON">
      <inspire>http://inspire.ec.europa.eu/theme/cp</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/cp</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/lu</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/lu</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/mr</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/mr</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/pf</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/pf</inspire>
      <iso>economy</iso>
      <iso>planningCadastre</iso>
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/EDUC"></entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/ENER">
      <inspire>http://inspire.ec.europa.eu/theme/er</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/er</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/mr</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/mr</inspire>
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/ENVI">
      <inspire>http://inspire.ec.europa.eu/theme/hy</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/hy</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/ps</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/ps</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/lc</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/lc</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/am</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/am</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/ac</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/ac</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/br</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/br</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/ef</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/ef</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/hb</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/hb</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/lu</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/lu</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/mr</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/mr</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/nz</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/nz</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/of</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/of</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/sr</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/sr</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/so</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/so</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/sd</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/sd</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/mf</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/mf</inspire>
      <iso>biota</iso>
      <iso>environment</iso>
      <iso>inlandWaters</iso>
      <iso>oceans</iso>
      <iso>climatologyMeteorologyAtmosphere</iso>
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/GOVE">
      <inspire>http://inspire.ec.europa.eu/theme/au</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/au</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/us</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/us</inspire>
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/HEAL">
      <inspire>http://inspire.ec.europa.eu/theme/hh</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/hh</inspire>
      <iso>health</iso>
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/INTR"></entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/JUST"></entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/OP_DATPRO"></entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/REGI">
      <inspire>http://inspire.ec.europa.eu/theme/ad</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/ad</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/rs</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/rs</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/gg</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/gg</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/cp</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/cp</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/gn</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/gn</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/el</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/el</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/ge</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/ge</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/oi</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/oi</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/bu</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/bu</inspire>
      <iso>planningCadastre</iso>
      <iso>boundaries</iso>
      <iso>elevation</iso>
      <iso>imageryBaseMapsEarthCover</iso>
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/SOCI">
      <inspire>http://inspire.ec.europa.eu/theme/pd</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/pd</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/su</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/su</inspire>
      <iso>location</iso>
      <iso>society</iso>
      <iso>disaster</iso>
      <iso>intelligenceMilitary</iso>
      <iso>extraTerrestrial</iso>
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/TECH">
      <inspire>http://inspire.ec.europa.eu/theme/hy</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/hy</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/ge</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/ge</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/oi</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/oi</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/mf</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/mf</inspire>
      <iso>geoscientificInformation</iso>
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/TRAN">
      <inspire>http://inspire.ec.europa.eu/theme/tn</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/tn</inspire>
      <iso>structure</iso>
      <iso>transportation</iso>
      <iso>utilitiesCommunication</iso>
    </entry>
  </xsl:variable>

  <!--## TODO?
  [o]	documentation	Document	0..*	A page or document about this Dataset.
  [o]	sample	Distribution	0..*	A sample distribution of the dataset.
  [o]	was generated by	Activity	0..*	An activity that generated, or provides the business context for, the creation of the dataset.
  -->
  <xsl:template mode="iso19115-3-to-dcat-resource"
                match="mdb:MD_Metadata"
                priority="50">
    <xsl:call-template name="iso19115-3-to-dcat-ap-nl-resource"/>

    <xsl:variable name="isSeriesMetadata" select="mdb:metadataScope/mdb:MD_MetadataScope/mdb:resourceScope/mcc:MD_ScopeCode/@codeListValue = 'series'" />

    <xsl:if test="not($isSeriesMetadata)">
      <xsl:call-template name="rdf-eu-dcat-ap-nl-theme"/>
    </xsl:if>
  </xsl:template>


  <!--
  [o]	theme	Concept	0..*	A category of the Dataset.	A Dataset may be associated with multiple themes.	https://w3c.github.io/dxwg/dcat/#Property:resource_theme
  Vocabulary http://publications.europa.eu/resource/authority/data-theme
  -->
  <xsl:template name="rdf-eu-dcat-ap-nl-theme">
    <xsl:variable name="values"
                  as="node()*"
                  select="mdb:identificationInfo/*/mri:topicCategory
                         |mdb:identificationInfo/*/mri:descriptiveKeywords/*/mri:keyword"/>

    <xsl:variable name="theme"
                  select="distinct-values($isoTopicToEuDcatApThemes[
                              */text() = $values/*/text()
                              or */text() = $values/*/@xlink:href]/@key)"/>
    <xsl:for-each select="$theme">
      <dcat:theme>
        <xsl:choose>
          <xsl:when test="$isExpandSkosConcept">
            <xsl:variable name="themeDescription"
                          select="$euDataTheme//skos:Concept[@rdf:about = current()]"/>
            <skos:Concept>
              <xsl:copy-of select="$themeDescription/@rdf:about"/>
              <xsl:copy-of select="$themeDescription/skos:prefLabel[@xml:lang = $languages/@iso2code]"
                           copy-namespaces="no"/>
            </skos:Concept>
          </xsl:when>
          <xsl:otherwise>
            <xsl:attribute name="rdf:resource" select="current()"/>
          </xsl:otherwise>
        </xsl:choose>
      </dcat:theme>
    </xsl:for-each>


    <!-- Add harcoded theme if not present -->
    <xsl:if test="count($theme) = 0 and count($fallbackDcatApThemes) > 0">
      <xsl:for-each select="$fallbackDcatApThemes">
        <xsl:variable name="entryKey" select="@key"/>
        <dcat:theme>
          <xsl:choose>
            <xsl:when test="$isExpandSkosConcept">
              <xsl:variable name="themeDescription"
                            select="$euDataTheme//skos:Concept[@rdf:about = $entryKey]"/>
              <skos:Concept>
                <xsl:copy-of select="$themeDescription/@rdf:about"/>
                <xsl:copy-of select="$themeDescription/skos:prefLabel[@xml:lang = $languages/@iso2code]"
                             copy-namespaces="no"/>
              </skos:Concept>
            </xsl:when>
            <xsl:otherwise>
              <xsl:attribute name="rdf:resource" select="$entryKey"/>
            </xsl:otherwise>
          </xsl:choose>
        </dcat:theme>
      </xsl:for-each>
    </xsl:if>
  </xsl:template>

  <!--
RDF Property:	dcterms:spatial
Definition:	The geographical area covered by the dataset.
Range:	dcterms:Location (A spatial region or named place)
Usage note:	The spatial coverage of a dataset may be encoded as an instance of dcterms:Location,
or may be indicated using an IRI reference (link) to a resource describing a location. It is recommended that links are to entries in a well maintained gazetteer such as Geonames.
-->
  <xsl:template mode="iso19115-3-to-dcat"
                match="mri:extent/*/gex:geographicElement/gex:EX_GeographicBoundingBox">
    <xsl:variable name="isServiceMetadata" select="exists(//mdb:MD_Metadata/mdb:identificationInfo/srv:SV_ServiceIdentification)" />

    <xsl:if test="not($isServiceMetadata)">
      <xsl:variable name="north" select="gex:northBoundLatitude/gco:Decimal"/>
      <xsl:variable name="east" select="gex:eastBoundLongitude/gco:Decimal"/>
      <xsl:variable name="south" select="gex:southBoundLatitude/gco:Decimal"/>
      <xsl:variable name="west" select="gex:westBoundLongitude/gco:Decimal"/>

      <xsl:variable name="geojson"
                    as="xs:string"
                    select="concat('{&quot;type&quot;:&quot;Polygon&quot;,&quot;coordinates&quot;:[[[',
                                     $west, ',', $north, '],[',
                                     $east, ',', $north, '],[',
                                     $east, ',', $south, '],[',
                                     $west, ',', $south, '],[',
                                     $west, ',', $north, ']]]}')"/>

      <dct:spatial>
        <rdf:Description>
          <rdf:type rdf:resource="http://purl.org/dc/terms/Location"/>
          <dcat:bbox rdf:datatype="http://www.opengis.net/ont/geosparql#geoJSONLiteral">
            <xsl:value-of select="$geojson"/>
          </dcat:bbox>
        </rdf:Description>
      </dct:spatial>
    </xsl:if>
  </xsl:template>


  <xsl:template mode="iso19115-3-to-dcat"
                match="mri:extent/*/gex:geographicElement/gex:EX_GeographicDescription">
    <xsl:variable name="isServiceMetadata" select="exists(//mdb:MD_Metadata/mdb:identificationInfo/srv:SV_ServiceIdentification)" />

    <xsl:if test="not($isServiceMetadata)">
      <xsl:for-each select="gex:geographicIdentifier/*">
        <xsl:variable name="uri"
                      as="xs:string?"
                      select="mcc:code/*/@xlink:href"/>
        <xsl:choose>
          <xsl:when test="string($uri)">
            <dct:spatial>
              <dct:Location rdf:about="{$uri}"/>
            </dct:spatial>
          </xsl:when>
          <xsl:otherwise>
            <xsl:for-each select="mcc:code">
              <dct:spatial>
                <rdf:Description>
                  <rdf:type rdf:resource="http://purl.org/dc/terms/Location"/>
                  <xsl:call-template name="rdf-localised">
                    <xsl:with-param name="nodeName" select="'skos:prefLabel'"/>
                  </xsl:call-template>
                </rdf:Description>
              </dct:spatial>
            </xsl:for-each>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
