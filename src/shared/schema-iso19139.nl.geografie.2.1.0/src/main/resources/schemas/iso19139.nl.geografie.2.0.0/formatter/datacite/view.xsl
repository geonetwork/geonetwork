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
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:gml="http://www.opengis.net/gml/3.2"
                xmlns:gml320="http://www.opengis.net/gml"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:datacite="http://datacite.org/schema/kernel-4"
                xmlns:tr="java:org.fao.geonet.api.records.formatters.SchemaLocalizations"
                xmlns:saxon="http://saxon.sf.net/"
                xmlns:gn="http://www.fao.org/geonetwork"
                version="3.0"
                extension-element-prefixes="saxon"
                exclude-result-prefixes="#all">

  <xsl:output method="xml"
              indent="yes"/>

  <xsl:import href="../../../iso19139/formatter/datacite/view.xsl"/>
</xsl:stylesheet>
