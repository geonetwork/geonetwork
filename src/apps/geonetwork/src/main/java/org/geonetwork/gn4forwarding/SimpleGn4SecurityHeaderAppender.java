/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
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
 * Appends (to a header) the security info (JUST username) as a JSON object.
 *
 * <p>example: {"username":"dave"}
 */
@Slf4j
public class SimpleGn4SecurityHeaderAppender extends AbstractGn4SecurityHeaderAppender {

    /**
     * encodes the security token as JSON.
     *
     * @param token - gn5 security summary (i.e. username)
     * @param config - not required (cf SimpleJwtGn4SecurityHeaderAppender)
     * @return encoded token (to be attached to header)
     */
    @Override
    protected String encodeToken(Gn4SecurityToken token, Map config) {
        // convert to JSON
        String tokenJSON = "";
        try {
            tokenJSON = objectMapper.writeValueAsString(token);
        } catch (JsonProcessingException e) {
            log.error("couldnt convert token to json", e);
        }
        return tokenJSON;
    }

    /**
     * For spring framework.
     *
     * @param headerName name of the header to set
     * @return for spring to identify in application.yaml for the gateway (filters)
     */
    @Shortcut
    public static HandlerFilterFunction<ServerResponse, ServerResponse> addSimpleGn4SecurityHeader(String headerName) {
        return ofRequestProcessor(internalExecute("{ \"headerName\" : \"" + headerName + "\" }"));
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
            var object = new SimpleGn4SecurityHeaderAppender();
            return object.execute(request, config);
        };
    }
}
