<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:gn-fn-index="https://www.geonetwork-opensource.org/xslt/functions/index"
                version="3.0"
                exclude-result-prefixes="#all">

  <xsl:import href="utility-keywords.xsl"/>

  <xsl:template mode="index"
                match="*:topicCategory/*:MD_TopicCategoryCode[string(.)]">
    <xsl:param name="languages" as="node()*"/>

    <xsl:copy-of select="gn-fn-index:build-codelist('cl_topic', 'topicCategory', ., $languages, '', '')"/>
  </xsl:template>


  <xsl:template name="index-keywords" match="*:MD_Metadata">
    <xsl:param name="languages" as="node()*"/>

    <xsl:variable name="allKeywords">
      <xsl:for-each-group select="*//*:MD_Keywords"
                          group-by="concat(*:thesaurusName/*/*:title/(*:CharacterString|*:Anchor)/text(), '-', *:type/*/@codeListValue[. != ''])">
        <xsl:sort select="current-grouping-key()"/>

        <xsl:variable name="thesaurusType"
                      select="*:type/*/@codeListValue[. != '']"/>

        <xsl:variable name="thesaurusTitle"
                      select="if (starts-with(current-grouping-key(), '-'))
                                  then concat('otherKeywords', current-grouping-key())
                                  else *:thesaurusName/*/*:title/(*:CharacterString|*:Anchor)/text()"/>

        <xsl:variable name="thesaurusRef"
                      select="*:thesaurusName/*/*:identifier[position() = 1]/*/*:code/(*:CharacterString|*:Anchor)"/>

        <xsl:variable name="thesaurusId"
                      select="if ($thesaurusRef != '')
                                  then normalize-space($thesaurusRef/text())
                                  else ''"/>
        <!-- TODO util:getThesaurusIdByTitle($thesaurusTitle) -->

        <xsl:variable name="thesaurusUri"
                      select="$thesaurusRef/@xlink:href"/>

        <xsl:variable name="thesaurusFieldName"
                      select="gn-fn-index:build-thesaurus-index-field-name($thesaurusId, $thesaurusTitle)"/>

        <xsl:variable name="keywords"
                      select="current-group()/*:keyword[*/normalize-space() != '']"/>

        <thesaurus>
          <info type="{$thesaurusType}"
                field="{$thesaurusFieldName}"
                id="{$thesaurusId}"
                uri="{$thesaurusUri}"
                title="{$thesaurusTitle}">
            <xsl:if test="not(starts-with($thesaurusTitle, 'otherKeywords'))">
              <multilingualTitle>
                <xsl:copy-of select="gn-fn-index:add-multilingual-field('multilingualTitle',
                            *:thesaurusName/*/*:title, $languages)/*"/>
              </multilingualTitle>
            </xsl:if>
          </info>
          <keywords>
            <xsl:for-each select="$keywords">
              <keyword>
                <xsl:variable name="keywordUri"
                              select="if (*:Anchor/@xlink:href[. != ''])
                                          then *:Anchor/@xlink:href
                                          else ''"/>
                <!-- TODO: util:getKeywordUri(
                                                (*/text())[1],
                                                $thesaurusId,
                                                $allLanguages/lang[@id = 'default']/@value) -->
                <xsl:attribute name="uri" select="$keywordUri"/>
                <values>
                  <xsl:copy-of select="gn-fn-index:add-multilingual-field('keyword',
                          ., $languages)/*"/>
                </values>

                <!--  If keyword is related to a thesaurus available
                in current catalogue, checked the keyword exists in the thesaurus.
                If not, report an error in indexingErrorMsg field.

                This case may trigger editor warning message when a keyword is not
                 found in the thesaurus. Try to anticipate this and advertise those
                 records in the admin. -->
                <xsl:if test="$thesaurusId != '' and $keywordUri = ''">
                  <indexingErrorMsg>
                    <string>indexingErrorMsg-keywordNotFoundInThesaurus</string>
                    <type>warning</type>
                    <values>
                      <keyword>
                        <xsl:value-of select="*/text()"/>
                      </keyword>
                      <thesaurus>
                        <xsl:value-of select="$thesaurusId"/>
                      </thesaurus>
                    </values>
                  </indexingErrorMsg>
                </xsl:if>

                <tree>
                  <defaults>
                    <!--TODO <xsl:call-template name="get-keyword-tree-values">
                      <xsl:with-param name="keyword"
                                      select="(*/text())[1]"/>
                      <xsl:with-param name="thesaurus"
                                      select="$thesaurusId"/>
                      <xsl:with-param name="language"
                                      select="$allLanguages/lang[@id = 'default']/@value"/>
                    </xsl:call-template>-->
                  </defaults>
                  <xsl:if test="$keywordUri != ''">
                    <keys>
                      <!--TODO <xsl:call-template name="get-keyword-tree-values">
                        <xsl:with-param name="keyword"
                                        select="$keywordUri"/>
                        <xsl:with-param name="thesaurus"
                                        select="$thesaurusId"/>
                        <xsl:with-param name="language"
                                        select="$allLanguages/lang[@id = 'default']/@value"/>
                      </xsl:call-template>-->
                    </keys>
                  </xsl:if>
                </tree>
              </keyword>
            </xsl:for-each>
          </keywords>
        </thesaurus>
      </xsl:for-each-group>

      <xsl:variable name="geoDescription"
                    select="//*:geographicElement/*/*:geographicIdentifier/
                                  */*:code[*/normalize-space(.) != '']
                                |//*:EX_Extent/*:description[*/normalize-space(.) != '']"/>
      <xsl:if test="$geoDescription">
        <thesaurus>
          <info type="place"/>
          <keywords>
            <xsl:for-each select="$geoDescription">
              <keyword>
                <values>
                  <xsl:copy-of select="gn-fn-index:add-multilingual-field('keyword',
                          ., $languages)/*"/>
                </values>
              </keyword>
            </xsl:for-each>
          </keywords>
        </thesaurus>
      </xsl:if>
    </xsl:variable>


    <xsl:call-template name="build-all-keyword-fields">
      <xsl:with-param name="allKeywords" select="$allKeywords"/>
    </xsl:call-template>

  </xsl:template>
</xsl:stylesheet>
