/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.schemas;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@ToString
@EqualsAndHashCode
public class Version implements Comparable<Version> {
    private final int major, minor, micro;

    /**
     * Parses a version number removing extra "-*" element and returning an integer. "2.7.0-SNAPSHOT"
     * is returned as 270.
     *
     * @param number The version number to parse
     * @return The version number as an integer
     */
    public static Version parseVersionNumber(String number) throws Exception {
        // Remove extra "-SNAPSHOT" info which may be in version number
        int dashIdx = number.indexOf("-");
        if (dashIdx != -1) {
            number = number.substring(0, number.indexOf("-"));
        }
        switch (numDots(number)) {
            case 0:
                number += ".0.0";
                break;
            case 1:
                number += ".0";
                break;
            default:
                break;
        }

        final String[] parts = StringUtils.split(number, ".");
        String major = parts[0];
        String minor = parts.length > 1 ? parts[1] : "0";
        String micro = parts.length > 2 ? parts[2] : "0";
        return new Version(major, minor, micro);
    }

    public Version(String major, String minor, String micro) {
        this.major = Integer.parseInt(major);
        this.minor = Integer.parseInt(minor);
        this.micro = Integer.parseInt(micro);
    }

    public Version() {
        this("0", "0", "0");
    }

    @Override
    public int compareTo(Version o) {
        if (major != o.major) {
            return Integer.compare(major, o.major);
        }
        if (minor != o.minor) {
            return Integer.compare(minor, o.minor);
        }
        if (micro != o.micro) {
            return Integer.compare(micro, o.micro);
        }
        return 0;
    }

    private static int numDots(String number) {
        int num = 0;

        for (int i = 0; i < number.length(); i++) {
            if (number.charAt(i) == '.') {
                num++;
            }
        }
        return num;
    }
}
