<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:cit="http://standards.iso.org/iso/19115/-3/cit/2.0"
                xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
                xmlns:mri="http://standards.iso.org/iso/19115/-3/mri/1.0"
                xmlns:mcc="http://standards.iso.org/iso/19115/-3/mcc/1.0"
                xmlns:gcx="http://standards.iso.org/iso/19115/-3/gcx/1.0"
                xmlns:gco="http://standards.iso.org/iso/19115/-3/gco/1.0"
                xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:prov="http://www.w3.org/ns/prov#"
                xmlns:dcat="http://www.w3.org/ns/dcat#"
                xmlns:geodcatap="http://data.europa.eu/930/"
                xmlns:dct="http://purl.org/dc/terms/"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:foaf="http://xmlns.com/foaf/0.1/"
                xmlns:vcard="http://www.w3.org/2006/vcard/ns#"
                xmlns:org="http://www.w3.org/ns/org#"
                xmlns:gn-fn-dcat="http://geonetwork-opensource.org/xsl/functions/dcat"
                exclude-result-prefixes="#all">

  <!--
  RDF Property:	dcat:contactPoint
  Definition:	Relevant contact information for the cataloged resource. Use of vCard is recommended [VCARD-RDF].
  Range:	vcard:Kind

  RDF Property:	dcterms:creator
  Definition:	The entity responsible for producing the resource.
  Range:	foaf:Agent
  Usage note:	Resources of type foaf:Agent are recommended as values for this property.

  RDF Property:	dcterms:publisher
  Definition:	The entity responsible for making the resource available.
  Usage note:	Resources of type foaf:Agent are recommended as values for this property.
  -->
  <xsl:template mode="iso19115-3-to-dcat"
                name="iso19115-3-to-dcat-agent"
                match="*[cit:CI_Responsibility]">

    <xsl:variable name="isSeriesMetadata" select="//mdb:MD_Metadata/mdb:metadataScope/mdb:MD_MetadataScope/mdb:resourceScope/mcc:MD_ScopeCode/@codeListValue = 'series'" />

    <!-- Process all contacts when processing the first one -->
    <xsl:if test="count(preceding-sibling::*[cit:CI_Responsibility]) = 0">

      <!-- Obtain default iso contact mappings to DCAT contacts -->
      <xsl:variable name="contactsMapping">
        <xsl:for-each select="../*[cit:CI_Responsibility]">
          <xsl:variable name="role"
                        as="xs:string?"
                        select="*/cit:role/*/@codeListValue"/>

          <xsl:variable name="dcatElementConfig"
                        as="node()?"
                        select="$isoContactRoleToDcatCommonNames[. = $role]"/>

          <xsl:if test="$dcatElementConfig">
            <xsl:copy-of select="$dcatElementConfig" />
          </xsl:if>
        </xsl:for-each>
      </xsl:variable>

      <!-- Creator -->
      <xsl:if test="not($isSeriesMetadata)">
      <xsl:choose>
        <xsl:when test="$contactsMapping/entry[@key='dct:creator']">
          <xsl:variable name="mappingRole" select="$contactsMapping/entry[@key='dct:creator']/text()" />

          <xsl:variable name="rolesForCreator" select="$isoContactRoleToDcatCommonNames[@key = 'dct:creator']/text()" />

          <xsl:variable name="contactsToProcess"
                        select="../*[cit:CI_Responsibility/cit:role/*/@codeListValue = $mappingRole]" />


          <!-- Sorted contacts to process with rolesForCreator ordering -->
          <xsl:variable name="contactsToProcessSorted">
            <xsl:for-each select="$rolesForCreator">
              <xsl:variable name="creatorRole" select="." />

              <xsl:if test="count($contactsToProcess[cit:CI_Responsibility/cit:role/*/@codeListValue = $creatorRole]) > 0">

                <xsl:variable name="dcatElementConfig"
                              as="node()?"
                              select="$isoContactRoleToDcatCommonNames[. = $creatorRole]"/>

                <xsl:variable name="allIndividualOrOrganisationWithoutIndividual"
                              select="$contactsToProcess[cit:CI_Responsibility/cit:role/*/@codeListValue = $creatorRole]/*/cit:party//cit:CI_Organisation"
                              as="node()*"/>

                <xsl:for-each-group select="$allIndividualOrOrganisationWithoutIndividual" group-by="cit:name">
                  <xsl:element name="{$dcatElementConfig/@key}">
                    <xsl:choose>
                      <xsl:when test="$dcatElementConfig/@as = 'vcard'">
                        <xsl:call-template name="rdf-contact-vcard"/>
                      </xsl:when>
                      <xsl:otherwise>
                        <xsl:call-template name="rdf-contact-foaf"/>
                      </xsl:otherwise>
                    </xsl:choose>
                  </xsl:element>
                </xsl:for-each-group>
              </xsl:if>
            </xsl:for-each>
          </xsl:variable>

          <xsl:copy-of select="$contactsToProcessSorted/*[1]"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="dcatElementConfig">
            <value name="dct:creator" as="{$isoContactRoleToDcatCommonNames[@key = 'dct:creator']/@as}"/>
          </xsl:variable>

          <xsl:variable name="allIndividualOrOrganisationWithoutIndividual"
                        select="*/cit:party//cit:CI_Organisation"
                        as="node()*"/>

          <xsl:for-each select="$dcatElementConfig/value">
            <xsl:variable name="contactType" select="@name" />
            <xsl:variable name="contactTypeAs" select="@as" />
            <xsl:for-each-group select="$allIndividualOrOrganisationWithoutIndividual" group-by="cit:name">
              <xsl:element name="{$contactType}">
                <xsl:choose>
                  <xsl:when test="$contactTypeAs = 'vcard'">
                    <xsl:call-template name="rdf-contact-vcard"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:call-template name="rdf-contact-foaf"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:element>
            </xsl:for-each-group>
          </xsl:for-each>
        </xsl:otherwise>
      </xsl:choose>
      </xsl:if>

      <!-- Publisher -->
      <xsl:choose>
        <xsl:when test="$contactsMapping/entry[@key='dct:publisher']">
          <xsl:variable name="mappingRole" select="$contactsMapping/entry[@key='dct:publisher']" />
          <xsl:for-each select="../*[cit:CI_Responsibility]">
            <xsl:variable name="role"
                          as="xs:string?"
                          select="*/cit:role/*/@codeListValue"/>

            <xsl:if test="$role = $mappingRole">
              <xsl:variable name="dcatElementConfig"
                            as="node()?"
                            select="$isoContactRoleToDcatCommonNames[. = $role]"/>

              <xsl:variable name="allIndividualOrOrganisationWithoutIndividual"
                            select="*/cit:party//(cit:CI_Organisation[not(cit:individual)]|cit:CI_Individual)"
                            as="node()*"/>

              <xsl:for-each-group select="$allIndividualOrOrganisationWithoutIndividual" group-by="cit:name">
                <xsl:element name="{$dcatElementConfig/@key}">
                  <xsl:choose>
                    <xsl:when test="$dcatElementConfig/@as = 'vcard'">
                      <xsl:call-template name="rdf-contact-vcard"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:call-template name="rdf-contact-foaf"/>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:element>
              </xsl:for-each-group>
            </xsl:if>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="dcatElementConfig">
            <value name="dct:publisher" as="{$isoContactRoleToDcatCommonNames[@key = 'dct:publisher']/@as}"/>
          </xsl:variable>

          <xsl:variable name="allIndividualOrOrganisationWithoutIndividual"
                        select="*/cit:party//cit:CI_Organisation"
                        as="node()*"/>

          <xsl:for-each select="$dcatElementConfig/value">
            <xsl:variable name="contactType" select="@name" />
            <xsl:variable name="contactTypeAs" select="@as" />
            <xsl:for-each-group select="$allIndividualOrOrganisationWithoutIndividual" group-by="cit:name">
              <xsl:element name="{$contactType}">
                <xsl:choose>
                  <xsl:when test="$contactTypeAs = 'vcard'">
                    <xsl:call-template name="rdf-contact-vcard"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:call-template name="rdf-contact-foaf"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:element>
            </xsl:for-each-group>
          </xsl:for-each>
        </xsl:otherwise>
      </xsl:choose>

      <!-- Contact point -->
      <xsl:choose>
        <xsl:when test="$contactsMapping/entry[@key='dcat:contactPoint']">
          <xsl:variable name="mappingRole" select="$contactsMapping/entry[@key='dcat:contactPoint']" />
          <xsl:for-each select="../*[cit:CI_Responsibility]">
            <xsl:variable name="role"
                          as="xs:string?"
                          select="*/cit:role/*/@codeListValue"/>

            <xsl:if test="$role = $mappingRole">
              <xsl:variable name="dcatElementConfig"
                            as="node()?"
                            select="$isoContactRoleToDcatCommonNames[. = $role]"/>

              <xsl:variable name="allIndividualOrOrganisationWithoutIndividual"
                            select="*/cit:party//(cit:CI_Organisation[not(cit:individual)]|cit:CI_Individual)"
                            as="node()*"/>

              <xsl:for-each-group select="$allIndividualOrOrganisationWithoutIndividual" group-by="cit:name">
                <xsl:element name="{$dcatElementConfig/@key}">
                  <xsl:choose>
                    <xsl:when test="$dcatElementConfig/@as = 'vcard'">
                      <xsl:call-template name="rdf-contact-vcard"/>
                    </xsl:when>
                    <xsl:otherwise>
                      <xsl:call-template name="rdf-contact-foaf"/>
                    </xsl:otherwise>
                  </xsl:choose>
                </xsl:element>
              </xsl:for-each-group>
            </xsl:if>
          </xsl:for-each>
        </xsl:when>
        <xsl:otherwise>
          <xsl:variable name="dcatElementConfig">
            <value name="dcat:contactPoint" as="{$isoContactRoleToDcatCommonNames[@key = 'dcat:contactPoint']/@as}"/>
          </xsl:variable>

          <xsl:variable name="allIndividualOrOrganisationWithoutIndividual"
                        select="*/cit:party//cit:CI_Organisation"
                        as="node()*"/>

          <xsl:for-each select="$dcatElementConfig/value">
            <xsl:variable name="contactType" select="@name" />
            <xsl:variable name="contactTypeAs" select="@as" />
            <xsl:for-each-group select="$allIndividualOrOrganisationWithoutIndividual" group-by="cit:name">
              <xsl:element name="{$contactType}">
                <xsl:choose>
                  <xsl:when test="$contactTypeAs = 'vcard'">
                    <xsl:call-template name="rdf-contact-vcard"/>
                  </xsl:when>
                  <xsl:otherwise>
                    <xsl:call-template name="rdf-contact-foaf"/>
                  </xsl:otherwise>
                </xsl:choose>
              </xsl:element>
            </xsl:for-each-group>
          </xsl:for-each>
        </xsl:otherwise>
      </xsl:choose>

      <!-- Map all other contacts for prov:qualifiedAttribution -->
      <xsl:if test="not($isSeriesMetadata)">
        <xsl:variable name="contactIndexToProcess" select="if ($contactsMapping/entry[@key='dct:creator'] and $contactsMapping/entry[@key='dct:publisher'] and $contactsMapping/entry[@key='dct:contactPoint']) then 0 else 1" />

        <xsl:for-each select="../*[cit:CI_Responsibility]">
          <xsl:if test="position() > $contactIndexToProcess">
            <xsl:variable name="role"
                          as="xs:string?"
                          select="*/cit:role/*/@codeListValue"/>

            <!-- Add it when not already mapped to creator, publisher or point of contact -->
            <xsl:if test="count($contactsMapping/entry[. = $role]) = 0">
              <xsl:variable name="allIndividualOrOrganisationWithoutIndividual"
                            select="*/cit:party//(cit:CI_Organisation[not(cit:individual)]|cit:CI_Individual)"
                            as="node()*"/>

              <xsl:for-each-group select="$allIndividualOrOrganisationWithoutIndividual" group-by="cit:name">
                <prov:qualifiedAttribution>
                  <prov:Attribution>
                    <prov:agent>
                      <xsl:call-template name="rdf-contact-foaf"/>
                    </prov:agent>
                    <dcat:hadRole>
                      <dcat:Role rdf:about="{concat($isoCodeListBaseUri, $role)}">
                        <!--
                            Property needs to have at least 1 value
                            Location:
                            [Focus node] - [http://standards.iso.org/iso/19115/resources/Codelists/cat/codelists.xml#custodian] -
                            [Result path] - [http://www.w3.org/2004/02/skos/core#prefLabel]
                        -->
                        <skos:prefLabel><xsl:value-of select="$role"/></skos:prefLabel>
                      </dcat:Role>
                    </dcat:hadRole>
                  </prov:Attribution>
                </prov:qualifiedAttribution>
              </xsl:for-each-group>
            </xsl:if>
          </xsl:if>
        </xsl:for-each>
      </xsl:if>
    </xsl:if>

  </xsl:template>
</xsl:stylesheet>
