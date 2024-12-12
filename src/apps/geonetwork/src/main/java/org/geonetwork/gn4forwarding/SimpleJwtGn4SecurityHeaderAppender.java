/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4forwarding;

import static org.springframework.web.servlet.function.HandlerFilterFunction.ofRequestProcessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.server.mvc.common.Shortcut;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * Appends (to a header) the security info (JUST username) as a signed JWT.
 *
 * <p>example: {"username":"dave"} -> JWT -> SIGNED JWT
 */
@Slf4j
public class SimpleJwtGn4SecurityHeaderAppender extends AbstractGn4SecurityHeaderAppender {

    /**
     * Encodes the token as signed JWT.
     *
     * <p>config.privateKeyUrl - URL for the private key (for signing) config.keyId -keyId for the private key
     *
     * @param token security info (i.e. username)
     * @param config filter config (see above).
     * @return string to attach to header
     */
    @Override
    protected String encodeToken(Gn4SecurityToken token, Map config) {
        String jwt;
        var privateKeyUrl = (String) config.get("privateKeyUrl");
        var keyId = (String) config.get("keyId");
        try {
            jwt = JwtSigning.createJWT(privateKeyUrl, token, keyId);
        } catch (Exception e) {
            log.error("couldnt create JWT", e);
            throw new RuntimeException(e);
        }
        return jwt;
    }

    /**
     * For spring framework. The name of this function is the name of filter used in application.yml. Make sure this is
     * annotated with @Shortcut
     *
     * @param headerName name of the header to send to GN5
     * @param privateKeyUrl URL for the private key (for signing)
     * @param keyId keyId for the private key
     * @return for spring to identify in application.yaml for the gateway (filters)
     */
    @Shortcut
    public static HandlerFilterFunction<ServerResponse, ServerResponse> addSimpleJwtGn4SecurityHeader(
            String headerName, String privateKeyUrl, String keyId) {
        return ofRequestProcessor(internalExecute("{ \"headerName\" : \"" + headerName + "\", \"privateKeyUrl\" : \""
                + privateKeyUrl + "\",\"keyId\": \"" + keyId + "\"}"));
    }

    /**
     * this will handle the debugging for one-request-at-a-time. In general oneRequestAtATime will be false and this
     * will not by synchronized. However, during debugging, it can be nice to do the requests one-by-one.
     *
     * @param jsonConfig config info
     * @return new request that has the gn4AuthHeaderName (with json security info - JUST USERNAME)
     */
    public static Function<ServerRequest, ServerRequest> internalExecute(String jsonConfig) {
        return request -> {
            Map config;
            try {
                config = AbstractGn4SecurityHeaderAppender.objectMapper.readValue(jsonConfig, Map.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            var object = new SimpleJwtGn4SecurityHeaderAppender();
            return object.execute(request, config);
        };
    }
}
