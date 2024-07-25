<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:gn-fn-index="https://www.geonetwork-opensource.org/xslt/functions/index"
                version="3.0"
                exclude-result-prefixes="#all">

  <xsl:template mode="index"
                match="*:contentInfo/*/*:featureCatalogue/*">
    <!--                match="*:contentInfo/*/*:featureCatalogue/*/*:featureType/*">-->
    <xsl:param name="languages" as="node()*"/>

    <xsl:variable name="isOnlyFeatureCatalog"
                  select="not(ancestor::*:MD_Metadata/*:identificationInfo)"
                  as="xs:boolean"/>

    <xsl:if test="$isOnlyFeatureCatalog">
      <xsl:for-each select="(*:name[*/text() != '']
                        |*:featureType/*/*:typeName[text() != ''])[1]">
        <xsl:copy-of
          select="gn-fn-index:add-multilingual-field('resourceTitle', ., $languages)"/>
      </xsl:for-each>

      <xsl:for-each select="*:versionNumber/*">
        <resourceEdition>
          <xsl:value-of select="."/>
        </resourceEdition>
      </xsl:for-each>

      <xsl:for-each select="*:scope">
        <xsl:copy-of select="gn-fn-index:add-multilingual-field('resourceAbstract', ., $languages)"/>
      </xsl:for-each>
    </xsl:if>
  </xsl:template>

  <xsl:template mode="index"
                match="*:featureType/*">
    <featureTypes>
      <typeName>
        <xsl:value-of select="normalize-space(*:typeName)"/>
      </typeName>
      <definition>
        <xsl:value-of select="*:definition/*:CharacterString/text()"/>
      </definition>
      <code>
        <xsl:value-of select="normalize-space(*:code/(*:CharacterString|*:Anchor)/text())"/>
      </code>
      <isAbstract>
        <xsl:value-of select="*:isAbstract/*:Boolean/text()"/>
      </isAbstract>
      <aliases>
        <xsl:value-of select="normalize-space(*:aliases/*:CharacterString/text())"/>"
      </aliases>
      <!--"inheritsFrom" : "<xsl:value-of select="*:FC_FeatureType/*:inheritsFrom/*/text("/>
      "inheritsTo" : "<xsl:value-of select="*:FC_FeatureType/*:inheritsTo/*/text("/>
      "constrainedBy" : "<xsl:value-of select="*:FC_FeatureType/*:constrainedBy/*/text("/>
      "definitionReference" : "<xsl:value-of select="*:FC_FeatureType/*:definitionReference/*/text("/>-->
      <!-- Index attribute table as JSON object -->
      <xsl:variable name="attributes"
                    select="*:carrierOfCharacteristics"/>
      <xsl:if test="count($attributes) > 0">
        <xsl:for-each select="$attributes">
          <attributeTable>
            <!-- TODO: Add multilingual support-->
            <xsl:for-each select="*/*:memberName/text()">
              <name>
                <xsl:value-of select="."/>
              </name>
            </xsl:for-each>
            <xsl:for-each select="*/*:definition/*:CharacterString/text()">
              <definition>
                <xsl:value-of select="."/>
              </definition>
            </xsl:for-each>
            <xsl:for-each select="*/*:code/(*:CharacterString|*:Anchor)/text()">
              <code>
                <xsl:value-of select="."/>
              </code>
            </xsl:for-each>
            <xsl:for-each select="*/*:code/*/@xlink:href">
              <link>
                <xsl:value-of select="."/>
              </link>
            </xsl:for-each>
            <xsl:for-each select="*/*:valueType/*:TypeName/*:aName/*/text()">
              <type>
                <xsl:value-of select="."/>
              </type>
            </xsl:for-each>
            <xsl:if test="*/*:cardinality">
              <xsl:variable name="cardinalityValue">
                <xsl:choose>
                  <xsl:when test="*/*:cardinality/*/*:range">
                    <xsl:for-each select="*/*:cardinality/*/*:range">
                      <xsl:value-of select="concat(*/*:lower/*/text(), '..', */*:upper/*/text())"/>
                      <xsl:if test="position() != last()">,</xsl:if>
                    </xsl:for-each>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:value-of select="*/*:cardinality/(*:CharacterString|*:Anchor)/text()"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:variable>

              <cardinality>
                <xsl:value-of select="$cardinalityValue"/>
              </cardinality>
            </xsl:if>
            <xsl:variable name="codeList"
                          select="*/*:listedValue[normalize-space(*) != '']"/>
            <xsl:for-each select="$codeList">
              <values>
                <label>
                  <xsl:value-of select="*/*:label/*:CharacterString/text()"/>
                </label>
                <code>
                  <xsl:value-of select="*/*:code/(*:CharacterString|*:Anchor)/text()"/>
                </code>
                <definition>
                  <xsl:value-of select="*/*:definition/*:CharacterString/text()"/>
                </definition>
              </values>
            </xsl:for-each>
          </attributeTable>
        </xsl:for-each>
      </xsl:if>
    </featureTypes>
  </xsl:template>
</xsl:stylesheet>
