<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:gn-fn-index="https://www.geonetwork-opensource.org/xslt/functions/index"
                version="3.0"
                exclude-result-prefixes="#all">
  <!-- Parent may be encoded using an associatedResource.
  Define which association type should be considered as parent. -->
  <xsl:param name="parentAssociatedResourceType" select="'partOfSeamlessDatabase'"/>
  <xsl:param name="childrenAssociatedResourceType" select="'isComposedOf'"/>


  <xsl:template mode="index"
                match="*:contentInfo/*/*:featureCatalogueCitation/@uuidref[. != '']">
    <xsl:copy-of select="gn-fn-index:build-record-link(., ../@xlink:href, ../@xlink:title, 'fcats')"/>
    <hasfeaturecat><xsl:value-of select="."/></hasfeaturecat>
  </xsl:template>

  <xsl:template mode="index"
                match="*:source/@uuidref[. != '']">
    <xsl:copy-of select="gn-fn-index:build-record-link(., ../@xlink:href, ../@xlink:title, 'sources')"/>
    <hassource><xsl:value-of select="."/></hassource>
  </xsl:template>

  <xsl:template mode="index"
                match="*:parentMetadata/@uuidref[. != '']|*:parentIdentifier/*[text() != '']">
    <xsl:copy-of select="gn-fn-index:build-record-link(., ../@xlink:href, ../@xlink:title, 'parent')"/>
    <parentUuid><xsl:value-of select="."/></parentUuid>
    <recordGroup><xsl:value-of select="."/></recordGroup>
  </xsl:template>


  <xsl:template mode="index"
                match="*:associatedResource/*|*:aggregationInfo/*">
    <xsl:variable name="code"
                  select="if (*:aggregateDataSetIdentifier/*/*:code/*/text() != '')
                              then *:aggregateDataSetIdentifier/*/*:code/*/text()
                              else if (*:metadataReference/@uuidref != '')
                              then *:metadataReference/@uuidref
                              else if (*:metadataReference/@xlink:href != '')
                              then *:metadataReference/@xlink:href
                              else *:metadataReference/*:CI_Citation/*:identifier/*:MD_Identifier/*:code/*/text()"/>
    <xsl:if test="$code != ''">
      <xsl:variable name="xlink"
                    select="*:metadataReference/@xlink:href"/>
      <xsl:variable name="associationType"
                    select="*:associationType/*/@codeListValue"/>
      <xsl:if test="$associationType = $parentAssociatedResourceType">
        <parentUuid><xsl:value-of select="$code"/></parentUuid>
        <xsl:copy-of select="gn-fn-index:build-record-link(
                                $code, $xlink, *:metadataReference/@xlink:title, 'parent')"/>
      </xsl:if>
      <xsl:if test="$associationType = $childrenAssociatedResourceType">
        <childUuid><xsl:value-of select="$code"/></childUuid>
        <xsl:copy-of select="gn-fn-index:build-record-link(
                                $code, $xlink, *:metadataReference/@xlink:title, 'children')"/>
      </xsl:if>

      <xsl:variable name="initiativeType"
                    select="*:initiativeType/*/@codeListValue"/>
      <xsl:variable name="properties">
        <properties>
          <p name="associationType" value="{$associationType}"/>
          <p name="initiativeType" value="{$initiativeType}"/>
        </properties>
      </xsl:variable>
      <xsl:copy-of select="gn-fn-index:build-record-link(
                                $code, $xlink, *:metadataReference/@xlink:title,
                                'siblings', $properties)"/>
      <agg_associated><xsl:value-of select="$code"/></agg_associated>
      <xsl:element name="{concat('agg_associated_', $associationType)}"><xsl:value-of select="$code"/></xsl:element>
    </xsl:if>
  </xsl:template>
</xsl:stylesheet>
