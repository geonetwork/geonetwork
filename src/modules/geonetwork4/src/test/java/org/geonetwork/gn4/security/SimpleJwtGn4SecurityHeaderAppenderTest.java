/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

import java.util.HashMap;
import org.junit.jupiter.api.Test;

public class SimpleJwtGn4SecurityHeaderAppenderTest {

    /** tests that JwtSigning.createJWT() is called with the correct arguments. */
    @Test
    public void testEncode() {
        var encoder = new SimpleJwtGn4SecurityHeaderAppender();

        var token = new Gn4SecurityToken("testcase_dave");
        var config = new HashMap<String, String>();
        config.put("privateKeyUrl", "my privateKeyUrl");
        config.put("keyId", "my keyId");

        try (var JwtSigning_ = mockStatic(JwtSigning.class)) {
            JwtSigning_.when(() -> JwtSigning.createJWT(eq("my privateKeyUrl"), any(), eq("my keyId")))
                    .thenReturn("JWT TESTCASE");
            var tokenEncoded = encoder.encodeToken(token, config);
            assertEquals("JWT TESTCASE", tokenEncoded);
        }
    }
}
