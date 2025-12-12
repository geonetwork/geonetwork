<?xml version="1.0" encoding="UTF-8"?>
<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:gmd="http://www.isotc211.org/2005/gmd"
	exclude-result-prefixes="#all"
	version="2.0">

	<xsl:output method="text"/>

  <xsl:include href="iso19139-to-jsonld.xsl"/>

  <xsl:template match="/">
    <textResponse>
      <xsl:apply-templates mode="getJsonLD"
                           select="*/gmd:MD_Metadata"/>
    </textResponse>
  </xsl:template>
</xsl:stylesheet>


