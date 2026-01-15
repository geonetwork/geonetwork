/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.gn4.security;

import static org.geonetwork.gn4.security.JwtSigning.AUDIENCE;
import static org.geonetwork.gn4.security.JwtSigning.ISSUER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Create private and public key for testing: openssl req -x509 -sha256 -nodes -days 36500 -newkey rsa:2048 -keyout
 * private.key -out certificate_pub.crt
 *
 * <p>I've created two different ones in test/resources/gn4forwarding/privatepublic1 and privatepublic2
 *
 * <p>I have given these 100 years validity - in Dec 2124, you'll have to regenerate the keys!
 */
public class JwtSigningTest {

    String privateKey1Location = "/gn4forwarding/privatepublic1/private.key";
    String privateKey2Location = "/gn4forwarding/privatepublic2/private.key";

    String publicKey1Location = "/gn4forwarding/privatepublic1/certificate_pub.crt";
    String publicKey2Location = "/gn4forwarding/privatepublic2/certificate_pub.crt";
    URL publicKey1URL;
    URL publicKey2URL;

    URL privateKey1URL;
    URL privateKey2URL;

    @BeforeEach
    public void setUp() throws Exception {
        privateKey1URL = this.getClass().getResource(privateKey1Location);
        privateKey2URL = this.getClass().getResource(privateKey2Location);

        publicKey1URL = this.getClass().getResource(publicKey1Location);
        publicKey2URL = this.getClass().getResource(publicKey2Location);
    }

    /**
     * make sure you can read the private and public keys.
     *
     * @throws Exception error - test fail
     */
    @Test
    public void readKeys() throws Exception {
        var pk1 = JwtSigning.getPrivateKey(privateKey1URL.toString());
        var pk2 = JwtSigning.getPrivateKey(privateKey2URL.toString());

        assertNotNull(pk1);
        assertNotNull(pk2);

        assertEquals("RSA", pk1.getAlgorithm());
        assertEquals("RSA", pk2.getAlgorithm());

        var publick1 = JwtSigning.getPublicKey(publicKey1URL.toString());
        var publick2 = JwtSigning.getPublicKey(publicKey2URL.toString());

        assertNotNull(publick1);
        assertNotNull(publick2);

        assertEquals("RSA", publick1.getAlgorithm());
        assertEquals("RSA", publick2.getAlgorithm());
    }

    /**
     * tests signing, content of the JWT and makes sure that the signing can be verified.
     *
     * @throws Exception error - test fail
     */
    @Test
    public void testSigning() throws Exception {
        var token = new Gn4SecurityToken("testcase_dave");
        var jwt = JwtSigning.createJWT(privateKey1URL.toString(), token, "testcasekeyid");

        assertNotNull(jwt);

        var signedJWT = SignedJWT.parse(jwt);

        assertEquals("testcasekeyid", signedJWT.getHeader().getKeyID());

        assertEquals("RS256", signedJWT.getHeader().getAlgorithm().getName());
        assertEquals(ISSUER, signedJWT.getJWTClaimsSet().getIssuer());
        assertEquals(AUDIENCE, signedJWT.getJWTClaimsSet().getAudience().get(0));
        assertEquals("testcase_dave", signedJWT.getJWTClaimsSet().getClaim("username"));

        var publicKey1 = JwtSigning.getPublicKey(publicKey1URL.toString());
        var publicKey2 = JwtSigning.getPublicKey(publicKey2URL.toString());

        // was signed by private key 1, should verify against public key 1
        JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey1);
        var valid = signedJWT.verify(verifier);
        assertTrue(valid);

        // should not verify against a different public key
        verifier = new RSASSAVerifier((RSAPublicKey) publicKey2);
        valid = signedJWT.verify(verifier);
        assertFalse(valid);
    }
}
