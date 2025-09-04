/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.configuration;

public enum OverrideType {
    LIST_STRING,
    LIST_INTEGER,
    LIST_DOUBLE,
    LIST_DATE,
    LIST_BOOLEAN,
    SINGLE_STRING,
    SINGLE_INTEGER,
    SINGLE_DOUBLE,
    SINGLE_DATE,
    SINGLE_BOOLEAN;

    public static boolean isList(OverrideType type) {
        return (type == LIST_STRING
                || type == LIST_INTEGER
                || type == LIST_DOUBLE
                || type == LIST_DATE
                || type == LIST_BOOLEAN);
    }

    public static SimpleType getSimpleType(OverrideType type) {
        if (type == LIST_STRING || type == SINGLE_STRING) {
            return SimpleType.STRING;
        } else if (type == LIST_INTEGER || type == SINGLE_INTEGER) {
            return SimpleType.INTEGER;
        } else if (type == LIST_DOUBLE || type == SINGLE_DOUBLE) {
            return SimpleType.DOUBLE;
        } else if (type == LIST_DATE || type == SINGLE_DATE) {
            return SimpleType.DATE;
        } else if (type == LIST_BOOLEAN || type == SINGLE_BOOLEAN) {
            return SimpleType.BOOLEAN;
        }
        throw new IllegalArgumentException("Unsupported type " + type);
    }

    public static Class getJavaType(OverrideType type) {
        if (type == LIST_STRING || type == SINGLE_STRING) {
            return String.class;
        } else if (type == LIST_INTEGER || type == SINGLE_INTEGER) {
            return Integer.class;
        } else if (type == LIST_DOUBLE || type == SINGLE_DOUBLE) {
            return Double.class;
        } else if (type == LIST_DATE || type == SINGLE_DATE) {
            return String.class; // JSON treats dates like strings
        } else if (type == LIST_BOOLEAN || type == SINGLE_BOOLEAN) {
            return Boolean.class;
        }
        throw new IllegalArgumentException("Unsupported type " + type);
    }

    public static Class getJavaType(SimpleType type) {
        if (type == SimpleType.STRING) {
            return String.class;
        } else if (type == SimpleType.INTEGER) {
            return Integer.class;
        } else if (type == SimpleType.DOUBLE) {
            return Double.class;
        } else if (type == SimpleType.DATE) {
            return String.class; // JSON treats dates like strings
        } else if (type == SimpleType.BOOLEAN) {
            return Boolean.class;
        }
        throw new IllegalArgumentException("Unsupported type " + type);
    }
}
