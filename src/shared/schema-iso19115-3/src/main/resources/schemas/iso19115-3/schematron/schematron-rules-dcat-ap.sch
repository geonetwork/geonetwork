<?xml version="1.0" encoding="UTF-8"?>
<!--
  ~ Copyright (C) 2001-2024 Food and Agriculture Organization of the
  ~ United Nations (FAO-UN), United Nations World Food Programme (WFP)
  ~ and United Nations Environment Programme (UNEP)
  ~
  ~ This program is free software; you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation; either version 2 of the License, or (at
  ~ your option) any later version.
  ~
  ~ This program is distributed in the hope that it will be useful, but
  ~ WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
  ~ General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program; if not, write to the Free Software
  ~ Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
  ~
  ~ Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
  ~ Rome - Italy. email: geonetwork@osgeo.org
  -->

<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron">

  <sch:title xmlns="http://www.w3.org/2001/XMLSchema">DCAT-AP</sch:title>
  <sch:ns prefix="gml" uri="http://www.opengis.net/gml"/>
  <sch:ns prefix="gmd" uri="http://standards.iso.org/iso/19115/-3/gmd"/>
  <sch:ns prefix="gmx" uri="http://standards.iso.org/iso/19115/-3/gmx"/>
  <sch:ns prefix="geonet" uri="http://www.fao.org/geonetwork"/>
  <sch:ns prefix="skos" uri="http://www.w3.org/2004/02/skos/core#"/>
  <sch:ns prefix="xlink" uri="http://www.w3.org/1999/xlink"/>
  <sch:ns prefix="srv" uri="http://standards.iso.org/iso/19115/-3/srv/2.0"/>
  <sch:ns prefix="mdb" uri="http://standards.iso.org/iso/19115/-3/mdb/2.0"/>
  <sch:ns prefix="mcc" uri="http://standards.iso.org/iso/19115/-3/mcc/1.0"/>
  <sch:ns prefix="mri" uri="http://standards.iso.org/iso/19115/-3/mri/1.0"/>
  <sch:ns prefix="mrs" uri="http://standards.iso.org/iso/19115/-3/mrs/1.0"/>
  <sch:ns prefix="mrd" uri="http://standards.iso.org/iso/19115/-3/mrd/1.0"/>
  <sch:ns prefix="mco" uri="http://standards.iso.org/iso/19115/-3/mco/1.0"/>
  <sch:ns prefix="msr" uri="http://standards.iso.org/iso/19115/-3/msr/2.0"/>
  <sch:ns prefix="lan" uri="http://standards.iso.org/iso/19115/-3/lan/1.0"/>
  <sch:ns prefix="gcx" uri="http://standards.iso.org/iso/19115/-3/gcx/1.0"/>
  <sch:ns prefix="gex" uri="http://standards.iso.org/iso/19115/-3/gex/1.0"/>
  <sch:ns prefix="dqm" uri="http://standards.iso.org/iso/19157/-2/dqm/1.0"/>
  <sch:ns prefix="cit" uri="http://standards.iso.org/iso/19115/-3/cit/2.0"/>
  <sch:ns prefix="mdq" uri="http://standards.iso.org/iso/19157/-2/mdq/1.0"/>
  <sch:ns prefix="mrl" uri="http://standards.iso.org/iso/19115/-3/mrl/2.0"/>
  <sch:ns prefix="gco" uri="http://standards.iso.org/iso/19115/-3/gco/1.0"/>
  <sch:ns prefix="rdf" uri="http://www.w3.org/1999/02/22-rdf-syntax-ns#"/>
  <sch:ns prefix="skos" uri="http://www.w3.org/2004/02/skos/core#"/>
  <sch:ns prefix="util" uri="java:org.fao.geonet.util.XslUtil"/>


  <sch:diagnostic id="rule.hvd.title.mandatory-failure-en" xml:lang="en">
    A name given to the resource is mandatory.
  </sch:diagnostic>
  <sch:diagnostic id="rule.hvd.title.mandatory-failure-fr" xml:lang="fr">
    Un titre est obligatoire.
  </sch:diagnostic>
  <sch:diagnostic id="rule.hvd.title.mandatory-success-en"
                  xml:lang="en">Resource title is <sch:value-of select="$title"/>.
  </sch:diagnostic>
  <sch:diagnostic id="rule.hvd.title.mandatory-success-fr"
                  xml:lang="fr">Le titre de la ressource est <sch:value-of select="$title"/>.
  </sch:diagnostic>



  <sch:diagnostic id="rule.hvd.abstract.mandatory-failure-en" xml:lang="en">
    A free-text account of the resource is mandatory.
  </sch:diagnostic>
  <sch:diagnostic id="rule.hvd.abstract.mandatory-failure-fr" xml:lang="fr">
    Un résumé est obligatoire.
  </sch:diagnostic>
  <sch:diagnostic id="rule.hvd.abstract.mandatory-success-en"
                  xml:lang="en">Abstract found.
  </sch:diagnostic>
  <sch:diagnostic id="rule.hvd.abstract.mandatory-success-fr"
                  xml:lang="fr">Le résumé est encodé.
  </sch:diagnostic>





  <sch:pattern>
    <sch:title>HVD</sch:title>
    <sch:rule  context="//*:MD_Metadata">

      <sch:let name="title"
               value="*:identificationInfo/*/*:citation/*/*:title/*[. != '']"/>
      <sch:let name="hasOneTitle"
               value="count($title) = 1"/>

      <sch:assert test="$hasOneTitle"
                  diagnostics="rule.hvd.title.mandatory-failure-en rule.hvd.title.mandatory-failure-fr"/>
      <sch:report test="$hasOneTitle"
                  diagnostics="rule.hvd.title.mandatory-success-en rule.hvd.title.mandatory-success-fr"/>



      <sch:let name="hasOneAbstract"
               value="count(*:identificationInfo/*/*:abstract/*[. != '']) = 1"/>

      <sch:assert test="$hasOneAbstract"
                  diagnostics="rule.hvd.abstract.mandatory-failure-en rule.hvd.abstract.mandatory-failure-fr"/>
      <sch:report test="$hasOneAbstract"
                  diagnostics="rule.hvd.abstract.mandatory-success-en rule.hvd.abstract.mandatory-success-fr"/>
    </sch:rule>
  </sch:pattern>
</sch:schema>
