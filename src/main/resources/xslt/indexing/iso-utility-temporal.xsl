<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:gn-fn-index="https://www.geonetwork-opensource.org/xslt/functions/index"
                xmlns:util="https://geonetwork-opensource.org/xsl-extension"
                version="3.0"
                exclude-result-prefixes="#all">

  <!-- If identification creation, publication and revision date
    should be indexed as a temporal extent information (eg. in INSPIRE
    metadata implementing rules, those elements are defined as part
    of the description of the temporal extent). -->
  <xsl:variable name="useDateAsTemporalExtent" select="true()"/>


  <xsl:template mode="index"
                match="*:identificationInfo/*/*:citation/*/*:date/*[
                                              *:date/*/text() != ''
                                              and gn-fn-index:is-isoDate(*:date/*/text())]">

    <xsl:variable name="dateType"
                  select="*:dateType/*/@codeListValue"
                  as="xs:string?"/>
    <xsl:variable name="date"
                  select="string(*:date/*:Date|*:date/*:DateTime)"/>

    <xsl:variable name="zuluDateTime" as="xs:string?">
      <xsl:value-of select="util:convertToISOZuluDateTime(normalize-space($date))"/>
    </xsl:variable>

    <xsl:choose>
      <xsl:when test="$zuluDateTime != ''">
        <resourceDate>
          <type>
            <xsl:value-of select="$dateType"/>
          </type>
          <date>
            <xsl:value-of select="$zuluDateTime"/>
          </date>
        </resourceDate>

        <xsl:element name="{$dateType}DateForResource">
          <xsl:value-of select="$zuluDateTime"/>
        </xsl:element>
        <xsl:element name="{$dateType}YearForResource">
          <xsl:value-of select="substring($zuluDateTime, 0, 5)"/>
        </xsl:element>
        <xsl:element name="{$dateType}MonthForResource">
          <xsl:value-of select="substring($zuluDateTime, 0, 8)"/>
        </xsl:element>


        <xsl:if test="$useDateAsTemporalExtent">
          <resourceTemporalDateRange>
            <gte>
              <xsl:value-of select="$zuluDateTime"/>
            </gte>
            <lte>
              <xsl:value-of select="$zuluDateTime"/>
            </lte>
          </resourceTemporalDateRange>
        </xsl:if>
      </xsl:when>
      <xsl:otherwise>
        <indexingErrorMsg>
          <string>indexingErrorMsg-invalidDateFormat</string>
          <type>warning</type>
          <values>
            <dateType>
              <xsl:value-of select="$dateType"/>
            </dateType>
            <date>
              <xsl:value-of select="$date"/>
            </date>
          </values>
        </indexingErrorMsg>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>


  <xsl:template mode="index"
                match="*:identificationInfo/*/*:extent/*/*:temporalElement/*/*:extent/*:TimePeriod">
    <xsl:variable name="start"
                  select="*:beginPosition|*:begin/*:TimeInstant/*:timePosition"/>
    <xsl:variable name="end"
                  select="*:endPosition|*:end/*:TimeInstant/*:timePosition"/>


    <xsl:variable name="zuluStartDate"
                  select="if (gn-fn-index:is-isoDate($start)) then util:convertToISOZuluDateTime($start) else ''"/>
    <xsl:variable name="zuluEndDate"
                  select="if (gn-fn-index:is-isoDate($end)) then util:convertToISOZuluDateTime($end) else ''"/>

    <xsl:choose>
      <xsl:when test="$zuluStartDate != ''
                                and ($zuluEndDate != '' or $end/@indeterminatePosition = 'now')">
        <resourceTemporalDateRange>
          <gte>
            <xsl:value-of select="$zuluStartDate"/>
          </gte>
          <xsl:if test="$start &lt; $end and not($end/@indeterminatePosition = 'now')">
            <lte>
              <xsl:value-of select="$zuluEndDate"/>
            </lte>
          </xsl:if>
        </resourceTemporalDateRange>
        <resourceTemporalExtentDateRange>
          <gte>
            <xsl:value-of select="$zuluStartDate"/>
          </gte>
          <xsl:if test="$start &lt; $end and not($end/@indeterminatePosition = 'now')">
            <lte>
              <xsl:value-of select="$zuluEndDate"/>
            </lte>
          </xsl:if>
        </resourceTemporalExtentDateRange>
      </xsl:when>
      <xsl:otherwise>
        <indexingErrorMsg>
          <string>indexingErrorMsg-invalidBounds</string>
          <type>warning</type>
          <values/>
        </indexingErrorMsg>
      </xsl:otherwise>
    </xsl:choose>

    <xsl:if test="$zuluStartDate != ''
                          and $zuluEndDate != ''
                          and $start &gt; $end">
      <indexingErrorMsg>
        <string>indexingErrorMsg-temporalRangeLowerGreaterThanUpper</string>
        <type>warning</type>
        <values>
          <lowerBound>
            <xsl:value-of select="$start"/>
          </lowerBound>
          <upperBound><xsl:value-of select="$end"/>"
          </upperBound>
        </values>
      </indexingErrorMsg>
    </xsl:if>


    <xsl:call-template name="build-range-details">
      <xsl:with-param name="start" select="$start"/>
      <xsl:with-param name="end" select="$end"/>
    </xsl:call-template>
  </xsl:template>

</xsl:stylesheet>
