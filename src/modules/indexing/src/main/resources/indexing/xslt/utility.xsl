<!--

    SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
    SPDX-License-Identifier: GPL-2.0-or-later

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:gn-fn-index="https://www.geonetwork-opensource.org/xslt/functions/index"
                xmlns:util="https://geonetwork-opensource.org/xsl-extension"
                version="3.0"
                exclude-result-prefixes="#all">

  <!-- Convert to ASCII,
       Replace . by -,
       Keep only letters, numbers and _ and -. -->
  <xsl:function name="gn-fn-index:build-field-name">
    <xsl:param name="value"/>

    <xsl:value-of select="replace(
                            replace(
                              replace(
                                normalize-unicode($value, 'NFKD'),
                                '\P{IsBasicLatin}', '')
                              , '\.', '-'),
                            '[^a-zA-Z0-9_-]', '')"/>
  </xsl:function>


  <xsl:function name="gn-fn-index:build-codelist" as="node()*">
    <xsl:param name="schema" as="xs:string"/>
    <xsl:param name="fieldName" as="xs:string"/>
    <xsl:param name="codelistName" as="xs:string"/>
    <xsl:param name="value" as="xs:string"/>
    <xsl:param name="languages" as="node()*"/>
    <xsl:param name="url" as="xs:string?"/>
    <xsl:param name="text" as="xs:string?"/>

    <xsl:element name="{$fieldName}">
      <key>
        <xsl:value-of select="$value"/>
      </key>
      <xsl:for-each select="$languages">
        <xsl:variable name="translation"
                      select="util:getCodelistTranslation($schema, $codelistName, $value, @code)"/>

        <xsl:if test="@default">
          <default>
            <xsl:value-of select="$translation"/>
          </default>
        </xsl:if>
        <xsl:element name="{concat('lang', @code)}">
          <xsl:value-of select="$translation"/>
        </xsl:element>
      </xsl:for-each>
      <link>
        <xsl:value-of select="$url"/>
      </link>
      <xsl:if test="$text != ''">
        <text>
          <xsl:value-of select="normalize-space($text)"/>
        </text>
      </xsl:if>
    </xsl:element>
  </xsl:function>

  <xsl:function name="gn-fn-index:build-record-link" as="node()*">
    <xsl:param name="uuid" as="xs:string"/>
    <xsl:param name="url" as="xs:string?"/>
    <xsl:param name="title" as="xs:string?"/>
    <xsl:param name="type" as="xs:string"/>
    <xsl:variable name="properties" as="node()">
      <properties/>
    </xsl:variable>
    <xsl:copy-of select="gn-fn-index:build-record-link($uuid, $url, $title, $type, $properties)"/>
  </xsl:function>

  <xsl:function name="gn-fn-index:build-record-link" as="node()*">
    <xsl:param name="uuid" as="xs:string"/>
    <xsl:param name="url" as="xs:string?"/>
    <xsl:param name="title" as="xs:string?"/>
    <xsl:param name="type" as="xs:string"/>
    <xsl:param name="otherProperties" as="node()?"/>

    <xsl:variable name="siteUrl" select="''"/>
    <!-- TODO: util:getSiteUrl()-->

    <xsl:variable name="origin"
                  select="if ($url = '')
                          then 'catalog'
                          else if ($url != '' and
                                   not(starts-with($url, $siteUrl)))
                            then 'remote'
                          else 'catalog'"/>

    <xsl:variable name="recordTitle"
                  select="if ($title != '' ) then $title
                          else ''"/>
    <!--
    TODO ? This suppose that the related record is already indexed.
    util:getIndexField(
                                '',
                                $uuid,
                                'resourceTitleObject',
                                '')"
    -->
    <recordLink>
      <type>
        <xsl:value-of select="normalize-space($type)"/>
      </type>
      <xsl:for-each select="$otherProperties//p[@name != '']">
        <xsl:element name="{@name}">
          <xsl:value-of select="@value"/>
        </xsl:element>
      </xsl:for-each>
      <to>
        <xsl:value-of select="normalize-space($uuid)"/>
      </to>
      <url>
        <xsl:value-of select="normalize-space($url)"/>
      </url>
      <title>
        <xsl:value-of select="normalize-space($recordTitle)"/>
      </title>
      <origin>
        <xsl:value-of select="normalize-space($origin)"/>
      </origin>
    </recordLink>

    <xsl:variable name="fieldName"
                  select="concat('recordLink_', $type, string-join($otherProperties//p[@name != '']/concat(@name, @value), '_'))"/>

    <xsl:element name="{$fieldName}_uuid">
      <xsl:value-of select="normalize-space($uuid)"/>
    </xsl:element>

    <xsl:if test="$recordTitle != ''">
      <xsl:element name="{$fieldName}">
        <xsl:value-of select="$recordTitle"/>
      </xsl:element>
    </xsl:if>

    <xsl:if test="$recordTitle != ''">
      <xsl:element name="{$fieldName}_url">
        <xsl:value-of select="normalize-space($url)"/>
      </xsl:element>
    </xsl:if>
  </xsl:function>


  <!-- A date, dateTime, Year or Year and Month
  Valid with regards to index date supported types:
  date_optional_time||yyyy-MM-dd||yyyy-MM||yyyy||epoch_millis
  -->
  <xsl:function name="gn-fn-index:is-isoDate" as="xs:boolean">
    <xsl:param name="value" as="xs:string?"/>
    <xsl:sequence select="if ($value castable as xs:date
                          or $value castable as xs:dateTime
                          or matches($value, '^[0-9]{4}$|^[0-9]{4}-(0[1-9]|1[012])$'))
                          then true() else false()"/>
  </xsl:function>

  <!-- 2020-12-12 -->
  <xsl:function name="gn-fn-index:is-date" as="xs:boolean">
    <xsl:param name="value" as="xs:string?"/>
    <xsl:sequence select="if ($value castable as xs:date)
                          then true() else false()"/>
  </xsl:function>

  <!-- Produce a thesaurus field name valid in an XML document
  and as an Elasticsearch field name. -->
  <xsl:function name="gn-fn-index:build-thesaurus-index-field-name">
    <xsl:param name="thesaurusId" as="xs:string?"/>
    <xsl:param name="thesaurusName" as="xs:string?"/>

    <xsl:variable name="oldFieldNameMapping" as="node()*">
      <!-- INSPIRE themes are loaded from INSPIRE registry. The thesaurus key changed. -->
      <thesaurus old="th_inspire-theme"
                 new="th_httpinspireeceuropaeutheme-theme"/>
      <thesaurus old="th_SpatialScope"
                 new="th_httpinspireeceuropaeumetadatacodelistSpatialScope-SpatialScope"/>
    </xsl:variable>

    <xsl:variable name="key">
      <xsl:choose>
        <xsl:when test="starts-with($thesaurusId, 'geonetwork.thesaurus')">
          <!-- eg. geonetwork.thesaurus.local.theme.dcsmm.area = dcsmm.area-->
          <xsl:value-of select="string-join(
                                  tokenize($thesaurusId, '\.')[position() > 4], '.')"/>
        </xsl:when>
        <xsl:when test="normalize-space($thesaurusId) != ''">
          <xsl:value-of select="normalize-space($thesaurusId)"/>
        </xsl:when>
        <xsl:when test="normalize-space($thesaurusName) != ''">
          <xsl:value-of select="replace(normalize-unicode($thesaurusName, 'NFKD'), '\P{IsBasicLatin}', '')"/>
        </xsl:when>
      </xsl:choose>
    </xsl:variable>

    <xsl:variable name="keyWithoutDot"
                  select="replace($key, '\.', '-')"/>

    <xsl:variable name="fieldName"
                  select="concat('th_', replace($keyWithoutDot, '[^a-zA-Z0-9_-]', ''))"/>

    <xsl:value-of select="if($oldFieldNameMapping[@old = $fieldName])
                          then $oldFieldNameMapping[@old = $fieldName]/@new
                          else $fieldName"/>
  </xsl:function>


  <xsl:template name="build-range-details">
    <xsl:param name="start" as="node()?"/>
    <xsl:param name="end" as="node()?"/>

    <xsl:variable name="rangeStartDetails">
      <xsl:if test="$start/text() castable as xs:date
                    or $start/text() castable as xs:dateTime
                    or $start/text() castable as xs:gYearMonth
                    or $start/text() castable as xs:gYear">
        <value>
          <date>
            <xsl:value-of select="$start/text()"/>
          </date>
        </value>
      </xsl:if>
      <xsl:for-each select="$start/@*[. != '']">
        <value>
          <xsl:element name="{name()}">
            <xsl:value-of select="."/>
          </xsl:element>
        </value>
      </xsl:for-each>
    </xsl:variable>
    <xsl:variable name="rangeEndDetails">
      <xsl:if test="$end/text() castable as xs:date
                    or $end/text() castable as xs:dateTime
                    or $end/text() castable as xs:gYearMonth
                    or $end/text() castable as xs:gYear">
        <value>
          <date>
            <xsl:value-of select="$end/text()"/>
          </date>
        </value>
      </xsl:if>
      <xsl:for-each select="$end/@*[. != '']">
        <value>
          <xsl:element name="{name()}">
            <xsl:value-of select="."/>
          </xsl:element>
        </value>
      </xsl:for-each>
    </xsl:variable>

    <xsl:if test="count($rangeStartDetails/value) > 0 or count($rangeEndDetails/value) > 0">
      <resourceTemporalExtentDetails>
        <start>
          <xsl:copy-of select="$rangeStartDetails/value/*"/>
        </start>
        <end>
          <xsl:copy-of select="$rangeEndDetails/value/*"/>
        </end>
      </resourceTemporalExtentDetails>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
