package org.geonetwork.utility;

public class TypeUtil {
    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBoolean(String value) {
        return value.equals("true") || value.equals("false");
    }
}
