<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:gn-fn-index="https://www.geonetwork-opensource.org/xslt/functions/index"
                xmlns:util="https://geonetwork-opensource.org/xsl-extension"
                version="3.0"
                exclude-result-prefixes="#all">
  <xsl:template mode="index"
                match="*:distributionFormat/*/*:formatSpecificationCitation/*/*:title/*/text()[. != '']
                |*:distributionFormat/*/*:name/*[. != '']">
    <format>
      <xsl:value-of select="normalize-space(.)"/>
    </format>
  </xsl:template>

  <xsl:template mode="index"
                match="*:distributionInfo//*:onLine/*[*:linkage/*/text() != '']">
     <xsl:param name="languages" as="node()*"/>

      <xsl:variable name="transferGroup"
                    select="count(ancestor::*:transferOptions/preceding-sibling::*:transferOptions)"/>

      <xsl:variable name="protocol" select="normalize-space(*:protocol/*/text())"/>
      <xsl:variable name="url" select="normalize-space(*:linkage/*[1]/text())"/>

      <linkUrl>
        <xsl:value-of select="$url"/>
      </linkUrl>
      <xsl:for-each select="distinct-values($protocol[. != ''])">
        <linkProtocol>
          <xsl:value-of select="."/>
        </linkProtocol>
      </xsl:for-each>
      <xsl:element name="linkUrlProtocol{replace($protocol, '[^a-zA-Z0-9]', '')}">
        <xsl:value-of select="$url"/>
      </xsl:element>
      <link>
        <hash><xsl:value-of select="util:md5Hex((normalize-space(.)))"/></hash>
        <idx><xsl:value-of select="position()"/></idx>
        <protocol><xsl:value-of select="*:protocol/*/text()"/></protocol>
        <mimeType><xsl:value-of select="if (*/*:MimeFileType)
                                              then */*:MimeFileType/@type
                                              else if (starts-with(*:protocol/*:CharacterString, 'WWW:DOWNLOAD:'))
                                              then replace(*:protocol/*:CharacterString, 'WWW:DOWNLOAD:', '')
                                              else ''"/></mimeType>
        <xsl:if test="normalize-space(*:linkage) != ''">
          <urlObject><xsl:copy-of select="gn-fn-index:add-multilingual-field(
                                'url', *:linkage, $languages)/*"/></urlObject>
        </xsl:if>
        <xsl:if test="normalize-space(*:name) != ''">
          <nameObject><xsl:copy-of select="gn-fn-index:add-multilingual-field(
                                'name', *:name, $languages)/*"/></nameObject>
        </xsl:if>
        <xsl:if test="normalize-space(*:description) != ''">
          <descriptionObject><xsl:copy-of select="gn-fn-index:add-multilingual-field(
                                'description', *:description, $languages)/*"/></descriptionObject>
        </xsl:if>
        <xsl:if test="../@*:nilReason">
          <nilReason><xsl:value-of select="../@*:nilReason"/></nilReason>
        </xsl:if>
        <function><xsl:value-of select="*:function/*:CI_OnLineFunctionCode/@codeListValue"/></function>
        <applicationProfile><xsl:value-of select="*:applicationProfile/(*:CharacterString|*:Anchor)/text()"/></applicationProfile>
        <group><xsl:value-of select="$transferGroup"/></group>
      </link>
  </xsl:template>

  <xsl:template name="index-additional-documents">
    <xsl:param name="languages" as="node()*"/>


    <xsl:variable name="additionalDocuments" as="node()*">
      <xsl:call-template name="collect-documents"/>
    </xsl:variable>

    <xsl:for-each select="$additionalDocuments">
      <link>
        <protocol>
          <xsl:value-of select="
                                        protocol/text()"/>
        </protocol>
        <function>
          <xsl:value-of select="
                                        function/text()"/>
        </function>
        <xsl:if test="normalize-space(url) != ''">
          <urlObject>
            <xsl:copy-of select="gn-fn-index:add-multilingual-field('url', url/*, $languages)/*"/>
          </urlObject>
        </xsl:if>
        <xsl:if test="normalize-space(title) != ''">
          <nameObject>
            <xsl:copy-of select="gn-fn-index:add-multilingual-field('name', title/*, $languages)/*"/>
          </nameObject>
        </xsl:if>
        <xsl:if test="normalize-space(description) != ''">
          <descriptionObject>
            <xsl:copy-of select="gn-fn-index:add-multilingual-field('description', description/*, $languages)/*"/>
          </descriptionObject>
        </xsl:if>
        <xsl:if test="nilReason">
          <nilReason>
            <xsl:value-of select="nilReason"/>
          </nilReason>
        </xsl:if>
        <applicationProfile>
          <xsl:value-of select="applicationProfile/text()"/>
        </applicationProfile>
      </link>
    </xsl:for-each>
  </xsl:template>


  <!--
    <xsl:template name="collect-distribution-links">
      <xsl:for-each select="*/descendant::*[
                              local-name() = 'onLine'
                              ]/*[*:linkage/* != '']">
        <item>
          <id>
            <xsl:value-of select="*:linkage/*:CharacterString"/>
          </id>
          <title>
            <xsl:apply-templates mode="get-iso19115-3-localized-string"
                                 select="*:name"/>
          </title>
          <url>
            <xsl:apply-templates mode="get-iso19115-3-localized-string"
                                 select="*:linkage"/>
          </url>
          <function>
            <xsl:value-of select="*:function/*/@codeListValue"/>
          </function>
          <applicationProfile>
            <xsl:value-of select="*:applicationProfile/*:CharacterString"/>
          </applicationProfile>
          <description>
            <xsl:apply-templates mode="get-iso19115-3-localized-string"
                                 select="*:description"/>
          </description>
          <protocol>
            <xsl:value-of select="*:protocol/*/text()"/>
          </protocol>
          <mimeType>
            <xsl:value-of select="if (*/*:MimeFileType)
                                  then */*:MimeFileType/@type
                                  else if (starts-with(*:protocol/*:CharacterString, 'WWW:DOWNLOAD:'))
                                  then replace(*:protocol/*:CharacterString, 'WWW:DOWNLOAD:', '')
                                  else ''"/>
          </mimeType>
          <type>onlinesrc</type>
        </item>
      </xsl:for-each>
    </xsl:template>-->


  <xsl:variable name="documentsConfig" as="node()*">
    <doc protocol="WWW:LINK" function="legend" type="legend">
      <element>portrayalCatalogueCitation</element>
    </doc>
    <doc protocol="WWW:LINK" function="featureCatalogue" type="fcats">
      <element>featureCatalogueCitation</element>
    </doc>
    <doc protocol="WWW:LINK" function="dataQualityReport" type="dq-report">
      <element>additionalDocumentation</element>
      <element>specification</element>
      <element>reportReference</element>
    </doc>
  </xsl:variable>

  <!--
  Collecting links in the metadata records.
  -->
  <xsl:template name="collect-documents">
    <xsl:variable name="root" select="."/>
    <xsl:for-each select="$documentsConfig">
      <xsl:variable name="docType"
                    select="current()"/>
      <xsl:for-each select="$root/descendant::*[
                              local-name() = $docType/element/text()
                              ]/*/*:onlineResource/*[*:linkage/* != '']">
        <item>
          <id>
            <xsl:value-of select="*:linkage/*:CharacterString"/>
          </id>
          <url>
            <xsl:copy-of select="*:linkage"/>
          </url>
          <title>
            <xsl:variable name="name"
                          select="if (*:name) then *:name else ../../*:title"/>
            <xsl:copy-of select="$name"/>
          </title>
          <description>
            <xsl:variable name="desc"
                          select="if (*:description)
                                        then *:description
                                        else ../../../../*:abstract"/>
            <xsl:copy-of select="$desc"/>
          </description>
          <protocol>
            <xsl:value-of select="$docType/@protocol"/>
          </protocol>
          <function>
            <xsl:value-of select="$docType/@function"/>
          </function>
          <type>
            <xsl:value-of select="$docType/@type"/>
          </type>
          <xsl:if test="../../../@*:nilReason">
            <nilReason>
              <xsl:value-of select="../../../@*:nilReason"/>
            </nilReason>
          </xsl:if>
        </item>
      </xsl:for-each>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
