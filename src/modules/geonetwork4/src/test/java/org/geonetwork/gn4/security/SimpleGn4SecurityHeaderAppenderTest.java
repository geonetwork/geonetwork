/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.gn4.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class SimpleGn4SecurityHeaderAppenderTest {

    /** tests that JSON is correctly setup. */
    @Test
    public void testEncode() {
        var encoder = new SimpleGn4SecurityHeaderAppender();

        var token = new Gn4SecurityToken("testcase_dave");
        var tokenEncoded = encoder.encodeToken(token, null);

        assertEquals("{\"username\":\"testcase_dave\"}", tokenEncoded);
    }
}
