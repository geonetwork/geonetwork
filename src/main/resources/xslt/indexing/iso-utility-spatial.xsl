<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                version="3.0"
                exclude-result-prefixes="#all">


  <xsl:template mode="index"
                match="*:identificationInfo/*/*:spatialResolution/*/*:equivalentScale/*/*:denominator/*:Integer[. castable as xs:decimal]">
    <resolutionScaleDenominator>
      <xsl:value-of select="."/>
    </resolutionScaleDenominator>
  </xsl:template>


  <xsl:template mode="index"
                match="*:identificationInfo/*/*:spatialResolution/*/*:equivalentScale/*/*:distance/*:Distance[. != '']">
    <resolutionDistance>
      <xsl:value-of select="if (contains(@uom, '#'))
                                    then concat(., ' ', tokenize(@uom, '#')[2])
                                    else  concat(., ' ', @uom)"/>
    </resolutionDistance>
  </xsl:template>


  <xsl:template mode="index"
                match="*:identificationInfo/*/*:extent/*/*:geographicElement/*:EX_GeographicBoundingBox[
                                ./*:westBoundLongitude/*:Decimal castable as xs:decimal and
                                ./*:eastBoundLongitude/*:Decimal castable as xs:decimal and
                                ./*:northBoundLatitude/*:Decimal castable as xs:decimal and
                                ./*:southBoundLatitude/*:Decimal castable as xs:decimal
                                ]">
    <xsl:variable name="format" select="'#0.000000'"></xsl:variable>

    <xsl:variable name="w"
                  select="format-number(./*:westBoundLongitude/*:Decimal/text(), $format)"/>
    <xsl:variable name="e"
                  select="format-number(./*:eastBoundLongitude/*:Decimal/text(), $format)"/>
    <xsl:variable name="n"
                  select="format-number(./*:northBoundLatitude/*:Decimal/text(), $format)"/>
    <xsl:variable name="s"
                  select="format-number(./*:southBoundLatitude/*:Decimal/text(), $format)"/>

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
            <geom>
              <xsl:value-of
                select="concat('{&quot;type&quot;: &quot;Point&quot;, &quot;coordinates&quot;: [', $w, ',', $s, ']}')"/>
            </geom>
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
  </xsl:template>


  <xsl:template mode="index"
                match="*:identificationInfo/*/*:extent/*/*:geographicElement/*:EX_BoundingPolygon">
    <xsl:variable name="geojson" select="''"/>
    <!--select="util:gmlToGeoJson(
                   fn:serialize((gml:*), map{'method':'xml', 'indent': true()}),
                    true(), 5)"/>-->
    <xsl:choose>
      <xsl:when test="matches($geojson, '(Error|Warning):.*')">
        <shapeParsingError>
          <xsl:value-of select="$geojson"/>
        </shapeParsingError>
      </xsl:when>
      <xsl:otherwise>
        <shape type="object">
          <xsl:value-of select="$geojson"/>
        </shape>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>



  <xsl:template mode="index"
                match="*:identificationInfo/*/*:extent/*/*:verticalElement/*">
    <xsl:variable name="min"
                  select="*:minimumValue/*/text()"/>
    <xsl:variable name="max"
                  select="*:maximumValue/*/text()"/>

    <xsl:if test="$min castable as xs:double">
      <resourceVerticalRange>
        <gte><xsl:value-of select="normalize-space($min)"/></gte>
        <xsl:if test="$max castable as xs:double
                              and xs:double($min) &lt; xs:double($max)">
          <lte><xsl:value-of select="normalize-space($max)"/></lte>
          <unit>m</unit>
        </xsl:if>
        </resourceVerticalRange>
    </xsl:if>
  </xsl:template>




  <xsl:template mode="index"
                match="*:referenceSystemInfo/*">
    <xsl:for-each select="*:referenceSystemIdentifier/*">
      <xsl:variable name="crs" select="*:code/*[1]/text()"/>
      <xsl:variable name="crsLabel"
                    select="if (*:description/*[1])
                                then *:description/*[1]/text()
                                else if (*:code/*/@xlink:title)
                                then *:code/*/@xlink:title
                                else $crs"/>
      <xsl:if test="$crs != ''">
        <coordinateSystem>
          <xsl:value-of select="$crs"/>
        </coordinateSystem>
      </xsl:if>

      <crsDetails>
        <code><xsl:value-of select="$crs"/></code>
        <codeSpace><xsl:value-of select="*:codeSpace/*/text()"/></codeSpace>
        <name><xsl:value-of select="$crsLabel"/></name>
        <url><xsl:value-of select="*:code/*/@xlink:href"/></url>
        </crsDetails>
    </xsl:for-each>
  </xsl:template>

</xsl:stylesheet>
