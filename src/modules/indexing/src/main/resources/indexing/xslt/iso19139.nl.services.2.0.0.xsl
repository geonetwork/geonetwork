<?xml version="1.0" encoding="UTF-8" ?>
<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gmx="http://www.isotc211.org/2005/gmx"
                xmlns:gmi="http://www.isotc211.org/2005/gmi"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:srv="http://www.isotc211.org/2005/srv"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:daobs="http://daobs.org"
                xmlns:saxon="http://saxon.sf.net/"
                extension-element-prefixes="saxon"
                exclude-result-prefixes="#all"
                version="2.0">

  <xsl:import href="iso19139.xsl" />

  <xsl:template match="*" mode="index-extra-fields">
    <!-- downloadable protocols -->
    <xsl:variable name="downloadableProtocols">
      <protocol value="OGC:WFS" />
      <protocol value="OGC:WCS" />
      <protocol value="OGC:SOS" />
      <protocol value="INSPIRE Atom" />
      <protocol value="OASIS:OData" />
      <protocol value="OGC:SensorThings" />
      <protocol value="W3C:SPARQL" />
      <protocol value="OAS" />
    </xsl:variable>

    <!-- media type downloadable protocols -->
    <!-- see https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-mediatypes -->
    <!-- All media types in the above link are downloadable except PNG, GIF, mapbox-vector-tile, they are links -->
    <xsl:variable name="downloadableMediaTypes">
      <downloadableMediaType value="gml" />
      <downloadableMediaType value="kml" />
      <downloadableMediaType value="geojson" />
      <downloadableMediaType value="gpkg" />
      <downloadableMediaType value="json" />
      <downloadableMediaType value="jsonld" />
      <downloadableMediaType value="rdf-xml" />
      <downloadableMediaType value="xml" />
      <downloadableMediaType value="zip" />
      <downloadableMediaType value="jp2" />
      <downloadableMediaType value="tiff" />
      <downloadableMediaType value="csv" />
    </xsl:variable>

    <xsl:variable name="protocolText">
      <xsl:for-each select="gmd:distributionInfo/gmd:MD_Distribution/gmd:transferOptions/gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource/gmd:protocol/*/text()">
        <xsl:value-of select="."/>
        <xsl:if test="position() != last()"><xsl:text>,</xsl:text></xsl:if>
      </xsl:for-each>
    </xsl:variable>

    <xsl:if test="gmd:hierarchyLevel/gmd:MD_ScopeCode/@codeListValue='dataset'
          and
            (not (
              contains($protocolText, 'WFS') or
              contains($protocolText, 'WCS') or
              contains($protocolText, 'INSPIRE Atom') or
              contains($protocolText, 'OGC:SensorThings') or
              contains($protocolText, 'OASIS:OData') or
              contains($protocolText, 'W3C:SPARQL') or
              contains($protocolText, 'OAS') or
              contains($protocolText, 'gml') or
              contains($protocolText, 'kml') or
              contains($protocolText, 'geojson') or
              contains($protocolText, 'x-sqlite3') or
              contains($protocolText, 'json') or
              contains($protocolText, 'json-ld') or
              contains($protocolText, 'rdf-xml') or
              contains($protocolText, 'xml') or
              contains($protocolText, 'zip') or
              contains($protocolText, 'jp2') or
              contains($protocolText, 'tiff') or
              contains($protocolText, 'csv')
              )
              and not (
              //srv:serviceType/gco:LocalName[contains(., 'WFS')] or
              //srv:serviceType/gco:LocalName[contains(., 'WCS')] or
              //srv:serviceType/gco:LocalName[contains(., 'INSPIRE Atom')] or
              //srv:serviceType/gco:LocalName[contains(., 'OGC:SensorThings')] or
              //srv:serviceType/gco:LocalName[contains(., 'OASIS:OData')] or
              //srv:serviceType/gco:LocalName[contains(., 'W3C:SPARQL')] or
              //srv:serviceType/gco:LocalName[contains(., 'OAS')] or
              //srv:serviceType/gco:LocalName[contains(., 'gml')] or
              //srv:serviceType/gco:LocalName[contains(., 'kml')] or
              //srv:serviceType/gco:LocalName[contains(., 'geojson')] or
              //srv:serviceType/gco:LocalName[contains(., 'x-sqlite3')] or
              //srv:serviceType/gco:LocalName[contains(., 'json')] or
              //srv:serviceType/gco:LocalName[contains(., 'json-ld')] or
              //srv:serviceType/gco:LocalName[contains(., 'rdf-xml')] or
              //srv:serviceType/gco:LocalName[contains(., 'xml')] or
              //srv:serviceType/gco:LocalName[contains(., 'zip')] or
              //srv:serviceType/gco:LocalName[contains(., 'jp2')] or
              //srv:serviceType/gco:LocalName[contains(., 'tiff')] or
              //srv:serviceType/gco:LocalName[contains(., 'csv')]
              )
            )">
      <nodynamicdownload>true</nodynamicdownload>
    </xsl:if>

    <xsl:variable name="isDownloadableService">
      <xsl:for-each select="srv:serviceType/gco:LocalName">
        <xsl:if test=". = 'download'">
          downloadable
        </xsl:if>
      </xsl:for-each>
    </xsl:variable>

    <xsl:variable name="isDownloadable">
      <xsl:for-each select="gmd:distributionInfo/gmd:MD_Distribution/gmd:transferOptions/gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource/gmd:protocol/*/text()">
        <xsl:if test="count($downloadableProtocols/downloadableProtocol[@value = .]) = 1 or
                      count($downloadableMediaTypes/downloadableMediaType[@value = .]) = 1">
          downloadable
        </xsl:if>
      </xsl:for-each>
    </xsl:variable>

    <xsl:variable name="isDynamicService">
      <xsl:for-each select="srv:serviceType/gco:LocalName">
        <xsl:if test=". = 'view'">
          dynamic
        </xsl:if>
      </xsl:for-each>
    </xsl:variable>

    <xsl:variable name="isDynamic">
      <xsl:for-each select="gmd:distributionInfo/gmd:MD_Distribution/gmd:transferOptions/gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource">

        <xsl:variable name="linkage" select="gmd:linkage/gmd:URL"/>
        <xsl:variable name="protocol" select="gmd:protocol/*/text()"/>

        <xsl:variable name="wmsLinkNoProtocol"
                      select="contains(lower-case($linkage), 'service=wms') and not(string($protocol))"/>
        <xsl:variable name="wmtsLinkNoProtocol"
                      select="contains(lower-case($linkage), 'service=wmts') and not(string($protocol))"/>

        <xsl:if
          test="contains(., 'OGC:WMS') or contains(., 'OGC:WMTS') or $wmsLinkNoProtocol or $wmtsLinkNoProtocol">
          dynamic
        </xsl:if>

      </xsl:for-each>
    </xsl:variable>

    <xsl:if test="string(normalize-space($isDownloadableService)) or string(normalize-space($isDownloadable))">
      <download>true</download>
    </xsl:if>

    <xsl:if test="string(normalize-space($isDynamicService)) or string(normalize-space($isDynamic))">
      <dynamic>true</dynamic>
    </xsl:if>

    <xsl:variable name="licenseMap">
      <!-- valid licenses from Codelist INSPIRE LimitationsOnPublicAccess -->
      <!-- https://docs.geostandaarden.nl/md/mdprofiel-iso19119/#Codelijst-INSPIRE-LimitationsOnPublicAccess -->
      <license value="http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1a">INSPIRE_Directive_Article13_1a</license>
      <license value="http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1b">INSPIRE_Directive_Article13_1b</license>
      <license value="http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1c">INSPIRE_Directive_Article13_1c</license>
      <license value="http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1d">INSPIRE_Directive_Article13_1d</license>
      <license value="http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1e">INSPIRE_Directive_Article13_1e</license>
      <license value="http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1f">INSPIRE_Directive_Article13_1f</license>
      <license value="http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1g">INSPIRE_Directive_Article13_1g</license>
      <license value="http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1h">INSPIRE_Directive_Article13_1h</license>
      <license value="http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/noLimitations">noLimitations</license>

      <!-- INSPIRE codelist Conditions Applying To Access and Use -->
      <license value="http://inspire.ec.europa.eu/metadata-codelist/ConditionsApplyingToAccessAndUse/noConditionsApply">noConditionsApply</license>
      <license value="http://inspire.ec.europa.eu/metadata-codelist/ConditionsApplyingToAccessAndUse/conditionsUnknown">conditionsUnknown</license>

      <!-- http://creativecommons.org -->
      <license value="http://creativecommons.org/publicdomain/mark/">Public Domain</license>
      <license value="http://creativecommons.org/publicdomain/zero/">CC0</license>
      <license value="http://creativecommons.org/licenses/by/">CC BY</license>
      <license value="http://creativecommons.org/licenses/by-sa/">CC BY-SA</license>
      <license value="http://creativecommons.org/licenses/by-nc/">CC BY-NC</license>
      <license value="http://creativecommons.org/licenses/by-nc-sa/">CC BY-NC-SA</license>
      <license value="http://creativecommons.org/licenses/by-nd/">CC BY-ND</license>
      <license value="http://creativecommons.org/licenses/by-nc-nd/">CC BY-NC-ND</license>
    </xsl:variable>

    <xsl:for-each select="gmd:identificationInfo[1]/*[1]">
      <xsl:for-each select="gmd:resourceConstraints">
        <xsl:for-each select="*/gmd:otherConstraints">

          <xsl:variable name="otherConstrCS" select="gco:CharacterString"/>
          <xsl:variable name="otherConstrAnchor" select="./gmx:Anchor"/>

          <!-- Index a list of license information values usually stored
                    in gmd:otherConstraints. If no constraint of that type found
                    the item is dropped. -->
          <xsl:choose>
            <xsl:when test="$licenseMap/license[starts-with($otherConstrAnchor/@xlink:href, @value)]">
              <license><xsl:value-of select="$licenseMap/license[starts-with($otherConstrAnchor/@xlink:href, @value)]" /></license>
            </xsl:when>

            <xsl:when test="$licenseMap/license[starts-with($otherConstrCS, @value)]">
              <license><xsl:value-of select="$licenseMap/license[starts-with($otherConstrCS, @value)]" /></license>
            </xsl:when>

            <xsl:when test="$otherConstrCS='Public Domain'
							or .='Open Database License (ODbL)'">
              <license><xsl:value-of select="$otherConstrCS" /></license>
            </xsl:when>

            <xsl:when test="contains(lower-case($otherConstrCS),'cc0')">
              <license>CC0</license>
            </xsl:when>

            <!-- This allow to index a gmx:Anchor with `geo gedeeld licentie` value and a random URL in xlink:href as a-->
            <xsl:when test="contains(lower-case($otherConstrCS),'geo gedeeld licentie') or contains(lower-case($otherConstrAnchor), 'geo gedeeld licentie')">
              <license>Geo Gedeeld licentie</license>
            </xsl:when>


            <xsl:when test="$otherConstrCS and normalize-space(lower-case($otherConstrCS))='geen beperkingen'">
              <!-- In case of a public domain or CC0 license please take
                            not into account the otherConstraint element containing
                            geen beperking.
                        -->
            </xsl:when>
            <xsl:otherwise>
              <!-- 14-11 JB: OtherConstraints no longer needed -->
              <!--Field name="license" string="OtherConstraints" store="true" index="true"/-->
            </xsl:otherwise>
          </xsl:choose>

        </xsl:for-each>
      </xsl:for-each>

    </xsl:for-each>

  </xsl:template>

</xsl:stylesheet>
