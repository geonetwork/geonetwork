<?xml version="1.0" encoding="UTF-8"?>
<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet version="3.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:cit="http://standards.iso.org/iso/19115/-3/cit/2.0"
                xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
                xmlns:mri="http://standards.iso.org/iso/19115/-3/mri/1.0"
                xmlns:mco="http://standards.iso.org/iso/19115/-3/mco/1.0"
                xmlns:mdq="http://standards.iso.org/iso/19157/-2/mdq/1.0"
                xmlns:mrl="http://standards.iso.org/iso/19115/-3/mrl/2.0"
                xmlns:mrs="http://standards.iso.org/iso/19115/-3/mrs/1.0"
                xmlns:mrd="http://standards.iso.org/iso/19115/-3/mrd/1.0"
                xmlns:mcc="http://standards.iso.org/iso/19115/-3/mcc/1.0"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:mdUtil="java:org.fao.geonet.api.records.MetadataUtils"
                xmlns:util="https://geonetwork-opensource.org/xsl-extension"
                xmlns:dcat="http://www.w3.org/ns/dcat#"
                xmlns:dct="http://purl.org/dc/terms/"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:foaf="http://xmlns.com/foaf/0.1/"
                exclude-result-prefixes="#all">

  <xsl:variable name="nodeUrl"
                select="util:getServerURL()"/>

  <!-- Resource
   Unsupported:
   * dcat:first|previous(sameAs replaces, previousVersion?)|next|last|hasVersion (using the Associated API, navigate to series and sort by date?)
   * dct:isReferencedBy (using the Associated API)
   * dcat:hasCurrentVersion  (using the Associated API)
   * dct:rights
   * odrl:hasPolicy
   -->
  <xsl:template name="iso19115-3-to-dcat-ap-nl-resource"
                match="mdb:MD_Metadata" >

    <xsl:variable name="isServiceMetadata" select="name(mdb:identificationInfo/*[1]) = 'srv:SV_ServiceIdentification'" />

    <xsl:variable name="isSeriesMetadata" select="mdb:metadataScope/mdb:MD_MetadataScope/mdb:resourceScope/mcc:MD_ScopeCode/@codeListValue = 'series'" />

    <xsl:if test="$isServiceMetadata">
      <xsl:apply-templates mode="iso19115-3-to-dcat"
                           select="mdb:metadataIdentifier" />
    </xsl:if>

    <xsl:choose>
      <xsl:when test="$isSeriesMetadata">
        <xsl:apply-templates mode="iso19115-3-to-dcat"
                             select="mdb:identificationInfo/*/mri:citation/*/cit:title
                                  |mdb:identificationInfo/*/mri:abstract
                                  |mdb:identificationInfo/*/mri:citation/*/cit:date
                                  |mdb:identificationInfo/*/mri:pointOfContact
                                  |mdb:dataQualityInfo/*/mdq:report/*/mdq:result[mdq:DQ_ConformanceResult and mdq:DQ_ConformanceResult/mdq:pass/*/text() = 'true']
                          "/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates mode="iso19115-3-to-dcat"
                             select="mdb:identificationInfo/*/mri:citation/*/cit:title
                                  |mdb:identificationInfo/*/mri:abstract
                                  |mdb:identificationInfo/*/mri:citation/*/cit:identifier
                                  |mdb:identificationInfo/*/mri:citation/*/cit:date
                                  |mdb:identificationInfo/*/mri:citation/*/cit:edition
                                  |mdb:identificationInfo/*/mri:defaultLocale
                                  |mdb:identificationInfo/*/mri:otherLocale
                                  |mdb:identificationInfo/*/mri:resourceConstraints/*
                                  |mdb:identificationInfo/*/mri:status
                                  |mdb:identificationInfo/*/mri:descriptiveKeywords
                                  |mdb:identificationInfo/*/mri:pointOfContact
                                  |mdb:identificationInfo/*/mri:associatedResource
                                  |mdb:dataQualityInfo/*/mdq:report/*/mdq:result[mdq:DQ_ConformanceResult and mdq:DQ_ConformanceResult/mdq:pass/*/text() = 'true']
                                  |mdb:resourceLineage/*/mrl:statement
                                  |mdb:metadataLinkage
                          "/>
      </xsl:otherwise>
    </xsl:choose>

    <!-- TODO: Enable it -->
    <!--<xsl:call-template name="related-record"/>-->

  </xsl:template>


<!--  <xsl:template mode="iso19115-3-to-eu-dcat-ap"
                match="mrl:source">
    <dct:source>
    Need to point to the Dataset and not the CatalogRecord
    See below
      <xsl:call-template name="rdf-object-ref-attribute">
        <xsl:with-param name="isAbout" select="false()"/>
      </xsl:call-template>
    </dct:source>
  </xsl:template>-->


  <!-- TODO: Enable it -->
  <!--<xsl:template name="related-record">
    <xsl:variable name="associations"
                        select="mdUtil:getAssociatedAsXml(mdb:metadataIdentifier/*/mcc:code/*/text())"
                        as="node()?"/>

    <xsl:variable name="legislations"
                        select="mdb:identificationInfo/*/mri:descriptiveKeywords/*/mri:keyword[starts-with(*/@xlink:href, 'http://data.europa.eu/eli')]"/>

    <xsl:variable name="metadata" select="."/>

    <xsl:for-each select="$associations/relations/*">
      <xsl:sort select="@url"/>
      <xsl:variable name="resourceIdentifierWithHttpCodeSpace"
                          select="(root/resourceIdentifier[starts-with(codeSpace, 'http')])[1]"/>
      <xsl:variable name="recordUri"
                          select="if ($resourceIdentifierWithHttpCodeSpace)
                                       then concat($resourceIdentifierWithHttpCodeSpace/codeSpace, $resourceIdentifierWithHttpCodeSpace/code)
                                       else if (@uuid) then concat($nodeUrl, 'api/records/', @uuid, '#resource')
                                       else @url" />

      <xsl:choose>
        <xsl:when test="local-name() = 'parent'">
          <dcat:inSeries rdf:resource="{$recordUri}"/>
        </xsl:when>
        <xsl:when test="local-name() = 'children'">
          <dcat:seriesMember rdf:resource="{$recordUri}"/>
        </xsl:when>
        <xsl:when test="local-name() = 'brothersAndSisters'">
          <dct:relation rdf:resource="{$recordUri}"/>
        </xsl:when>
        <xsl:when test="local-name() = 'sources'">
          <dct:source rdf:resource="{$recordUri}"/>
        </xsl:when>
        <xsl:when test="local-name() = 'siblings'">
          <dct:references rdf:resource="{$recordUri}"/>
        </xsl:when>
        <xsl:when test="local-name() = 'datasets'">
          <dcat:servesDataset>
            <dcat:Dataset rdf:about="{$recordUri}"/>
          </dcat:servesDataset>
        </xsl:when>
        <xsl:when test="local-name() = 'services'">

          <xsl:variable name="mainLink"
                        select="(root/link[not(function = ('information', 'dataQualityReport'))])[1]"/>

          <xsl:variable name="serviceUri"
                        select="if (root/resourceIdentifier) then concat(root/resourceIdentifier[1]/codeSpace, root/resourceIdentifier[1]/code) else ." />

          <xsl:choose>
            &lt;!&ndash; Only record with resourceType is service are mapped to a distribution.
            Other related services which can be software, applications are mapped to foaf:page &ndash;&gt;
            <xsl:when test="root/resourceType = 'service'">
              &lt;!&ndash;<dcat:distribution>
                <dcat:Distribution>
                  <xsl:for-each select="$mainLink/urlObject/default">
                    <dcat:accessURL rdf:resource="{.}"/>
                    <dcat:accessService rdf:resource="{$serviceUri}"/>
                  </xsl:for-each>

                  <xsl:call-template name="rdf-index-field-localised">
                    <xsl:with-param name="nodeName" select="'dct:title'"/>
                    <xsl:with-param name="field" select="root/resourceTitleObject"/>
                  </xsl:call-template>

                  <xsl:call-template name="rdf-index-field-localised">
                    <xsl:with-param name="nodeName" select="'dct:description'"/>
                    <xsl:with-param name="field" select="root/resourceAbstractObject"/>
                  </xsl:call-template>
                  &lt;!&ndash;
                   RDF Property:	dcterms:issued
                   Definition:	Date of formal issuance (e.g., publication) of the distribution.
                  &ndash;&gt;
                  <xsl:for-each select="$metadata//mrd:MD_Distributor/mrd:distributionOrderProcess/*/mrd:plannedAvailableDateTime|
                                               $metadata/mdb:identificationInfo/*/mri:citation/*/cit:date/*[cit:dateType/*/@codeListValue = 'publication']">
                    <xsl:apply-templates mode="iso19115-3-to-dcat"
                                         select=".">
                      <xsl:with-param name="dateType" select="'publication'"/>
                    </xsl:apply-templates>
                  </xsl:for-each>

                  &lt;!&ndash;
                  RDF Property:	dcterms:modified
                  Definition:	Most recent date on which the distribution was changed, updated or modified.
                  Range:	rdfs:Literal encoded using the relevant ISO 8601 Date and Time compliant string [DATETIME] and typed using the appropriate XML Schema datatype [XMLSCHEMA11-2] (xsd:gYear, xsd:gYearMonth, xsd:date, or xsd:dateTime).
                  &ndash;&gt;
                  <xsl:for-each select="$metadata//mrd:MD_Distributor/mrd:distributionOrderProcess/*/mrd:plannedAvailableDateTime|
                                               $metadata/mdb:identificationInfo/*/mri:citation/*/cit:date/*[cit:dateType/*/@codeListValue = 'revision']">
                    <xsl:apply-templates mode="iso19115-3-to-dcat"
                                         select=".">
                      <xsl:with-param name="dateType" select="'revision'"/>
                    </xsl:apply-templates>
                  </xsl:for-each>

                  <xsl:apply-templates mode="iso19115-3-to-dcat"
                                       select="$metadata/mdb:identificationInfo/*/mri:resourceConstraints/*[mco:useConstraints]"/>
                  <xsl:apply-templates mode="iso19115-3-to-dcat"
                                       select="$metadata/mdb:identificationInfo/*/mri:resourceConstraints/*[mco:accessConstraints]"/>

                  <xsl:apply-templates mode="iso19115-3-to-dcat"
                                       select="$metadata/mdb:identificationInfo/*/mri:defaultLocale"/>

                  <xsl:apply-templates mode="iso19115-3-to-dcat"
                                       select="$legislations"/>

                  <xsl:call-template name="rdf-format-as-mediatype">
                    <xsl:with-param name="format" select="$mainLink/protocol"/>
                  </xsl:call-template>
                </dcat:Distribution>
              </dcat:distribution>&ndash;&gt;
            </xsl:when>
            <xsl:otherwise>
              <foaf:page>
                <foaf:Document rdf:about="{$mainLink/urlObject/default}">
                  <dct:title><xsl:value-of select="root/resourceTitleObject/default"/></dct:title>
                  <dct:description xml:lang="fre"><xsl:value-of select="root/resourceAbstractObject/default"/></dct:description>
                </foaf:Document>
              </foaf:page>
            </xsl:otherwise>
          </xsl:choose>
        </xsl:when>
        <xsl:otherwise>
          &lt;!&ndash; TODO: other type of relations &ndash;&gt;
        </xsl:otherwise>
      </xsl:choose>
    </xsl:for-each>
  </xsl:template>-->

</xsl:stylesheet>
