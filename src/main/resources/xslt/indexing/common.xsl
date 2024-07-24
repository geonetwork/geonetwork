<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:err="http://www.w3.org/2005/xqt-errors"
                version="3.0"
                exclude-result-prefixes="#all">

  <xsl:include href="constant.xsl"/>
  <xsl:include href="utility.xsl"/>

  <xsl:template match="/">
    <indexRecords>
      <xsl:apply-templates mode="index" select="*"/>
    </indexRecords>
  </xsl:template>

  <xsl:template mode="index"
                match="indexRecord">
    <xsl:copy>
      <xsl:copy-of select="*[name() != 'document']"/>

      <indexingDate>
        <xsl:value-of select="format-dateTime(current-dateTime(), $dateTimeFormat)"/>
      </indexingDate>

      <xsl:try>
        <xsl:variable name="xml"
                      select="parse-xml(document)"/>
        <xsl:apply-templates mode="index"
                             select="$xml"/>
        <xsl:catch>
          <!-- TODO: send to logger -->
          <xsl:message>XSLT error
            <xsl:value-of select="$err:description"/>
          </xsl:message>
          <indexingErrorMsg>
            <string>indexingErrorMsg-xslt-error</string>
            <type>error</type>
            <values>
              <code>
                <xsl:value-of select="$err:code"/>
              </code>
              <description>
                <xsl:value-of select="$err:description"/>
              </description>
            </values>
          </indexingErrorMsg>
        </xsl:catch>
      </xsl:try>
    </xsl:copy>
  </xsl:template>


  <xsl:template name="has-xlinks">
    <hasxlinks><xsl:value-of select="count(.//*[starts-with(@xlink:href, 'local://')]) > 0"/></hasxlinks>
  </xsl:template>

</xsl:stylesheet>
