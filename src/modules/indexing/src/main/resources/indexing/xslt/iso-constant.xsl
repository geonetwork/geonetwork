<!--

    SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
    SPDX-License-Identifier: GPL-2.0-or-later

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                version="3.0"
                exclude-result-prefixes="#all">

  <xsl:variable name="excludedCodelists"
                as="xs:string*"
                select="('CI_RoleCode', 'CI_DateTypeCode', 'MD_CharacterSetCode', 'LanguageCode')"/>

</xsl:stylesheet>
