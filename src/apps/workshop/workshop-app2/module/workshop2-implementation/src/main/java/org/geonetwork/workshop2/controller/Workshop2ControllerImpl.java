/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.workshop2.controller;

import org.geonetwork.workshop2.Workshop2Api;
import org.geonetwork.workshop2.model.Workshop2MetadataSummarySchemaDto;
import org.geonetwork.workshop2.service.MetadataSummaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Workshop2ControllerImpl implements Workshop2Api {

    private MetadataSummaryService metadataSummaryService;

    public Workshop2ControllerImpl(MetadataSummaryService metadataSummaryService) {
        this.metadataSummaryService = metadataSummaryService;
    }

    /**
     * trivial implementation of the PATH_GET_METADATA_SUMMARY that hands off to the metadataSummaryService (in shared/)
     *
     * @return summary, in http form (spring content negotiation will convert to JSON)
     */
    @Override
    @RequestMapping(
            method = RequestMethod.GET,
            value = Workshop2Api.PATH_GET_METADATA_SUMMARY,
            produces = {"application/json"})
    public ResponseEntity<Workshop2MetadataSummarySchemaDto> getMetadataSummary() {
        var result = metadataSummaryService.createSummary();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
