<?xml version="1.0" encoding="UTF-8"?>
<!--

    SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
    SPDX-License-Identifier: GPL-2.0-or-later

-->
<xsl:stylesheet
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:gmd="http://www.isotc211.org/2005/gmd"
  xmlns:xs="http://www.w3.org/2001/XMLSchema"
	exclude-result-prefixes="#all"
	version="3.0">

  <xsl:import href="iso19139-to-jsonld.xsl"/>

	<xsl:output method="text"/>

  <xsl:template match="/">
    <textResponse>
      <xsl:for-each select="root/gmd:MD_Metadata">
        <xsl:call-template name="iso19139toJsonLD">
          <xsl:with-param name="record" select="."/>
          <xsl:with-param name="lang" select="$lang"/>
        </xsl:call-template>
      </xsl:for-each>
    </textResponse>
  </xsl:template>
</xsl:stylesheet>





