<!--

    SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
    SPDX-License-Identifier: GPL-2.0-or-later

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:util="https://geonetwork-opensource.org/xsl-extension"
                version="3.0"
                exclude-result-prefixes="#all">

  <!-- Template to build the following index fields for the metadata keywords:
          - tag: contains all the keywords.
          - tagNumber: total number of keywords.
          - isOpenData: checks if any keyword is defined in openDataKeywords to flag it as open data.
          - keywordType-{TYPE}: Index field per keyword type (examples: keywordType-theme, keywordType-place).
          - th_{THESAURUSID}: Field with keywords of a thesaurus, eg. th_regions
          - th_{THESAURUSID}Number: Field with keywords of a thesaurus, eg. th_regionsNumber
          - allKeywords: Object field with all thesaurus and all keywords.
          - {THESAURUSID}_tree: Object with keywords tree per thesaurus.
  -->
  <xsl:template name="build-all-keyword-fields" as="node()*">
    <xsl:param name="allKeywords" as="node()?"/>

    <!-- Build global tag field -->
    <xsl:variable name="keywordList"
                  select="$allKeywords//keyword[values/*]"
                  as="node()*"/>
    <xsl:for-each select="$keywordList">
      <tag>
        <xsl:copy-of select="values/*"/>
        <xsl:if test="@uri != ''">
          <key>
            <xsl:value-of select="@uri"/>
          </key>
        </xsl:if>
      </tag>
    </xsl:for-each>

    <!-- Total number of keywords -->
    <tagNumber>
      <xsl:value-of select="count($keywordList)"/>
    </tagNumber>

    <!-- Checks if any keyword is defined in openDataKeywords to flag it as open data -->
    <isOpenData>
      <xsl:value-of select="count(
                        $allKeywords//keyword/values/value[matches(
                          normalize-unicode(
                            replace(
                              normalize-unicode(
                                lower-case(normalize-space(text())),
                                'NFKD'),
                            '\p{Mn}', ''),
                          'NFKC'),
                        $openDataKeywords)]) > 0"/>
    </isOpenData>


    <!-- Build index field for type
    keywordType-place: [{default: France}]-->
    <xsl:for-each-group select="$allKeywords"
                        group-by="thesaurus/info/@type">
      <xsl:if test="matches(current-grouping-key(), '^[A-Za-z\-_]+$')">
        <xsl:element name="keywordType-{current-grouping-key()}">
          <xsl:for-each select="$allKeywords/thesaurus[info/@type = current-grouping-key()]/keywords/keyword">
            <keywords>
              <xsl:copy-of select="values/*"/>
              <xsl:if test="@uri != ''">
                <key>
                  <xsl:value-of select="@uri"/>
                </key>
              </xsl:if>
            </keywords>
          </xsl:for-each>
        </xsl:element>
      </xsl:if>
    </xsl:for-each-group>

    <!-- Fields with keywords and keyword count of a thesaurus, eg. th_regions, th_regionsNumber -->
    <xsl:for-each select="$allKeywords/thesaurus[info/@field]">
      <!-- Keyword count of a thesaurus -->
      <xsl:element name="{info/@field}Number">
        <xsl:value-of select="count(keywords/keyword)"/>
      </xsl:element>

      <!-- Keywords of a thesaurus -->
      <xsl:element name="{info/@field}">
        <xsl:for-each select="keywords/keyword">
          <keywords>
            <xsl:copy-of select="values/*"/>
            <xsl:if test="@uri != ''">
              <key>
                <xsl:value-of select="@uri"/>
              </key>
            </xsl:if>
          </keywords>
        </xsl:for-each>
      </xsl:element>
    </xsl:for-each>

    <allKeywords>
      <xsl:for-each select="$allKeywords/thesaurus[info/@field]">
        <xsl:element name="{if (info/@field != '') then info/@field else 'otherKeywords'}">
          <xsl:if test="info/@id != ''">
            <id>
              <xsl:value-of select="info/@id"/>
            </id>
          </xsl:if>
          <xsl:choose>
            <xsl:when test="exists(info/multilingualTitle)">
              <multilingualTitle>
                <xsl:copy-of select="info/multilingualTitle/*"/>
              </multilingualTitle>
            </xsl:when>
            <xsl:otherwise>
              <title>
                <xsl:value-of select="info/@title"/>
              </title>
            </xsl:otherwise>
          </xsl:choose>
          <theme>
            <xsl:value-of select="info/@type"/>
          </theme>
          <xsl:if test="info/@uri != ''">
            <link>
              <xsl:value-of select="info/@uri"/>
            </link>
          </xsl:if>
          <xsl:for-each select="keywords/keyword">
            <keywords>
              <xsl:copy-of select="values/*"/>
              <xsl:if test="@uri != ''">
                <link>
                  <xsl:value-of select="@uri"/>
                </link>
              </xsl:if>
            </keywords>
          </xsl:for-each>
        </xsl:element>
      </xsl:for-each>
    </allKeywords>

    <!-- TODO Object with keywords tree per thesaurus
    <xsl:for-each select="$allKeywords/thesaurus[keywords/keyword/tree/*/value]">
      <xsl:element name="{info/@field}_tree">
        <xsl:variable name="defaults"
                      select="distinct-values(keywords/keyword/tree/defaults/value)"/>
        <xsl:variable name="keys"
                      select="distinct-values(keywords/keyword/tree/keys/value)"/>

        <xsl:if test="count($defaults) > 0">"default": [
          <xsl:for-each select="$defaults">
            <xsl:sort select="."/>
            <xsl:value-of select="concat($doubleQuote, util:escapeForJson(.), $doubleQuote)"/><xsl:if test="position() != last()">,</xsl:if>
          </xsl:for-each>
          ]<xsl:if test="count($keys) > 0">,</xsl:if>
        </xsl:if>
        <xsl:if test="count($keys) > 0">"key": [
          <xsl:for-each select="$keys">
            <xsl:sort select="."/>
            <xsl:value-of select="concat($doubleQuote, util:escapeForJson(.), $doubleQuote)"/><xsl:if test="position() != last()">,</xsl:if>
          </xsl:for-each>
          ]
        </xsl:if>
        }</xsl:element>
    </xsl:for-each>-->

    <xsl:copy-of select="$allKeywords//indexingErrorMsg"/>
  </xsl:template>
</xsl:stylesheet>
