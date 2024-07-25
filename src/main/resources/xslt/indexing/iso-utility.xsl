<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:gn-fn-index="https://www.geonetwork-opensource.org/xslt/functions/index"
                xmlns:util="https://geonetwork-opensource.org/xsl-extension"
                version="3.0"
                exclude-result-prefixes="#all">

  <xsl:include href="iso-constant.xsl"/>
  <xsl:include href="iso-utility-keywords.xsl"/>
  <xsl:include href="iso-utility-contacts.xsl"/>
  <xsl:include href="iso-utility-identiifers.xsl"/>
  <xsl:include href="iso-utility-spatial.xsl"/>
  <xsl:include href="iso-utility-temporal.xsl"/>
  <xsl:include href="iso-utility-quality.xsl"/>
  <xsl:include href="iso-utility-featurecatalogue.xsl"/>
  <xsl:include href="iso-utility-associations.xsl"/>
  <xsl:include href="iso-utility-distributions.xsl"/>


  <!--
   Multilingual fields are stored as an object.
   ```json
   {
    default: "Français", -> The default language
    langfre: "Français", -> The default language is the first property
    langeng: "English",
    ...
    (optional) link: "http://" -> Anchor xlink:href attribute
   }
   ```

    A multilingual field in ISO19139 looks like:
    ```xml
    <gmd:title xsi:type="gmd:PT_FreeText_PropertyType">
      <gco:CharacterString|gmx:Anchor xlink:href="http">Template for Vector data in ISO19139 (multilingual)</gco:CharacterString>
      <gmd:PT_FreeText>
        <gmd:textGroup>
          <gmd:LocalisedCharacterString locale="#FRE">Modèle de données vectorielles en
            ISO19139 (multilingue)
          </gmd:LocalisedCharacterString>
        </gmd:textGroup>
      ```

   -->
  <xsl:function name="gn-fn-index:add-multilingual-field" as="node()*">
    <xsl:param name="fieldName" as="xs:string"/>
    <xsl:param name="field" as="node()*"/>
    <xsl:param name="languages" as="node()*"/>

    <xsl:variable name="defaultLangValue"
                  select="normalize-space(($field/(*:CharacterString|*:Anchor)/text())[1])"/>

    <xsl:element name="{$fieldName}Object">
      <xsl:for-each select="$languages">
        <xsl:if test="@default and $defaultLangValue != ''">
          <default>
            <xsl:value-of select="$defaultLangValue"/>
          </default>
          <xsl:if test="@code">
            <xsl:element name="{concat('lang', @code)}">
              <xsl:value-of select="$defaultLangValue"/>
            </xsl:element>
          </xsl:if>
        </xsl:if>

        <xsl:variable name="translation"
                      select="normalize-space($field/*/*:LocalisedCharacterString[@locale = current()/@id]/text()[. != ''])"/>

        <xsl:if test="$translation">
          <xsl:element name="{concat('lang', @code)}">
            <xsl:value-of select="$translation"/>
          </xsl:element>
        </xsl:if>
      </xsl:for-each>

      <xsl:if test="$field/*:Anchor/@xlink:href[. != '']">
        <link>
          <xsl:value-of select="$field/*:Anchor/@xlink:href"/>
        </link>
      </xsl:if>
    </xsl:element>
  </xsl:function>



  <xsl:template name="index-codelists" match="*:MD_Metadata">
    <xsl:param name="languages" as="node()*"/>

    <xsl:for-each-group select=".//*[@codeListValue != '' and
                            not(local-name() = $excludedCodelists)]"
                        group-by="@codeListValue">
      <xsl:variable name="parentName"
                    select="local-name(..)"/>
      <xsl:variable name="codelistName"
                    select="name(.)"/>

      <xsl:variable name="fieldName"
                    select="concat('cl_', $parentName)"/>

      <xsl:copy-of select="gn-fn-index:build-codelist($fieldName, $codelistName, current-grouping-key(), $languages, @codeList, text())"/>
    </xsl:for-each-group>
  </xsl:template>


  <xsl:template name="index-resourceType">
    <!-- Record is dataset if no hierarchyLevel  -->
    <xsl:variable name="isDataset" as="xs:boolean"
                  select="
                      count(*:metadataScope[*/*:resourceScope/*/@codeListValue = 'dataset']) > 0 or
                      count(*:hierarchyLevel[*/@codeListValue='dataset']) > 0 or
                      count(*:metadataScope) = 0"/>
    <xsl:variable name="isService" as="xs:boolean"
                  select="
                      count(*:metadataScope[*/*:resourceScope/*/@codeListValue='service']) > 0 or
                      count(*:hierarchyLevel[*/@codeListValue='service']) > 0"/>
    <!-- ISO19115-3 records can be only a feature catalogue description.
         In this case,
         * add the resourceType=featureCatalog to enable search when linking records
         * Index feature catalogue name as title, scope as abstract.
         * (TODO: Check which scopeCode is more appropriate eg. featureType ?)
         -->
    <xsl:variable name="isFeatureCatalog"
                  select="exists(*:contentInfo/*/*:featureCatalogue)"
                  as="xs:boolean"/>
    <xsl:variable name="isOnlyFeatureCatalog"
                  select="not(*:identificationInfo)
                            and $isFeatureCatalog"
                  as="xs:boolean"/>

    <xsl:choose>
      <xsl:when test="$isOnlyFeatureCatalog">
        <resourceType>featureCatalog</resourceType>
      </xsl:when>
      <xsl:when test="$isDataset">
        <resourceType>dataset</resourceType>
        <xsl:if test="$isFeatureCatalog">
          <resourceType>featureCatalog</resourceType>
        </xsl:if>
      </xsl:when>
      <xsl:otherwise>
        <xsl:for-each select="*:metadataScope/*/*:resourceScope/*/@codeListValue[normalize-space(.) != '']
                                          |*:hierarchyLevel/*/@codeListValue[normalize-space(.) != '']">
          <resourceType>
            <xsl:value-of select="."/>
          </resourceType>
        </xsl:for-each>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>


  <xsl:template name="index-extents" match="*:MD_Metadata">
    <xsl:param name="languages" as="node()*"/>
  </xsl:template>


  <xsl:template name="index-overviews" match="*:MD_Metadata">
    <xsl:param name="languages" as="node()*"/>
  </xsl:template>


  <xsl:template name="index-distribution" match="*:MD_Metadata">
    <xsl:param name="languages" as="node()*"/>
  </xsl:template>


  <xsl:template name="index-constraints">
    <xsl:param name="languages" as="node()*"/>

    <xsl:for-each select=".//*:resourceConstraints/*">
      <xsl:variable name="fieldPrefix" select="local-name()"/>

      <xsl:for-each select="*:otherConstraints">
        <xsl:copy-of
          select="gn-fn-index:add-multilingual-field(concat($fieldPrefix, 'OtherConstraints'), . , $languages)"/>
      </xsl:for-each>
      <xsl:for-each select="*:useLimitation">
        <xsl:copy-of
          select="gn-fn-index:add-multilingual-field(concat($fieldPrefix, 'UseLimitation'), ., $languages)"/>
      </xsl:for-each>
    </xsl:for-each>

    <xsl:for-each select="*:resourceConstraints/*:MD_LegalConstraints/*:otherConstraints">
      <xsl:copy-of select="gn-fn-index:add-multilingual-field('license', ., $languages)"/>
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
