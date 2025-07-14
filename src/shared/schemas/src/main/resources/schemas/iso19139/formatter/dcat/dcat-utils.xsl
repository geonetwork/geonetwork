<?xml version="1.0" encoding="UTF-8"?>
<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:util="https://geonetwork-opensource.org/xsl-extension"
                version="3.0"
                exclude-result-prefixes="#all">

    <xsl:template mode="get-language"
                  match="gmd:MD_Metadata"
                  as="node()*">
        <xsl:variable name="defaultLanguage"
                      select="$metadata/gmd:language"/>
        <xsl:for-each select="$defaultLanguage">
            <xsl:variable name="iso3code"
                          as="xs:string?"
                          select="*/@codeListValue"/>

            <language id="{../gmd:locale/*[gmd:languageCode/*/@codeListValue = $iso3code]/@id}"
                      iso3code="{$iso3code}"
                      iso2code="{util:twoCharLangCode($iso3code)}"
                      default=""/>
        </xsl:for-each>

        <xsl:for-each select="$metadata/gmd:locale/*[not(@id = $defaultLanguage/@id)]">
            <language id="{@id}"
                      iso3code="{gmd:languageCode/*/@codeListValue}"
                      iso2code="{util:twoCharLangCode(gmd:languageCode/*/@codeListValue)}"/>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
