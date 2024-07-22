<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:gn-fn-index="https://www.geonetwork-opensource.org/xslt/functions/index"
                xmlns:util="https://geonetwork-opensource.org/xsl-extension"
                version="3.0"
                exclude-result-prefixes="#all">


  <xsl:output indent="yes" method="xml"/>

  <xsl:include href="iso-utility.xsl"/>
  <xsl:include href="common.xsl"/>

  <xsl:variable name="multilingualProperties" as="node()*">
    <property name="standardName" xpath="metadataStandard/*/title"/>
    <property name="standardVersion" xpath="metadataStandard/*/edition"/>
    <property name="resourceTypeName" xpath="metadataScope/*/name"/>
    <property name="resourceTitle" xpath="identificationInfo/*/citation/*/title"/>
    <property name="resourceAltTitle" xpath="identificationInfo/*/citation/*/alternateTitle"/>
    <property name="resourceAbstract" xpath="identificationInfo/*/abstract"/>
    <property name="resourceCredit" xpath="identificationInfo/*/credit"/>
    <property name="supplementalInformation" xpath="identificationInfo/*/supplementalInformation"/>
    <property name="purpose" xpath="identificationInfo/*/purpose"/>
    <property name="extentIdentifier"
              xpath="identificationInfo/*/extent/*/geographicElement/*/geographicIdentifier/*/code"/>
    <property name="extentDescription"
              xpath="identificationInfo/*/extent/*/description"/>
    <property name="orderingInstructions"
              xpath="distributionInfo/*/distributor/*/distributionOrderProcess/*/orderingInstructions"/>
    <property name="license"
              xpath="identificationInfo/*/resourceConstraints/*/otherConstraints"/>

    <!-- ISO 19115-3 only -->
    <property name="lineage" xpath="resourceLineage/*/statement"/>
    <property name="sourceDescription" xpath="resourceLineage/*/source/*/description"/>

    <!-- ISO 19139 only -->
    <property name="lineage" xpath="dataQualityInfo/*/lineage/*/statement"/>
  </xsl:variable>



  <xsl:template mode="index"
                match="*:MD_Metadata">
    <xsl:variable name="properties" as="node()*">
      <docType>metadata</docType>

      <xsl:variable name="identifier"
                    as="xs:string?"
                    select="*:metadataIdentifier/*/*:code/*:CharacterString[. != '']
                                |*:fileIdentifier/*:CharacterString[. != '']"/>
      <metadataIdentifier>
        <xsl:value-of select="$identifier"/>
      </metadataIdentifier>

      <xsl:for-each select="(*:dateInfo/*[*:dateType/*/@codeListValue = 'revision']/*:date/*[gn-fn-index:is-isoDate(.)])[1]
                                          |(*:dateStamp/*[gn-fn-index:is-isoDate(.)])[1]">
        <dateStamp>
          <xsl:value-of select="util:convertToISOZuluDateTime(normalize-space(.))"/>
        </dateStamp>
      </xsl:for-each>


      <xsl:variable name="languages" as="node()*">
        <xsl:variable name="mainLanguageCode" as="xs:string?"
                      select="*:defaultLocale/*/*:language/*/@codeListValue[normalize-space(.) != '']
                                  |*:language[1]/*/@codeListValue[normalize-space(.) != '']"/>

        <xsl:variable name="mainLanguage" as="xs:string?"
                      select="if ($mainLanguageCode)
                              then $mainLanguageCode
                              else *:language[1]/*:CharacterString[normalize-space(.) != '']"/>

        <xsl:variable name="otherLanguages" as="attribute()*"
                      select="*:otherLocale/*/*:language/*/@codeListValue[normalize-space(.) != '']
                                |*:locale/*/*:languageCode/*/@codeListValue[normalize-space(.) != '']"/>

        <lang default=""
              id="{$otherLanguages/*:PT_Locale[*:language/*/@codeListValue = $mainLanguage]/@id}"
              code="{$mainLanguage}"/>
        <xsl:for-each select="$otherLanguages[*/*:language/*/@codeListValue != $mainLanguage]">
          <lang id="{ancestor::*:PT_Locale/@id}" code="{.}"/>
        </xsl:for-each>
      </xsl:variable>

      <xsl:for-each select="$languages[@default]">
        <mainLanguage>
          <xsl:value-of select="@code"/>
        </mainLanguage>
      </xsl:for-each>
      <xsl:for-each select="$languages[not(@default)]">
        <otherLanguage>
          <xsl:value-of select="@code"/>
        </otherLanguage>
        <otherLanguageId>
          <xsl:value-of select="@id"/>
        </otherLanguageId>
      </xsl:for-each>

      <xsl:for-each select="*:identificationInfo/*/*:defaultLocale/*/*:language/*/@codeListValue[. != '']
                                        |*:identificationInfo/*/*:language/(*:CharacterString|*:LanguageCode/@codeListValue)">
        <resourceLanguage>
          <xsl:value-of select="."/>
        </resourceLanguage>
      </xsl:for-each>
      <xsl:for-each select="*:identificationInfo/*/(*:defaultLocale/*/*:characterEncoding|*:characterSet)/*/@codeListValue[. != '']">
        <cl_resourceCharacterSet>
          <key>
            <xsl:value-of select="."/>
          </key>
          <default>
            <xsl:value-of select="."/><!-- TODO: translation-->
          </default>
          <link>
            <xsl:value-of select="@codeList"/>
          </link>
          <xsl:if test="text() != ''">
            <text>
              <xsl:value-of select="normalize-space(text())"/>
            </text>
          </xsl:if>
        </cl_resourceCharacterSet>>
      </xsl:for-each>


      <xsl:call-template name="has-xlinks"/>

      <xsl:call-template name="index-resourceType"/>

      <xsl:call-template name="index-codelists">
        <xsl:with-param name="languages" select="$languages"/>
      </xsl:call-template>

      <xsl:call-template name="index-constraints">
        <xsl:with-param name="languages" select="$languages"/>
      </xsl:call-template>

      <xsl:call-template name="index-keywords">
        <xsl:with-param name="languages" select="$languages"/>
      </xsl:call-template>

      <xsl:call-template name="index-additional-documents">
        <xsl:with-param name="languages" select="$languages"/>
      </xsl:call-template>

      <xsl:call-template name="index-contact">
        <xsl:with-param name="languages" select="$languages"/>
      </xsl:call-template>

      <xsl:apply-templates mode="index" select="*">
        <xsl:with-param name="languages" select="$languages"/>
      </xsl:apply-templates>
    </xsl:variable>

    <xsl:for-each select="$properties">
      <xsl:sort select="name()"/>
      <xsl:copy-of select="."/>
    </xsl:for-each>
  </xsl:template>


  <!--
  Sample structure of multilingual properties in ISO:

  ```xml
        <cit:title xsi:type="lan:PT_FreeText_PropertyType">
            <gco:CharacterString>Template for Vector data (multilingual)</gco:CharacterString>
            <lan:PT_FreeText>
              <lan:textGroup>
                <lan:LocalisedCharacterString locale="#EN">Template for Vector data (multilingual)
                </lan:LocalisedCharacterString>
              </lan:textGroup>
  ```
  -->
  <xsl:template mode="index"
                match="*[string-join(ancestor-or-self::*/local-name()[matches(substring(., 1, 1), '[a-z]')], '/*/') = $multilingualProperties/@xpath]">
    <xsl:param name="languages" as="node()*"/>

    <xsl:variable name="fieldConfiguration"
                  select="$multilingualProperties[@xpath = string-join(current()/ancestor-or-self::*/local-name()[matches(substring(., 1, 1), '[a-z]')], '/*/')][1]"/>

    <xsl:copy-of
      select="gn-fn-index:add-multilingual-field($fieldConfiguration/@name, ., $languages)"/>
  </xsl:template>


  <xsl:template mode="index"
                match="*:identificationInfo/*/*:graphicOverview/*">
    <xsl:param name="languages" as="node()*"/>

    <xsl:variable name="fileName"
                  select="*:fileName/(*:CharacterString[. != '']|*:FileName[@src != ''])"/>

    <overview>
      <url>
        <xsl:value-of
          select="if (local-name($fileName) = 'FileName') then @src else normalize-space($fileName/text())"/>
      </url>
      <xsl:if test="normalize-space(*:fileDescription) != ''">,
        <nameObject>
          <xsl:copy-of select="gn-fn-index:add-multilingual-field('name', *:fileDescription, $languages)/*"/>
        </nameObject>
      </xsl:if>
    </overview>
  </xsl:template>


  <xsl:template mode="index" match="*:identificationInfo/*/*:citation/*/*:edition/*/text()[. != '']">
    <resourceEdition><xsl:value-of select="."/></resourceEdition>
  </xsl:template>


  <xsl:template mode="index" match="@*|node()">
    <xsl:param name="languages" as="node()*"/>
    <xsl:apply-templates mode="index" select="@*|node()">
      <xsl:with-param name="languages" select="$languages"/>
    </xsl:apply-templates>
  </xsl:template>

</xsl:stylesheet>
