<?xml version="1.0" encoding="UTF-8"?>
<!--

    SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
    SPDX-License-Identifier: GPL-2.0-or-later

-->
<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
  xmlns:lan="http://standards.iso.org/iso/19115/-3/lan/1.0"
  xmlns:json-util="https://geonetwork-opensource.org/xsl-extension"
  xmlns:gn-fn-iso19115-3.2018="http://geonetwork-opensource.org/xsl/functions/profiles/iso19115-3.2018"
  exclude-result-prefixes="#all"
  version="2.0">

  <xsl:include href="iso19115-3.2018-to-jsonld.xsl"/>

  <xsl:output method="text"/>

  <xsl:template match="/">
    <xsl:variable name="json">
      <xsl:for-each select="mdb:MD_Metadata">
        <xsl:variable name="defaultLanguage"
                      select="((mdb:defaultLocale/*/lan:language/*/@codeListValue, 'eng')[. != ''])[1]"/>

        <xsl:variable name="requestedLanguageExist"
                      select="$lang != ''
                          and count(mdb:otherLocale/*[lan:language/*/@codeListValue = $lang]/@id) > 0"/>

        <xsl:variable name="requestedLanguage"
                      select="if ($requestedLanguageExist)
                          then $lang
                          else $defaultLanguage"/>

        <xsl:call-template name="iso19115-3.2018toJsonLD">
          <xsl:with-param name="record" select="."/>
          <xsl:with-param name="requestedLanguage" select="$requestedLanguage"/>
        </xsl:call-template>
      </xsl:for-each>
    </xsl:variable>

    <xsl:value-of select="json-util:prettyPrintJson(string($json))"/>
  </xsl:template>
</xsl:stylesheet>

