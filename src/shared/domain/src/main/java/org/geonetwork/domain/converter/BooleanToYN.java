/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.domain.converter;

import org.hibernate.type.CharBooleanConverter;

/** Handles conversion to/from {@code Boolean} as {@code 'y'} or {@code 'n'} */
public class BooleanToYN extends CharBooleanConverter {
    public static final BooleanToYN INSTANCE = new BooleanToYN();
    private static final String[] VALUES = new String[] {"n", "y"};

    @Override
    protected String[] getValues() {
        return VALUES;
    }

    @Override
    public Boolean toDomainValue(Character relationalForm) {
        if (relationalForm == null) {
            return null;
        } else {
            switch (relationalForm) {
                case 'n':
                    return false;
                case 'y':
                    return true;
                default:
                    return null;
            }
        }
    }

    @Override
    public Character toRelationalValue(Boolean domainForm) {
        return domainForm == null ? null : Character.valueOf((char) (domainForm ? 'y' : 'n'));
    }
}
