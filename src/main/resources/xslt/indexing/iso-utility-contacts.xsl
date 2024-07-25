<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:gn-fn-index="https://www.geonetwork-opensource.org/xslt/functions/index"
                version="3.0"
                exclude-result-prefixes="#all">

  <xsl:template name="index-contact">
    <xsl:param name="languages" as="node()*"/>

    <xsl:for-each select="*:contact">
      <xsl:apply-templates mode="index-contact" select=".">
        <xsl:with-param name="fieldSuffix" select="''"/>
        <xsl:with-param name="languages" select="$languages"/>
      </xsl:apply-templates>
    </xsl:for-each>
    <xsl:for-each select=".//*:pointOfContact|.//*:producer">
      <xsl:apply-templates mode="index-contact" select=".">
        <xsl:with-param name="fieldSuffix" select="'ForResource'"/>
        <xsl:with-param name="languages" select="$languages"/>
      </xsl:apply-templates>
    </xsl:for-each>
    <xsl:for-each select=".//*:processor">
      <xsl:apply-templates mode="index-contact" select=".">
        <xsl:with-param name="fieldSuffix" select="'ForProcessing'"/>
        <xsl:with-param name="languages" select="$languages"/>
      </xsl:apply-templates>
    </xsl:for-each>

    <xsl:for-each select=".//*:distributor/*:MD_Distributor[*:distributorContact]">
      <xsl:apply-templates mode="index-contact" select=".">
        <xsl:with-param name="fieldSuffix" select="'ForDistribution'"/>
        <xsl:with-param name="languages" select="$languages"/>
      </xsl:apply-templates>
    </xsl:for-each>
  </xsl:template>


  <!-- TODO: here is a limited vision of 1 org with first individual.
  ISO19115-3 allows more combinations. -->
  <xsl:template mode="index-contact" match="*[*:CI_Responsibility]|*[*:CI_ResponsibleParty]">
    <xsl:param name="fieldSuffix" select="''" as="xs:string"/>
    <xsl:param name="languages" as="node()*"/>

    <xsl:variable name="role"
                  select="replace(*[1]/*:role/*/@codeListValue, ' ', '')"
                  as="xs:string?"/>

    <xsl:variable name="organisationName"
                  select="(.//*:CI_Organisation/*:name)[1]"
                  as="node()?"/>
    <xsl:variable name="uuid" select="@uuid"/>
    <xsl:variable name="elementName" select="name()"/>

    <xsl:variable name="logo" select="(.//*:logo/*/*:fileName/*)[1]"/>
    <xsl:variable name="website" select="(.//*:onlineResource/*/*:linkage/*/text())[1]"/>
    <xsl:variable name="email"
                  select="(.//*:contactInfo/*/*:address/*/*:electronicMailAddress/*:CharacterString
                              |*[1]/*:contactInfo/*/*:address/*/*:electronicMailAddress/*:CharacterString)[1]"/>
    <xsl:variable name="phone"
                  select="(./*:contactInfo/*/*:phone/*/*:number[normalize-space(.) != '']/*/text()
                              |*[1]/*:contactInfo/*/*:phone/*/*:voice[normalize-space(.) != '']/*/text())[1]"/>
    <xsl:variable name="individualName"
                  select="(.//*:CI_Individual/*:name/*:CharacterString/text()
                              |*[1]/*:individualName/*:CharacterString/text())[1]"/>
    <xsl:variable name="positionName"
                  select="(.//*:positionName/*:CharacterString/text())[1]"/>
    <xsl:variable name="address" select="string-join(.//*:contactInfo/*/*:address/*/(
                                        *:deliveryPoint|*:postalCode|*:city|
                                        *:administrativeArea|*:country)/*:CharacterString/text(), ', ')"/>

    <xsl:variable name="roleField"
                  select="concat(replace($role, '[^a-zA-Z0-9-]', ''), 'Org', $fieldSuffix)"/>
    <xsl:variable name="orgField"
                  select="concat('Org', $fieldSuffix)"/>

    <xsl:if test="normalize-space($organisationName) != ''">
      <!-- TODO: Does not work for element not siblings eg. mrl:processor -->
      <xsl:if test="count(preceding-sibling::*[name() = $elementName
                        and .//*:CI_Organisation/*:name/*:CharacterString = $organisationName/*:CharacterString]) = 0">
        <xsl:copy-of select="gn-fn-index:add-multilingual-field(
                              $orgField, $organisationName, $languages)"/>
      </xsl:if>

      <xsl:if test="count(preceding-sibling::*[name() = $elementName
                      and .//*:CI_Organisation/*:name/*:CharacterString = $organisationName/*:CharacterString
                      and .//*:role/*/@codeListValue = $role]) = 0">
        <xsl:copy-of select="gn-fn-index:add-multilingual-field(
                              $roleField, $organisationName, $languages)"/>
      </xsl:if>
    </xsl:if>

    <xsl:variable name="identifiers"
                  select=".//*:partyIdentifier/*"/>

    <xsl:element name="contact{$fieldSuffix}">
      <xsl:if test="$organisationName">
        <organisationObject>
          <xsl:copy-of select="gn-fn-index:add-multilingual-field(
                                'organisation', $organisationName, $languages)/*"/>
        </organisationObject>
      </xsl:if>
      <role>
        <xsl:value-of select="$role"/>
      </role>
      <email>
        <xsl:value-of select="$email"/>
      </email>
      <website>
        <xsl:value-of select="$website"/>
      </website>
      <logo>
        <xsl:value-of select="$logo"/>
      </logo>
      <individual>
        <xsl:value-of select="$individualName"/>
      </individual>
      <position>
        <xsl:value-of select="$positionName"/>
      </position>
      <phone>
        <xsl:value-of select="$phone"/>
      </phone>
      <address>
        <xsl:value-of select="$address"/>
      </address>
      <xsl:if test="@*:nilReason">
        <nilReason>
          <xsl:value-of select="@*:nilReason"/>
        </nilReason>
      </xsl:if>
      <xsl:if test="count($identifiers) > 0">
        <xsl:for-each select="$identifiers">
          <identifiers>
            <code>
              <xsl:value-of select="*:code/(*:CharacterString|*:Anchor)"/>
            </code>
            <codeSpace>
              <xsl:value-of select="(*:codeSpace/(*:CharacterString|*:Anchor))[1]/normalize-space()"/>
            </codeSpace>
            <link>
              <xsl:value-of select="*:code/*:Anchor/@xlink:href"/>
            </link>
          </identifiers>
        </xsl:for-each>
      </xsl:if>
    </xsl:element>
  </xsl:template>

  <xsl:template mode="index-contact" match="*"/>

</xsl:stylesheet>
