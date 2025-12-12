<!--

    SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
    SPDX-License-Identifier: GPL-2.0-or-later

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                version="3.0"
                exclude-result-prefixes="#all">

  <xsl:template mode="index"
                match="*:identificationInfo/*/*:citation/*/*:identifier/*[string((*:code/(*:CharacterString|*:Anchor))[1]) != '']">
    <resourceIdentifier>
      <code>
        <xsl:value-of select="*:code/(*:CharacterString|*:Anchor)"/>
      </code>
      <codeSpace>
        <xsl:value-of select="*:codeSpace/(*:CharacterString|*:Anchor)"/>
      </codeSpace>
      <link>
        <xsl:value-of select="*:code/*:Anchor/@xlink:href"/>
      </link>
    </resourceIdentifier>
  </xsl:template>

</xsl:stylesheet>
