/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.editing;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.geonetwork.domain.Metadata;
import org.geonetwork.domain.repository.MetadataRepository;
import org.geonetwork.utility.date.ISODate;
import org.geonetwork.utility.legacy.xml.Xml;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.springframework.stereotype.Component;

/**
 * This class is responsible of reading and writing xml on the database. It works on tables like (id, data,
 * lastChangeDate).
 */
@Component
@Slf4j
@AllArgsConstructor
public class MetadataXmlSerializer {
    private final MetadataRepository metadataRepository;

    public Element select(String id) throws IOException, JDOMException {
        // TODO: removeHiddenElements
        // TODO: xlink?
        Metadata metadataEntity =
                metadataRepository.findById(Integer.parseInt(id)).get();

        return Xml.loadString(metadataEntity.getData(), false);
    }

    public void update(String id, String newUuid, Element xml, String changeDate, boolean updateDateStamp) {
        // TODO   if (resolveXLinks()) Processor.removeXLink(xml);

        Metadata metadataEntity =
                metadataRepository.findById(Integer.parseInt(id)).get();

        // TODO Needed ?    metadataEntity.setDataAndFixCR(xml);
        metadataEntity.setData(Xml.getString(xml));

        if (updateDateStamp) {
            if (changeDate == null) {
                metadataEntity.setChangedate(new ISODate().getDateAndTimeUtc());
            } else {
                metadataEntity.setChangedate(new ISODate(changeDate).getDateAndTimeUtc());
            }
        }

        if (newUuid != null) {
            metadataEntity.setUuid(newUuid);
        }

        metadataRepository.save(metadataEntity);
    }
}
