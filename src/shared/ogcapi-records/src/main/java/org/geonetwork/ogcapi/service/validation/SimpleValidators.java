/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.validation;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.text.MessageFormat;

/** simple validation */
public class SimpleValidators {

    /**
     * validates a catalogId. This must only have letters, numbers, or "-" or "_".
     *
     * @param catalogId from user
     * @throws Exception if the catalogId is bad
     */
    public static void validateCatalogId(String catalogId) throws Exception {
        if (isBlank(catalogId)) {
            throw new Exception("no catalogId!");
        }
        if (!catalogId.matches("^[a-zA-Z0-9_-]+$")) {
            throw new Exception(MessageFormat.format("catalogId ''{0}'' is invalid!", catalogId));
        }
    }
}
