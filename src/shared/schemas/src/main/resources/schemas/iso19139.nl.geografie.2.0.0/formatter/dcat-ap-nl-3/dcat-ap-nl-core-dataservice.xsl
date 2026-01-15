<?xml version="1.0" encoding="UTF-8"?>
<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:cit="http://standards.iso.org/iso/19115/-3/cit/2.0"
                xmlns:srv="http://standards.iso.org/iso/19115/-3/srv/2.0"
                xmlns:gco="http://standards.iso.org/iso/19115/-3/gco/1.0"
                xmlns:gcx="http://standards.iso.org/iso/19115/-3/gcx/1.0"
                xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
                xmlns:mrd="http://standards.iso.org/iso/19115/-3/mrd/1.0"
                xmlns:dcat="http://www.w3.org/ns/dcat#"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                exclude-result-prefixes="#all">

  <!--
  Filter to define which service operation has to be considered
  as an endpoint URL or description depending on linkage and protocol.
  -->
  <xsl:param name="endpointDescriptionUrlExpression"
             as="xs:string"
             select="'GetCapabilities|WSDL'"/>
  <xsl:param name="endpointDescriptionProtocolsExpression"
             as="xs:string"
             select="'OpenAPI|Swagger|GetCapabilities|WSDL|Description'"/>

  <!--
  RDF Property:	dcat:endpointURL
  Definition:	The root location or primary endpoint of the service (a Web-resolvable IRI).
  Domain:	dcat:DataService
  Range:	rdfs:Resource
  -->

  <!--
  RDF Property:	dcat:endpointDescription
  Definition:	A description of the services available via the end-points, including their operations, parameters etc.
  Domain:	dcat:DataService
  Range:	rdfs:Resource
  Usage note:	The endpoint description gives specific details of the actual endpoint instances, while dcterms:conformsTo is used to indicate the general standard or specification that the endpoints implement.
  Usage note:	An endpoint description may be expressed in a machine-readable form, such as an OpenAPI (Swagger) description [OpenAPI],
  an OGC GetCapabilities response [WFS], [ISO-19142], [WMS], [ISO-19128],
  a SPARQL Service Description [SPARQL11-SERVICE-DESCRIPTION],
  an [OpenSearch]
  or [WSDL20] document,
  a Hydra API description [HYDRA],
   else in text or some other informal mode if a formal representation is not possible.
  -->

  <xsl:template mode="iso19115-3-to-dcat"
                match="srv:containsOperations/*/srv:connectPoint/*[not(
                                matches(cit:protocol/(gco:CharacterString|gcx:Anchor)/text(), $endpointDescriptionProtocolsExpression, 'i')
                                or matches(cit:linkage/(gco:CharacterString|gcx:Anchor)/text(), $endpointDescriptionUrlExpression, 'i')
                                or cit:function/*/@codeListValue = 'information')]/cit:linkage" priority="5">

    <xsl:if test="count(../../preceding-sibling::srv:connectPoint) = 0">
      <xsl:variable name="urlValue" select="normalize-space((gco:CharacterString|gcx:Anchor)/text())" />
      <dcat:endpointURL rdf:resource="{if (contains($urlValue, '?')) then substring-before($urlValue, '?') else $urlValue}"/>
      <dcat:endpointDescription rdf:resource="{$urlValue}"/>

      <xsl:variable name="distributionRelatedProtocol" select="//mdb:distributionInfo/*/mrd:transferOptions/*/mrd:onLine[*/cit:linkage/*/text() = $urlValue][1]/*/cit:protocol/*/text()" />
      <xsl:if test="string($distributionRelatedProtocol)">
        <xsl:call-template name="rdf-format-as-mediatype">
          <xsl:with-param name="format" select="$distributionRelatedProtocol"/>
        </xsl:call-template>
      </xsl:if>
    </xsl:if>

  </xsl:template>

  <xsl:template mode="iso19115-3-to-dcat"
                match="srv:containsOperations/*/srv:connectPoint/*[
                                matches(cit:protocol/(gco:CharacterString|gcx:Anchor)/text(), $endpointDescriptionProtocolsExpression, 'i')
                                or matches(cit:linkage/(gco:CharacterString|gcx:Anchor)/text(), $endpointDescriptionUrlExpression, 'i')
                                or cit:function/*/@codeListValue = 'information']/cit:linkage">

    <xsl:if test="count(../../preceding-sibling::srv:connectPoint) = 0">
      <dcat:endpointURL rdf:resource="{substring-before(normalize-space((gco:CharacterString|gcx:Anchor)/text()), '?')}"/>
      <dcat:endpointDescription rdf:resource="{normalize-space((gco:CharacterString|gcx:Anchor)/text())}"/>

      <xsl:variable name="urlValue" select="normalize-space((gco:CharacterString|gcx:Anchor)/text())" />

      <xsl:variable name="distributionRelatedProtocol" select="//mdb:distributionInfo/*/mrd:transferOptions/*/mrd:onLine[*/cit:linkage/*/text() = $urlValue][1]/*/cit:protocol/*/text()" />
      <xsl:if test="string($distributionRelatedProtocol)">
        <xsl:call-template name="rdf-format-as-mediatype">
          <xsl:with-param name="format" select="$distributionRelatedProtocol"/>
        </xsl:call-template>
      </xsl:if>
    </xsl:if>

  </xsl:template>

</xsl:stylesheet>
