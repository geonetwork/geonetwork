<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:gn-fn-index="https://www.geonetwork-opensource.org/xslt/functions/index"
                version="3.0"
                exclude-result-prefixes="#all">

  <xsl:template mode="index"
                match="*:dataQualityInfo/*/*:report/*">

    <xsl:variable name="title" select="(*:result/*/*:specification/*/*:title/(*:CharacterString|*:Anchor))[1]"/>
    <xsl:variable name="pass"
                  select="*:result/*/*:pass/*:Boolean"/>

    <xsl:if test="string($title) and *:result/*/*:pass">
      <specificationConformance>
        <title>
          <xsl:value-of select="$title"/>
        </title>
        <xsl:if test="gn-fn-index:is-isoDate((*:result/*/*:specification/*/*:date/*/*:date/*:Date)[1])">
          <date>
            <xsl:value-of select="(*:result/*/*:specification/*/*:date/*/*:date/*:Date)[1]"/>
          </date>
        </xsl:if>
        <xsl:if test="*:result/*/*:specification/*/*:title/*/@xlink:href">
          <link>
            <xsl:value-of select="*:result/*/*:specification/*/*:title/*/@xlink:href"/>
          </link>
        </xsl:if>
        <xsl:if test="*:result/*/*:explanation/*/text() != ''">
          <explanation>
            <xsl:value-of select="(*:result/*/*:explanation/*/text())[1]"/>
          </explanation>
        </xsl:if>
        <pass>
          <xsl:value-of select="$pass"/>
        </pass>
      </specificationConformance>

      <xsl:element name="conformTo_{gn-fn-index:build-field-name($title)}">
        <xsl:value-of select="$pass"/>
      </xsl:element>
    </xsl:if>


    <xsl:for-each select=".[
                normalize-space(*:measure/*/*:nameOfMeasure/*:CharacterString) != ''
                or normalize-space(*:measure/*/*:measureDescription/*:CharacterString) != ''
                ]/*:result/(*:DQ_QuantitativeResult|*:DQ_DescriptiveResult)">

      <xsl:variable name="name"
                    select="(../../*:measure/*/*:nameOfMeasure/*:CharacterString)[1]"/>
      <xsl:variable name="value"
                    select="*:value/*:Record[. != '']|*:statement/*:CharacterString[. != '']"/>
      <xsl:variable name="unit"
                    select="*:valueUnit//*:identifier"/>
      <xsl:variable name="description"
                    select="(../../*:measure/*/*:measureDescription/*:CharacterString)[1]"/>

      <xsl:variable name="measureDate"
                    select="*:dateTime/*:DateTime"/>

      <measure>
        <name><xsl:value-of select="$name"/></name>
        <xsl:if test="$description != ''">
          <description><xsl:value-of select="$description"/></description>
        </xsl:if>
        <xsl:if test="$measureDate != ''">
          <date><xsl:value-of select="$measureDate"/></date>
        </xsl:if>
        <value><xsl:value-of select="$value[1]"/></value>
        <xsl:if test="$unit != ''">
          <unit><xsl:value-of select="$unit"/></unit>
        </xsl:if>
        <type><xsl:value-of select="local-name(.)"/></type>
      </measure>

      <xsl:for-each select="$value">
        <xsl:element name="measure_{gn-fn-index:build-field-name($name)}">
          <xsl:value-of select="."/>
        </xsl:element>
      </xsl:for-each>
    </xsl:for-each>
  </xsl:template>


  <xsl:template mode="index"
                match="*:processStep/*[*:description/*:CharacterString != '']">
    <xsl:param name="languages" as="node()*"/>

    <processSteps>
      <descriptionObject>
        <xsl:copy-of select="gn-fn-index:add-multilingual-field(
                                'description', *:description, $languages)/*"/>
      </descriptionObject>
      <xsl:if test="normalize-space(*:stepDateTime) != ''">
        <date>
          <xsl:value-of select="*:stepDateTime//*:timePosition/text()"/>
        </date>
      </xsl:if>
      <xsl:if test="normalize-space(*:source) != ''">
        <xsl:for-each select="*:source/*[*:description/*:CharacterString != '']">
          <source>
            <descriptionObject>
              <xsl:copy-of
                select="gn-fn-index:add-multilingual-field(
                                            'description', *:description, $languages)/*"/>
            </descriptionObject>
          </source>
        </xsl:for-each>
      </xsl:if>

      <!-- TODO: ISO19139 -->
      <xsl:variable name="processor"
                    select="*:processor/*[.//*:CI_Organisation/*:name != '' or *:organisationName/*:CharacterString != '']"/>
      <xsl:if test="count($processor) > 0">
        <xsl:for-each select="$processor">
          <xsl:variable name="individualName"
                        select="(.//*:CI_Individual/*:name/*:CharacterString/text()|*:individualName/*:CharacterString/text())[1]"/>
          <processor>

            <organisationObject>
              <xsl:copy-of
                select="gn-fn-index:add-multilingual-field(
                                            'description', (.//*:CI_Organisation/*:name|*:organisationName),
                                             $languages)/*"/>
            </organisationObject>
            <xsl:if test="$individualName != ''">
              <individual>
                <xsl:value-of select="$individualName"/>
              </individual>
            </xsl:if>
          </processor>
        </xsl:for-each>
      </xsl:if>
    </processSteps>
  </xsl:template>


  <xsl:template mode="index"
                match="*:resourceMaintenance/*">
    <xsl:param name="languages" as="node()*"/>
    <maintenance>
      <frequency>
        <xsl:value-of select="*:maintenanceAndUpdateFrequency/*/@codeListValue"/>
      </frequency>
      <xsl:for-each select="*:dateOfNextUpdate[*/text() != '']">
        <nextUpdateDate><xsl:value-of select="*/text()"/></nextUpdateDate>
      </xsl:for-each>
      <xsl:for-each select="(*:userDefinedMaintenanceFrequency[*/text() != ''])[1]">
        <userDefinedFrequency><xsl:value-of select="*/text()"/></userDefinedFrequency>
      </xsl:for-each>
      <xsl:for-each select="(*:maintenanceNote[*/text() != ''])[1]">
        <noteObject>
          <xsl:copy-of
            select="gn-fn-index:add-multilingual-field(
                                          'maintenanceNote', .,
                                           $languages)/*"/>
        </noteObject>
      </xsl:for-each>
      </maintenance>
  </xsl:template>
</xsl:stylesheet>
