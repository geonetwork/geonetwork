<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:dc="http://purl.org/dc/elements/1.1/"
                xmlns:dct="http://purl.org/dc/terms/"
                xmlns:gn-fn-index="https://www.geonetwork-opensource.org/xslt/functions/index"
                xmlns:util="https://geonetwork-opensource.org/xsl-extension"
                version="3.0"
                exclude-result-prefixes="#all">


  <xsl:output indent="no" method="xml"/>

  <xsl:include href="common.xsl"/>
  <xsl:import href="utility-keywords.xsl"/>

  <xsl:variable name="multilingualProperties" as="node()*">
    <property name="resourceTitle" xpath="simpledc/title"/>
    <property name="resourceAbstract" xpath="simpledc/abstract"/>
    <property name="resourceAbstract" xpath="simpledc/description"/>
    <property name="license" xpath="simpledc/accessRights"/>
    <property name="license" xpath="simpledc/rights"/>
    <property name="lineage" xpath="simpledc/source"/>
  </xsl:variable>


  <xsl:function name="gn-fn-index:add-multilingual-field" as="node()*">
    <xsl:param name="fieldName" as="xs:string"/>
    <xsl:param name="field" as="node()*"/>
    <xsl:param name="languages" as="node()*"/>

    <xsl:variable name="defaultLangValue"
                  select="normalize-space(($field/text())[1])"/>

    <xsl:element name="{$fieldName}Object">
      <default>
        <xsl:value-of select="$defaultLangValue"/>
      </default>
    </xsl:element>
  </xsl:function>

  <xsl:template mode="index"
                match="simpledc">

    <docType>metadata</docType>

    <xsl:variable name="identifier"
                  as="xs:string?"
                  select="dc:identifier[1][. != '']"/>
    <metadataIdentifier>
      <xsl:value-of select="$identifier"/>
    </metadataIdentifier>


    <xsl:for-each select="(dct:modified[. != ''])[1]">
      <dateStamp>
        <xsl:value-of select="util:convertToISOZuluDateTime(normalize-space(.))"/>
      </dateStamp>
    </xsl:for-each>


    <xsl:variable name="languages" as="node()*">
      <xsl:variable name="mainLanguageCode" as="xs:string?"
                    select="(dc:language[normalize-space(.) != ''])[1]"/>

      <xsl:variable name="mainLanguage" as="xs:string?"
                    select="$mainLanguageCode"/>

      <lang default=""
            id=""
            code="{$mainLanguage}"/>
    </xsl:variable>

    <xsl:for-each select="$languages[@default]">
      <mainLanguage>
        <xsl:value-of select="@code"/>
      </mainLanguage>
      <resourceLanguage>
        <xsl:value-of select="@code"/>
      </resourceLanguage>
    </xsl:for-each>

    <xsl:for-each select="dc:type[. != '']">
      <resourceType>
        <xsl:value-of select="."/>
      </resourceType>
    </xsl:for-each>


    <xsl:variable name="revisionDateType"
                  select="'revision'"/>

    <xsl:variable name="creationDateType"
                  select="'creation'"/>


    <xsl:for-each select="dct:created[. != '']">
      <xsl:variable name="creationDate"
                    select="util:convertToISOZuluDateTime(string(current()))"/>
      <xsl:element name="{$creationDateType}DateForResource">
        <xsl:value-of select="$creationDate"/>
      </xsl:element>
      <xsl:element name="{$creationDateType}YearForResource">
        <xsl:value-of select="substring($creationDate, 0, 5)"/>
      </xsl:element>
      <xsl:element name="{$creationDateType}MonthForResource">
        <xsl:value-of select="substring($creationDate, 0, 8)"/>
      </xsl:element>
    </xsl:for-each>

    <xsl:for-each select="dct:modified[. != '']">
      <xsl:variable name="revisionDate"
                    select="util:convertToISOZuluDateTime(string(current()))"/>
      <xsl:element name="{$revisionDateType}DateForResource">
        <xsl:value-of select="$revisionDate"/>
      </xsl:element>
      <xsl:element name="{$revisionDateType}YearForResource">
        <xsl:value-of select="substring($revisionDate, 0, 5)"/>
      </xsl:element>
      <xsl:element name="{$revisionDateType}MonthForResource">
        <xsl:value-of select="substring($revisionDate, 0, 8)"/>
      </xsl:element>
    </xsl:for-each>


    <xsl:for-each select="dc:format[. != '']">
      <format>
        <xsl:value-of select="."/>
      </format>
    </xsl:for-each>

    <xsl:call-template name="index-keywords"/>

    <xsl:apply-templates mode="index" select="*"/>
  </xsl:template>


  <xsl:template name="index-keywords">

    <xsl:variable name="allKeywords">
      <xsl:variable name="keywords"
                    select="dc:subject[. != '']"/>
      <xsl:if test="count($keywords) > 0">
        <thesaurus>
          <info type="theme" field="otherKeywords-theme"/>
          <keywords>
            <xsl:for-each select="$keywords">
              <keyword>
                <values>
                  <default>
                    <xsl:value-of select="."/>
                  </default>
                </values>
              </keyword>
            </xsl:for-each>
          </keywords>
        </thesaurus>
      </xsl:if>

      <xsl:variable name="geoDescription"
                    select="dct:spatial[. != '']"/>
      <xsl:if test="count($geoDescription) > 0">
        <thesaurus>
          <info type="place" field="otherKeywords-place"/>
          <keywords>
            <xsl:for-each select="$geoDescription">
              <keyword>
                <values>
                  <default>
                    <xsl:value-of select="."/>
                  </default>
                </values>
              </keyword>
            </xsl:for-each>
          </keywords>
        </thesaurus>
      </xsl:if>
    </xsl:variable>

    <xsl:call-template name="build-all-keyword-fields">
      <xsl:with-param name="allKeywords" select="$allKeywords"/>
    </xsl:call-template>
  </xsl:template>


  <xsl:template mode="index"
                match="*[string-join(ancestor-or-self::*/local-name(), '/') = $multilingualProperties/@xpath]">
    <xsl:param name="languages" as="node()*"/>

    <xsl:variable name="fieldConfiguration"
                  select="$multilingualProperties[@xpath = string-join(current()/ancestor-or-self::*/local-name(), '/')][1]"/>
    <xsl:copy-of
      select="gn-fn-index:add-multilingual-field($fieldConfiguration/@name, ., $languages)"/>
  </xsl:template>


  <xsl:template mode="index"
                match="(dct:references|dc:relation)[
                              normalize-space(.) != ''
                              and matches(., '.*(.gif|.png|.jpeg|.jpg)$', 'i')]">
    <overview>
      <url>
        <xsl:value-of
          select="current()"/>
      </url>
    </overview>
  </xsl:template>


  <xsl:template mode="index" match="dc:coverage/text()[. != '']">
    <xsl:variable name="coverage" select="."/>

    <xsl:choose>
      <xsl:when test="starts-with(., 'North')">
        <xsl:variable name="nt" select="substring-after($coverage,'North ')"/>
        <xsl:variable name="n" select="substring-before($nt, ',')"/>
        <xsl:variable name="st" select="substring-after($coverage,'South ')"/>
        <xsl:variable name="s" select="substring-before($st, ',')"/>
        <xsl:variable name="et" select="substring-after($coverage,'East ')"/>
        <xsl:variable name="e" select="substring-before($et, ',')"/>
        <xsl:variable name="wt" select="substring-after($coverage,'West ')"/>
        <xsl:variable name="w"
                      select="if (contains($wt, '. ')) then substring-before($wt, '. ') else $wt"/>
        <xsl:variable name="p" select="substring-after($coverage,'(')"/>
        <xsl:variable name="place" select="substring-before($p,')')"/>

        <xsl:choose>
          <xsl:when test="-180 &lt;= number($e) and number($e) &lt;= 180 and
                              -180 &lt;= number($w) and number($w) &lt;= 180 and
                              -90 &lt;= number($s) and number($s) &lt;= 90 and
                              -90 &lt;= number($n) and number($n) &lt;= 90">
            <xsl:choose>
              <xsl:when test="$e = $w and $s = $n">
                <location>
                  <xsl:value-of select="concat($s, ',', $w)"/>
                </location>
              </xsl:when>
              <xsl:when
                test="($e = $w and $s != $n) or ($e != $w and $s = $n)">
                <!-- Probably an invalid bbox indexing a point only -->
                <location>
                  <xsl:value-of select="concat($s, ',', $w)"/>
                </location>
              </xsl:when>
              <xsl:otherwise>
                <geom>
                  <xsl:text>{"type": "Polygon",</xsl:text>
                  <xsl:text>"coordinates": [[</xsl:text>
                  <xsl:value-of select="concat('[', $w, ',', $s, ']')"/>
                  <xsl:text>,</xsl:text>
                  <xsl:value-of select="concat('[', $e, ',', $s, ']')"/>
                  <xsl:text>,</xsl:text>
                  <xsl:value-of select="concat('[', $e, ',', $n, ']')"/>
                  <xsl:text>,</xsl:text>
                  <xsl:value-of select="concat('[', $w, ',', $n, ']')"/>
                  <xsl:text>,</xsl:text>
                  <xsl:value-of select="concat('[', $w, ',', $s, ']')"/>
                  <xsl:text>]]}</xsl:text>
                </geom>

                <location>
                  <xsl:value-of select="concat(
                                              (number($s) + number($n)) div 2,
                                              ',',
                                              (number($w) + number($e)) div 2)"/>
                </location>
              </xsl:otherwise>
            </xsl:choose>
          </xsl:when>
        </xsl:choose>
      </xsl:when>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>
