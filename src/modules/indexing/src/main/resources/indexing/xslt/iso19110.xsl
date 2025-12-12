<!--

    SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
    SPDX-License-Identifier: GPL-2.0-or-later

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:gn-fn-index="https://www.geonetwork-opensource.org/xslt/functions/index"
                xmlns:util="https://geonetwork-opensource.org/xsl-extension"
                version="3.0"
                exclude-result-prefixes="#all">


  <xsl:output indent="no" method="xml"/>

  <xsl:include href="iso-utility.xsl"/>
  <xsl:include href="common.xsl"/>


  <xsl:template mode="index"
                match="*:FC_FeatureCatalogue">

    <docType>metadata</docType>
    <resourceType>featureCatalog</resourceType>

    <xsl:variable name="identifier"
                  as="xs:string?"
                  select="*:metadataIdentifier/*/*:code/*:CharacterString[. != '']
                              |*:fileIdentifier/*:CharacterString[. != '']"/>
    <metadataIdentifier>
      <xsl:value-of select="$identifier"/>
    </metadataIdentifier>

    <xsl:variable name="languages" as="node()*">
      <lang default=""/>
    </xsl:variable>


    <xsl:for-each select="(*:name/gco:CharacterString[. != '']|*:featureType/*:typeName/gco:LocalName[. != ''])[1]">
      <resourceTitleObject>
        <default><xsl:value-of select="."/></default>
      </resourceTitleObject>
    </xsl:for-each>


    <xsl:for-each select="(*:scope/gco:CharacterString[. != '']|*:featureType/*:definition/gco:CharacterString[. != ''])[1]">
      <resourceAbstractObject>
        <default><xsl:value-of select="."/></default>
      </resourceAbstractObject>
    </xsl:for-each>


    <xsl:for-each select="(.//*:versionDate/*[gn-fn-index:is-isoDate(.)])[1]">
      <xsl:variable name="date" select="util:convertToISOZuluDateTime(normalize-space(.))"/>
      <dateStamp>
        <xsl:value-of select="$date"/>
      </dateStamp>
      <resourceDate>
        <type>revision</type>
        <date><xsl:value-of select="$date"/></date>
      </resourceDate>
    </xsl:for-each>

    <xsl:call-template name="has-xlinks"/>


    <xsl:call-template name="index-contact">
      <xsl:with-param name="languages" select="$languages"/>
    </xsl:call-template>

    <xsl:apply-templates mode="index" select="*">
      <xsl:with-param name="languages" select="$languages"/>
    </xsl:apply-templates>
  </xsl:template>


  <xsl:template mode="index" match="*:FC_FeatureCatalogue/*/*:versionNumber/*/text()[. != '']">
    <resourceEdition><xsl:value-of select="."/></resourceEdition>
  </xsl:template>

</xsl:stylesheet>
