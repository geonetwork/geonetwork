<?xml version="1.0" encoding="UTF-8"?>
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">
	<sch:ns uri="http://www.isotc211.org/2005/gmd" prefix="gmd"/>
	<sch:ns uri="http://www.isotc211.org/2005/gco" prefix="gco"/>
	<sch:ns uri="http://www.isotc211.org/2005/gmx" prefix="gmx"/>
	<sch:ns uri="http://www.opengis.net/gml" prefix="gml"/>
	<sch:ns uri="http://www.w3.org/1999/xlink" prefix="xlink"/>
	<sch:ns uri="http://www.w3.org/2001/XMLSchema-instance" prefix="xsi"/>
	<sch:ns uri="http://www.w3.org/2004/02/skos/core#" prefix="skos"/>
	<sch:ns uri="http://www.fao.org/geonetwork" prefix="geonet"/>

	<sch:let name="lowercase" value="'abcdefghijklmnopqrstuvwxyz'"/>
	<sch:let name="uppercase" value="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>

	<sch:pattern id="Waarschuwingen bij validatie tegen het Nederlands metadata profiel op ISO 19115 versie 2.1">
		<sch:title>Waarschuwingen bij validatie tegen het Nederlands profiel op ISO 19115 voor geografie versie 2.1.0 (2020)</sch:title>
		
		<sch:rule id="Waarschuwingen_-_Codelijst_protocol" etf_name="Waarschuwingen - Codelijst protocol" context="//gmd:CI_OnlineResource/gmd:protocol/gmx:Anchor[text() = 'OGC:CSW' or text() = 'OGC:WMS' or text() = 'OGC:WMTS' or text() = 'OGC:WFS' or text() = 'OGC:WCS' or text() = 'OGC:WCTS' or text() = 'OGC:WPS' or text() = 'UKST' or text() = 'INSPIRE Atom' or text() = 'OGC:WFS-G' or text() = 'OGC:SOS' or text() = 'OGC:SPS' or text() = 'OGC:SAS' or text() = 'OGC:WNS' or text() = 'OGC:ODS' or text() = 'OGC:OGS' or text() = 'OGC:OUS' or text() = 'OGC:OPS' or text() = 'OGC:ORS' or text() = 'OGC:SensorThings' or text() = 'W3C:SPARQL' or text() = 'OASIS:OData' or text() = 'OAS' or text() = 'landingpage'  or text() = 'dataset' or text() = 'application' or text() = 'UKST']">

			<sch:let name="anchorUri" value="normalize-space(@xlink:href)"/>
			<sch:let name="txt" value="normalize-space(text())"/>
			<sch:let name="combination" value="concat($anchorUri, '=', $txt)"/>
			<sch:let name="codelist" value="'http://www.opengeospatial.org/standards/cat=OGC:CSW, http://www.opengeospatial.org/standards/wms=OGC:WMS, http://www.opengeospatial.org/standards/wmts=OGC:WMTS, http://www.opengeospatial.org/standards/wfs=OGC:WFS, http://www.opengeospatial.org/standards/wcs=OGC:WCS, http://www.opengeospatial.org/standards/sos=OGC:SOS, =INSPIRE Atom, http://www.opengis.net/def/serviceType/ogc/csw=OGC:CSW, http://www.opengis.net/def/serviceType/ogc/wms=OGC:WMS, http://www.opengis.net/def/serviceType/ogc/wmts=OGC:WMTS, http://www.opengis.net/def/serviceType/ogc/wfs=OGC:WFS, http://www.opengis.net/def/serviceType/ogc/wcs=OGC:WCS, http://www.opengis.net/def/serviceType/ogc/sos=OGC:SOS, https://tools.ietf.org/html/rfc4287=INSPIRE Atom, http://www.opengeospatial.org/standards/=OGC:WCTS, http://www.opengeospatial.org/standards/wps=OGC:WPS, =OGC:WFS-G, http://www.opengeospatial.org/standards/sps=OGC:SPS, http://www.ogcnetwork.net/SAS=OGC:SAS, =OGC:WNS, http://www.opengeospatial.org/standards/ols#ODS=OGC:ODS, http://www.opengeospatial.org/standards/ols#OGS=OGC:OGS, http://www.opengeospatial.org/standards/ols#OUS=OGC:OUS, http://www.opengeospatial.org/standards/ols#OPS=OGC:OPS, http://www.opengeospatial.org/standards/ols#ORS=OGC:ORS, http://www.opengeospatial.org/standards/sensorthings=OGC:SensorThings, https://www.w3.org/TR/rdf-sparql-query/=W3C:SPARQL, https://www.oasis-open.org/committees/odata=OASIS:OData, https://github.com/OAI/OpenAPI-Specification/blob/master/versions/3.0.0.md=OAS, =landingpage, =application, =dataset, =UKST'" />

			<sch:let name="codelistNew" value="'http://www.opengis.net/def/serviceType/ogc/csw=OGC:CSW, http://www.opengis.net/def/serviceType/ogc/wms=OGC:WMS, http://www.opengis.net/def/serviceType/ogc/wmts=OGC:WMTS, http://www.opengis.net/def/serviceType/ogc/wfs=OGC:WFS, http://www.opengis.net/def/serviceType/ogc/wcs=OGC:WCS, http://www.opengis.net/def/serviceType/ogc/sos=OGC:SOS, https://tools.ietf.org/html/rfc4287=INSPIRE Atom'" />
			<sch:let name="codelistNewServiceTypes" value="'OGC:CSW, OGC:WMS, OGC:WFS, OGC:WMTS, OGC:SOS, OGC:WCS, INSPIRE Atom'"/>
			<!-- <sch:assert id="Codelijst_protocol_oude_URI_in_gebruik" etf_name="Codelijst protocol: er zijn nog codes voor protocol in gebruik uit de codelijst protocol die gaan vervallen" test="contains($codelist, $combination)">De combinatie van de URI '<sch:value-of select="$anchorUri"/>' en waarde '<sch:value-of select="$txt"/>' komt binnenkort te vervallen uit de codelijst https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#codelist-protocol. Zie http://inspire.ec.europa.eu/metadata-codelist/ProtocolValue voor de codes die gebruikt gaan worden</sch:assert> -->
			<sch:assert id="Waarschuwing_Codelijst_protocol_combinatie_van_URI_en_waarde_oud" etf_name="Waarschuwing - Codelijst protocol: geldige combinatie van URI en waarde INSPIRE codelist protocol" test="contains($codelistNew, $combination) or not(contains($codelistNewServiceTypes, $txt))">De combinatie van de URI '<sch:value-of select="$anchorUri"/>' en waarde '<sch:value-of select="$txt"/>' is binnenkort geen geldige combinatie meer. De codelijst http://inspire.ec.europa.eu/metadata-codelist/ProtocolValue bevat de nieuwe codes voor Protocol voor de service types: <sch:value-of select="$codelistNewServiceTypes"/>. Advies is om de nieuwe URI te gebruiken uit de codelist http://inspire.ec.europa.eu/metadata-codelist/ProtocolValue.</sch:assert>
		</sch:rule>

		<sch:rule id="Waarschuwingen_-_INSPIRE_dataservice_koppeling" etf_name="Waarschuwingen - INSPIRE dataservice koppeling" context="//gmd:MD_Metadata/gmd:distributionInfo/gmd:MD_Distribution/gmd:transferOptions">

			<sch:let name="nrOnLine_Protocol_Without_Description" value="count(gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource[not(./gmd:description)])"/>

			<sch:assert id="Omschrijving_verplicht_voor_een_URL" etf_name="Omschrijving accessPoint of endPoint is verplicht voor een URL" test="$nrOnLine_Protocol_Without_Description = 0">Voor de INSPIRE dataservice koppeling is het verplicht dat alle distributie URLs een omschrijving (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#description) hebben. Voor <sch:value-of select="$nrOnLine_Protocol_Without_Description"/> URL(s) ontbreekt echter een omschrijving. Zie de URLs: <sch:value-of select="normalize-space(string-join(gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource/gmd:linkage/gmd:URL[not(../../gmd:description)], ', '))"/>.
				</sch:assert>

			<sch:let name="nr_onLine_Description_with_CharacterStrings" value="count(gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource/gmd:description/gco:CharacterString)"/>

			<sch:assert id="Anchor_verplicht_voor_omschrijving" etf_name="Anchor verplicht voor element omschrijving accessPoint of endPoint" test="$nr_onLine_Description_with_CharacterStrings = 0">Voor de INSPIRE dataservice koppeling is het verplicht dat in het element omschrijving van een URL (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#description) in een Anchor is opgenomen. Voor <sch:value-of select="$nr_onLine_Description_with_CharacterStrings"/> URL(s) is de omschrijving in een CharacterString opgenomen. Zie de omschrijvingen: <sch:value-of select="normalize-space(string-join(gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource/gmd:description/gco:CharacterString, ', '))"/>.</sch:assert>

			<!-- Protocol moet altijd opgegeven zijn. Iets andere test dan eerdere test op protocol en URL, daarin wordt tegen een specifieke codelijst getest. Voor de INSPIRE dataset service koppeling kan die codelijst anders worden, dus deze test controleert alleen of er een element protocol is opgegeven -->
			<sch:let name="nrOnLine_URL_Without_Protocol" value="count(gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource[not(./gmd:protocol)])"/>
			<sch:assert id="Protocol_verplicht_voor_een_URL" etf_name="Protocol verplicht voor een URL" test="$nrOnLine_URL_Without_Protocol = 0">Voor de INSPIRE dataservice koppeling is het verplicht dat alle distributie URLs een protocol (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#protocol) hebben. Voor <sch:value-of select="$nrOnLine_URL_Without_Protocol"/> URL(s) ontbreekt echter een protocol. Zie de URLs: <sch:value-of select="normalize-space(string-join(gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource/gmd:linkage/gmd:URL[not(../../gmd:protocol)], ', '))"/>.
			</sch:assert>

			<!-- protocol moet een anchor zijn, altijd verplicht.  -->
			<sch:let name="nrOnLine_Protocol_not_Anchor" value="count(gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource/gmd:protocol[not(./gmx:Anchor)])"/>

			<sch:assert id="Protocol_moet_altijd_een_Anchor_zijn" etf_name="Anchor verplicht voor protocol" test="$nrOnLine_Protocol_not_Anchor = 0">Voor de INSPIRE dataservice koppeling is het verplicht dat het element protocol (https://docs.geostandaarden.nl/md/mdprofiel-iso19115/#protocol) via een Anchor is beschreven. Voor <sch:value-of select="$nrOnLine_Protocol_not_Anchor"/> element(en) is dat niet het geval. Zie de protocol elementen: <sch:value-of select="normalize-space(string-join(gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource/gmd:protocol[not(./gmx:Anchor)], ', '))"/>.
				</sch:assert>


			<!-- applicatieprofiel: moet er minimaal eentje aanwezig zijn, voor een omschrijving=accesspoint
			NB: aanname: alleen gebruik Anchors
			-->

			<!-- Use a schematron var to do the filtering (with a contains function) in the next step . Because if the xlink:href attribute is spread over multiple lines (so cntains a line ending, which is valid), the filter won't work if the value is used directly. wWthout http:// for this value,because of schematron syntax  -->
			<sch:let name="accessPointUri" value="inspire.ec.europa.eu/metadata-codelist/OnLineDescriptionCode/accessPoint"/>

			<sch:let name="nr_onLine_ApplicationProfile" value="count(gmd:MD_DigitalTransferOptions/gmd:onLine/gmd:CI_OnlineResource[contains(gmd:description/gmx:Anchor/@xlink:href, $accessPointUri)]/gmd:applicationProfile/gmx:Anchor[normalize-space(@xlink:href) = 'http://inspire.ec.europa.eu/metadata-codelist/SpatialDataServiceType/view' or normalize-space(@xlink:href) = 'http://inspire.ec.europa.eu/metadata-codelist/SpatialDataServiceType/download'])"/>

			<sch:assert id="Minimaal_applicationProfile_View_Download" etf_name="Er is minimaal een applicationProfile opgegeven voor een View en Download service" test="$nr_onLine_ApplicationProfile &gt; 1">Voor de INSPIRE dataservice koppeling is het verplicht dat tenminste voor een View en Download service het applicatie profiel is opgegeven, met een waarde uit de codelijst ServiceType (http://inspire.ec.europa.eu/metadata-codelist/SpatialDataServiceType/). Het applicatie profiel moet opgegeven zijn via een Anchor.
			</sch:assert>

		</sch:rule>

	</sch:pattern>
</sch:schema>