<!--

    SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
    SPDX-License-Identifier: GPL-2.0-or-later

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:gn-fn-index="https://www.geonetwork-opensource.org/xslt/functions/index"
                version="3.0"
                exclude-result-prefixes="#all">

  <xsl:template mode="index"
                match="*:identificationInfo/*/*:serviceType/*:ScopedName[string(text()) != '']">
    <serviceType>
      <xsl:value-of select="text()"/>
    </serviceType>
  </xsl:template>

  <xsl:template mode="index"
                match="*:identificationInfo/*/*:serviceTypeVersion/*[string(text()) != '']">
    <serviceTypeVersion>
      <xsl:value-of select="text()"/>
    </serviceTypeVersion>
  </xsl:template>


  <xsl:template mode="index"
                match="*:identificationInfo/*:SV_ServiceIdentification/*:operatesOn">
    <xsl:variable name="xlink"
                  select="@xlink:href"/>

    <xsl:variable name="getRecordByIdId">
      <xsl:if test="$xlink != ''">
        <xsl:analyze-string select="$xlink"
                            regex=".*[i|I][d|D]=([a-zA-Z0-9\-\.\{{\}}]*).*">
          <xsl:matching-substring>
            <xsl:value-of select="regex-group(1)"/>
          </xsl:matching-substring>
        </xsl:analyze-string>
      </xsl:if>
    </xsl:variable>

    <xsl:variable name="datasetId">
      <xsl:choose>
        <xsl:when test="$getRecordByIdId != ''">
          <xsl:value-of select="$getRecordByIdId"/>
        </xsl:when>
        <xsl:when test="@uuidref != ''">
          <xsl:value-of select="@uuidref"/>
        </xsl:when>
      </xsl:choose>
    </xsl:variable>

    <xsl:if test="$datasetId != ''">
      <recordOperateOn>
        <xsl:value-of select="$datasetId"/>
      </recordOperateOn>
      <xsl:copy-of select="gn-fn-index:build-record-link($datasetId, $xlink, @xlink:title, 'datasets')"/>
    </xsl:if>
  </xsl:template>

</xsl:stylesheet>
