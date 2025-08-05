<?xml version="1.0" ?><?xar XSLT?>
<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->

<!-- Schematron message -->

<xsl:stylesheet
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:axsl="http://www.w3.org/1999/XSL/TransformAlias"
  version="2.0">

  <xsl:import href="iso_schematron_skeleton_for_saxon.xsl"/>

  <xsl:template name="process-prolog">
    <axsl:output method="text"/>
  </xsl:template>

  <!-- use default rule for process-root:  copy contens / ignore title -->
  <!-- use default rule for process-pattern: ignore name and see -->
  <!-- use default rule for process-name:  output name -->
  <!-- use default rule for process-assert and process-report:
       call process-message -->

  <xsl:template name="process-message">
    <xsl:param name="pattern"/>
    <xsl:param name="role"/>
    <axsl:message>
      <xsl:apply-templates mode="text"
      />
      (<xsl:value-of select="$pattern"/>
      <xsl:if test="$role">/
        <xsl:value-of select="$role"/>
      </xsl:if>
      )
    </axsl:message>
  </xsl:template>

</xsl:stylesheet>
