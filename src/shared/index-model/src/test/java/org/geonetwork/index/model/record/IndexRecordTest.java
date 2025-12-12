/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.index.model.record;

import static org.geonetwork.index.model.record.IndexRecordFieldNames.Codelists.CHARACTER_SET;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.Codelists.RESOURCE_CHARACTER_SET;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.Codelists.STATUS;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.Codelists.TOPIC;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.CommonField.DEFAULT_TEXT;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.CommonField.KEY;
import static org.geonetwork.index.model.record.IndexRecordFieldNames.LINK;
import static org.geonetwork.index.model.record.RecordLink.Origin.remote;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class IndexRecordTest {

    /**
     * Access samples as a ClassPathResource, reading contents into a String.
     *
     * @param sample Name of class path resource to load as sample data
     * @return sample document, or null if unavailable
     */
    String samples(String sample) {
        try {
            String json = Files.readString(Path.of(new ClassPathResource("samples/" + sample).getURI()));
            return json;
        } catch (Exception e) {
            System.err.printf("Test skipped as `samples/%s' not available\n", sample);
            return null;
        }
    }

    void test_dataset() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json =
                    Files.readString(Path.of(new ClassPathResource("samples/iso19115-3.2018_dataset.json").getURI()));

            IndexRecord indexRecord = objectMapper.readValue(json, IndexRecord.class);

            assertEquals("metadata", indexRecord.getDocType().name());
            assertEquals("samples/iso19115-3.2018_dataset", indexRecord.getUuid());
            assertEquals("iso19115-3.2018", indexRecord.getDocumentStandard());
            assertEquals("ISO 19115-3", indexRecord.getStandardName().get(DEFAULT_TEXT));
            assertEquals("2018", indexRecord.getStandardVersion().get(DEFAULT_TEXT));
            assertEquals("2024-06-03T14:04:06.161Z", indexRecord.getDateStamp());
            assertEquals("fre", indexRecord.getMainLanguage());
            assertEquals(
                    "utf8",
                    indexRecord
                            .getCodelists()
                            .get(CHARACTER_SET)
                            .getFirst()
                            .getProperties()
                            .get(KEY));
            assertEquals(
                    "Utf8",
                    indexRecord
                            .getCodelists()
                            .get(CHARACTER_SET)
                            .getFirst()
                            .getProperties()
                            .get(DEFAULT_TEXT));
            assertEquals(
                    "http://standards.iso.org/iso/19115/resources/Codelists/cat/codelists.xml#MD_CharacterSetCode",
                    indexRecord
                            .getCodelists()
                            .get(CHARACTER_SET)
                            .getFirst()
                            .getProperties()
                            .get(LINK));
            assertEquals("dataset", indexRecord.getResourceType().getFirst());
            assertEquals("Jeu de données SIG", indexRecord.getResourceTypeName().get(DEFAULT_TEXT));
            assertEquals(
                    "completed",
                    indexRecord
                            .getCodelists()
                            .get(STATUS)
                            .getFirst()
                            .getProperties()
                            .get(KEY));
            assertEquals(
                    "Finalisé",
                    indexRecord
                            .getCodelists()
                            .get(STATUS)
                            .getFirst()
                            .getProperties()
                            .get(DEFAULT_TEXT));
            assertEquals(
                    "http://standards.iso.org/iso/19115/resources/Codelists/cat/codelists.xml#MD_ProgressCode",
                    indexRecord
                            .getCodelists()
                            .get(STATUS)
                            .getFirst()
                            .getProperties()
                            .get(LINK));
            assertEquals(
                    "Occupation du sol en Wallonie - WALOUS 2020",
                    indexRecord.getResourceTitle().get(DEFAULT_TEXT));
            assertEquals(
                    "WAL_OCS__2020",
                    indexRecord.getResourceAltTitle().getFirst().get(DEFAULT_TEXT));
            assertEquals(
                    "2020-09-09T22:00:00.000Z",
                    indexRecord
                            .getResourceDateDetails()
                            .get("creationDateForResource")
                            .getFirst());
            assertEquals(
                    "2020",
                    indexRecord
                            .getResourceDateDetails()
                            .get("creationYearForResource")
                            .getFirst());
            assertEquals(
                    "2020-09",
                    indexRecord
                            .getResourceDateDetails()
                            .get("creationMonthForResource")
                            .getFirst());
            assertEquals(
                    "2020-09-09T22:00:00.000Z",
                    indexRecord.getResourceDate().getFirst().getDate());
            assertEquals("creation", indexRecord.getResourceDate().getFirst().getType());
            assertEquals("publication", indexRecord.getResourceDate().getLast().getType());
            assertEquals(3, indexRecord.getResourceTemporalDateRange().size());
            assertEquals(
                    "2020-09-09T22:00:00.000Z",
                    indexRecord.getResourceTemporalDateRange().getFirst().getGte());
            assertEquals(
                    "2020-09-09T22:00:00.000Z",
                    indexRecord.getResourceTemporalDateRange().getFirst().getLte());
            assertEquals(
                    "2020-03-31T22:00:00.000Z",
                    indexRecord.getResourceTemporalDateRange().getLast().getGte());
            assertEquals(
                    "2020-04-23T22:00:00.000Z",
                    indexRecord.getResourceTemporalDateRange().getLast().getLte());
            assertEquals(2, indexRecord.getResourceIdentifier().size());
            assertEquals(
                    "47b348f1-6e7a-4baa-963c-0232a43c0cff",
                    indexRecord.getResourceIdentifier().getFirst().getCode());
            assertEquals(
                    "http://geodata.wallonie.be/id/",
                    indexRecord.getResourceIdentifier().getFirst().getCodeSpace());
            assertEquals("", indexRecord.getResourceIdentifier().getFirst().getLink());
            assertEquals("1.0", indexRecord.getResourceEdition());
            assertEquals(
                    "Cette couche de données reprend la cartographie de l’occupation du sol de l’ensemble du"
                            + " territoire wallon pour l’année 2020.\n\n"
                            + "Ce jeu de données raster reprend la cartographie de l’occupation du sol de"
                            + " l’ensemble du territoire wallon pour l’année 2020 (WAL_OCS_IA__2020). Si sa"
                            + " création diffère de la carte d’occupation 2018 (WAL_OCS__2018), la plupart de ses"
                            + " caractéristiques sont similaires. Elle permet donc d’observer l’évolution du"
                            + " territoire wallon sous le prisme de son occupation du sol.\n"
                            + " \n"
                            + "L’occupation du sol se définit comme la « couverture physique et biologique de la"
                            + " surface terrestre, y compris les surfaces artificielles, les zones agricoles, les"
                            + " forêts, les zones (semi-)naturelles, les zones humides et les masses d'eau »"
                            + " (directive européenne INSPIRE 2007/2/CE, 2007).\n\n"
                            + "WAL_OCS_IA__2020 est issue des prédictions de segmentation d’un modèle"
                            + " d’apprentissage profond entrainé sur les données de 2018 en utilisant comme"
                            + " réalité-terrain WAL_OCS__2018 et une version affinée de cette donnée. Le modèle"
                            + " se base sur les 4 bandes spectrales des orthophotos de 2020 et le MNH (dérivé de"
                            + " la soustraction entre le MNS photogrammétrique 2020 et le MNT issu de"
                            + " l’acquisition LiDAR 2013-2014). Le modèle est le résultat d’une approche agile et"
                            + " itérative avec le comité de suivi du projet.\n\n"
                            + "La donnée a été validée à l’aide d’un échantillon aléatoire de 1710 points"
                            + " photointerprétés indépendamment du processus de classification, contre-validés,"
                            + " et analysés selon les règles de l’art. Son exactitude globale est de 92.29%"
                            + " [86.77%].\n\n"
                            + "L’information sur l’occupation du sol est fournie dans le système de projection"
                            + " Lambert Belge 2008 (EPSG :3812 – Pour usage en LB72, voir information sur la"
                            + " grille NTV2"
                            + " -http://geoportail.wallonie.be/home/ressources/outils/Lambert-belge-2008-LB08.html).\n\n"
                            + "Les spécifications techniques de la WAL_OCS_IA__2020 sont similaires à celles de"
                            + " la carte d’occupation du sol 2018 (WAL_OCS__2018), à savoir\n"
                            + "- une résolution spatiale de 1m/pixel (calé sur la grille INSPIRE de la Région"
                            + " Wallonne) \n"
                            + "- une unité minimale de cartographie de 15m²\n"
                            + "- 11 classes principales. La définition et la symbologie de chacune de ces classes"
                            + " est disponible dans les documents associés\n"
                            + "Par contre, contrairement à WAL_OCS__2018, il n’y a pas dans cette version"
                            + " d’étagement de classes (appelé double label) pour un seul pixel car considéré"
                            + " comme trop sujet à confusion. Cette carte d’occupation du sol présente uniquement"
                            + " la situation vue du ciel.\n\n"
                            + "La mise en œuvre de la donnée WAL_OCS_IA__2020, produite en 2021, s’inscrit dans"
                            + " le cadre d’un marché public visant à mettre à jour la carte d’occupation des sols"
                            + " de Wallonie de 2018 (WAL_OCS__2018). C’est la société belge Aerospacelab SA qui a"
                            + " été responsable du développement et de l'implémentation de la solution. \n\n"
                            + "L’un des objectifs majeurs de ce marché était l’accessibilité de la solution."
                            + " Ainsi son code source, les logiciels utilisées, et les données sur lesquelles"
                            + " elle se base suivent la même philosophie de libre accès.\n\n"
                            + "Cette donnée est complétée, d’une part, par une donnée raster indiquant les"
                            + " changements et les transitions de classes (pour les classes principales) survenus"
                            + " entre 2018 et 2020, et d’autre part, par une donnée vectorielle directement"
                            + " dérivée de la donnée raster.\n\n"
                            + "Source à mentionner lors de l'utilisation de cette donnée : SPW - Aerospacelab SA",
                    indexRecord.getResourceAbstract().get(DEFAULT_TEXT));
            assertEquals(
                    "utf8",
                    indexRecord
                            .getCodelists()
                            .get(RESOURCE_CHARACTER_SET)
                            .getFirst()
                            .getProperties()
                            .get(KEY));
            assertEquals(
                    "Utf8",
                    indexRecord
                            .getCodelists()
                            .get(RESOURCE_CHARACTER_SET)
                            .getFirst()
                            .getProperties()
                            .get(DEFAULT_TEXT));

            assertEquals(
                    "SPW - Aerospacelab SA",
                    indexRecord.getResourceCredit().getFirst().get(DEFAULT_TEXT));
            assertEquals(
                    "Informations supplémentaires",
                    indexRecord.getSupplementalInformation().getFirst().get(DEFAULT_TEXT));
            assertEquals("But", indexRecord.getPurpose().getFirst().get(DEFAULT_TEXT));
            assertEquals(
                    "https://metawal.wallonie.be/geonetwork/srv/api/records/47b348f1-6e7a-4baa-963c-0232a43c0cff/attachments/WALOUS_2020.png",
                    indexRecord.getOverview().getFirst().getUrl());
            assertEquals(
                    "WALOUS_2020.png",
                    indexRecord.getOverview().getFirst().getName().get(DEFAULT_TEXT));
            assertEquals("fre", indexRecord.getResourceLanguage().getFirst());
            assertEquals(indexRecord.getTagNumber(), indexRecord.getTag().size());
            assertEquals("Occupation du sol", indexRecord.getTag().getFirst().get(DEFAULT_TEXT));
            assertTrue(indexRecord.getIsOpenData());
            assertEquals(
                    1, indexRecord.getKeywordByType().get("keywordType-place").size());
            assertEquals(
                    11, indexRecord.getKeywordByType().get("keywordType-theme").size());
            assertEquals(
                    "Régional",
                    indexRecord
                            .getKeywordByType()
                            .get("keywordType-theme")
                            .getFirst()
                            .getProperties()
                            .get(DEFAULT_TEXT));
            assertEquals(
                    "Régional",
                    indexRecord
                            .getKeywordByType()
                            .get("keywordType-theme")
                            .getFirst()
                            .getProperties()
                            .get("langfre"));
            assertEquals(
                    "http://inspire.ec.europa.eu/metadata-codelist/SpatialScope/regional",
                    indexRecord
                            .getKeywordByType()
                            .get("keywordType-theme")
                            .getFirst()
                            .getProperties()
                            .get(LINK));
            assertEquals(
                    indexRecord.getNumberOfKeywordByThesaurus().get("th_otherKeywords-Number"),
                    indexRecord.getKeywordByThesaurus().get("th_otherKeywords-").size());
            assertEquals(
                    indexRecord.getNumberOfKeywordByThesaurus().get("th_httpinspireeceuropaeutheme-themeNumber"),
                    indexRecord
                            .getKeywordByThesaurus()
                            .get("th_httpinspireeceuropaeutheme-theme")
                            .size());

            Thesaurus inspireThesaurus = indexRecord.getAllKeywords().get("th_httpinspireeceuropaeutheme-theme");
            assertEquals(
                    "geonetwork.thesaurus.external.theme.httpinspireeceuropaeutheme-theme", inspireThesaurus.getId());
            assertEquals("GEMET - INSPIRE themes, version 1.0", inspireThesaurus.getTitle());
            assertEquals("theme", inspireThesaurus.getTheme());
            assertEquals(
                    "https://metawal.wallonie.be/geonetwork/srv/api/registries/vocabularies/external.theme.httpinspireeceuropaeutheme-theme",
                    inspireThesaurus.getLink());
            assertEquals(1, inspireThesaurus.getKeywords().size());
            assertEquals(
                    "Occupation des terres",
                    inspireThesaurus.getKeywords().getFirst().getProperties().get(DEFAULT_TEXT));
            assertEquals(
                    "http://inspire.ec.europa.eu/theme/lc",
                    inspireThesaurus.getKeywords().getFirst().getProperties().get(LINK));

            KeywordHierarchy hierarchyTree =
                    indexRecord.getKeywordHierarchyByThesaurus().get("th_Themes_geoportail_wallon_hierarchy_tree");
            assertEquals(4, hierarchyTree.getDefaultTexts().size());
            assertEquals(
                    "Aménagement du territoire", hierarchyTree.getDefaultTexts().getFirst());
            assertEquals(
                    "https://metawal.wallonie.be/thesaurus/theme-geoportail-wallon#ThemesGeoportailWallon/10^https://metawal.wallonie.be/thesaurus/theme-geoportail-wallon#SubThemesGeoportailWallon/1030",
                    hierarchyTree.getKeys().get(1));

            assertEquals(
                    "imageryBaseMapsEarthCover",
                    indexRecord
                            .getCodelists()
                            .get(TOPIC)
                            .getFirst()
                            .getProperties()
                            .get(KEY));
            assertEquals(
                    "Carte de référence de la couverture terrestre",
                    indexRecord
                            .getCodelists()
                            .get(TOPIC)
                            .getFirst()
                            .getProperties()
                            .get(DEFAULT_TEXT));

            IndexingErrorMsg indexingErrorMessage =
                    indexRecord.getIndexingErrorMsg().getFirst();
            assertEquals("indexingErrorMsg-keywordNotFoundInThesaurus", indexingErrorMessage.getString());
            assertEquals("warning", indexingErrorMessage.getType());
            assertEquals("Open Data", indexingErrorMessage.getValues().get("keyword"));

            assertEquals("1 m", indexRecord.getResolutionDistance().getFirst());
            assertEquals(5000, indexRecord.getResolutionScaleDenominator().getFirst());

            assertEquals(
                    4,
                    indexRecord
                            .getConstraints()
                            .get("MD_LegalConstraintsOtherConstraintsObject")
                            .size());
            assertEquals(
                    "No limitations to public access",
                    indexRecord
                            .getConstraints()
                            .get("MD_LegalConstraintsOtherConstraintsObject")
                            .getFirst()
                            .get(DEFAULT_TEXT));

            assertEquals(
                    1,
                    indexRecord
                            .getUseLimitations()
                            .get("MD_LegalConstraintsUseLimitationObject")
                            .size());
            assertEquals(
                    "Conditions d'accès et d'utilisation spécifiques",
                    indexRecord
                            .getUseLimitations()
                            .get("MD_LegalConstraintsUseLimitationObject")
                            .getFirst()
                            .get(DEFAULT_TEXT));

            assertEquals(4, indexRecord.getLicenses().size());
            assertEquals(
                    "No limitations to public access",
                    indexRecord.getLicenses().getFirst().get(DEFAULT_TEXT));

            assertEquals(
                    "Région wallonne",
                    indexRecord.getExtentDescription().getFirst().get(DEFAULT_TEXT));

            assertEquals(
                    "{\"type\":\"Polygon\",\"coordinates\":"
                            + "[[[5.2841,50.3382],[5.6431,50.2618],[5.683,50.1171],"
                            + "[5.6431,49.792],[5.1378,50.0061],[4.9782,50.4145],[5.2841,50.3382]]]}",
                    indexRecord.getShapes().getFirst());
            assertEquals(
                    "{\"type\":\"Polygon\",\"coordinates\":"
                            + "[[[2.75,49.45],[6.5,49.45],[6.5,50.85],[2.75,50.85],[2.75,49.45]]]}",
                    indexRecord.getGeometries().getFirst());
            assertEquals("50.150000000000006,4.625", indexRecord.getLocations().getFirst());

            assertEquals(
                    "2020-03-31T22:00:00.000Z",
                    indexRecord.getResourceTemporalExtentDateRange().getFirst().getGte());
            assertEquals(
                    "2020-04-23T22:00:00.000Z",
                    indexRecord.getResourceTemporalExtentDateRange().getFirst().getLte());
            assertEquals(
                    "2020-04-01",
                    indexRecord
                            .getResourceTemporalExtentDetails()
                            .getFirst()
                            .getStart()
                            .getDate());
            assertEquals(
                    "#ISO-8601",
                    indexRecord
                            .getResourceTemporalExtentDetails()
                            .getFirst()
                            .getStart()
                            .getFrame());
            assertEquals(
                    "era",
                    indexRecord
                            .getResourceTemporalExtentDetails()
                            .getFirst()
                            .getStart()
                            .getCalendarEraName());
            assertEquals(
                    "before",
                    indexRecord
                            .getResourceTemporalExtentDetails()
                            .getFirst()
                            .getStart()
                            .getIndeterminatePosition());
            assertEquals(
                    "2020-09-09T22:00:00.000Z",
                    indexRecord.getResourceTemporalDateRange().getFirst().getLte());

            assertEquals(300, indexRecord.getResourceVerticalRange().getFirst().getLte());
            assertEquals(0, indexRecord.getResourceVerticalRange().getFirst().getGte());
            assertEquals("m", indexRecord.getResourceVerticalRange().getFirst().getUnit());

            assertEquals("EPSG:3812", indexRecord.getCoordinateSystem().getFirst());
            assertEquals(
                    "EPSG:3812",
                    indexRecord.getCoordinateSystemDetails().getFirst().getCode());
            assertEquals(
                    "http://www.opengis.net/def/crs/EPSG/0/3812",
                    indexRecord.getCoordinateSystemDetails().getFirst().getUrl());
            assertEquals(
                    "ETRS89 / Belgian Lambert 2008 (EPSG:3812)",
                    indexRecord.getCoordinateSystemDetails().getFirst().getName());
            assertEquals(
                    "EPSG", indexRecord.getCoordinateSystemDetails().getFirst().getCodeSpace());

            assertEquals(2, indexRecord.getHassource().size());
            assertEquals(
                    "7369222c-5241-452a-af07-4929506212f9",
                    indexRecord.getHassource().getFirst());

            assertEquals(4, indexRecord.getAssociatedRecords().size());

            assertEquals(10, indexRecord.getLinks().size());
            assertEquals("WWW:LINK", indexRecord.getLinks().getFirst().getProtocol());
            assertEquals("legend", indexRecord.getLinks().getFirst().getFunction());
            assertEquals(
                    "https://metawal.wallonie.be/geonetwork/srv/api/records/47b348f1-6e7a-4baa-963c-0232a43c0cff/attachments/WAL_OCS_IA__2020.lyr",
                    indexRecord.getLinks().getFirst().getUrl().get(DEFAULT_TEXT));
            assertEquals(
                    "Légende pour ArcGIS",
                    indexRecord.getLinks().getFirst().getName().get(DEFAULT_TEXT));
            assertEquals(
                    "Fichier de style lyr pour ArcGIS",
                    indexRecord.getLinks().getFirst().getDescription().get(DEFAULT_TEXT));
            assertEquals("", indexRecord.getLinks().getFirst().getApplicationProfile());

            assertEquals(
                    "Les principales étapes de production de la donnée WAL_OCS_IA__2020"
                            + " sont les suivantes :\n1. Acquisition des données sources: orthophotos 2020"
                            + " (missions aériennes) et modèle numérique de terrain"
                            + " (obtenu par photogrammétrie) ;"
                            + " \n2. Classification des pixels par un modèle d’apprentissage profond de"
                            + " segmentation sur bases des données sources ; \n3. Validation. \n\n"
                            + "Plus d’informations peuvent être trouvées dans les délivrables "
                            + "du projet disponibles via les ressources associées.",
                    indexRecord.getResourceLineage().getFirst().get(DEFAULT_TEXT));

            assertEquals(2, indexRecord.getHassource().size());
            assertEquals(
                    "7369222c-5241-452a-af07-4929506212f9",
                    indexRecord.getHassource().getFirst());

            assertEquals(4, indexRecord.getAssociatedRecords().size());
            assertEquals(
                    "7369222c-5241-452a-af07-4929506212f9",
                    indexRecord.getAssociatedRecords().getFirst().getTo());
            assertEquals(
                    "https://metawal.wallonie.be/geonetwork/srv/api/records/7369222c-5241-452a-af07-4929506212f9",
                    indexRecord.getAssociatedRecords().getFirst().getUrl());
            assertEquals(
                    "Orthophotos 2020",
                    indexRecord.getAssociatedRecords().getFirst().getTitle());
            assertEquals(remote, indexRecord.getAssociatedRecords().getFirst().getOrigin());
            assertEquals(
                    "sources", indexRecord.getAssociatedRecords().getFirst().getType());

            assertEquals(
                    2,
                    indexRecord
                            .getAssociatedResourceFields()
                            .get("recordLink_sources")
                            .size());
            assertEquals(
                    "Orthophotos 2020",
                    indexRecord
                            .getAssociatedResourceFields()
                            .get("recordLink_sources")
                            .getFirst());
            assertEquals(
                    2,
                    indexRecord
                            .getAssociatedResourceFields()
                            .get("recordLink_sources_uuid")
                            .size());
            assertEquals(
                    "7369222c-5241-452a-af07-4929506212f9",
                    indexRecord
                            .getAssociatedResourceFields()
                            .get("recordLink_sources_uuid")
                            .getFirst());
            assertEquals(
                    2,
                    indexRecord
                            .getAssociatedResourceFields()
                            .get("recordLink_sources_url")
                            .size());
            assertEquals(
                    "https://metawal.wallonie.be/geonetwork/srv/api/records/7369222c-5241-452a-af07-4929506212f9",
                    indexRecord
                            .getAssociatedResourceFields()
                            .get("recordLink_sources_url")
                            .getFirst());

            assertEquals(
                    "Source description",
                    indexRecord.getSourceDescription().getFirst().get(DEFAULT_TEXT));

            assertEquals(1, indexRecord.getProcessSteps().size());
            assertEquals("2024-06-06", indexRecord.getProcessSteps().getFirst().getDate());
            assertEquals(
                    "desc",
                    indexRecord.getProcessSteps().getFirst().getDescription().get(DEFAULT_TEXT));
            assertEquals(1, indexRecord.getProcessSteps().getFirst().getSource().size());
            assertEquals(
                    "desc",
                    indexRecord
                            .getProcessSteps()
                            .getFirst()
                            .getSource()
                            .getFirst()
                            .getDescription()
                            .get(DEFAULT_TEXT));
            assertEquals(
                    "desc",
                    indexRecord
                            .getProcessSteps()
                            .getFirst()
                            .getSource()
                            .getFirst()
                            .getDescription()
                            .get("langfre"));
            assertEquals(
                    1, indexRecord.getProcessSteps().getFirst().getProcessor().size());
            assertEquals(
                    "Process step processor",
                    indexRecord
                            .getProcessSteps()
                            .getFirst()
                            .getProcessor()
                            .getFirst()
                            .getOrganisation()
                            .get(DEFAULT_TEXT));

            assertEquals(1, indexRecord.getMeasures().size());
            assertEquals(
                    "Exactitude globale", indexRecord.getMeasures().getFirst().getName());
            assertEquals("83.80 %", indexRecord.getMeasures().getFirst().getValue());
            assertEquals(
                    "DQ_QuantitativeResult",
                    indexRecord.getMeasures().getFirst().getType());
            assertEquals(
                    "83.80 %",
                    indexRecord
                            .getMeasureFields()
                            .get("measure_Exactitudeglobale")
                            .getFirst());

            assertEquals("TIFF (.tif, .tiff)", indexRecord.getFormats().getFirst());

            assertEquals(3, indexRecord.getLinkUrls().size());
            assertEquals(
                    "https://geoportail.wallonie.be/walous",
                    indexRecord.getLinkUrls().getFirst());
            assertEquals(1, indexRecord.getLinkProtocols().size());
            assertEquals(
                    "WWW:LINK", indexRecord.getLinkProtocols().stream().toList().getFirst());

            assertEquals(
                    3,
                    indexRecord
                            .getLinkByProtocols()
                            .get("linkUrlProtocolWWWLINK")
                            .size());
            assertEquals(
                    "https://geoportail.wallonie.be/walous",
                    indexRecord
                            .getLinkByProtocols()
                            .get("linkUrlProtocolWWWLINK")
                            .getFirst());

            assertEquals(
                    "Cette donnée n'est accessible qu'en téléchargement direct ou via le panier de"
                            + " téléchargement du géoportail.\n"
                            + "Les instructions pour obtenir une copie physique d’une donnée sont détaillées sur"
                            + " https://geoportail.wallonie.be/telecharger.\n\n"
                            + "Sources à mentionner : SPW - Aerospacelab SA",
                    indexRecord.getOrderingInstructions().getFirst().get(DEFAULT_TEXT));

            assertEquals(
                    "e69b5aea-b174-47a7-b92f-3b782230ee9b",
                    indexRecord.getAssociatedUuids().getFirst());
            assertEquals(
                    "e69b5aea-b174-47a7-b92f-3b782230ee9b",
                    indexRecord
                            .getAssociatedUuidsByType()
                            .get("agg_associated_partOfSeamlessDatabase")
                            .getFirst());
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }

    void test_service() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json =
                    Files.readString(Path.of(new ClassPathResource("samples/iso19115-3.2018_service.json").getURI()));

            IndexRecord indexRecord = objectMapper.readValue(json, IndexRecord.class);
            assertEquals("service", indexRecord.getResourceType().getFirst());
            assertEquals("Service", indexRecord.getResourceTypeName().get(DEFAULT_TEXT));
            // TODO     assertEquals("download", indexRecord.getServiceType().getFirst());
            assertTrue(indexRecord.getInspireConformResource());

            assertEquals(
                    "Règlement (CE) n o 976/2009 de la Commission "
                            + "du 19 octobre 2009 portant modalités d’application"
                            + " de la directive 2007/2/CE du Parlement européen"
                            + " et du Conseil en ce qui concerne les services en réseau",
                    indexRecord.getSpecificationConformance().getFirst().getTitle());
            assertEquals(
                    "http://data.europa.eu/eli/reg/2009/976",
                    indexRecord.getSpecificationConformance().getFirst().getLink());
            assertEquals(
                    "Voir la spécification référencée",
                    indexRecord.getSpecificationConformance().getFirst().getExplanation());
            assertEquals(
                    "2009-10-19",
                    indexRecord.getSpecificationConformance().getFirst().getDate());
            assertEquals(
                    "true", indexRecord.getSpecificationConformance().getFirst().getPass());

            assertEquals(
                    "true",
                    indexRecord
                            .getConformsTo()
                            .get("conformTo_ReglementCEno9762009delaCommissiondu19octobre2009"
                                    + "portantmodalitesdapplicationdeladirective20072CEdu"
                                    + "ParlementeuropeenetduConseilencequiconcernelesservicesenreseau"));

            assertEquals(20, indexRecord.getRecordOperateOn().size());
            assertEquals(
                    "ad3eab81-e5de-43fd-b892-91189fdde604",
                    indexRecord.getRecordOperateOn().getFirst());
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }

    @Test
    void test_datamodel() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json =
                    Files.readString(Path.of(new ClassPathResource("samples/iso19115-3.2018_datamodel.json").getURI()));

            IndexRecord indexRecord = objectMapper.readValue(json, IndexRecord.class);
            assertEquals("featureCatalog", indexRecord.getResourceType().getFirst());
            assertEquals(1, indexRecord.getFeatureTypes().size());
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }

    @Test
    void test_serialization_for_all_properties() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonFromIndex =
                    Files.readString(Path.of(new ClassPathResource("samples/iso19115-3.2018_dataset.json").getURI()));

            IndexRecord indexRecord = objectMapper.readValue(jsonFromIndex, IndexRecord.class);

            String jsonFromSerialization =
                    objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(indexRecord);
            TypeReference<HashMap<String, Object>> type = new TypeReference<>() {};
            Map<String, Object> mapOfFieldFromIndex = objectMapper.readValue(jsonFromIndex, type);
            Map<String, Object> mapOfFieldFromSerialization = objectMapper.readValue(jsonFromSerialization, type);

            ArrayList<String> indexFieldList = new ArrayList<>(mapOfFieldFromIndex.keySet());
            indexFieldList.removeAll(mapOfFieldFromSerialization.keySet());
            assertEquals(
                    0,
                    indexFieldList.size(),
                    "The fields are not identical. Missing fields from serialization: " + indexFieldList);

            //      JsonNode tree1 = objectMapper.readTree(jsonFromIndex);
            //      JsonNode tree2 = objectMapper.readTree(jsonFromSerialization);

            // TODO assertEquals(tree1, tree2, "The JSON structures are not identical.");
        } catch (Exception e) {
            fail(e.getMessage(), e);
        }
    }
}
