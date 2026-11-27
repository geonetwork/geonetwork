<?xml version="1.0" encoding="UTF-8"?>
<!--

    SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
    SPDX-License-Identifier: GPL-2.0-or-later

-->

<!--
  DCAT-AP records are stored as RDF/XML so the DCAT output is the record
  itself, without any GeoNetwork internal information.
  Equivalent of the GeoNetwork 4 dcat-ap plugin present/csw/own.xsl.
-->
<xsl:stylesheet version="3.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:geonet="http://www.fao.org/geonetwork"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                exclude-result-prefixes="#all">

  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="/rdf:RDF">
    <xsl:copy>
      <xsl:copy-of select="@*"/>
      <xsl:copy-of select="*[name() != 'geonet:info']"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
