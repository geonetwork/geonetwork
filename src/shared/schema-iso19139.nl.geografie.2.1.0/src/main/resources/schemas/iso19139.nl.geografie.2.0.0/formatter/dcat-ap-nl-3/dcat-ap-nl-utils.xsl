<?xml version="1.0" encoding="UTF-8"?>
<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet version="3.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:mcc="http://standards.iso.org/iso/19115/-3/mcc/1.0"
                xmlns:gco="http://standards.iso.org/iso/19115/-3/gco/1.0"
                xmlns:gcx="http://standards.iso.org/iso/19115/-3/gcx/1.0"
                xmlns:lan="http://standards.iso.org/iso/19115/-3/lan/1.0"
                xmlns:cit="http://standards.iso.org/iso/19115/-3/cit/2.0"
                xmlns:mri="http://standards.iso.org/iso/19115/-3/mri/1.0"
                xmlns:dct="http://purl.org/dc/terms/"
                xmlns:dcat="http://www.w3.org/ns/dcat#"
                xmlns:foaf="http://xmlns.com/foaf/0.1/"
                xmlns:vcard="http://www.w3.org/2006/vcard/ns#"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
                xmlns:owl="http://www.w3.org/2002/07/owl#"
                xmlns:adms="http://www.w3.org/ns/adms#"
                xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                xmlns:gn-fn-dcat="http://geonetwork-opensource.org/xsl/functions/dcat"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                exclude-result-prefixes="#all">


  <!-- Use iso2code for language code -->
  <xsl:template name="rdf-localised">
    <xsl:param name="nodeName"
               as="xs:string"/>

    <xsl:if test="*/text() != ''">
      <xsl:element name="{$nodeName}">
        <xsl:attribute name="xml:lang" select="$languages[@default]/@iso2code"/>
        <xsl:value-of select="*/text()"/>
      </xsl:element>
    </xsl:if>

    <xsl:variable name="hasDefaultLanguageCharacterString"
                  select="count(gco:CharacterString|gcx:Anchor) > 0"/>

    <xsl:for-each select="lan:PT_FreeText/*/lan:LocalisedCharacterString[text() != '']">
      <xsl:variable name="translationLanguage"
                    select="@locale"/>

      <xsl:choose>
        <xsl:when test="$hasDefaultLanguageCharacterString
                        and $translationLanguage = $languages[@default]/concat('#', @id)">
          <!-- Ignored default language which may be repeated in translations. -->
        </xsl:when>
        <xsl:otherwise>
          <xsl:element name="{$nodeName}">
            <xsl:attribute name="xml:lang" select="$languages[concat('#', @id) = $translationLanguage]/@iso2code"/>
            <xsl:value-of select="text()"/>
          </xsl:element>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
