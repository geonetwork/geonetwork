<?xml version="1.0" encoding="UTF-8"?>
<!--

    SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
    SPDX-License-Identifier: GPL-2.0-or-later

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gmd="http://www.isotc211.org/2005/gmd"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                version="2.0"
                exclude-result-prefixes="#all">

  <xsl:import href="../../../iso19139.nl.geografie.2.0.0/formatter/dcat-ap-nl-3/view.xsl"/>

  <!-- Used for metadata that does not have ISO topic categories (for example, service metadata) and does not have also INSPIRE GEMET Themes keywords -->
  <xsl:variable name="fallbackDcatApThemes" as="node()*">
    <entry key="http://publications.europa.eu/resource/authority/data-theme/GOVE" />
  </xsl:variable>
</xsl:stylesheet>
