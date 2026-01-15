<?xml version="1.0" encoding="UTF-8"?>
<!--

    SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
    SPDX-License-Identifier: GPL-2.0-or-later

-->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:cit="http://standards.iso.org/iso/19115/-3/cit/2.0"
                xmlns:dqm="http://standards.iso.org/iso/19157/-2/dqm/1.0"
                xmlns:gco="http://standards.iso.org/iso/19115/-3/gco/1.0"
                xmlns:cat="http://standards.iso.org/iso/19115/-3/cat/1.0"
                xmlns:lan="http://standards.iso.org/iso/19115/-3/lan/1.0"
                xmlns:mcc="http://standards.iso.org/iso/19115/-3/mcc/1.0"
                xmlns:mrc="http://standards.iso.org/iso/19115/-3/mrc/2.0"
                xmlns:mco="http://standards.iso.org/iso/19115/-3/mco/1.0"
                xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
                xmlns:reg="http://standards.iso.org/iso/19115/-3/reg/1.0"
                xmlns:mri="http://standards.iso.org/iso/19115/-3/mri/1.0"
                xmlns:mrs="http://standards.iso.org/iso/19115/-3/mrs/1.0"
                xmlns:mrl="http://standards.iso.org/iso/19115/-3/mrl/2.0"
                xmlns:mex="http://standards.iso.org/iso/19115/-3/mex/1.0"
                xmlns:msr="http://standards.iso.org/iso/19115/-3/msr/2.0"
                xmlns:mrd="http://standards.iso.org/iso/19115/-3/mrd/1.0"
                xmlns:mdq="http://standards.iso.org/iso/19157/-2/mdq/1.0"
                xmlns:srv="http://standards.iso.org/iso/19115/-3/srv/2.0"
                xmlns:gcx="http://standards.iso.org/iso/19115/-3/gcx/1.0"
                xmlns:gex="http://standards.iso.org/iso/19115/-3/gex/1.0"
                xmlns:gml="http://www.opengis.net/gml/3.2"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
                xmlns:owl="http://www.w3.org/2002/07/owl#"
                xmlns:dct="http://purl.org/dc/terms/"
                xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                exclude-result-prefixes="#all">

  <xsl:variable name="euLicenses"
                select="document('vocabularies/licences-skos.rdf')"/>

  <xsl:variable name="isMappingResourceConstraintsToEuVocabulary"
                as="xs:boolean"
                select="true()"/>

  <xsl:variable name="dcatApAccessTypes" as="node()*">
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">unrestricted</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">licenceUnrestricted</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/RESTRICTED">restricted</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">private</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/CONFIDENTIAL">confidential</entry>

    <!-- TODO: review with dct:rights -->
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/noLimitations</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">https://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1a</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">https://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1b</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">https://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1c</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">https://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1d</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">https://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1e</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">https://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1f</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">https://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1g</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">https://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1h</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1a</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1b</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1c</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1d</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1e</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1f</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1g</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/NON_PUBLIC">http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess/INSPIRE_Directive_Article13_1h</entry>

    <!-- Dutch specific -->
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">Geen beperkingen</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC" match="start">Naamsvermelding verplicht,</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/publicdomain/mark/1.0/deed.nl</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/publicdomain/zero/1.0/deed.nl</entry>

    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/licenses/by/3.0/deed.nl</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/licenses/by/4.0/deed.nl</entry>

    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC" match="start">Gelijk Delen, Naamsvermelding verplicht,</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/licenses/by-sa/3.0/deed.nl</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/licenses/by-sa/4.0/deed.nl</entry>

    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC" match="start">Niet Commercieel, Naamsvermelding verplicht </entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/licenses/by-nc/3.0/deed.nl</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/licenses/by-nc/4.0/deed.nl</entry>

    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC" match="start">Niet Commercieel, Gelijk Delen, Naamsvermelding verplicht,</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/licenses/by-nc-sa/3.0/deed.nl</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/licenses/by-nc-sa/4.0/deed.nl</entry>

    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC" match="start">Geen Afgeleide Werken, Naamsvermelding verplicht,</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/licenses/by-nd/3.0/deed.nl</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/licenses/by-nd/4.0/deed.nl</entry>

    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC" match="start">Niet Commercieel, Geen Afgeleide Werken, Naamsvermelding verplicht,</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/licenses/by-nc-nd/3.0/deed.nl</entry>
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">http://creativecommons.org/licenses/by-nc-nd/4.0/deed.nl</entry>

    <!-- TODO: review other allowed values related to this license -->
    <entry key="http://publications.europa.eu/resource/authority/access-right/PUBLIC">Geo Gedeeld licentie</entry>
  </xsl:variable>

  <!--
  RDF Property:	dcterms:accessRights
  Definition:	Information about who can access the resource or an indication of its security status.
  Range: dcterms:RightsStatement
  = First constraints of mdb:identificationInfo/*/mri:resourceConstraints/*[mco:accessConstraints] (then dct:rights)

  RDF Property:	dcterms:license
  Definition:	A legal document under which the resource is made available.
  Range:	dcterms:LicenseDocument
  Usage note:	Information about licenses and rights MAY be provided for the Resource. See also guidance at 9. License and rights statements.
  = First useLimitation or constraints of mdb:identificationInfo/*/mri:resourceConstraints/*[mco:useConstraints] (then dct:rights)

  RDF Property:	dcterms:rights
  Definition:	A statement that concerns all rights not addressed with dcterms:license or dcterms:accessRights, such as copyright statements.
  Range:	dcterms:RightsStatement
  Usage note:	Information about licenses and rights MAY be provided for the Resource. See also guidance at 9. License and rights statements.
  -->
  <xsl:template mode="iso19115-3-to-dcat"
                match="mdb:identificationInfo/*/mri:resourceConstraints/*">

    <xsl:variable name="isServiceMetadata" select="name(../..) = 'srv:SV_ServiceIdentification'" />

    <xsl:if test="count(../preceding-sibling::mri:resourceConstraints/*) = 0">

      <xsl:if test="$isServiceMetadata">
        <xsl:variable name="useAccessConstraints"
                      as="node()*">
          <xsl:copy-of select="../../mri:resourceConstraints/*[mco:useConstraints]/mco:otherConstraints"/>
          <xsl:copy-of select="../../mri:resourceConstraints/*[mco:accessConstraints]/mco:otherConstraints"/>
        </xsl:variable>

        <xsl:variable name="licenses" as="node()*">
          <xsl:for-each select="$useAccessConstraints">

            <xsl:variable name="httpUriInAnchorOrText"
                          select="(gcx:Anchor/@xlink:href[starts-with(., 'http')]
                                  |gco:CharacterString[starts-with(., 'http')])[1]"/>

            <xsl:choose>
              <xsl:when test="$httpUriInAnchorOrText != '' and $isMappingResourceConstraintsToEuVocabulary = true()">

                <xsl:variable name="licenseUriWithoutHttp"
                              select="replace($httpUriInAnchorOrText,'https?://','')"/>

                <xsl:variable name="euDcatLicense"
                              select="$euLicenses/rdf:RDF/skos:Concept[
                                                  matches(skos:exactMatch/@rdf:resource,
                                                          concat('https?://', $licenseUriWithoutHttp, '/?'))
                                                  or matches(@rdf:about,
                                                          concat('https?://', $licenseUriWithoutHttp, '/?'))]"/>

                <xsl:if test="$euDcatLicense != ''">
                  <dct:license>
                    <dct:LicenseDocument rdf:about="{$euDcatLicense/@rdf:about}">
                      <xsl:copy-of select="$euDcatLicense/(skos:prefLabel[@xml:lang = $languages/@iso2code]
                                                      |skos:exactMatch)"
                                   copy-namespaces="no"/>
                    </dct:LicenseDocument>
                  </dct:license>
                </xsl:if>
              </xsl:when>
              <xsl:when test="$httpUriInAnchorOrText != ''">
                <dct:license>
                  <dct:LicenseDocument rdf:about="{$httpUriInAnchorOrText}"/>
                </dct:license>
              </xsl:when>
            </xsl:choose>
          </xsl:for-each>
        </xsl:variable>
        <xsl:copy-of select="$licenses[1]"/>
      </xsl:if>

      <!-- Check if there are non PUBLIC constraints -->
      <xsl:variable name="rightsStatementsNonPublic">
        <xsl:for-each select="distinct-values(../../mri:resourceConstraints/*[mco:accessConstraints]/mco:otherConstraints/(gco:CharacterString|gcx:Anchor/@xlink:href))">
          <xsl:variable name="dcatAccessType"
                        select="$dcatApAccessTypes[(lower-case(.) = lower-case(current()) and not(@match)) or
                                                   (starts-with(lower-case(current()), lower-case(.)) and (@match = 'start'))] "/>

          <xsl:if test="$dcatAccessType/@key != 'http://publications.europa.eu/resource/authority/access-right/PUBLIC'">
            <right key="{$dcatAccessType/@key}" />
          </xsl:if>
        </xsl:for-each>
      </xsl:variable>

      <xsl:variable name="rightsStatementsPublic">
        <xsl:for-each select="distinct-values(../../mri:resourceConstraints/*[mco:accessConstraints]/mco:otherConstraints/(gco:CharacterString|gcx:Anchor/@xlink:href))">
          <xsl:variable name="dcatAccessType"
                        select="$dcatApAccessTypes[(lower-case(.) = lower-case(current()) and not(@match)) or
                                                   (starts-with(lower-case(current()), lower-case(.)) and (@match = 'start'))] "/>

          <xsl:if test="$dcatAccessType/@key = 'http://publications.europa.eu/resource/authority/access-right/PUBLIC'">
            <right key="{$dcatAccessType/@key}" />
          </xsl:if>
        </xsl:for-each>
      </xsl:variable>


      <!-- If there are non PUBLIC constraints, use the first one.
           Otherwise, if there are PUBLIC constraints, use the first one.
           Otherwise use RESTRICTED -->
      <xsl:choose>
        <xsl:when test="count($rightsStatementsNonPublic/right) > 0">
          <dct:accessRights>
            <dct:RightsStatement rdf:about="{$rightsStatementsNonPublic/right[1]/@key}"/>
          </dct:accessRights>
        </xsl:when>
        <xsl:when test="count($rightsStatementsPublic/right) > 0">
          <dct:accessRights>
            <dct:RightsStatement rdf:about="{$rightsStatementsPublic/right[1]/@key}"/>
          </dct:accessRights>
        </xsl:when>
        <xsl:otherwise>
          <dct:accessRights>
            <dct:RightsStatement rdf:about="http://publications.europa.eu/resource/authority/access-right/RESTRICTED"/>
          </dct:accessRights>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:if>
  </xsl:template>


  <xsl:template mode="iso19115-3-to-dcat-distribution"
                match="mdb:identificationInfo/*/mri:resourceConstraints/*">
    <xsl:if test="count(../preceding-sibling::mri:resourceConstraints/*) = 0">
      <xsl:variable name="useConstraints"
                    as="node()*">
        <xsl:copy-of select="../../mri:resourceConstraints/*[mco:useConstraints]/mco:otherConstraints"/>
        <!-- TODO: review to use accessConstraints -->
        <xsl:copy-of select="../../mri:resourceConstraints/*[mco:accessConstraints]/mco:otherConstraints"/>
      </xsl:variable>

      <xsl:variable name="rights" as="node()*">
        <xsl:for-each select="$useConstraints">
          <xsl:variable name="httpUriInAnchorOrText"
                        select="(gcx:Anchor/@xlink:href[starts-with(., 'http')]
                                  |gco:CharacterString[starts-with(., 'http')])[1]"/>

          <xsl:variable name="inspireLimitationsOnPublicAccess"
                        select="(gcx:Anchor/@xlink:href[starts-with(., 'http://inspire.ec.europa.eu/metadata-codelist/LimitationsOnPublicAccess')])[1]" />

          <xsl:if test="not(string($httpUriInAnchorOrText)) or ($inspireLimitationsOnPublicAccess)">
            <dct:rights>
              <dct:RightsStatement>
                <xsl:call-template name="rdf-localised">
                  <xsl:with-param name="nodeName" select="'dct:description'"/>
                </xsl:call-template>
              </dct:RightsStatement>
            </dct:rights>
          </xsl:if>
        </xsl:for-each>
      </xsl:variable>

      <xsl:if test="count($rights) > 0">
        <xsl:copy-of select="$rights[1]" />
      </xsl:if>

      <xsl:variable name="licenses" as="node()*">
        <xsl:for-each select="$useConstraints">

          <xsl:variable name="httpUriInAnchorOrText"
                        select="(gcx:Anchor/@xlink:href[starts-with(., 'http')]
                                  |gco:CharacterString[starts-with(., 'http')])[1]"/>

          <xsl:choose>
            <xsl:when test="$httpUriInAnchorOrText != '' and $isMappingResourceConstraintsToEuVocabulary = true()">

              <xsl:variable name="licenseUriWithoutHttp"
                            select="replace($httpUriInAnchorOrText,'https?://','')"/>

              <xsl:variable name="euDcatLicense"
                            select="$euLicenses/rdf:RDF/skos:Concept[
                                                  matches(skos:exactMatch/@rdf:resource,
                                                          concat('https?://', $licenseUriWithoutHttp, '/?'))
                                                  or matches(@rdf:about,
                                                          concat('https?://', $licenseUriWithoutHttp, '/?'))]"/>

              <xsl:if test="$euDcatLicense != ''">
                <dct:license>
                  <dct:LicenseDocument rdf:about="{$euDcatLicense/@rdf:about}">
                    <xsl:copy-of select="$euDcatLicense/(skos:prefLabel[@xml:lang = $languages/@iso2code]
                                                      |skos:exactMatch)"
                                 copy-namespaces="no"/>
                  </dct:LicenseDocument>
                </dct:license>
              </xsl:if>
            </xsl:when>

            <!--<xsl:when test="$httpUriInAnchorOrText != ''">
              <dct:license>
                <dct:LicenseDocument rdf:about="{$httpUriInAnchorOrText}"/>
              </dct:license>
            </xsl:when>-->
          </xsl:choose>
        </xsl:for-each>
      </xsl:variable>

      <xsl:copy-of select="$licenses[1]"/>
    </xsl:if>
  </xsl:template>

  <!--
  RDF Property:	odrl:hasPolicy
  Definition:	An ODRL conformant policy expressing the rights associated with the resource.
  Range:	odrl:Policy
  Usage note:	Information about rights expressed as an ODRL policy [ODRL-MODEL] using the ODRL vocabulary [ODRL-VOCAB] MAY be provided for the resource. See also guidance at 9. License and rights statements.
  https://www.w3.org/TR/odrl-model/00Model.png

  TODO
  -->
</xsl:stylesheet>
