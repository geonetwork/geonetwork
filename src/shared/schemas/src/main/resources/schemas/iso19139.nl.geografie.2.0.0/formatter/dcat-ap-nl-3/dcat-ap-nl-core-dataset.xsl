<?xml version="1.0" encoding="UTF-8"?>
<!--

    (c) 2003 Open Source Geospatial Foundation - all rights reserved
    This code is licensed under the GPL 2.0 license,
    available at the root application directory.

-->
<xsl:stylesheet version="2.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xs="http://www.w3.org/2001/XMLSchema"
                xmlns:xlink="http://www.w3.org/1999/xlink"
                xmlns:mdb="http://standards.iso.org/iso/19115/-3/mdb/2.0"
                xmlns:mcc="http://standards.iso.org/iso/19115/-3/mcc/1.0"
                xmlns:gco="http://standards.iso.org/iso/19115/-3/gco/1.0"
                xmlns:mri="http://standards.iso.org/iso/19115/-3/mri/1.0"
                xmlns:cit="http://standards.iso.org/iso/19115/-3/cit/2.0"
                xmlns:gcx="http://standards.iso.org/iso/19115/-3/gcx/1.0"
                xmlns:mco="http://standards.iso.org/iso/19115/-3/mco/1.0"
                xmlns:mmi="http://standards.iso.org/iso/19115/-3/mmi/1.0"
                xmlns:mrl="http://standards.iso.org/iso/19115/-3/mrl/2.0"
                xmlns:gex="http://standards.iso.org/iso/19115/-3/gex/1.0"
                xmlns:srv="http://standards.iso.org/iso/19115/-3/srv/2.0"
                xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
                xmlns:skos="http://www.w3.org/2004/02/skos/core#"
                xmlns:dcat="http://www.w3.org/ns/dcat#"
                xmlns:adms="http://www.w3.org/ns/adms#"
                xmlns:dcatap="http://data.europa.eu/r5r/"
                xmlns:prov="http://www.w3.org/ns/prov#"
                xmlns:dct="http://purl.org/dc/terms/"
                exclude-result-prefixes="#all">

  <!-- Used for metadata that does not have ISO topic categories (for example, service metadata) and does not have also INSPIRE GEMET Themes keywords -->
  <xsl:variable name="fallbackDcatApThemes" as="node()*">
    <!-- <entry key="http://publications.europa.eu/resource/authority/data-theme/GOVE" /> -->
  </xsl:variable>

  <!--
