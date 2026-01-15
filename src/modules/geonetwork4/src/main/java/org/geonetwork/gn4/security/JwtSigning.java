/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.gn4.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.net.URI;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import org.apache.commons.io.IOUtils;

/**
 * Sign the Jwt payload.
 *
 * <p>Java needs the keys to be in a specific format!
 *
 * <p>Create keys: openssl req -x509 -sha256 -nodes -days 365 -newkey rsa:2048 -keyout private.key -out
 * certificate_pub.crt
 *
 * <p>see https://connect2id.com/products/nimbus-jose-jwt/examples/jwt-with-rsa-signature see
 * https://stackoverflow.com/questions/11410770/load-rsa-public-key-from-file
 * https://stackoverflow.com/questions/58249182/generate-json-web-token-jwt-with-a-private-key
 * https://jwkset.com/generate
 */
public class JwtSigning {

    public static final String ISSUER = "http://gn5.geonetwork.org";
    public static final String AUDIENCE = "g4.from.g5.proxy";

    public static PrivateKey getPrivateKey(String url) throws Exception {
        String privateKeyBase64;
        try (var stream = new URI(url).toURL().openStream()) {
            privateKeyBase64 = IOUtils.toString(stream);
        }
        if (privateKeyBase64.startsWith("-----BEGIN PRIVATE KEY-----")) {
            privateKeyBase64 = privateKeyBase64.replace("-----BEGIN PRIVATE KEY-----", "");
            privateKeyBase64 = privateKeyBase64.replace("-----END PRIVATE KEY-----", "");
            privateKeyBase64 = privateKeyBase64.trim();
        }
        privateKeyBase64 = privateKeyBase64.replace("\n", "").trim();
        byte[] privateKeyDecoded = Base64.getDecoder().decode(privateKeyBase64);
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyDecoded);
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(pkcs8EncodedKeySpec);

        return privateKey;
    }

    public static PublicKey getPublicKey(String url) throws Exception {
        CertificateFactory f = CertificateFactory.getInstance("X.509");
        X509Certificate certificate;
        try (var stream = new URI(url).toURL().openStream()) {
            certificate = (X509Certificate) f.generateCertificate(stream);
        }
        return certificate.getPublicKey();
    }

    public static String createJWT(String privateKeyUrl, Gn4SecurityToken securityInfo, String keyId) throws Exception {
        var privateKey = getPrivateKey(privateKeyUrl);

        JWSSigner signer = new RSASSASigner(privateKey);

        var expire = Instant.now().plus(1, ChronoUnit.HOURS);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(securityInfo.getUsername())
                .issuer(ISSUER)
                .audience(AUDIENCE)
                .expirationTime(Date.from(expire))
                .claim("username", securityInfo.getUsername())
                // .claim("userGroupProfile", securityInfo.getUserGroupProfile())
                .build();

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).keyID(keyId).build(), claimsSet);

        signedJWT.sign(signer);

        String jwt = signedJWT.serialize();

        //    signedJWT = SignedJWT.parse(jwt);
        //    var publicKey = getPublicKey("file:///Users/db/delme/key/certificate_pub.crt");
        //
        //    JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) publicKey);
        //    var valid = signedJWT.verify(verifier);

        return jwt;
    }
}
