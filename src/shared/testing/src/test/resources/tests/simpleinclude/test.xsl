<!--

    SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
    SPDX-License-Identifier: GPL-2.0-or-later

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="3.0"
                exclude-result-prefixes="#all"
                xmlns:gc="http://geocat.net/">
  <xsl:import href="test-include.xsl"/>

  <xsl:variable name="test_xsl" select="'from-test.xsl'"/>

  <xsl:template match="/">
    <xsl:comment>comment from  <xsl:value-of select="$test_xsl"/> </xsl:comment>
    <xsl:comment>comment from  <xsl:value-of select="$test_include_xsl"/> </xsl:comment>
    <gc:geocat>Hello World TestCase</gc:geocat>
  </xsl:template>
</xsl:stylesheet>
