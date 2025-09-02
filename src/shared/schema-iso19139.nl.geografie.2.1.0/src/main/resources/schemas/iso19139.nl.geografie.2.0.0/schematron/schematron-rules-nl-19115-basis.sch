<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
	<sch:ns uri="http://www.isotc211.org/2005/gmd" prefix="gmd"/>
	<sch:ns uri="http://www.isotc211.org/2005/gco" prefix="gco"/>
	<sch:ns uri="http://www.isotc211.org/2005/gmx" prefix="gmx"/>
	<sch:ns uri="http://www.opengis.net/gml" prefix="gml"/>
	<sch:ns uri="http://www.w3.org/1999/xlink" prefix="xlink"/>
	<sch:ns uri="http://www.w3.org/2001/XMLSchema-instance" prefix="xsi"/>
	<sch:ns uri="http://www.w3.org/2004/02/skos/core#" prefix="skos"/>
	<sch:ns prefix="geonet" uri="http://www.fao.org/geonetwork"/>
	<sch:let name="lowercase" value="'abcdefghijklmnopqrstuvwxyz'"/>
	<sch:let name="uppercase" value="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>
	<!-- werkt nog niet
	<sch:let name="gemet-nl" value="document('GEMET-InspireThemes-nl.rdf')"/>
	-->

	<sch:pattern id="Validatie tegen het Nederlands metadata profiel op ISO 19115 versie 2.1.0">
		<sch:title>Validatie tegen het Nederlands metadata profiel op ISO 19115 voor geografie versie 2.1.0 (2020)</sch:title>
		<!-- INSPIRE Thesaurus en Conformiteit-->
		<sch:let name="thesaurus1" value="normalize-space(string-join(/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:descriptiveKeywords[1]/gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation/gmd:title[gco:CharacterString or gmx:Anchor]//text(), ''))"/>
		<sch:let name="thesaurus2" value="normalize-space(string-join(/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:descriptiveKeywords[2]/gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation/gmd:title[gco:CharacterString or gmx:Anchor]//text(), ''))"/>
		<sch:let name="thesaurus3" value="normalize-space(string-join(/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:descriptiveKeywords[3]/gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation/gmd:title[gco:CharacterString or gmx:Anchor]//text(), ''))"/>
		<sch:let name="thesaurus4" value="normalize-space(string-join(/gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:descriptiveKeywords[4]/gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation/gmd:title[gco:CharacterString or gmx:Anchor]//text(), ''))"/>
		<sch:let name="thesaurus" value="concat(string($thesaurus1),string($thesaurus2),string($thesaurus3),string($thesaurus4))"/>
		<sch:let name="thesaurus_INSPIRE_Exsists" value="contains($thesaurus,'GEMET - INSPIRE themes, version 1.0')"/>
		<sch:let name="conformity_Spec_Title1" value="normalize-space(string-join(//gmd:MD_Metadata/gmd:dataQualityInfo[1]/gmd:DQ_DataQuality/gmd:report[1]/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title[./gco:CharacterString or ./gmx:Anchor]//text(), ''))"/>
		<sch:let name="conformity_Spec_Title2" value="normalize-space(string-join(//gmd:MD_Metadata/gmd:dataQualityInfo[1]/gmd:DQ_DataQuality/gmd:report[2]/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title[./gco:CharacterString or ./gmx:Anchor]//text(), ''))"/>
		<sch:let name="conformity_Spec_Title3" value="normalize-space(string-join(//gmd:MD_Metadata/gmd:dataQualityInfo[1]/gmd:DQ_DataQuality/gmd:report[3]/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title[./gco:CharacterString or ./gmx:Anchor]//text(), ''))"/>
		<sch:let name="conformity_Spec_Title4" value="normalize-space(string-join(//gmd:MD_Metadata/gmd:dataQualityInfo[1]/gmd:DQ_DataQuality/gmd:report[4]/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult/gmd:specification/gmd:CI_Citation/gmd:title[./gco:CharacterString or ./gmx:Anchor]//text(), ''))"/>
		<sch:let name="conformity_Spec_Title_All" value="concat(string($conformity_Spec_Title1),string($conformity_Spec_Title2),string($conformity_Spec_Title3),string($conformity_Spec_Title4))"/>
		<sch:let name="conformity_Spec_Title_Exsists" value="contains($conformity_Spec_Title_All,'VERORDENING (EU) Nr. 1089/2010 VAN DE COMMISSIE van 23 november 2010 ter uitvoering van Richtlijn 2007/2/EG van het Europees Parlement en de Raad betreffende de interoperabiliteit van verzamelingen ruimtelijke gegevens en van diensten met betrekking tot ruimtelijke gegevens')"/>

		<sch:rule id="Algemene_metadata_regels" etf_name="Algemene metadata regels" context="/gmd:MD_Metadata">


		<!--  fileIdentifier for report https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#metadata-unieke-identifier -->
			<sch:let name="fileIdentifier" value="normalize-space(gmd:fileIdentifier/gco:CharacterString)"/>
            		<!-- Taal van de metadata https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#taal-van-de-metadata
				NB: voor deze tests is dut en eng voldoende -->
			<sch:let name="mdLanguage" value="(gmd:language/*/@codeListValue = 'dut' or gmd:language/*/@codeListValue = 'eng')"/>
            			<sch:let name="mdLanguage_value" value="string(gmd:language/*/@codeListValue)"/>

		<!-- Metadata hiërarchielevel variable  -->
			<!-- Hiërarchieniveau https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#hi%C3%ABrarchieniveau -->
			<sch:let name="hierarchyLevel" value="(gmd:hierarchyLevel[1]/*/@codeListValue = 'dataset' or gmd:hierarchyLevel[1]/*/@codeListValue = 'series')"/>
			<sch:let name="hierarchyLevel_value" value="string(gmd:hierarchyLevel[1]/*/@codeListValue)"/>
          <!-- Hiërarchieniveau naam, Beschrijving hiërarchisch niveau https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#hi%C3%ABrarchieniveaunaam-->
			<sch:let name="hierarchyLevelName" value="normalize-space(gmd:hierarchyLevelName[1]/gco:CharacterString)"/>
		<!-- Metadata verantwoordelijke organisatie (name) https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-metadata -->
			<sch:let name="mdResponsibleParty_OrganisationString" value="normalize-space(gmd:contact[1]/gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString)"/>
			<sch:let name="mdResponsibleParty_OrganisationURI" value="normalize-space(gmd:contact[1]/gmd:CI_ResponsibleParty/gmd:organisationName/gmx:Anchor/@xlink:href)"/>
			<sch:let name="mdResponsibleParty_OrganisationAnchorLabel" value="normalize-space(gmd:contact[1]/gmd:CI_ResponsibleParty/gmd:organisationName/gmx:Anchor)"/>

			<!-- Metadata verantwoordelijke organisatie (role) NL profiel https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-metadata:-rol -->
			<!-- fix role for https://github.com/Geonovum/metadata-schematron/issues/20, choose the first element where it is a match in the codelist -->
			<!-- Thijs: todo, fox for #20 seems to break things -->
			<sch:let name="mdResponsibleParty_Role" value="gmd:contact/gmd:CI_ResponsibleParty/gmd:role/*[@codeListValue = 'resourceProvider' or @codeListValue = 'custodian' or @codeListValue = 'owner' or @codeListValue = 'user' or @codeListValue = 'distributor' or @codeListValue = 'owner' or @codeListValue = 'originator' or @codeListValue = 'pointOfContact' or @codeListValue = 'principalInvestigator' or @codeListValue = 'processor' or @codeListValue = 'publisher' or @codeListValue = 'author'][1]"/>

		<!--  voor INSPIRE toegestane waarde in combi met INSPIRE specificatie -->

			<sch:let name="mdResponsibleParty_Role_INSPIRE" value="gmd:contact[1]/gmd:CI_ResponsibleParty/gmd:role/*/@codeListValue = 'pointOfContact' "/>

		<!-- Metadata verantwoordelijke organisatie (email) https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-metadata:-e-mail -->
			<sch:let name="mdResponsibleParty_Mail" value="normalize-space(gmd:contact[1]/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:address/gmd:CI_Address/gmd:electronicMailAddress[1]/gco:CharacterString)"/>
        <!-- Metadata datum https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#metadatadatum -->
			<sch:let name="dateStamp" value="normalize-space(string(gmd:dateStamp/gco:Date))"/>
		<!-- Metadatastandaard naam https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#metadata-standaard-naam -->
			<sch:let name="metadataStandardName" value="translate(normalize-space(gmd:metadataStandardName/gco:CharacterString), $lowercase, $uppercase)"/>
		<!-- Metadatastandaard versie https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#metadatastandaard-versie -->
			<sch:let name="metadataStandardVersion" value="translate(normalize-space(gmd:metadataStandardVersion/gco:CharacterString), $lowercase, $uppercase)"/>

			<!-- Metadata karakterset : in v.2.0.0 optioneel gewworden -->
			<sch:let name="metadataCharacterset" value="string(gmd:characterSet/*/@codeListValue)"/>
			<sch:let name="metadataCharacterset_value" value="gmd:characterSet/*[@codeListValue ='ucs2' or @codeListValue ='ucs4' or @codeListValue ='utf7' or @codeListValue ='utf8' or @codeListValue ='utf16' or @codeListValue ='8859part1' or @codeListValue ='8859part2' or @codeListValue ='8859part3' or @codeListValue ='8859part4' or @codeListValue ='8859part5' or @codeListValue ='8859part6' or @codeListValue ='8859part7' or @codeListValue ='8859part8' or @codeListValue ='8859part9' or @codeListValue ='8859part10' or @codeListValue ='8859part11' or  @codeListValue ='8859part12' or @codeListValue ='8859part13' or @codeListValue ='8859part14' or @codeListValue ='8859part15' or @codeListValue ='8859part16' or @codeListValue ='jis' or @codeListValue ='shiftJIS' or @codeListValue ='eucJP' or @codeListValue ='usAscii' or @codeListValue ='ebcdic' or @codeListValue ='eucKR' or @codeListValue ='big5' or @codeListValue ='GB2312']"/>

		<!-- rules and assertions -->

			<sch:assert id="Metadata_ID" etf_name="Metadata ID" test="$fileIdentifier" see="https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#metadata-unieke-identifier">Er is geen Metadata ID (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#metadata-unieke-identifier) opgegeven.</sch:assert>
			<sch:report id="Metadata_ID_info" etf_name="Metadata ID info" test="$fileIdentifier">Metadata ID: <sch:value-of select="$fileIdentifier"/>
			</sch:report>
			<sch:assert id="Metadata_taal" etf_name="Metadata_taal" test="$mdLanguage" see="https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#taal-van-de-metadata">De metadata taal (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#taal-van-de-metadata) ontbreekt of heeft een verkeerde waarde. Dit hoort een waarde en verwijzing naar de codelijst te zijn.</sch:assert>
			<sch:report id="Metadata_taal_info" etf_name="Metadata_taal_info" test="$mdLanguage" see="https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#taal-van-de-metadata">Metadata taal (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#taal-van-de-metadata) voldoet
			 </sch:report>

			<sch:assert id="Metadata_hierarchieniveau" etf_name="Metadata_hierarchieniveau" test="$hierarchyLevel" see="https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#hi%C3%ABrarchieniveau">Metadata hierarchieniveau (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#hi%C3%ABrarchieniveau) ontbreekt of heeft een verkeerde waarde</sch:assert>
			<sch:report id="Metadata_hierarchieniveau_info" etf_name="Metadata_hierarchieniveau_info" test="$hierarchyLevel">Metadata hierarchieniveau (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#hi%C3%ABrarchieniveau) voldoet</sch:report>

        	<sch:assert id="Beschrijving_hierarchisch_niveau_ISO_nr_7" etf_name="Beschrijving hierarchisch niveau (ISO nr. 7)" test="not($hierarchyLevel_value = 'series' and not($hierarchyLevelName))">Beschrijving hierarchisch niveau (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#hi%C3%ABrarchieniveaunaam) ontbreekt. Dit is verplicht als hierarchieniveau = 'series'.</sch:assert>
			<sch:report id="Beschrijving_hierarchisch_niveau_ISO_nr_7_info" etf_name="Beschrijving hierarchisch niveau (ISO nr. 7) info" test="$hierarchyLevelName">Tenminste 1 beschrijving hierarchisch niveau (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#hi%C3%ABrarchieniveaunaam) is gevonden
			</sch:report>
			<sch:assert id="Naam_organisatie_metadata_ISO_nr_376" etf_name="Naam organisatie metadata (ISO nr. 376)" test="$mdResponsibleParty_OrganisationString or ($mdResponsibleParty_OrganisationURI and $mdResponsibleParty_OrganisationAnchorLabel)">Naam en/of de URI (in geval van een Anchor) van de organisatie metadata (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-metadata) ontbreekt.</sch:assert>
			<sch:report id="Naam_organisatie_metadata_ISO_nr_376_info" etf_name="Naam organisatie metadata (ISO nr. 376) info" test="$mdResponsibleParty_OrganisationString or $mdResponsibleParty_OrganisationURI">Naam organisatie metadata (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-metadata): <sch:value-of select="$mdResponsibleParty_OrganisationString"/><sch:value-of select="$mdResponsibleParty_OrganisationURI"/>
			</sch:report>

			<sch:assert id="Rol_organisatie_metadata_ISO_nr_379" etf_name="Rol organisatie metadata (ISO nr. 379)" test="$mdResponsibleParty_Role">Rol organisatie metadata (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-bron:-rol) ontbreekt of heeft een verkeerde waarde</sch:assert>

		<!-- In geval van INSPIRE: Rol organisatie metadata in combi met specificatie INSPIRE -->
			<sch:assert id="INSPIRE_Rol_organisatie_metadata_ISO_nr_379" etf_name="INSPIRE Rol organisatie metadata (ISO nr. 379)" test="not($conformity_Spec_Title_Exsists) or ($conformity_Spec_Title_Exsists and $mdResponsibleParty_Role_INSPIRE)">Rol organisatie metadata (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-bron:-rol) ontbreekt of heeft een verkeerde waarde, deze dient voor INSPIRE contactpunt te zijn</sch:assert>
		<!-- eind INSPIRE in combi met specificatie INSPIRE -->
			<sch:assert id="E-mail_organisatie_metadata_ISO_nr_386" etf_name="E-mail organisatie metadata (ISO nr. 386)"  test="$mdResponsibleParty_Mail">E-mail organisatie metadata (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-metadata:-e-mail) ontbreekt</sch:assert>
			<sch:report id="E-mail_organisatie_metadata_ISO_nr_386_info" etf_name="E-mail organisatie metadata (ISO nr. 386) info" test="$mdResponsibleParty_Mail">E-mail organisatie metadata (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-metadata:-e-mail): <sch:value-of select="$mdResponsibleParty_Mail"/>
			</sch:report>
			<sch:assert id="Metadata_datum_ISO_nr_9" etf_name="Metadata datum (ISO nr. 9)" test="((number(substring(substring-before($dateStamp,'-'),1,4)) &gt; 1000 ))">Metadata datum (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#metadatadatum) ontbreekt of heeft het verkeerde formaat (YYYY-MM-DD)</sch:assert>
			<sch:report id="Metadata_datum_ISO_nr_9_info" etf_name="Metadata datum (ISO nr. 9) info" test="$dateStamp">Metadata datum (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#metadatadatum): <sch:value-of select="$dateStamp"/>
			</sch:report>

			<sch:assert id="Metadatastandaard_naam_ISO_nr_10" etf_name="Metadatastandaard naam (ISO nr. 10)" test="$metadataStandardName = 'ISO 19115'">Metadatastandaard naam (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#metadata-standaard-naam) ontbreekt of is niet correct ingevuld, Metadatastandaard naam dient de waarde 'ISO 19115' te hebben</sch:assert>
			<sch:report id="Metadatastandaard_naam_ISO_nr_10_info" etf_name="Metadatastandaard naam (ISO nr. 10) info" test="$metadataStandardName">Metadatastandaard naam (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#metadata-standaard-naam): <sch:value-of select="$metadataStandardName"/>
			</sch:report>
			<sch:assert id="Versie_metadatastandaard_ISO_nr_11" etf_name="Versie metadatastandaard (ISO nr. 11)" test="contains($metadataStandardVersion, 'PROFIEL OP ISO 19115')">Versie metadatastandaard  (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#metadatastandaard-versie) ontbreekt of is niet correct ingevuld, Metadatastandaard versie dient de waarde 'Nederlands metadata profiel op ISO 19115 voor geografie 2.0' te bevatten</sch:assert>
			<sch:report id="Versie_metadatastandaard_ISO_nr_11_info" etf_name="Versie metadatastandaard (ISO nr. 11) info" test="$metadataStandardVersion">Versie metadatastandaard (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#metadatastandaard-versie): <sch:value-of select="$metadataStandardVersion"/>
			</sch:report>

			<!-- TB: optioneel geworden in v 2.0.0, geen test meer mogelijk -->
			<!-- <sch:assert id="Metadata_karakterset_ISO_nr_4" etf_name="Metadata karakterset (ISO nr. 4)" test="not($metadataCharacterset) or $metadataCharacterset_value">Metadata karakterset (ISO nr. 4) ontbreekt of heeft een verkeerde waarde</sch:assert>
			<sch:report id="Metadata_karakterset_ISO_nr_4_info" etf_name="Metadata karakterset (ISO nr. 4) info" test="not($metadataCharacterset) or $metadataCharacterset_value">Metadata karakterset (ISO nr. 4) opgegeven en voldoet</sch:report> -->

		<!-- alle regels over elementen binnen gmd:identificationInfo -->

		<!-- Dataset titel, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#titel-van-de-bron -->
			<sch:let name="datasetTitle" value="normalize-space(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:title/gco:CharacterString)"/>

			<!-- Datum van de bron en Datum type van de bron, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#datum-van-de-bron en https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#datum-type-van-de-bron-->
			<sch:let name="publicationDateString" value="string(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/*[./gmd:CI_Date/gmd:dateType/*/@codeListValue='publication']/gmd:CI_Date/gmd:date/gco:Date)"/>
			<sch:let name="creationDateString" value="string(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/*[./gmd:CI_Date/gmd:dateType/*/@codeListValue='creation']/gmd:CI_Date/gmd:date/gco:Date)"/>
			<sch:let name="revisionDateString" value="string(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/*[./gmd:CI_Date/gmd:dateType/*/@codeListValue='revision']/gmd:CI_Date/gmd:date/gco:Date)"/>
			<sch:let name="publicationDate" value="((number(substring(substring-before($publicationDateString,'-'),1,4)) &gt; 1000 ))"/>
			<sch:let name="creationDate" value="((number(substring(substring-before($creationDateString,'-'),1,4)) &gt; 1000 ))"/>
			<sch:let name="revisionDate" value="((number(substring(substring-before($revisionDateString,'-'),1,4)) &gt; 1000 ))"/>

			<!-- Samenvatting https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#samenvatting -->
			<sch:let name="abstract" value="normalize-space(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:abstract/gco:CharacterString)"/>

			<!-- Status https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#status -->
			<sch:let name="status" value="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:status/*[@codeListValue = 'completed' or @codeListValue = 'historicalArchive' or @codeListValue = 'obsolete' or @codeListValue = 'onGoing' or @codeListValue = 'planned' or @codeListValue = 'required' or @codeListValue = 'underDevelopment']"/>

			<!-- 5.2.10 Verantwoordelijke organisatie bron https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-bron -->
			<sch:let name="responsibleParty_OrganisationString" value="normalize-space(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact[1]/gmd:CI_ResponsibleParty/gmd:organisationName/gco:CharacterString)"/>
			<sch:let name="responsibleParty_OrganisationURI" value="normalize-space(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact[1]/gmd:CI_ResponsibleParty/gmd:organisationName/gmx:Anchor/@xlink:href)"/>
			<sch:let name="responsibleParty_OrganisationAnchor" value="normalize-space(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact[1]/gmd:CI_ResponsibleParty/gmd:organisationName/gmx:Anchor)"/>

		<!-- 5.2.11 Verantwoordelijke organisatie bron: e-mail https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-bron-email -->
			<sch:let name="responsibleParty_Mail" value="normalize-space(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact[1]/gmd:CI_ResponsibleParty/gmd:contactInfo/gmd:CI_Contact/gmd:address[1]/gmd:CI_Address/gmd:electronicMailAddress[1]/gco:CharacterString)"/>

		<!-- 5.2.12 Verantwoordelijke organisatie bron rol, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-bron:-rol -->
			<sch:let name="responsibleParty_Role" value="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:pointOfContact[1]/gmd:CI_ResponsibleParty/gmd:role/*[@codeListValue = 'resourceProvider' or @codeListValue = 'custodian' or @codeListValue = 'owner' or @codeListValue = 'user' or @codeListValue = 'distributor' or @codeListValue = 'owner' or @codeListValue = 'originator' or @codeListValue = 'pointOfContact' or @codeListValue = 'principalInvestigator' or @codeListValue = 'processor' or @codeListValue = 'publisher' or @codeListValue = 'author']"/>

		<!-- 5.2.13 Trefwoord https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#trefwoord -->
			<sch:let name="keyword" value="normalize-space(string-join(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:descriptiveKeywords[1]/gmd:MD_Keywords/gmd:keyword[1][gco:CharacterString or gmx:Anchor]//text(), ''))"/>

		<!-- 5.2.4 Unieke Identifier van de bron, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#unieke-identifier-van-de-bron -->
			<sch:let name="identifierString" value="normalize-space(string-join(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier/gmd:code[./gco:CharacterString or ./gmx:Anchor]//text(), ''))"/>
			<!-- Het gebuik van een URI is conditioneel, het is verplicht voor INSPIRE datasets? -> nog inbouwen om te detecteren of het INSPIRE data betreft of doen we dat net als met de oude versie van het profiel in een aparte validator?
		 	-->
			<sch:let name="identifierURI" value="normalize-space(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:citation/gmd:CI_Citation/gmd:identifier/gmd:MD_Identifier/gmd:code/gmx:Anchor/@xlink:href)"/>

		<!-- Ruimtelijk schema https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#ruimtelijk-schema-van-de-bron
		 codelijst: https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-spatialrepresentationtypecode -->
		<!-- conditioneel (alleen INSPIRE), dus nu geen controle inbouwen in deze validator -->
			<sch:let name="spatialRepresentationType" value="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:spatialRepresentationType/*[@codeListValue='vector' or @codeListValue='grid' or @codeListValue='textTable' or @codeListValue='tin']"/>

		<!-- Taal van de bron, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#taal-van-de-bron -->
			<sch:let name="language" value="(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:language/*/@codeListValue = 'dut' or gmd:identificationInfo/gmd:MD_DataIdentification/gmd:language/*/@codeListValue = 'eng')"/>
      <sch:let name="language_value" value="string(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:language/*/@codeListValue)"/>

		<!-- Dataset karakterset, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#karakterset-van-de-bron:
		 optioneel in v2.0.0, geen test meer
		-->
			<sch:let name="characterset" value="string(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:characterSet/*/@codeListValue)"/>
			<sch:let name="characterset_value" value="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:characterSet/*[@codeListValue ='ucs2' or @codeListValue ='ucs4' or @codeListValue ='utf7' or @codeListValue ='utf8' or @codeListValue ='utf16' or @codeListValue ='8859part1' or @codeListValue ='8859part2' or @codeListValue ='8859part3' or @codeListValue ='8859part4' or @codeListValue ='8859part5' or @codeListValue ='8859part6' or @codeListValue ='8859part7' or @codeListValue ='8859part8' or @codeListValue ='8859part9' or @codeListValue ='8859part10' or @codeListValue ='8859part11' or  @codeListValue ='8859part12' or @codeListValue ='8859part13' or @codeListValue ='8859part14' or @codeListValue ='8859part15' or @codeListValue ='8859part16' or @codeListValue ='jis' or @codeListValue ='shiftJIS' or @codeListValue ='eucJP' or @codeListValue ='usAscii' or @codeListValue ='ebcdic' or @codeListValue ='eucKR' or @codeListValue ='big5' or @codeListValue ='GB2312']"/>

		<!-- Thema's, Onderwerp https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#onderwerp
		codelijst : https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-spatialrepresentationtypecode -->
			<sch:let name="topicCategory" value="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:topicCategory/*[normalize-space(string-join(text(), '')) = 'farming' or normalize-space(string-join(text(), '')) = 'biota' or normalize-space(string-join(text(), '')) = 'boundaries' or normalize-space(string-join(text(), '')) = 'climatologyMeteorologyAtmosphere' or normalize-space(string-join(text(), '')) = 'economy' or normalize-space(string-join(text(), '')) = 'elevation' or normalize-space(string-join(text(), '')) = 'environment' or normalize-space(string-join(text(), '')) = 'geoscientificInformation' or normalize-space(string-join(text(), '')) = 'health' or normalize-space(string-join(text(), '')) = 'imageryBaseMapsEarthCover' or normalize-space(string-join(text(), '')) = 'intelligenceMilitary' or normalize-space(string-join(text(), '')) = 'inlandWaters' or normalize-space(string-join(text(), '')) = 'location' or normalize-space(string-join(text(), '')) = 'oceans' or normalize-space(string-join(text(), '')) = 'planningCadastre' or normalize-space(string-join(text(), '')) = 'society' or normalize-space(string-join(text(), '')) = 'structure' or normalize-space(string-join(text(), '')) = 'transportation' or normalize-space(string-join(text(), '')) = 'utilitiesCommunication']"/>

		<!-- Gebruiksbeperkingen, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#gebruiksbeperkingen -->
			<sch:let name="useLimitation" value="normalize-space(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints[1]/gmd:MD_Constraints/gmd:useLimitation[1]/gco:CharacterString)"/>

			<!-- Overige beperkingen https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#overige-beperkingen -->
			<!-- Tests of een waarde of label is opgegeven
			Bij Anchors: of het element ook een xlink:href heeft
			-->
			<sch:let name="otherConstraint1" value="normalize-space(string-join(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints[2]/gmd:MD_LegalConstraints/gmd:otherConstraints[1][gco:CharacterString or gmx:Anchor/@xlink:href]//text(), ''))"/>
			<sch:let name="otherConstraint2" value="normalize-space(string-join(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints[2]/gmd:MD_LegalConstraints/gmd:otherConstraints[2][gco:CharacterString or gmx:Anchor/@xlink:href]//text(), ''))"/>

			<sch:let name="otherConstraints" value="concat($otherConstraint1,$otherConstraint2)"/>

			<sch:let name="otherConstraintURI1" value="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints[2]/gmd:MD_LegalConstraints/gmd:otherConstraints[1]/gmx:Anchor"/>
			<sch:let name="otherConstraintURI2" value="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints[2]/gmd:MD_LegalConstraints/gmd:otherConstraints[2]/gmx:Anchor"/>

			<!-- Thijs Brentjens add the URI to the test as well -->
			<sch:let name="otherConstraintIsCodelistdatalicense" value="($otherConstraint1='Geen beperkingen' and (contains($otherConstraintURI1/@xlink:href,'://creativecommons.org/publicdomain/mark/') or contains($otherConstraint2,'://creativecommons.org/publicdomain/mark/'))) or ($otherConstraint1='Geen beperkingen' and (contains($otherConstraintURI1/@xlink:href,'://creativecommons.org/publicdomain/zero/') or contains($otherConstraint2,'://creativecommons.org/publicdomain/zero/'))) or (contains($otherConstraint1,'Naamsvermelding verplicht, ') and (contains($otherConstraintURI1/@xlink:href,'://creativecommons.org/licenses/by/') or contains($otherConstraint2,'://creativecommons.org/licenses/by/'))) or (contains($otherConstraint1, 'Gelijk Delen, Naamsvermelding verplicht, ') and (contains($otherConstraintURI1/@xlink:href,'://creativecommons.org/licenses/by-sa/') or contains($otherConstraint2,'://creativecommons.org/licenses/by-sa/'))) or (contains($otherConstraint1, 'Niet Commercieel, Naamsvermelding verplicht, ') and (contains($otherConstraintURI1/@xlink:href,'://creativecommons.org/licenses/by-nc/') or contains($otherConstraint2,'://creativecommons.org/licenses/by-nc/'))) or (contains($otherConstraint1, 'Niet Commercieel, Gelijk Delen, Naamsvermelding verplicht, ') and (contains($otherConstraintURI1/@xlink:href,'://creativecommons.org/licenses/by-nc-sa/') or contains($otherConstraint2,'://creativecommons.org/licenses/by-nc-sa/'))) or (contains($otherConstraint1, 'Geen Afgeleide Werken, Naamsvermelding verplicht, ') and (contains($otherConstraintURI1/@xlink:href,'://creativecommons.org/licenses/by-nd/') or contains($otherConstraint2,'://creativecommons.org/licenses/by-nd/'))) or (contains($otherConstraint1, 'Niet Commercieel, Geen Afgeleide Werken, Naamsvermelding verplicht, ') and (contains($otherConstraintURI1/@xlink:href,'://creativecommons.org/licenses/by-nc-nd/') or contains($otherConstraint2,'://creativecommons.org/licenses/by-nc-nd/'))) or ($otherConstraint1='Geo Gedeeld licentie' and (starts-with($otherConstraintURI1/@xlink:href,'http') or starts-with($otherConstraint2,'http')) ) or ($otherConstraint2='Geen beperkingen' and (contains($otherConstraintURI2/@xlink:href,'://creativecommons.org/publicdomain/mark/') or contains($otherConstraint1,'://creativecommons.org/publicdomain/mark/'))) or ($otherConstraint2='Geen beperkingen' and (contains($otherConstraintURI2/@xlink:href,'://creativecommons.org/publicdomain/zero/') or contains($otherConstraint1,'://creativecommons.org/publicdomain/zero/'))) or (contains($otherConstraint2, 'Naamsvermelding verplicht, ') and (contains($otherConstraintURI2/@xlink:href,'://creativecommons.org/licenses/by/') or contains($otherConstraint1,'://creativecommons.org/licenses/by/')) ) or (contains($otherConstraint2, 'Gelijk Delen, Naamsvermelding verplicht, ') and (contains($otherConstraintURI2/@xlink:href,'://creativecommons.org/licenses/by-sa/') or contains($otherConstraint1,'://creativecommons.org/licenses/by-sa/'))) or (contains($otherConstraint2, 'Niet Commercieel, Naamsvermelding verplicht, ') and (contains($otherConstraintURI2/@xlink:href,'://creativecommons.org/licenses/by-nc/') or contains($otherConstraint1,'://creativecommons.org/licenses/by-nc/'))) or (contains($otherConstraint2, 'Niet Commercieel, Gelijk Delen, Naamsvermelding verplicht, ') and (contains($otherConstraintURI2/@xlink:href,'://creativecommons.org/licenses/by-nc-sa/') or contains($otherConstraint1,'://creativecommons.org/licenses/by-nc-sa/'))) or (contains($otherConstraint2, 'Geen Afgeleide Werken, Naamsvermelding verplicht, ') and (contains($otherConstraintURI2/@xlink:href,'://creativecommons.org/licenses/by-nd/') or contains($otherConstraint1,'://creativecommons.org/licenses/by-nd/'))) or (contains($otherConstraint2, 'Niet Commercieel, Geen Afgeleide Werken, Naamsvermelding verplicht, ') and (contains($otherConstraintURI2/@xlink:href,'://creativecommons.org/licenses/by-nc-nd/') or contains($otherConstraint1,'://creativecommons.org/licenses/by-nc-nd/'))) or ($otherConstraint2='Geo Gedeeld licentie' and (starts-with($otherConstraintURI2/@xlink:href,'http') or starts-with($otherConstraint1,'http')))" />

			<!-- <sch:let name="otheConstraintsHasValidDatalicenseURI" value="" /> -->
			<!-- test op bestaande URI in een van de codelijsten: https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-datalicenties, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#Codelijst-INSPIRE-ConditionsApplyingToAccessAndUse of  https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#Codelijst-INSPIRE-LimitationsOnPublicAccess
			inhoudelijk niet mogelijk, want de laatste rij van https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-datalicenties is:
			"Verwijzing naar een geldige URL van de licentie" . Dit is te algemeen, niet testbaar als waarde in een codelijst.
		 	-->

		<!-- (Juridische) toegangsrestricties -->
			<!-- aanscherping om public domein CC0 of Geogedeeld te gebruiken -->
			<!-- waarde moet in dat geval otherRestrictions zijn-->
			<!-- Docs: Altijd de 2e set elementen van resourceConstraints, want useLimitation is de 1e (overgenomen uit profiel 1.3) -->
			<sch:let name="accessConstraints_value" value="normalize-space(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints[2]/gmd:MD_LegalConstraints/gmd:accessConstraints/*[@codeListValue = 'otherRestrictions']/@codeListValue)"/>

			<sch:let name="nrMDLegalConstraints" value="count(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:resourceConstraints/gmd:MD_LegalConstraints)"/>

			<!-- Omgrenzende rechthoek https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#omgrenzende-rechthoek -->

			<sch:let name="west" value="number(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement[1]/gmd:EX_GeographicBoundingBox/gmd:westBoundLongitude/gco:Decimal)"/>
			<sch:let name="east" value="number(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement[1]/gmd:EX_GeographicBoundingBox/gmd:eastBoundLongitude/gco:Decimal)"/>
			<sch:let name="north" value="number(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement[1]/gmd:EX_GeographicBoundingBox/gmd:northBoundLatitude/gco:Decimal)"/>
			<sch:let name="south" value="number(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:geographicElement[1]/gmd:EX_GeographicBoundingBox/gmd:southBoundLatitude/gco:Decimal)"/>

		<!-- Temporele dekking begin -->
			<sch:let name="begin_beginPosition" value="normalize-space(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:temporalElement/gmd:EX_TemporalExtent/gmd:extent/*/gml:beginPosition)"/>
			<sch:let name="begin_begintimePosition" value="normalize-space(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:temporalElement/gmd:EX_TemporalExtent/gmd:extent/*/gml:begin/*/gml:timePosition)"/>
			<sch:let name="begin_timePosition" value="normalize-space(gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:temporalElement/gmd:EX_TemporalExtent/gmd:extent/*/gml:timePosition)"/>
		<!-- spatial resolution -->
			<sch:let name="spatialResolution" value="gmd:identificationInfo/gmd:MD_DataIdentification/gmd:spatialResolution"/>
		<!-- reference system  -->
			<sch:let name="referenceSystemInfo" value="gmd:referenceSystemInfo"/>

		<!-- rules and assertions -->
			<!-- Dataset titel, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#titel-van-de-bron -->
			<sch:assert id="Dataset_titel_ISO_nr_360" etf_name="Dataset titel (ISO nr. 360)" test="$datasetTitle">Dataset titel (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#titel-van-de-bron) ontbreekt</sch:assert>
			<sch:report id="Dataset_titel_ISO_nr_360_info" etf_name="Dataset titel (ISO nr. 360) info" test="$datasetTitle">Dataset titel (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#titel-van-de-bron): <sch:value-of select="$datasetTitle"/>
			</sch:report>

			<!-- Datum van de bron en Datum type van de bron, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#datum-van-de-bron en https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#datum-type-van-de-bron-->
			<sch:assert id="Datum_van_de_bronISO_nr_394_en_Datumtype_ISO_nr395" etf_name="Datum van de bron(ISO nr. 394) en Datumtype (ISO nr.395)" test="$publicationDate or $creationDate or $revisionDate">Datum van de bron (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#datum-van-de-bron) of Datumtype (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#datum-type-van-de-bron) ontbreken of heeft het verkeerde formaat (YYYY-MM-DD)</sch:assert>
			<sch:report id="Datum_van_de_bronISO_nr_394_en_Datatype_ISO_nr395_info" etf_name="Datum van de bron(ISO nr. 394) en Datatype (ISO nr.395) info" test="$publicationDate or $creationDate or $revisionDate">Tenminste 1 datum van de bron (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#datum-van-de-bron) is gevonden</sch:report>

			<!-- 5.2.5 Samenvatting https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#samenvatting -->
			<sch:assert id="Samenvatting_ISO_nr_25" etf_name="Samenvatting (ISO nr. 25)" test="$abstract">Samenvatting (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#samenvatting) ontbreekt</sch:assert>
			<sch:report id="Samenvatting_ISO_nr_25_info" etf_name="Samenvatting (ISO nr. 25) info" test="$abstract">Samenvatting (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#samenvatting): <sch:value-of select="$abstract"/>
			</sch:report>

			<sch:assert id="Status_ISO_nr_28" etf_name="Status (ISO nr. 28)" test="$status">Status (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#status) ontbreekt of heeft een verkeerde waarde</sch:assert>
			<sch:report id="Status_ISO_nr_28_info" etf_name="Status (ISO nr. 28) info" test="$status">Status (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#status) voldoet
			</sch:report>

			<!-- 5.2.10 Verantwoordelijke organisatie bron https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-bron -->
			<sch:assert id="Verantwoordelijke_organisatie_bron_ISO_nr_376" etf_name="Verantwoordelijke organisatie bron (ISO nr. 376)" test="$responsibleParty_OrganisationString or $responsibleParty_OrganisationURI">Verantwoordelijke organisatie bron (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-bron) ontbreekt</sch:assert>
			<sch:report id="Verantwoordelijke_organisatie_bron_ISO_nr_376_info" etf_name="Verantwoordelijke organisatie bron (ISO nr. 376) info" test="$responsibleParty_OrganisationString or $responsibleParty_OrganisationURI">Verantwoordelijke organisatie bron (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-bron): <sch:value-of select="$responsibleParty_OrganisationString"/> <sch:value-of select="$responsibleParty_OrganisationAnchor"/> (URI: <sch:value-of select="$responsibleParty_OrganisationURI"/>)
			</sch:report>

			<sch:assert id="Rol_verantwoordelijke_organisatie_bron_ISO_nr_379" etf_name="Rol verantwoordelijke organisatie bron (ISO nr. 379)" test="$responsibleParty_Role">Rol verantwoordelijke organisatie bron (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-bron:-rol) ontbreekt of heeft een verkeerde waarde</sch:assert>
			<sch:report id="Rol_verantwoordelijke_organisatie_bron_ISO_nr_379_info" etf_name="Rol verantwoordelijke organisatie bron (ISO nr. 379) info" test="$responsibleParty_Role">Rol verantwoordelijke organisatie bron (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-bron:-rol) voldoet
			</sch:report>
			<sch:assert id="E-mail_verantwoordelijke_organisatie_bron_ISO_nr_386" etf_name="E-mail verantwoordelijke organisatie bron (ISO nr. 386)" test="$responsibleParty_Mail">E-mail verantwoordelijke organisatie bron (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-bron-email) ontbreekt</sch:assert>
			<sch:report id="E-mail_verantwoordelijke_organisatie_bron_ISO_nr_386_info" etf_name="E-mail verantwoordelijke organisatie bron (ISO nr. 386) info" test="$responsibleParty_Mail">E-mail verantwoordelijke organisatie bron (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verantwoordelijke-organisatie-bron-email): <sch:value-of select="$responsibleParty_Mail"/>
			</sch:report>
			<sch:assert id="Trefwoorden_ISO_nr_53" etf_name="Trefwoorden (ISO nr. 53)" test="$keyword">Trefwoorden (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#trefwoord) ontbreken</sch:assert>
			<sch:report id="Trefwoorden_ISO_nr_53_info" etf_name="Trefwoorden (ISO nr. 53) info" test="$keyword">Tenminste 1 trefwoord (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#trefwoord) is gevonden (<sch:value-of select="$keyword"/>)
			</sch:report>
		<!-- Thesaurus alleen voor INSPIRE-->
		<!--
			<sch:assert id="Thesaurus_ISO_nr_360" etf_name="Thesaurus (ISO nr. 360)" test="$thesaurus_INSPIRE_Exsists">Thesaurus (ISO nr. 360), datum en datumtype ontbreekt</sch:assert>
		-->
		<!-- eind Thesaurus alleen voor INSPIRE-->

		<!-- Als  de GEMET INSPIRE themes thesaurus voorkomt, is verwijzing naar inspire specificatie verplicht -->

		<sch:assert id="Specificatie_ISO_nr_360" etf_name="Specificatie (ISO nr. 360)" test="not($thesaurus_INSPIRE_Exsists) or ($thesaurus_INSPIRE_Exsists and $conformity_Spec_Title_Exsists)">Specificatie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#thesaurus) mist de verplichte waarde voor INSPIRE datasets. Als dit geen INSPIRE dataset is verwijder dan de thesaurus GEMET -INSPIRE themes, voor INSPIRE datasets in specificatie opnemen; VERORDENING (EU) Nr. 1089/2010 VAN DE COMMISSIE van 23 november 2010 ter uitvoering van Richtlijn 2007/2/EG van het Europees Parlement en de Raad betreffende de interoperabiliteit van verzamelingen ruimtelijke gegevens en van diensten met betrekking tot ruimtelijke gegevens</sch:assert>

		<!-- eind	-->

			<!-- 5.2.4 Unieke Identifier van de bron https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#unieke-identifier-van-de-bron -->
			<sch:assert id="Unieke_Identifier_van_de_bron_ISO_nr_207" etf_name="Unieke Identifier van de bron (ISO nr. 207)" test="$identifierString">Unieke Identifier van de bron (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#unieke-identifier-van-de-bron) ontbreekt</sch:assert>
			<sch:report id="Unieke_Identifier_van_de_bron_ISO_nr_207_info" etf_name="Unieke Identifier van de bron (ISO nr. 207) info" test="$identifierString">Unieke Identifier van de bron (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#unieke-identifier-van-de-bron): <sch:value-of select="$identifierString"/>.
			</sch:report>

		    <!-- 5.2.4 Unieke Identifier van de bron https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#unieke-identifier-van-de-bron met een URI, dan beginnen met HTTP-->
		    <sch:assert id="Unieke_Identifier_van_de_bron_ISO_nr_207_URI" etf_name="Unieke Identifier van de bron (ISO nr. 207)" test=" not($identifierURI) or ($identifierURI and starts-with($identifierURI, 'http'))">Unieke Identifier van de bron (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#unieke-identifier-van-de-bron) met een URI moet beginnen met HTTP </sch:assert>
		    <sch:report id="Unieke_Identifier_van_de_bron_ISO_nr_207_URI_info" etf_name="Unieke Identifier van de bron (ISO nr. 207) info" test="$identifierURI">Unieke Identifier van de bron (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#unieke-identifier-van-de-bron): <sch:value-of select="$identifierURI"/>.
		    </sch:report>

			<!-- v.2.1.0: taal van de bron is verplicht geworden: dus niet op testen als assertion -->
			<sch:assert id="Taal_van_de_bron_ISO_nr_39" etf_name="Taal van de bron (ISO nr. 39)" test="$language">Taal van de bron (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#taal-van-de-bron) is verplicht, maar ontbreekt of heeft een verkeerde waarde. Dit hoort een waarde en verwijzing naar de codelijst te zijn.</sch:assert>

			<!-- 5.2.8 Karakterset van de bron: conditioneel geworden. Geen assertion meer, bevestigd door Ine. https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#karakterset-van-de-bron -->
			<!-- <sch:assert id="Dataset_karakterset_ISO_nr_40" etf_name="Dataset karakterset (ISO nr. 40)" test="not($characterset) or $characterset_value">Dataset karakterset (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#karakterset-van-de-bron) ontbreekt of heeft een verkeerde waarde</sch:assert>
			<sch:report id="Dataset_karakterset_ISO_nr_40_info" etf_name="Dataset karakterset (ISO nr. 40) info" test="$characterset_value">Dataset karakterset (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#karakterset-van-de-bron) voldoet
			</sch:report> -->

			<!-- 5.2.9 Onderwerp https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#onderwerp -->
			<sch:assert id="OnderwerpISO_nr_41" etf_name="Onderwerp(ISO nr. 41)" test="$topicCategory">Onderwerp (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#onderwerp) ontbreekt of heeft een verkeerde waarde</sch:assert>
			<sch:report id="OnderwerpISO_nr_41_info" etf_name="Onderwerp(ISO nr. 41) info" test="$topicCategory">Onderwerp (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#onderwerp) voldoet
			</sch:report>


			<sch:assert id="Minimum_x-coordinaat_ISO_nr_344" etf_name="Minimum x-coordinaat (ISO nr. 344)"  test="(-180.00 &lt; $west) and ( $west &lt; 180.00) or ( $west = 0.00 ) or ( $west = -180.00 ) or ( $west = 180.00 )">Minimum x-coördinaat (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#omgrenzende-rechthoek) ontbreekt of heeft een verkeerde waarde</sch:assert>
			<sch:report id="Minimum_x-coordinaat_ISO_nr_344_info" etf_name="Minimum x-coordinaat (ISO nr. 344) info" test="(-180.00 &lt; $west) and ( $west &lt; 180.00) or ( $west = 0.00 ) or ( $west = -180.00 ) or ( $west = 180.00 )">Minimum x-coördinaat (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#omgrenzende-rechthoek): <sch:value-of select="$west"/>
			</sch:report>
			<sch:assert id="Maximum_x-coordinaat_ISO_nr_345" etf_name="Maximum x-coordinaat (ISO nr. 345)"  test="(-180.00 &lt; $east) and ($east &lt; 180.00) or ( $east = 0.00 ) or ( $east = -180.00 ) or ( $east = 180.00 )">Maximum x-coördinaat (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#omgrenzende-rechthoek) ontbreekt of heeft een verkeerde waarde</sch:assert>
			<sch:report id="Maximum_x-coordinaat_ISO_nr_345_info" etf_name="Maximum x-coordinaat (ISO nr. 345) info"  test="(-180.00 &lt; $east) and ($east &lt; 180.00) or ( $east = 0.00 ) or ( $east = -180.00 ) or ( $east = 180.00 )">Maximum x-coördinaat (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#omgrenzende-rechthoek): <sch:value-of select="$east"/>
			</sch:report>
			<sch:assert id="Minimum_y-coordinaat_ISO_nr_346" etf_name="Minimum y-coordinaat (ISO nr. 346)" test="(-90.00 &lt; $south) and ($south &lt; $north) or (-90.00 = $south) or ($south = $north)">Minimum y-coördinaat (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#omgrenzende-rechthoek) ontbreekt of heeft een verkeerde waarde</sch:assert>
			<sch:report id="Minimum_y-coordinaat_ISO_nr_346_info" etf_name="Minimum y-coordinaat (ISO nr. 346) info" test="(-90.00 &lt; $south) and ($south &lt; $north) or (-90.00 = $south) or ($south = $north)">Minimum y-coördinaat (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#omgrenzende-rechthoek): <sch:value-of select="$south"/>
			</sch:report>
			<sch:assert id="Maximum_y-coordinaat_ISO_nr_347" etf_name="Maximum y-coordinaat (ISO nr. 347)" test="($south &lt; $north) and ($north &lt; 90.00) or ($south = $north) or ($north = 90.00)">Maximum y-coördinaat (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#omgrenzende-rechthoek) ontbreekt of heeft een verkeerde waarde</sch:assert>
			<sch:report id="Maximum_y-coordinaat_ISO_nr_347_info" etf_name="Maximum y-coordinaat (ISO nr. 347) info" test="($south &lt; $north) and ($north &lt; 90.00) or ($south = $north) or ($north = 90.00)">Maximum y-coördinaat (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#omgrenzende-rechthoek): <sch:value-of select="$north"/>
			</sch:report>
			<!-- optineel geworden in v1.3
			<sch:assert id="Temporele_dekking_-_BeginDatum_ISO_nr_351" etf_name="Temporele dekking - BeginDatum (ISO nr. 351)" test="$begin_beginPosition or $begin_begintimePosition or $begin_timePosition">Temporele dekking - BeginDatum (ISO nr. 351) ontbreekt</sch:assert>
			<sch:report id="Temporele_dekking_-_BeginDatum_ISO_nr_351_info" etf_name="Temporele dekking - BeginDatum (ISO nr. 351) info" test="$begin_beginPosition or $begin_begintimePosition or $begin_timePosition">Temporele dekking - BeginDatum (ISO nr. 351): <sch:value-of select="$begin_beginPosition"/><sch:value-of select="$begin_begintimePosition"/><sch:value-of select="$begin_timePosition"/>
			</sch:report>
			 -->

			<sch:assert id="Gebruiksbeperkingen_ISO_nr_68" etf_name="Gebruiksbeperkingen (ISO nr. 68)" test="$useLimitation">Gebruiksbeperkingen (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#gebruiksbeperkingen) ontbreken</sch:assert>
			<sch:report id="Gebruiksbeperkingen_ISO_nr_68_info" etf_name="Gebruiksbeperkingen (ISO nr. 68) info" test="$useLimitation">Gebruiksbeperkingen (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#gebruiksbeperkingen): <sch:value-of select="$useLimitation"/>
			</sch:report>
			<sch:assert id="Overige_beperkingen_1_Anchor_URI_aanwezig" etf_name="Overige beperkingen 1 Anchor URI aanwezig" test="not($otherConstraintURI1) or ($otherConstraintURI1 and $otherConstraintURI1/@xlink:href)">Overige beperkingen eerste element: de URI (xlink:href) dient ingevuld te zijn</sch:assert>
			<sch:assert id="Overige_beperkingen_2_Anchor_URI_aanwezig" etf_name="Overige beperkingen 2 Anchor URI aanwezig" test="not($otherConstraintURI2) or ($otherConstraintURI2 and $otherConstraintURI2/@xlink:href)">Overige beperkingen tweede element: de URI (xlink:href) dient ingevuld te zijn</sch:assert>

			<sch:assert id="Juridische_toegangsrestricties_ISO_nr_70_en_Overige_beperkingen_ISO_nr_72" etf_name="(Juridische) toegangsrestricties (ISO nr. 70) en Overige beperkingen (ISO nr 72)" test="$accessConstraints_value and $otherConstraints ">(Juridische) toegangsrestricties (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#juridische-toegangsrestricties) en Overige beperkingen (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#overige-beperkingen) dienen ingevuld te zijn</sch:assert>
			<sch:assert id="Juridische_toegangsrestricties_ISO_nr_70" etf_name="(Juridische) toegangsrestricties (ISO nr. 70)" test="$accessConstraints_value">(Juridische) toegangsrestricties (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#juridische-toegangsrestricties) dient de waarde 'anders' te hebben in combinatie met een publiek domein, CC0 of GeoGedeeld licentie bij overige beperkingen (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#overige-beperkingen)</sch:assert>
			<!-- Voor NL GI-beraad moet accessConstraints_value aanwezig zijn -->
			<sch:assert id="Juridische_toegangsrestricties_verplicht_met_'otherRestrictions'_aanwezig" etf_name="Juridische toegangsrestricties verplicht met 'otherRestrictions' aanwezig" test="$accessConstraints_value">Het element Juridische toegangsrestricties met de waarde 'otherRestrictions' is niet aanwezig, maar is wel verplicht voor organisaties die zich conformeren aan afspraken in het GI-beraad (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#juridische-toegangsrestricties).</sch:assert>

			<!-- <sch:assert id="Juridische_toegangsrestricties:_het_element__MD_LegalConstraints_moet_2_maal_aanwezig_zijn" etf_name="Juridische toegangsrestricties: het element MD_LegalConstraints moet 2 maal aanwezig zijn" test="$nrMDLegalConstraints = 2">Het element MD_LegalConstraints moet 2 maal aanwezig zijn, maar is dat niet (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#juridische-toegangsrestricties).</sch:assert> -->

			<sch:assert id="Overige_beperkingen_ISO_nr_72" etf_name="Overige beperkingen (ISO nr 72)" test="not($accessConstraints_value = 'otherRestrictions') or ($accessConstraints_value = 'otherRestrictions' and $otherConstraint1 and $otherConstraint2) or ($accessConstraints_value = 'otherRestrictions' and $otherConstraintURI1 and $otherConstraintURI1/@xlink:href)">Het element overige beperkingen (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#overige-beperkingen) dient twee maal binnen dezelfde toegangsrestricties voor te komen; één met de beschrijving en één met de URL, in 2 elementen of in 1 Anchor. De URL moet verwijzen naar de publiek domein, CC0 of GeoGedeeld licentie,als (juridische) toegangsrestricties (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#juridische-toegangsrestricties) de waarde 'anders' heeft</sch:assert>
			<sch:report id="Overige_beperkingen_ISO_nr_72_1_info" etf_name="Overige beperkingen (ISO nr 72) 1 info" test="$otherConstraint1">Overige beperkingen (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#overige-beperkingen) 1: <sch:value-of select="$otherConstraint1"/>
			</sch:report>
			<sch:report id="Overige_beperkingen_ISO_nr_72_2_info" etf_name="Overige beperkingen (ISO nr 72) 2 info" test="$otherConstraint2">Overige beperkingen (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#overige-beperkingen) 2: <sch:value-of select="$otherConstraint2"/>
			</sch:report>

			<sch:report id="Juridische_toegangsrestricties_ISO_nr_70_info" etf_name="(Juridische) toegangsrestricties (ISO nr. 70) info"  test="$accessConstraints_value">(Juridische) toegangsrestricties (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#juridische-toegangsrestricties) aanwezig: <sch:value-of select="$accessConstraints_value"/>
			</sch:report>

			<!-- Thijs Brentjens: nieuwe controle op codelijst https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-datalicenties -->
			<sch:assert id="Overige_beperkingen_en_codelijst_Datalicenties" etf_name="Overige beperkingen en codelijst Datalicenties" test="not($accessConstraints_value = 'otherRestrictions') or ($accessConstraints_value = 'otherRestrictions' and $otherConstraintIsCodelistdatalicense)">Als het element juridische toegangsrestricties de waarde otherRestrictions bevat dient een link uit de codelijst https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-datalicenties naar de licentie en de bijbehorende beschrijving opgenomen te worden. Opgenomen informatie:&#160;<sch:value-of select="$otherConstraint1"/>&#160;met licentie:&#160;<sch:value-of select="normalize-space($otherConstraintURI1/@xlink:href)"/>&#160;<sch:value-of select="$otherConstraint2"/>&#160;en&#160;<sch:value-of select="$otherConstraint2"/>&#160;<sch:value-of select="normalize-space($otherConstraintURI2/@xlink:href)"/>&#160;<sch:value-of select="$otherConstraint1"/></sch:assert>

			<!-- <sch:report id="Datalicenties_info" etf_name="Datalicenties info" test="not($accessConstraints_value = 'otherRestrictions') and not($accessConstraints_value = 'otherRestrictions' and $otherConstraintIsCodelistdatalicense)"><sch:value-of select="$otherConstraint1"/> met licentie:<sch:value-of select="$otherConstraintURI1/@xlink:href"/><sch:value-of select="$otherConstraint2"/> en <sch:value-of select="$otherConstraint2"/> <sch:value-of select="$otherConstraintURI2/@xlink:href"/><sch:value-of select="$otherConstraint1"/></sch:report> -->

			<sch:assert id="Toepassingsschaal_ISO_nr_57_Resolutie_ISO_nr_61" etf_name="Toepassingsschaal (ISO nr. 57) Resolutie (ISO nr. 61)" test="$spatialResolution">Toepassingsschaal (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#toepassingsschaal) of Resolutie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#resolutie) is verplicht als hij gespecificeerd kan worden. </sch:assert>

			<sch:assert id="Code_referentiesysteem_ISO_nr_207" etf_name="Code referentiesysteem (ISO nr. 207)" test="$referenceSystemInfo">Code referentiesysteem (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codereferentiesysteem) ontbreekt</sch:assert>

		<!-- alle regels over elementen binnen distributionInfo -->

			<sch:let name="distributionFormatName" value="normalize-space(gmd:distributionInfo/gmd:MD_Distribution/gmd:distributionFormat/gmd:MD_Format/gmd:name/gco:CharacterString)"/>
			<sch:let name="distributionFormatVersion" value="normalize-space(gmd:distributionInfo/gmd:MD_Distribution/gmd:distributionFormat/gmd:MD_Format/gmd:version/gco:CharacterString)"/>
			<sch:let name="distributionFormatSpecification" value="normalize-space(gmd:distributionInfo/gmd:MD_Distribution/gmd:distributionFormat/gmd:MD_Format/gmd:specification/gco:CharacterString)"/>

	  <!-- Thijs: om fouten in de validator te voorkomen als er metadata is aangeleverd met meerdere blokken dataQualityInfo (de NGR editor kan dit soort fouten veroorzaken), gebruik altijd alleen het eerste blok. Doe dit bij alle elementen gmd:dataQualityInfo -->
		<!-- alle regels over elementen binnen gmd:dataQualityInfo -->
			<sch:let name="dataQualityInfo" value="gmd:dataQualityInfo[1]/gmd:DQ_DataQuality"/>
		<!-- Algemene beschrijving herkomst, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#algemene-beschrijving-herkomst  -->
			<sch:let name="statement" value="normalize-space($dataQualityInfo/gmd:lineage/gmd:LI_Lineage/gmd:statement/gco:CharacterString)"/>

			<sch:let name="dataQualityInfoCount" value="count(gmd:dataQualityInfo)" />
			<sch:assert id="dataQualityInfo_1_keer" etf_name="Kwaliteitsbeschrijving 1 keer" test="$dataQualityInfoCount &lt; 2">Set elementen kwaliteitsbeschrijving komt maximaal 1 keer voor</sch:assert>

			<sch:let name="levelCount" value="count($dataQualityInfo/gmd:scope/gmd:DQ_Scope/gmd:level)" />
			<sch:assert id="level_1_keer" etf_name="Niveau kwaliteitsbeschrijving 1 keer" test="$levelCount &lt; 2">Niveau kwaliteitsbeschrijving (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#niveau-kwaliteitsbeschrijving) komt vaker dan 1 keer voor</sch:assert>

		<!--  Niveau kwaliteitsbeschrijving, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#niveau-kwaliteitsbeschrijving -->
			<sch:let name="level" value="string($dataQualityInfo/gmd:scope/gmd:DQ_Scope/gmd:level/*/@codeListValue[. = 'dataset' or . = 'series'])"/>
		<!-- rules and assertions -->
			<sch:assert id="Algemene_beschrijving_herkomst_ISO_nr_83" etf_name="Algemene beschrijving herkomst (ISO nr. 83)" test="$statement">Algemene beschrijving herkomst (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#algemene-beschrijving-herkomst) ontbreekt</sch:assert>
			<sch:report id="Algemene_beschrijving_herkomst_ISO_nr_83_info" etf_name="Algemene beschrijving herkomst (ISO nr. 83) info" test="$statement">Algemene beschrijving herkomst (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#algemene-beschrijving-herkomst): <sch:value-of select="$statement"/>
			</sch:report>
			<sch:assert id="Niveau_kwaliteitsbeschrijving_ISO_nr139" etf_name="Niveau kwaliteitsbeschrijving (ISO nr.139)" test="$level">Niveau kwaliteitsbeschrijving (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#niveau-kwaliteitsbeschrijving) ontbreekt</sch:assert>
			<sch:report id="Niveau_kwaliteitsbeschrijving_ISO_nr139_info" etf_name="Niveau kwaliteitsbeschrijving (ISO nr.139) info" test="$level">Niveau kwaliteitsbeschrijving (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#niveau-kwaliteitsbeschrijving): <sch:value-of select="$level"/>
			</sch:report>

		</sch:rule>


		<!-- URL  naar een service -->

		 <sch:rule id="INSPIRE_Online_toegang_tot_de_bron" etf_name="INSPIRE Online toegang tot de bron" context="//gmd:MD_Metadata/gmd:distributionInfo/gmd:MD_Distribution/gmd:transferOptions/gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource">
			 <sch:let name="all_transferOptions_URL" value="ancestor::gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource/gmd:linkage"/>

			 <!-- URL voor INSPIRE in combi met specificatie INSPIRE -->
		 	<sch:assert id="URL_ISO_nr_397" etf_name="URL (ISO nr. 397)" test="not($conformity_Spec_Title_Exsists) or ($conformity_Spec_Title_Exsists and $all_transferOptions_URL[normalize-space(*/text())])">URL (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#url) onbreekt, voor INSPIRE is de link naar de gerelateerde services (view en download) verplicht.</sch:assert>

		<!-- eind  URL voor INSPIRE  -->
		</sch:rule>


		<!-- regels voor meerdere transfer options-->
		<!-- conditioneel -->
		<sch:rule id="Online_toegang_tot_de_bron" etf_name="Online toegang tot de bron" context="//gmd:MD_Metadata/gmd:distributionInfo/gmd:MD_Distribution/gmd:transferOptions/gmd:MD_DigitalTransferOptions/gmd:onLine">
		<!-- URL, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#url -->
			<sch:let name="transferOptions_URL" value="normalize-space(gmd:CI_OnlineResource/gmd:linkage/gmd:URL)"/>
		<!-- Protocol https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#protocol -->
		<!--
		MediaType
		Protocol
	 	-->
			<sch:let name="transferOptions_Protocol" value="gmd:CI_OnlineResource/gmd:protocol/*[normalize-space(text()) = 'OGC:CSW' or normalize-space(text()) = 'OGC:WMS' or normalize-space(text()) = 'OGC:WMTS' or normalize-space(text()) = 'OGC:WFS' or normalize-space(text()) = 'OGC:WCS' or normalize-space(text()) = 'INSPIRE Atom' or normalize-space(text()) = 'OGC:SOS' or normalize-space(text()) = 'OGC:API features' or normalize-space(text()) = 'OGC:SensorThings' or normalize-space(text()) = 'W3C:SPARQL' or normalize-space(text()) = 'OASIS:OData' or normalize-space(text()) = 'OAS' or normalize-space(text()) = 'landingpage'  or normalize-space(text()) = 'OGC:OLS' ]"/>


			<!-- Mediatypes: https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-mediatypes -->
			<sch:let name="transferOptions_Mediatype" value="gmd:CI_OnlineResource/gmd:protocol/*[translate(text(), $uppercase, $lowercase) = 'gml' or translate(text(), $uppercase, $lowercase) = 'kml' or translate(text(), $uppercase, $lowercase) = 'geojson' or translate(text(), $uppercase, $lowercase) = 'gpkg' or translate(text(), $uppercase, $lowercase) = 'json' or translate(text(), $uppercase, $lowercase) = 'jsonld' or translate(text(), $uppercase, $lowercase) = 'rdf-xml' or translate(text(), $uppercase, $lowercase) = 'xml' or translate(text(), $uppercase, $lowercase) = 'zip' or translate(text(), $uppercase, $lowercase) = 'png' or translate(text(), $uppercase, $lowercase) = 'png' or translate(text(), $uppercase, $lowercase) = 'gif' or translate(text(), $uppercase, $lowercase) = 'jp2' or translate(text(), $uppercase, $lowercase) = 'tiff' or translate(text(), $uppercase, $lowercase) = 'csv' or translate(text(), $uppercase, $lowercase) = 'mapbox-vector-tile']"/>

			<sch:let name="transferOptions_Protocol_isOGCService" value="gmd:CI_OnlineResource/gmd:protocol/*[text() = 'OGC:WMS' or text() = 'OGC:WFS' or text() = 'OGC:WCS' or text() = 'INSPIRE Atom']"/>
		<!-- Naam https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#naam -->
			<sch:let name="transferOptions_Name" value="normalize-space(gmd:CI_OnlineResource/gmd:name/gco:CharacterString)"/>

		<!-- Omschrijving, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#description
		Dit is conditioneel, dus opletten: Het is verplicht als er een URL van het type 'endPoint' is opgegeven.
		Kan een Anchor zijn.
		Bij een URL van het type 'accessPoint' kan het element leeg gelaten worden. Aanname is dat als er geen waarde is opgegeven, het een accessPoint is.
		Dit betekent dat er feitelijk niet getest kan worden op aanwezigheid: het ontbreken van het element is namelijk ook correct.
		-->
			<sch:let name="transferOptions_Description_Value" value="normalize-space(string-join(gmd:CI_OnlineResource/gmd:description[./gco:CharacterString or ./gmx:Anchor]//text(), ''))"/>

<!-- [string-length(normalize-space(text())) > 0] -->

		<!-- asssertions and report -->
			<sch:assert id="Protocol_ISO_nr_398_verplicht_bij_URL_ISO_nr_397" etf_name="Protocol (ISO nr. 398) verplicht bij URL (ISO nr. 397)" test="not($transferOptions_URL) or ($transferOptions_URL and ($transferOptions_Protocol or $transferOptions_Mediatype))">Protocol (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#protocol) is verplicht als URL (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#url) is ingevuld.</sch:assert>
			<sch:assert id="URL_ISO_nr_397_en_Protocol_ISO_nr_398" etf_name="URL (ISO nr. 397) en Protocol (ISO nr. 398)" test="not($transferOptions_Protocol or $transferOptions_Mediatype) or (($transferOptions_Protocol or $transferOptions_Mediatype) and $transferOptions_URL)">Protocol (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#protocol) alleen opnemen als URL (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#url) is ingevuld.</sch:assert>
			<sch:report id="URL_ISO_nr_397_info" etf_name="URL (ISO nr. 397) info" test="$transferOptions_URL"> URL (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#url): <sch:value-of select="$transferOptions_URL"/>
			</sch:report>
			<sch:report id="Protocol_ISO_nr_398_info" etf_name="Protocol (ISO nr. 398) info" test="$transferOptions_Protocol or $transferOptions_Mediatype">Protocol (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#protocol): <sch:value-of select="gmd:CI_OnlineResource/gmd:protocol/*/text()"/>
			</sch:report>
			<sch:assert id="Naam_ISO_nr_400" etf_name="Naam (ISO nr. 400)" test="not($transferOptions_Protocol_isOGCService) or ($transferOptions_Protocol_isOGCService and $transferOptions_Name)">Naam (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#naam) is verplicht als Protocol (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#protocol) de waarde OGC:WMS, OGC:WFS, OGC:WCS of INSPIRE Atom heeft.</sch:assert>
			<sch:report id="Naam_ISO_nr_400_info" etf_name="Naam (ISO nr. 400) info" test="$transferOptions_Name">Naam (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#naam) is : <sch:value-of select="$transferOptions_Name"/>
			</sch:report>

			<!-- Thijs Brentjens: nieuwe assertion. Als de URL een endpoint is, moet de codelijst https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-mediatypes gebruikt worden. Bij een accessPoint de codelijst https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-protocol-->
			<sch:assert id="Bij_een_URL_endPoint_moet_een_waarde_uit_de_codelijst_mediatype_opgegeven_zijn" etf_name="Bij een URL endPoint moet een waarde uit de codelijst mediatype opgegeven zijn" test="not($transferOptions_Description_Value = 'endPoint') or ($transferOptions_Description_Value = 'endPoint' and $transferOptions_Mediatype)">Protocol (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#protocol) moet een waarde uit de codelijst media types (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-mediatypes) bevatten als de URL omschrijving (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#Codelist-INSPIRE-OnLineDescriptionCode) een endPoint is.</sch:assert>
			<sch:assert id="Bij_een_URL_accessPoint_moet_een_waarde_uit_de_codelijst_protocol_opgegeven_zijn" etf_name="Bij een URL accessPoint moet een waarde uit de codelijst protocol opgegeven zijn" test="not($transferOptions_Description_Value = 'accessPoint') or ($transferOptions_Description_Value = 'accessPoint' and $transferOptions_Protocol)">Protocol (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#protocol) moet een waarde uit de codelijst protocol (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-protocol) bevatten als de URL omschrijving (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#Codelist-INSPIRE-OnLineDescriptionCode) een accessPoint is.</sch:assert>

		</sch:rule>

		<!-- Nieuw: codelijst protocol apart testen -->
		<!-- TODO: codelijst check moet niet-hoofdlettergevoelig zijn ?
		Let op de manier van de codelijst checken, zie ook mediatype: alles naar kleine letters omschrijven
		-->
		<sch:rule id="Codelijst_protocol" etf_name="Codelijst protocol" context="//gmd:distributionInfo/gmd:MD_Distribution/gmd:transferOptions/gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource/gmd:protocol/gmx:Anchor[text() = 'OGC:CSW' or text() = 'OGC:WMS' or text() = 'OGC:WMTS' or text() = 'OGC:WFS' or text() = 'OGC:WCS' or text() = 'OGC:WCTS' or text() = 'OGC:WPS' or text() = 'UKST' or text() = 'INSPIRE Atom' or text() = 'OGC:WFS-G' or text() = 'OGC:SOS' or text() = 'OGC:SPS' or text() = 'OGC:SAS' or text() = 'OGC:WNS' or text() = 'OGC:ODS' or text() = 'OGC:OGS' or text() = 'OGC:OUS' or text() = 'OGC:OPS' or text() = 'OGC:ORS' or text() = 'OGC:SensorThings' or text() = 'W3C:SPARQL' or text() = 'OASIS:OData' or text() = 'OAS' or text() = 'landingpage'  or text() = 'dataset' or text() = 'application' or text() = 'UKST']">

			<sch:let name="anchorUri" value="normalize-space(@xlink:href)"/>
			<sch:let name="txt" value="normalize-space(text())"/>
			<sch:let name="combination" value="concat($anchorUri, '=', $txt)"/>
			<sch:let name="codelist" value="'http://www.opengis.net/def/serviceType/ogc/csw=OGC:CSW, http://www.opengis.net/def/serviceType/ogc/wms=OGC:WMS, http://www.opengis.net/def/serviceType/ogc/wmts=OGC:WMTS, http://www.opengis.net/def/serviceType/ogc/wfs=OGC:WFS, http://www.opengis.net/def/serviceType/ogc/wcs=OGC:WCS, http://www.opengis.net/def/serviceType/ogc/sos=OGC:SOS, https://tools.ietf.org/html/rfc4287=INSPIRE Atom, http://www.opengis.net/def/interface/ogcapi-features=OGC:API features, http://www.opengeospatial.org/standards/ols=OGC:OLS, http://www.opengis.net/def/serviceType/ogc/ols=OGC:OLS, http://www.opengeospatial.org/standards/sensorthings=OGC:SensorThings, https://www.w3.org/TR/rdf-sparql-query/=W3C:SPARQL, https://www.oasis-open.org/committees/odata=OASIS:OData, https://github.com/OAI/OpenAPI-Specification/=OAS, https://www.w3.org/TR/vocab-dcat-2/#Property:resource_landing_page=landingpage'" />

			<sch:assert id="Codelijst_protocol:_geldige_combinatie_van_URI_en_waarde" etf_name="Codelijst protocol: geldige combinatie van URI en waarde" test="contains($codelist, $combination)">De combinatie van de URI '<sch:value-of select="$anchorUri"/>' en waarde '<sch:value-of select="$txt"/>' is geen geldige combinatie uit de codelijst protocol https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-protocol</sch:assert>

		</sch:rule>



		<sch:rule id="Codelijst_media_types" etf_name="Codelijst media types" context="//gmd:CI_OnlineResource/gmd:protocol/gmx:Anchor[translate(text(), $uppercase, $lowercase) = 'gml' or translate(text(), $uppercase, $lowercase) = 'kml' or translate(text(), $uppercase, $lowercase) = 'geojson' or translate(text(), $uppercase, $lowercase) = 'gpkg' or translate(text(), $uppercase, $lowercase) = 'json' or translate(text(), $uppercase, $lowercase) = 'jsonld' or translate(text(), $uppercase, $lowercase) = 'rdf-xml' or translate(text(), $uppercase, $lowercase) = 'xml' or translate(text(), $uppercase, $lowercase) = 'zip' or translate(text(), $uppercase, $lowercase) = 'png' or translate(text(), $uppercase, $lowercase) = 'png' or translate(text(), $uppercase, $lowercase) = 'gif' or translate(text(), $uppercase, $lowercase) = 'jp2' or translate(text(), $uppercase, $lowercase) = 'tiff' or translate(text(), $uppercase, $lowercase) = 'csv' or translate(text(), $uppercase, $lowercase) = 'mapbox-vector-tile']">
			<sch:let name="anchorUri" value="normalize-space(translate(@xlink:href, $uppercase, $lowercase))"/>
			<sch:let name="txt" value="normalize-space(translate(text(), $uppercase, $lowercase))"/>
			<sch:let name="combination" value="concat($anchorUri, '=', $txt)"/>
			<sch:let name="codelist" value="'https://www.iana.org/assignments/media-types/application/gml+xml=gml, http://www.iana.org/assignments/media-types/application/vnd.google-earth.kml+xml=kml, https://www.iana.org/assignments/media-types/application/geo+json=geojson, http://inspire.ec.europa.eu/media-types/application/x-sqlite3=gpkg, https://www.iana.org/assignments/media-types/application/json=json, https://www.iana.org/assignments/media-types/application/ld+json=jsonld, https://www.iana.org/assignments/media-types/application/rdf+xml=rdf-xml, https://www.iana.org/assignments/media-types/application/xml=xml, https://www.iana.org/assignments/media-types/application/zip=zip, https://www.iana.org/assignments/media-types/image/png=png, https://www.iana.org/assignments/media-types/image/gif=gif, https://www.iana.org/assignments/media-types/image/jp2=jp2, https://www.iana.org/assignments/media-types/image/tiff=tiff, https://www.iana.org/assignments/media-types/text/csv=csv, https://www.iana.org/assignments/media-types/application/vnd.mapbox-vector-tile=mapbox-vector-tile'" />

			<sch:assert id="Codelijst_media_types:_geldige_combinatie_van_URI_en_waarde" etf_name="Codelijst media types: geldige combinatie van URI en waarde" test="contains($codelist, $combination)">De combinatie van de URI '<sch:value-of select="$anchorUri"/>' en waarde '<sch:value-of select="$txt"/>' is geen geldige combinatie uit de codelijst media types https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-mediatypes</sch:assert>
		</sch:rule>

		<sch:rule id="Codelijst_INSPIRE_OnLineDescriptionCode" etf_name="Codelijst INSPIRE OnLineDescriptionCode" context="//gmd:CI_OnlineResource/gmd:description/gmx:Anchor">
			<sch:let name="anchorUri" value="normalize-space(translate(@xlink:href, $uppercase, $lowercase))"/>
			<sch:let name="txt" value="normalize-space(translate(text(), $uppercase, $lowercase))"/>
			<sch:let name="combination" value="concat($anchorUri, '=', $txt)"/>
			<!-- NB: lowercase -->
			<sch:let name="codelist" value="'http://inspire.ec.europa.eu/metadata-codelist/onlinedescriptioncode/endpoint=endpoint, http://inspire.ec.europa.eu/metadata-codelist/onlinedescriptioncode/accesspoint=accesspoint'" />

			<sch:assert id="Codelijst_INSPIRE_OnLineDescriptionCode:_geldige_combinatie_van_URI_en_waarde" etf_name="Codelijst INSPIRE OnLineDescriptionCode: geldige combinatie van URI en waarde" test="contains($codelist, $combination)">De combinatie van de URI '<sch:value-of select="$anchorUri"/>' en waarde '<sch:value-of select="$txt"/>' is geen geldige combinatie uit de codelijst INSPIRE OnLineDescriptionCode https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#Codelist-INSPIRE-OnLineDescriptionCode</sch:assert>

		</sch:rule>

		<!--  Conformiteitindicatie meerdere specificaties -->
		<sch:rule id="Conformiteit_specificaties" etf_name="Conformiteit specificaties" context="//gmd:MD_Metadata/gmd:dataQualityInfo[1]/gmd:DQ_DataQuality/gmd:report/gmd:DQ_DomainConsistency/gmd:result/gmd:DQ_ConformanceResult">

		<!-- Specificatie title, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatie
	 	TODO: check of URI ook bij andere tests voor conformity_SpecTitle nodig is, of dat het zo volstaat -->
			<sch:let name="conformity_SpecTitle" value="normalize-space(string-join(gmd:specification/gmd:CI_Citation/gmd:title[./gco:CharacterString or ./gmx:Anchor]//text(), ''))"/>
			<sch:let name="conformity_SpecTitleString" value="normalize-space(gmd:specification/gmd:CI_Citation/gmd:title/gco:CharacterString)"/>
			<sch:let name="conformity_SpecTitleURI" value="normalize-space(gmd:specification/gmd:CI_Citation/gmd:title/gmx:Anchor/@xlink:href)"/>
		<!-- Verklaring https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verklaring -->
			<sch:let name="conformity_Explanation" value="normalize-space(gmd:explanation/gco:CharacterString)"/>

		<!-- Specificatie date https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatiedatum	-->
			<sch:let name="conformity_DateString" value="string(gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:date/gco:Date)"/>
			<sch:let name="conformity_Date" value="((number(substring(substring-before($conformity_DateString,'-'),1,4)) &gt; 1000 ))"/>

			<!-- Specificatiedatum type https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatiedatum-type -->
			<!-- Codelijst: https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-datetypecode -->
			<sch:let name="conformity_Datetype" value="gmd:specification/gmd:CI_Citation/gmd:date/gmd:CI_Date/gmd:dateType/*[@codeListValue='creation' or @codeListValue='publication' or @codeListValue='revision']"/>
			<sch:let name="conformity_SpecCreationDate" value="gmd:specification/gmd:CI_Citation/gmd:date[./gmd:CI_Date/gmd:dateType/*/@codeListValue='creation']/*/gmd:date/gco:Date"/>
			<sch:let name="conformity_SpecPublicationDate" value="gmd:specification/gmd:CI_Citation/gmd:date[./gmd:CI_Date/gmd:dateType/*/@codeListValue='publication']/*/gmd:date/gco:Date"/>
			<sch:let name="conformity_SpecRevisionDate" value="gmd:specification/gmd:CI_Citation/gmd:date[./gmd:CI_Date/gmd:dateType/*/@codeListValue='revision']/*/gmd:date/gco:Date"/>

			<!-- Conformiteit indicatie met de specificatie https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#conformiteit-indicatie-met-de-specificatie -->
			<sch:let name="conformity_Pass" value="normalize-space(gmd:pass/gco:Boolean)"/>

		<!-- Specificatie alleen voor INSPIRE-->
			<!--
			<sch:assert id="INSPIRE_Specificatie_ISO_nr_360_" etf_name="INSPIRE Specificatie (ISO nr. 360 )" test="$conformity_SpecTitle">Specificatie (ISO nr. 360 ) ontbreekt.</sch:assert>
			<sch:assert id="INSPIRE_Verklaring_ISO_nr_131" etf_name="INSPIRE Verklaring (ISO nr. 131)" test="$conformity_Explanation">Verklaring (ISO nr. 131) ontbreekt.</sch:assert>
			<sch:assert id="INSPIRE_Specificatie_datum_ISO_nr_394" etf_name="INSPIRE Specificatie datum (ISO nr. 394" test="$conformity_Date">Specificatie datum (ISO nr. 394) ontbreekt.</sch:assert>
			<sch:assert id="INSPIRE_Specificatiedatum_type_ISO_nr_395" etf_name="INSPIRE Specificatiedatum type (ISO nr. 395)" test="$conformity_Datetype">Specificatiedatum type (ISO nr. 395) ontbreekt.</sch:assert>
			<sch:assert id="INSPIRE_Conformiteitindicatie_met_de_specificatie__ISO_nr_132" etf_name="INSPIRE Conformiteitindicatie met de specificatie  (ISO nr. 132)" test="$conformity_Pass">Conformiteitindicatie met de specificatie  (ISO nr. 132) ontbreekt.</sch:assert>
		-->
		<!-- eind Specificatie alleen voor INSPIRE-->

		<!-- Voor niet INSPIRE data als title is ingevuld, moeten date, datetype, explanation en pass ingevuld zijn -->

			<sch:assert id="Verklaring_ISO_nr_131_en_Specificatie_ISO_nr_360" etf_name="Verklaring (ISO nr. 131) en Specificatie (ISO nr. 360)" test="not($conformity_SpecTitle) or ($conformity_SpecTitle and $conformity_Explanation)">Verklaring (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verklaring) is verplicht als een specificatie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatie) is opgegeven.</sch:assert>
			<sch:assert id="Datum_ISO_nr_394_en_Specificatie_ISO_nr_360" etf_name="Datum (ISO nr. 394) en Specificatie (ISO nr. 360)" test="not($conformity_SpecTitle and not($conformity_Date))">Datum (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatiedatum) is verplicht als een specificatie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatie) is opgegeven. </sch:assert>
			<sch:assert id="Datumtype_ISO_nr_395__en_Specificatie_ISO_nr_360" etf_name="Datumtype (ISO nr. 395)  en Specificatie (ISO nr. 360)" test="not($conformity_SpecTitle and not($conformity_Datetype))">Datumtype (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatiedatum-type) is verplicht als een specificatie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatie) is opgegeven. </sch:assert>
			<sch:assert id="Conformiteit_ISO_nr_132__en_Specificatie_ISO_nr_360" etf_name="Conformiteit (ISO nr. 132)  en Specificatie (ISO nr. 360)" test="not($conformity_SpecTitle) or ($conformity_SpecTitle and $conformity_Pass)">Conformiteit (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#conformiteit-indicatie-met-de-specificatie) is verplicht als een specificatie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatie) is opgegeven.</sch:assert>


		<!-- als er geen titel is ingevuld, moeten date, datetype explanation en pass leeg zijn -->
			<sch:assert id="Verklaring_ISO_nr_131" etf_name="Verklaring (ISO nr. 131)" test="not($conformity_Explanation) or ($conformity_Explanation and $conformity_SpecTitle)">Verklaring (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verklaring) hoort leeg te zijn als geen specificatie is opgegeven</sch:assert>
			<sch:assert id="Datum_ISO_nr_394" etf_name="Datum (ISO nr. 394)" test="not($conformity_Date and not($conformity_SpecTitle))">Datum (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatiedatum) hoort leeg te zijn als geen specificatie is opgegeven. </sch:assert>
			<sch:assert id="Datumtype_ISO_nr_395" etf_name="Datumtype (ISO nr. 395)" test="not($conformity_Datetype and not($conformity_SpecTitle))">Datumtype (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatiedatum-type) hoort leeg te zijn als geen specificatie is opgegeven.</sch:assert>
			<sch:assert id="Conformiteit_ISO_nr_132" etf_name="Conformiteit (ISO nr. 132" test="not($conformity_Pass) or ($conformity_Pass and $conformity_SpecTitle)">Conformiteit (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#conformiteit-indicatie-met-de-specificatie) hoort leeg te zijn als geen specificatie is opgegeven.</sch:assert>

			<sch:report id="Verklaring_ISO_nr_131_info" etf_name="Verklaring (ISO nr. 131) info" test="$conformity_Explanation">Verklaring (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#verklaring): <sch:value-of select="$conformity_Explanation"/>
			</sch:report>
			<sch:report id="Conformiteitindicatie_met_de_specificatie_ISO_nr_132_info" etf_name="Conformiteitindicatie met de specificatie (ISO nr. 132) info" test="$conformity_Pass">Conformiteitindicatie met de specificatie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#conformiteit-indicatie-met-de-specificatie): <sch:value-of select="$conformity_Pass"/>
			</sch:report>
			<sch:report id="Specificatie_ISO_nr_360_info" etf_name="Specificatie (ISO nr. 360) info" test="$conformity_SpecTitleString or $conformity_SpecTitleURI">Specificatie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatie): <sch:value-of select="$conformity_SpecTitleString"/><sch:value-of select="$conformity_SpecTitleURI"/>
			</sch:report>
			<sch:report id="Datum_ISO_nr_394_en_datum_type_ISO_nr_395_info" etf_name="Datum (ISO nr. 394) en datum type (ISO nr. 395) info" test="$conformity_SpecCreationDate or $conformity_SpecPublicationDate or $conformity_SpecRevisionDate">Datum (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatiedatum) en datum type (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#specificatiedatum-type) zijn aanwezig voor specificatie.</sch:report>
		</sch:rule>

   		<!-- Resolutie en toepassingschaal -->
		<sch:rule id="Resolutie_en_toepassingschaal" etf_name="Resolutie en toepassingschaal" context="//gmd:identificationInfo/gmd:MD_DataIdentification">
       			<sch:let name="distance" value="gmd:spatialResolution/gmd:MD_Resolution/gmd:distance/*/text()"/>
       			<sch:let name="denominator" value="gmd:spatialResolution/gmd:MD_Resolution/gmd:equivalentScale/gmd:MD_RepresentativeFraction/gmd:denominator/*/text()"/>

			<sch:assert id="Toepassingsschaal_ISO_nr_57_of_Resolutie_ISO_nr_61" etf_name="Toepassingsschaal (ISO nr. 57) of Resolutie (ISO nr. 61)" test="$denominator or $distance ">Toepassingsschaal (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#toepassingsschaal) of Resolutie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#resolutie) ontbreekt, vul een van deze in</sch:assert>
			<sch:assert id="Toepassingsschaal_ISO_nr_57_en_Resolutie_ISO_nr_61" etf_name="Toepassingsschaal (ISO nr. 57) en Resolutie (ISO nr. 61)" test="not($denominator and  $distance) ">Toepassingsschaal (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#toepassingsschaal) of Resolutie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#resolutie) invullen, niet beide</sch:assert>
       		</sch:rule>

		<!-- Spatial resolution equivalentScale https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#toepassingsschaal -->
		<sch:rule id="Toepassingsschaal" etf_name="Toepassingsschaal" context="//gmd:spatialResolution/gmd:MD_Resolution/gmd:equivalentScale/gmd:MD_RepresentativeFraction/gmd:denominator/gco:Integer">
			<sch:let name="denominator" value="text()"/>
			<sch:assert id="Toepassingsschaal_ISO_nr_57" etf_name="Toepassingsschaal (ISO nr. 57)" test="not(string(number($denominator))='NaN')">Toepassingsschaal (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#toepassingsschaal) heeft een verkeerde waarde, toepassingsschaal is niet numeriek of is leeg.</sch:assert>

			<sch:report id="Toepassingsschaal_ISO_nr_57_info" etf_name="Toepassingsschaal (ISO nr. 57) info" test="$denominator">Toepassingsschaal (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#toepassingsschaal): <sch:value-of select="$denominator"/>
			</sch:report>
		</sch:rule>

		<!-- Spatial resolution distance https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#resolutie -->
		<sch:rule id="Resolutie" etf_name="Resolutie" context="//gmd:spatialResolution/gmd:MD_Resolution/gmd:distance/gco:Distance">

			<sch:let name="distance" value="text()"/>
			<sch:let name="distance_UOM" value="@uom= 'meters' "/>

			<sch:assert id="Resolutie_ISO_nr_61" etf_name="Resolutie (ISO nr. 61)" test="not(string(number($distance))='NaN')">Resolutie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#resolutie) heeft een verkeerde waarde, resolutie is niet numeriek of is leeg</sch:assert>
			<sch:report id="Resolutie_ISO_nr_61_info" etf_name="Resolutie (ISO nr. 61) info" test="$distance">Resolutie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#resolutie) is: <sch:value-of select="$distance"/>
			</sch:report>

			<sch:assert id="Resolutie_ISO_nr_61_meeteenheid" etf_name="Resolutie (ISO nr. 61) meeteenheid" test="$distance_UOM" see="https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#resolutie">Resolutie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#resolutie) heeft geen of een verkeerde waarde voor Unit of measure, de waarde moet meters zijn.</sch:assert>
			<sch:report id="Resolutie_ISO_nr_61_meeteenheid_info" etf_name="Resolutie (ISO nr. 61) meeteenheid info" test="$distance_UOM" see="https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#resolutie">Unit of measure voor Resolutie (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#resolutie): <sch:value-of select="$distance_UOM"/>
			</sch:report >
		</sch:rule>


		<!-- Referentiesysteem  -->
		<sch:rule id="Referentiesysteem" etf_name="Referentiesysteem" context="//gmd:MD_Metadata/gmd:referenceSystemInfo/gmd:MD_ReferenceSystem">
			<!--  Ruimtelijk referentiesysteem  https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codereferentiesysteem -->
			<!-- RS_Identifier -->
			<sch:let name="referenceSystemInfo_CodeString" value="normalize-space(gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:code/gco:CharacterString)"/>
			<sch:let name="referenceSystemInfo_CodeURI" value="normalize-space(gmd:referenceSystemIdentifier/gmd:RS_Identifier/gmd:code/gmx:Anchor[string-length(normalize-space(string-join(text(), ''))) > 0]/@xlink:href)"/>

			<sch:assert id="Code_referentiesysteem_ISO_nr_207_2" etf_name="Code referentiesysteem (ISO nr. 207)" test="$referenceSystemInfo_CodeString or $referenceSystemInfo_CodeURI">Code referentiesysteem (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codereferentiesysteem) waarde en/of URI ontbreekt</sch:assert>
			<sch:report id="Code_referentiesysteem_ISO_nr_207_info" etf_name="Code referentiesysteem (ISO nr. 207) info" test="$referenceSystemInfo_CodeString or $referenceSystemInfo_CodeURI">Code referentiesysteem (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codereferentiesysteem): <sch:value-of select="$referenceSystemInfo_CodeString"/> <sch:value-of select="$referenceSystemInfo_CodeURI"/>
			</sch:report>

			<sch:assert id="Code_referentiesysteem_ISO_nr_207_is_een_URI" etf_name="Code referentiesysteem (ISO nr. 207) is een URI" test="starts-with($referenceSystemInfo_CodeString,'http') or starts-with($referenceSystemInfo_CodeURI,'http')">Code referentiesysteem (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codereferentiesysteem) moet een URI zijn, maar is dat niet. Opgegeven waarde: <sch:value-of select="$referenceSystemInfo_CodeString"/> <sch:value-of select="$referenceSystemInfo_CodeURI"/></sch:assert>

		</sch:rule>

        <!-- Tests rondom Thesauri Controlled originating vocabulary, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#thesaurus e.v. -->
		<sch:rule id="Thesaurus" etf_name="Thesaurus" context="//gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation">

			<!-- 5.2.14 Thesaurus title, https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#thesaurus -->

			<!-- all_thesaurus_Titles: niet gebruikt, dus uitgecommentarieerd voor nu -->
			<!-- <sch:let name="all_thesaurus_Titles" value="ancestor::gmd:MD_DataIdentification/gmd:descriptiveKeywords/gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation/gmd:title"/> -->

			<sch:let name="thesaurus_TitleString" value="normalize-space(gmd:title/gco:CharacterString)"/>
			<sch:let name="thesaurus_TitleURI" value="normalize-space(gmd:title/gmx:Anchor[string-length(normalize-space(text())) > 0]/@xlink:href)"/>

			<!-- 5.2.15 Thesaurusdatum https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#thesaurusdatum -->
			<sch:let name="thesaurus_publicationDateSring" value="string(gmd:date[./gmd:CI_Date/gmd:dateType/*/@codeListValue='publication']/*/gmd:date/gco:Date)"/>
			<sch:let name="thesaurus_creationDateString" value="string(gmd:date[./gmd:CI_Date/gmd:dateType/*/@codeListValue='creation']/*/gmd:date/gco:Date)"/>
			<sch:let name="thesaurus_revisionDateString" value="string(gmd:date[./gmd:CI_Date/gmd:dateType/*/@codeListValue='revision']/*/gmd:date/gco:Date)"/>
			<sch:let name="thesaurus_PublicationDate" value="((number(substring(substring-before($thesaurus_publicationDateSring,'-'),1,4)) &gt; 1000 ))"/>
			<sch:let name="thesaurus_CreationDate" value="((number(substring(substring-before($thesaurus_creationDateString,'-'),1,4)) &gt; 1000 ))"/>
			<sch:let name="thesaurus_RevisionDate" value="((number(substring(substring-before($thesaurus_revisionDateString,'-'),1,4)) &gt; 1000 ))"/>

        	<!-- Thesaurus datum en datumtype 5.2.15 Thesaurusdatum https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#thesaurusdatum en
			 5.2.16 Thesaurusdatum type https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#thesaurusdatum-type -->
			<sch:assert id="thesaurus_datum_ISO_nr394_en_datumtype_ISO_nr_395" etf_name="thesaurus datum (ISO nr.394) en datumtype (ISO nr. 395)" test="($thesaurus_TitleString or $thesaurus_TitleURI) and ($thesaurus_CreationDate or $thesaurus_PublicationDate or $thesaurus_RevisionDate)">Een thesaurus datum (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#thesaurusdatum) en datumtype (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#thesaurusdatum-type) is verplicht als Thesaurus title (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#thesaurus) is opgegeven. Datum formaat moet YYYY-MM-DD zijn. (Thesaurus: <sch:value-of select="$thesaurus_TitleString"/><sch:value-of select="$thesaurus_TitleURI"/>) </sch:assert>

		</sch:rule>

		<sch:rule id="Temporele_dekking" etf_name="Temporele dekking" context="//gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:extent/gmd:EX_Extent/gmd:temporalElement/*/gmd:extent">
			<!-- section 6.2 https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#Tempporele-dekking -->
			<sch:let name="timePeriodBegin" value="gml:TimePeriod//gml:beginPosition"/>
			<sch:let name="timePeriodEnd" value="gml:TimePeriod//gml:endPosition"/>
			<sch:let name="timePosition" value="gml:TimeInstant//gml:timePosition"/>
			<sch:assert id="Temporele_dekking_period_of_position" etf_name="Temporele dekking is een periode of individuele datum" test="$timePeriodBegin or $timePosition">De Temporele dekking is opgegeven. Er moet dan een tijdsperiode of individuele datum worden opgegeven conform https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#Tempporele-dekking, maar dat is niet het geval.</sch:assert>

			<sch:assert id="Temporele_dekking_beginposition_indeterminate" etf_name="Het attribuut indeterminatePosition ontbreekt bij een lege beginPosition" test="(string-length(normalize-space($timePeriodBegin)) &lt; 4 and ($timePeriodBegin/@indeterminatePosition='unknown' or $timePeriodBegin/@indeterminatePosition='now')) or string-length(normalize-space($timePeriodBegin)) &gt; 3">De beginPosition van de Temporele Dekking is opgegeven, maar leeg. In dat geval moet het attribuut indeterminatePosition opgegeven zijn, maar dat is niet het geval. Zie https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#Tempporele-dekking</sch:assert>

			<sch:assert id="Temporele_dekking_endposition_indeterminate" etf_name="Het attribuut indeterminatePosition ontbreekt bij een lege endPosition" test="(string-length(normalize-space($timePeriodEnd)) &lt; 4 and ($timePeriodEnd/@indeterminatePosition='unknown' or $timePeriodEnd/@indeterminatePosition='now')) or string-length(normalize-space($timePeriodEnd)) &gt; 3">De endPosition van de Temporele Dekking is opgegeven, maar leeg. In dat geval moet het attribuut indeterminatePosition opgegeven zijn, maar dat is niet het geval. Zie https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#Tempporele-dekking</sch:assert>

		</sch:rule>

		<!-- new in profile v2.0: account for gmx:Anchor -->
		<sch:rule id="INSPIRE_Thesaurus_trefwoorden" etf_name="INSPIRE Thesaurus trefwoorden" context="//gmd:MD_Metadata/gmd:identificationInfo/gmd:MD_DataIdentification/gmd:descriptiveKeywords
			[normalize-space(gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation/gmd:title/gco:CharacterString) = 'GEMET - INSPIRE themes, version 1.0' or gmd:MD_Keywords/gmd:thesaurusName/gmd:CI_Citation/gmd:title/gmx:Anchor/@xlink:href = 'http://www.eionet.europa.eu/gemet/nl/inspire-themes/']
			/gmd:MD_Keywords/gmd:keyword">

			<!-- Controlled originating vocabulary -->
			<sch:let name="quote" value="&quot;'&quot;"/>
			<sch:assert id="INSPIRE_Trefwoorden_ISO_nr_53" etf_name="INSPIRE Trefwoorden (ISO nr. 53)" test="((normalize-space(current())='Administratieve eenheden'
)
		        or (normalize-space(current())='Adressen'
)
		        or (normalize-space(current())='Atmosferische omstandigheden'
)
		        or (normalize-space(current())='Beschermde gebieden'
)
		        or (normalize-space(current())='Biogeografische gebieden'
)
		        or (normalize-space(current())='Bodem')
		         or (normalize-space(current())='Bodemgebruik')
		         or (normalize-space(current())='Energiebronnen')
		         or (normalize-space(current())='Faciliteiten voor landbouw en aquacultuur')
		         or (normalize-space(current())='Faciliteiten voor productie en industrie')
		        or (normalize-space(current())=concat('Gebieden met natuurrisico',$quote,'s'))
		         or (normalize-space(current())='Gebiedsbeheer, gebieden waar beperkingen gelden, gereguleerde gebieden en rapportage-eenheden')
		         or (normalize-space(current())='Gebouwen')
		         or (normalize-space(current())='Geografisch rastersysteem')
		         or (normalize-space(current())='Geografische namen')
		         or (normalize-space(current())='Geologie')
		         or (normalize-space(current())='Habitats en biotopen')
		         or (normalize-space(current())='Hoogte')
		         or (normalize-space(current())='Hydrografie')
		         or (normalize-space(current())='Kadastrale percelen')
		         or (normalize-space(current())='Landgebruik')
		         or (normalize-space(current())='Menselijke gezondheid en veiligheid')
		         or (normalize-space(current())='Meteorologische geografische kenmerken')
		         or (normalize-space(current())='Milieubewakingsvoorzieningen')
		         or (normalize-space(current())='Minerale bronnen')
		         or (normalize-space(current())='Nutsdiensten en overheidsdiensten')
		         or (normalize-space(current())='Oceanografische geografische kenmerken')
		         or (normalize-space(current())='Orthobeeldvorming')
		         or (normalize-space(current())='Spreiding van de bevolking — demografie')
		         or (normalize-space(current())='Spreiding van soorten')
		         or (normalize-space(current())='Statistische eenheden')
		         or (normalize-space(current())='Systemen voor verwijzing door middel van coördinaten')
		         or (normalize-space(current())='Vervoersnetwerken')
		         or (normalize-space(current())='Zeegebieden'))">
Deze keywords moeten uit GEMET- INSPIRE themes thesaurus komen. Gevonden keywords: <sch:value-of select="./*[../gco:CharacterString or ../gmx:Anchor]"/></sch:assert>

		<!--eind  Controlled originating vocabulary -  -->

		</sch:rule>

		<!-- Rule id="Waarschuwingen_-_INSPIRE_dataservice_koppeling" moved to dataset/sch_19115_warnings.xml -->


	</sch:pattern>
</sch:schema>
