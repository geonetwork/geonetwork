<?xml version="1.0" encoding="UTF-8"?>
<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:gco="http://www.isotc211.org/2005/gco"
                xmlns:gmx="http://www.isotc211.org/2005/gmx"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                version="2.0"
                exclude-result-prefixes="#all">

  <xsl:output method="xml" version="1.0" encoding="UTF-8" indent="yes"/>


  <xsl:variable name="metadata"
                select="//gmd:MD_Metadata"/>

  <xsl:template match="/" priority="99">
    <xsl:for-each select="$metadata">
      <xsl:apply-templates select="." />
    </xsl:for-each>
  </xsl:template>


  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