Theme mapping
https://joinup.ec.europa.eu/collection/semantic-interoperability-community-semic/solution/dcat-application-profile-implementation-guidelines/discussion/di1-tools-dcat-ap
https://github.com/SEMICeu/iso-19139-to-dcat-ap/blob/master/alignments/iso-topic-categories-to-inspire-themes.rdf
-->
  <xsl:variable name="isoTopicToEuDcatApThemes"
                as="node()*">
    <entry key="http://publications.europa.eu/resource/authority/data-theme/AGRI">
      <inspire>http://inspire.ec.europa.eu/theme/af</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/af</inspire>
      <iso>farming</iso>
      <hvd>http://data.europa.eu/bna/c_642643e6</hvd> <!-- Agricultural parcels -->
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/ECON">
      <inspire>http://inspire.ec.europa.eu/theme/cp</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/cp</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/lu</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/lu</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/mr</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/mr</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/pf</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/pf</inspire>
      <iso>economy</iso>
      <iso>planningCadastre</iso>
      <hvd>http://data.europa.eu/bna/c_04bf94a3</hvd> <!-- Poverty -->
      <hvd>http://data.europa.eu/bna/c_20cd11bb</hvd> <!-- EU International trade in goods statistics ... -->
      <hvd>http://data.europa.eu/bna/c_23385471</hvd> <!-- Potential labour force -->
      <hvd>http://data.europa.eu/bna/c_2aed31f9</hvd> <!-- Industrial producer price index breakdowns by activity -->
      <hvd>http://data.europa.eu/bna/c_317b9493</hvd> <!-- Population, Fertility, Mortality -->
      <hvd>http://data.europa.eu/bna/c_34abf8c1</hvd> <!-- Industrial production -->
      <hvd>http://data.europa.eu/bna/c_424bb0b4</hvd> <!-- Current healthcare expenditure -->
      <hvd>http://data.europa.eu/bna/c_4ac557e7</hvd> <!-- Government expenditure and revenue -->
      <hvd>http://data.europa.eu/bna/c_59627af3</hvd> <!-- National accounts – key indicators on households -->
      <hvd>http://data.europa.eu/bna/c_92874eb2</hvd> <!-- Environmental accounts and statistics -->
      <hvd>http://data.europa.eu/bna/c_95da87c7</hvd> <!-- National accounts – key indicators on corporations -->
      <hvd>http://data.europa.eu/bna/c_a2c6dcd8</hvd> <!-- Employment -->
      <hvd>http://data.europa.eu/bna/c_a49ec591</hvd> <!-- Volume of sales by activity -->
      <hvd>http://data.europa.eu/bna/c_a8b937c4</hvd> <!-- Inequality -->
      <hvd>http://data.europa.eu/bna/c_b72b721f</hvd> <!-- National accounts – GDP main aggregates -->
      <hvd>http://data.europa.eu/bna/c_c0022235</hvd> <!-- Harmonised Indices of consumer prices -->
      <hvd>http://data.europa.eu/bna/c_dd8f4797</hvd> <!-- Consolidated government gross debt -->
      <hvd>http://data.europa.eu/bna/c_fd4e881c</hvd> <!-- Unemployment -->
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/EDUC"></entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/ENER">
      <inspire>http://inspire.ec.europa.eu/theme/er</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/er</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/mr</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/mr</inspire>
      <iso>economy</iso>
      <hvd>http://data.europa.eu/bna/c_b7de66cd</hvd> <!-- Energy resources -->
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/ENVI">
      <inspire>http://inspire.ec.europa.eu/theme/hy</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/hy</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/ps</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/ps</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/lc</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/lc</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/am</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/am</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/ac</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/ac</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/br</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/br</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/ef</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/ef</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/hb</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/hb</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/lu</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/lu</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/mr</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/mr</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/nz</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/nz</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/of</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/of</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/sr</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/sr</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/so</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/so</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/sd</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/sd</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/mf</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/mf</inspire>
      <iso>biota</iso>
      <iso>environment</iso>
      <iso>inlandWaters</iso>
      <iso>oceans</iso>
      <iso>climatologyMeteorologyAtmosphere</iso>
      <iso>geoscientificInformation</iso>
      <iso>imageryBaseMapsEarthCover</iso>
      <iso>planningCadastre</iso>
      <hvd>http://data.europa.eu/bna/c_164e0bf5</hvd> <!-- Meteorological -->
      <hvd>http://data.europa.eu/bna/c_13e3cf16</hvd> <!-- NWP model data -->
      <hvd>http://data.europa.eu/bna/c_36807466</hvd> <!-- Climate data: validated observations -->
      <hvd>http://data.europa.eu/bna/c_3af3368c</hvd> <!-- Observations data measured by weather stations -->
      <hvd>http://data.europa.eu/bna/c_be47b010</hvd> <!-- Weather alerts -->
      <hvd>http://data.europa.eu/bna/c_d13a4420</hvd> <!-- Radar data -->
      <hvd>http://data.europa.eu/bna/c_92874eb2</hvd> <!-- Environmental accounts and statistics -->
      <hvd>http://data.europa.eu/bna/c_dd313021</hvd> <!-- Earth observation and environment -->
      <hvd>http://data.europa.eu/bna/c_63b37dd4</hvd> <!-- Air -->
      <hvd>http://data.europa.eu/bna/c_af646f5b</hvd> <!-- Area management / restriction / regulation zones ... -->
      <hvd>http://data.europa.eu/bna/c_c873f344</hvd> <!-- Bio-geographical regions -->
      <hvd>http://data.europa.eu/bna/c_59e64dd4</hvd> <!-- Climate -->
      <hvd>http://data.europa.eu/bna/c_315692ad</hvd> <!-- Elevation -->
      <hvd>http://data.europa.eu/bna/c_4ba9548e</hvd> <!-- Emissions -->
      <hvd>http://data.europa.eu/bna/c_b7de66cd</hvd> <!-- Energy resources -->
      <hvd>http://data.europa.eu/bna/c_7b8fbb64</hvd> <!-- Environmental monitoring facilities -->
      <hvd>http://data.europa.eu/bna/c_e3f55603</hvd> <!-- Geology -->
      <hvd>http://data.europa.eu/bna/c_c3919aec</hvd> <!-- Habitats and biotopes -->
      <hvd>http://data.europa.eu/bna/c_4d63300b</hvd> <!-- Horizontal legislation -->
      <hvd>http://data.europa.eu/bna/c_06b1eec4</hvd> <!-- Hydrography -->
      <hvd>http://data.europa.eu/bna/c_b21e1296</hvd> <!-- Land cover -->
      <hvd>http://data.europa.eu/bna/c_ad9ae929</hvd> <!-- Land use -->
      <hvd>http://data.europa.eu/bna/c_4dd389c5</hvd> <!-- Mineral resources -->
      <hvd>http://data.europa.eu/bna/c_63be22bd</hvd> <!-- Natural risk zones -->
      <hvd>http://data.europa.eu/bna/c_b7f6a4f3</hvd> <!-- Nature preservation and biodiversity -->
      <hvd>http://data.europa.eu/bna/c_e4358335</hvd> <!-- Noise -->
      <hvd>http://data.europa.eu/bna/c_b40e6d46</hvd> <!-- Oceanographic geographical features -->
      <hvd>http://data.europa.eu/bna/c_91185a85</hvd> <!-- Orthoimagery -->
      <hvd>http://data.europa.eu/bna/c_59c93ba5</hvd> <!-- Production and industrial facilities -->
      <hvd>http://data.europa.eu/bna/c_83aa10a6</hvd> <!-- Protected sites -->
      <hvd>http://data.europa.eu/bna/c_f399050e</hvd> <!-- Sea regions -->
      <hvd>http://data.europa.eu/bna/c_87a129d9</hvd> <!-- Soil -->
      <hvd>http://data.europa.eu/bna/c_793164b6</hvd> <!-- Species distribution -->
      <hvd>http://data.europa.eu/bna/c_38933a65</hvd> <!-- Waste -->
      <hvd>http://data.europa.eu/bna/c_43f88346</hvd> <!-- Water -->
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/GOVE">
      <inspire>http://inspire.ec.europa.eu/theme/au</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/au</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/us</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/us</inspire>
      <iso>boundaries</iso>
      <iso>utilitiesCommunication</iso>
      <hvd>http://data.europa.eu/bna/c_4ac557e7</hvd> <!-- Government expenditure and revenue -->
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/HEAL">
      <inspire>http://inspire.ec.europa.eu/theme/hh</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/hh</inspire>
      <iso>health</iso>
      <hvd>http://data.europa.eu/bna/c_424bb0b4</hvd> <!-- Current healthcare expenditure -->
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/INTR"></entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/JUST">
      <iso>intelligenceMilitary</iso>
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/OP_DATPRO"></entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/REGI">
      <inspire>http://inspire.ec.europa.eu/theme/ad</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/ad</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/rs</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/rs</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/gg</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/gg</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/cp</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/cp</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/gn</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/gn</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/el</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/el</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/ge</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/ge</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/oi</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/oi</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/bu</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/bu</inspire>
      <iso>planningCadastre</iso>
      <iso>elevation</iso>
      <iso>imageryBaseMapsEarthCover</iso>
      <iso>geoscientificInformation</iso>
      <iso>location</iso>
      <iso>structure</iso>
      <hvd>http://data.europa.eu/bna/c_ac64a52d</hvd> <!-- Geospatial -->
      <hvd>http://data.europa.eu/bna/c_60182062</hvd> <!-- Buildings -->
      <hvd>http://data.europa.eu/bna/c_642643e6</hvd> <!-- Agricultural parcels -->
      <hvd>http://data.europa.eu/bna/c_6a3f6896</hvd> <!-- Cadastral parcels -->
      <hvd>http://data.europa.eu/bna/c_6c2bb82d</hvd> <!-- Geographical names -->
      <hvd>http://data.europa.eu/bna/c_9427236f</hvd> <!-- Administrative units -->
      <hvd>http://data.europa.eu/bna/c_c3de25e4</hvd> <!-- Addresses -->
      <hvd>http://data.europa.eu/bna/c_fbd2fc3f</hvd> <!-- Reference parcels -->
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/SOCI">
      <inspire>http://inspire.ec.europa.eu/theme/pd</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/pd</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/su</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/su</inspire>
      <iso>society</iso>
      <iso>boundaries</iso>
      <hvd>http://data.europa.eu/bna/c_e1da4e07</hvd> <!-- Statistics -->
      <hvd>http://data.europa.eu/bna/c_04bf94a3</hvd> <!-- Poverty -->
      <hvd>http://data.europa.eu/bna/c_20cd11bb</hvd> <!-- EU International trade in goods statistics ... -->
      <hvd>http://data.europa.eu/bna/c_23385471</hvd> <!-- Potential labour force -->
      <hvd>http://data.europa.eu/bna/c_2aed31f9</hvd> <!-- Industrial producer price index breakdowns by activity -->
      <hvd>http://data.europa.eu/bna/c_317b9493</hvd> <!-- Population, Fertility, Mortality -->
      <hvd>http://data.europa.eu/bna/c_34abf8c1</hvd> <!-- Industrial production -->
      <hvd>http://data.europa.eu/bna/c_424bb0b4</hvd> <!-- Current healthcare expenditure -->
      <hvd>http://data.europa.eu/bna/c_4ac557e7</hvd> <!-- Government expenditure and revenue -->
      <hvd>http://data.europa.eu/bna/c_4acb6bf3</hvd> <!-- Mortality -->
      <hvd>http://data.europa.eu/bna/c_59627af3</hvd> <!-- National accounts – key indicators on households -->
      <hvd>http://data.europa.eu/bna/c_6a7250c1</hvd> <!-- Fertility -->
      <hvd>http://data.europa.eu/bna/c_92874eb2</hvd> <!-- Environmental accounts and statistics -->
      <hvd>http://data.europa.eu/bna/c_95da87c7</hvd> <!-- National accounts – key indicators on corporations -->
      <hvd>http://data.europa.eu/bna/c_a2c6dcd8</hvd> <!-- Employment -->
      <hvd>http://data.europa.eu/bna/c_a3767648</hvd> <!-- Tourism flows in Europe -->
      <hvd>http://data.europa.eu/bna/c_a49ec591</hvd> <!-- Volume of sales by activity -->
      <hvd>http://data.europa.eu/bna/c_a8b937c4</hvd> <!-- Inequality -->
      <hvd>http://data.europa.eu/bna/c_b72b721f</hvd> <!-- National accounts – GDP main aggregates -->
      <hvd>http://data.europa.eu/bna/c_c0022235</hvd> <!-- Harmonised Indices of consumer prices -->
      <hvd>http://data.europa.eu/bna/c_dd8f4797</hvd> <!-- Consolidated government gross debt -->
      <hvd>http://data.europa.eu/bna/c_f2b50efd</hvd> <!-- Population -->
      <hvd>http://data.europa.eu/bna/c_fd4e881c</hvd> <!-- Unemployment -->
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/TECH">
      <inspire>http://inspire.ec.europa.eu/theme/hy</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/hy</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/ge</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/ge</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/oi</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/oi</inspire>
      <inspire>http://inspire.ec.europa.eu/theme/mf</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/mf</inspire>
      <iso>geoscientificInformation</iso>
      <hvd>http://data.europa.eu/bna/c_91185a85</hvd> <!-- Orthoimagery -->
    </entry>
    <entry key="http://publications.europa.eu/resource/authority/data-theme/TRAN">
      <inspire>http://inspire.ec.europa.eu/theme/tn</inspire>
      <inspire>http://www.eionet.europa.eu/gemet/nl/inspire-theme/tn</inspire>
      <iso>transportation</iso>
      <hvd>http://data.europa.eu/bna/c_b79e35eb</hvd> <!-- Mobility -->
      <hvd>http://data.europa.eu/bna/c_4b74ea13</hvd> <!-- Transport networks -->
      <hvd>http://data.europa.eu/bna/c_b151a0ba</hvd> <!-- Inland waterways datasets -->
      <hvd>http://data.europa.eu/bna/c_03ba8d92</hvd> <!-- Regular lock and bridge operating times -->
      <hvd>http://data.europa.eu/bna/c_1226dc1a</hvd> <!-- Bank of waterway at mean water level -->
      <hvd>http://data.europa.eu/bna/c_1e787364</hvd> <!-- Reference data for water level gauges relevant to navigation -->
      <hvd>http://data.europa.eu/bna/c_2037ada4</hvd> <!-- Navigation rules and recommendations -->
      <hvd>http://data.europa.eu/bna/c_25f43866</hvd> <!-- Rates of waterway infrastructure charges -->
      <hvd>http://data.europa.eu/bna/c_298ffb73</hvd> <!-- Links to the external xml-files with operation times of restricting structures -->
      <hvd>http://data.europa.eu/bna/c_3e8e3bf7</hvd> <!-- Location and characteristics of ports and transhipment sites -->
      <hvd>http://data.europa.eu/bna/c_407951ff</hvd> <!-- Location of ports and transhipment sites -->
      <hvd>http://data.europa.eu/bna/c_593bc53d</hvd> <!-- Short term changes of aids to navigation -->
      <hvd>http://data.europa.eu/bna/c_664c9e5a</hvd> <!-- Boundaries of the fairway/navigation channel -->
      <hvd>http://data.europa.eu/bna/c_66b946cb</hvd> <!-- Short term changes of lock and bridge operating times -->
      <hvd>http://data.europa.eu/bna/c_7e19ef26</hvd> <!-- Other physical limitations on waterways -->
      <hvd>http://data.europa.eu/bna/c_883d0205</hvd> <!-- Contours of locks and dams -->
      <hvd>http://data.europa.eu/bna/c_99bc517f</hvd> <!-- Isolated dangers in the fairway/navigation channel under and above water -->
      <hvd>http://data.europa.eu/bna/c_9cbe4435</hvd> <!-- List of navigation aids and traffic signs -->
      <hvd>http://data.europa.eu/bna/c_b121e2f6</hvd> <!-- Temporary obstructions in the fairway -->
      <hvd>http://data.europa.eu/bna/c_b24028d7</hvd> <!-- Official aids-to-navigation (e.g. buoys, beacons, lights, notice marks) -->
      <hvd>http://data.europa.eu/bna/c_bc8941d9</hvd> <!-- Shoreline construction -->
      <hvd>http://data.europa.eu/bna/c_c19af83a</hvd> <!-- Waterway axis with kilometres indication -->
      <hvd>http://data.europa.eu/bna/c_e50004c6</hvd> <!-- State of the rivers, canals, locks and bridges -->
      <hvd>http://data.europa.eu/bna/c_e5f69a04</hvd> <!-- Present and future water levels at gauges -->
      <hvd>http://data.europa.eu/bna/c_f6886b00</hvd> <!-- Restrictions caused by flood and ice -->
      <hvd>http://data.europa.eu/bna/c_f76b01e6</hvd> <!-- Fairway characteristics -->
      <hvd>http://data.europa.eu/bna/c_fa2a1c3a</hvd> <!-- Long-time obstructions in the fairway and reliability -->
      <hvd>http://data.europa.eu/bna/c_fef208ab</hvd> <!-- Water depths contours in the navigation channel -->
    </entry>
  </xsl:variable>


  <!--
  [o]	theme	Concept	0..*	A category of the Dataset.	A Dataset may be associated with multiple themes.	https://w3c.github.io/dxwg/dcat/#Property:resource_theme
  Vocabulary http://publications.europa.eu/resource/authority/data-theme
  -->
  <xsl:template name="rdf-eu-dcat-ap-nl-theme">
    <xsl:variable name="values"
                  as="node()*"
                  select="mdb:identificationInfo/*/mri:topicCategory
                         |mdb:identificationInfo/*/mri:descriptiveKeywords/*/mri:keyword"/>

    <xsl:variable name="theme"
                  select="distinct-values($isoTopicToEuDcatApThemes[
                              */text() = $values/*/text()
                              or */text() = $values/*/@xlink:href]/@key)"/>
    <xsl:for-each select="$theme">
      <dcat:theme>
        <xsl:choose>
          <xsl:when test="$isExpandSkosConcept">
            <xsl:variable name="themeDescription"
                          select="$euDataTheme//skos:Concept[@rdf:about = current()]"/>
            <skos:Concept>
              <xsl:copy-of select="$themeDescription/@rdf:about"/>
              <xsl:copy-of select="$themeDescription/skos:prefLabel[@xml:lang = $languages/@iso2code]"
                           copy-namespaces="no"/>
            </skos:Concept>
          </xsl:when>
          <xsl:otherwise>
            <xsl:attribute name="rdf:resource" select="current()"/>
          </xsl:otherwise>
        </xsl:choose>
      </dcat:theme>
    </xsl:for-each>


    <!-- Add harcoded theme if not present -->
    <xsl:if test="count($theme) = 0 and count($fallbackDcatApThemes) > 0">
      <xsl:for-each select="$fallbackDcatApThemes">
        <xsl:variable name="entryKey" select="@key"/>
        <dcat:theme>
          <xsl:choose>
            <xsl:when test="$isExpandSkosConcept">
              <xsl:variable name="themeDescription"
                            select="$euDataTheme//skos:Concept[@rdf:about = $entryKey]"/>
              <skos:Concept>
                <xsl:copy-of select="$themeDescription/@rdf:about"/>
                <xsl:copy-of select="$themeDescription/skos:prefLabel[@xml:lang = $languages/@iso2code]"
                             copy-namespaces="no"/>
              </skos:Concept>
            </xsl:when>
            <xsl:otherwise>
              <xsl:attribute name="rdf:resource" select="$entryKey"/>
            </xsl:otherwise>
          </xsl:choose>
        </dcat:theme>
      </xsl:for-each>
    </xsl:if>
  </xsl:template>

  <!--
RDF Property:	dcterms:spatial
Definition:	The geographical area covered by the dataset.
Range:	dcterms:Location (A spatial region or named place)
Usage note:	The spatial coverage of a dataset may be encoded as an instance of dcterms:Location,
or may be indicated using an IRI reference (link) to a resource describing a location. It is recommended that links are to entries in a well maintained gazetteer such as Geonames.
-->
  <xsl:template mode="iso19115-3-to-dcat"
                match="mri:extent/*/gex:geographicElement/gex:EX_GeographicBoundingBox">
    <xsl:variable name="isServiceMetadata" select="exists(//mdb:MD_Metadata/mdb:identificationInfo/srv:SV_ServiceIdentification)" />

    <xsl:if test="not($isServiceMetadata)">
      <xsl:variable name="north" select="gex:northBoundLatitude/gco:Decimal"/>
      <xsl:variable name="east" select="gex:eastBoundLongitude/gco:Decimal"/>
      <xsl:variable name="south" select="gex:southBoundLatitude/gco:Decimal"/>
      <xsl:variable name="west" select="gex:westBoundLongitude/gco:Decimal"/>

      <xsl:variable name="geojson"
                    as="xs:string"
                    select="concat('{&quot;type&quot;:&quot;Polygon&quot;,&quot;coordinates&quot;:[[[',
                                     $west, ',', $north, '],[',
                                     $east, ',', $north, '],[',
                                     $east, ',', $south, '],[',
                                     $west, ',', $south, '],[',
                                     $west, ',', $north, ']]]}')"/>

      <dct:spatial>
        <rdf:Description>
          <rdf:type rdf:resource="http://purl.org/dc/terms/Location"/>
          <dcat:bbox rdf:datatype="http://www.opengis.net/ont/geosparql#geoJSONLiteral">
            <xsl:value-of select="$geojson"/>
          </dcat:bbox>
        </rdf:Description>
      </dct:spatial>
    </xsl:if>
  </xsl:template>


  <xsl:template mode="iso19115-3-to-dcat"
                match="mri:extent/*/gex:geographicElement/gex:EX_GeographicDescription">
    <xsl:variable name="isServiceMetadata" select="exists(//mdb:MD_Metadata/mdb:identificationInfo/srv:SV_ServiceIdentification)" />

    <xsl:if test="not($isServiceMetadata)">
      <xsl:for-each select="gex:geographicIdentifier/*">
        <xsl:variable name="uri"
                      as="xs:string?"
                      select="mcc:code/*/@xlink:href"/>
        <xsl:choose>
          <xsl:when test="string($uri)">
            <dct:spatial>
              <dct:Location rdf:about="{$uri}"/>
            </dct:spatial>
          </xsl:when>
          <xsl:otherwise>
            <xsl:for-each select="mcc:code">
              <dct:spatial>
                <rdf:Description>
                  <rdf:type rdf:resource="http://purl.org/dc/terms/Location"/>
                  <xsl:call-template name="rdf-localised">
                    <xsl:with-param name="nodeName" select="'skos:prefLabel'"/>
                  </xsl:call-template>
                </rdf:Description>
              </dct:spatial>
            </xsl:for-each>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:for-each>
    </xsl:if>
  </xsl:template>

  <!-- Map process steps to prov:wasGeneratedBy -->
  <xsl:template mode="iso19115-3-to-dcat"
                match="mdb:resourceLineage/*/mrl:processStep/*/mrl:description">

    <prov:wasGeneratedBy>
      <prov:Activity>
        <xsl:call-template name="rdf-localised">
          <xsl:with-param name="nodeName" select="'prov:label'"/>
        </xsl:call-template>
      </prov:Activity>
    </prov:wasGeneratedBy>
  </xsl:template>
</xsl:stylesheet>
