/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.gn4.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.web.servlet.function.ServerRequest;

/**
 * This is the parent class that handles most of the interaction for creating Gn4SecurityHeaderAppenders.
 *
 * <p>1. subclass this, and fill in the encodeToken(method). 2. add the static methods needed by the spring
 * infrastructure @Shortcut public static HandlerFilterFunction<ServerResponse, ServerResponse> filterName(..args..)
 * {...}
 *
 * <p>ARGUMENT should be a JSON string with the following: 1. headerName -- name of the header to populate 2.
 * oneRequestAtATime - true = only allow one request at a time (useful for debugging). default = false 3. anything else
 * needed for the filter
 */
@Slf4j
public abstract class AbstractGn4SecurityHeaderAppender {

    // json
    public static final ObjectMapper objectMapper = new ObjectMapper();
    // for oneRequestAtATime synchronization
    private static final Object LOCK = new Object();

    /**
     * main entry for the GN5->GN4 security token. JUST USERNAME
     *
     * @param request incoming gn4 request
     * @param config info about this filter (from application.yml)
     * @return new request that has the gn4AuthHeaderName (with json security info - JUST USERNAME)
     */
    public ServerRequest execute_impl(ServerRequest request, Map config) {
        log1(request);
        if (!config.containsKey("headerName") || ((String) config.get("headerName")).isEmpty()) {
            var e = new RuntimeException("Missing required parameter 'headerName'");
            log.error("bad AbstractGn4SecurityHeaderAppender config", e);
            throw e;
        }

        var gn4AuthHeaderName = (String) config.get("headerName");

        // remove the output header (or it's a MAJOR security problem)
        var changedRequest = ServerRequest.from(request).headers(new Consumer<HttpHeaders>() {
            // security - remove this incoming header, or it could be trusted in GN4!!
            @Override
            public void accept(HttpHeaders httpHeaders) {
                httpHeaders.remove(gn4AuthHeaderName);
            }
        });

        //        var gn5Session = request.session();
        //        SecurityContext securityContext = (SecurityContextImpl)
        // gn5Session.getAttribute("SPRING_SECURITY_CONTEXT");
        var securityContext = SecurityContextHolder.getContext();
        var username = getUserName(securityContext);
        // don't pass on anonymous to GN4 - it will automatically happen and we don't want to create the
        //  "anonymousUser" in the database.
        if (username == null || "anonymousUser".equals(username)) {
            return changedRequest.build();
        }

        var token = new Gn4SecurityToken(username);

        // get the header
        String tokenJSON = encodeToken(token, config);

        // add header to GN4 request
        return changedRequest.header(gn4AuthHeaderName, tokenJSON).build();
    }

    protected abstract String encodeToken(Gn4SecurityToken token, Map config);

    /**
     * Gets the Username from the gn5 request (security context "SPRING_SECURITY_CONTEXT" attribute).
     *
     * @param securityContext from request (SPRING_SECURITY_CONTEXT attribute) that will be proxied to gn4
     * @return null or the Username
     */
    public String getUserName(SecurityContext securityContext) {
        if (securityContext == null) {
            // no security context - let GN4 create it own
            return null;
        }
        var auth = securityContext.getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            //   security context doesn't have an auth- let GN4 create it own
            // (I don't think this should happen - if there's a security context, there should be an auth (might be
            // anonymous)
            return null;
        }
        var principle = auth.getPrincipal();
        if (principle instanceof User user) {
            return user.getUsername();
        }
        if (principle instanceof DefaultOAuth2User user) {
            // #getName() should return the username of the user in the GN DB!!
            //
            // configure:
            //        provider:
            //          github:
            //            user-name-attribute: login
            return user.getName();
        }
        if (principle instanceof String principleString) {
            return principleString;
        }
        throw new RuntimeException("unknown auth type - add support here!");
    }

    /**
     * this will handle the debugging for one-request-at-a-time. In general oneRequestAtATime will be false and this
     * will not by synchronized. However, during debugging, it can be nice to do the requests one-by-one.
     *
     * @param config config info
     * @return new request that has the gn4AuthHeaderName (with json security info - JUST USERNAME)
     */
    public ServerRequest execute(ServerRequest request, Map config) {

        var oneRequestAtATime = false;
        if (config.containsKey("oneRequestAtATime") && config.get("oneRequestAtATime") instanceof Boolean) {
            oneRequestAtATime = (Boolean) config.get("oneRequestAtATime");
        }

        if (oneRequestAtATime) {
            synchronized (LOCK) {
                return execute_impl(request, config);
            }
        } else {
            return execute_impl(request, config);
        }
    }

    // ------------------ debugging info -------------------

    /**
     * logs the incoming requests (GN5 that's going to GN4). Logs as trace
     *
     * @param request - incoming gn5 request
     */
    public void log1(ServerRequest request) {
        if (!log.isTraceEnabled()) {
            return;
        }
        log.trace("addGn4SecurityHeader: start");
        log.trace("addGn4SecurityHeader: incoming GN5 URL:{}", request.uri());

        var gn5Session = request.session();
        if (gn5Session != null) {
            log.trace("addGn4SecurityHeader: has  GN5 session, id={}", gn5Session.getId());
            // logger.trace("addGn4SecurityHeader: has  GN5 session, base64 decoded id="+
            //       new String(Base64.getDecoder().decode(gn5Session.getId())));
            log.trace(
                    "addGn4SecurityHeader: session has attributes: {}",
                    String.join(",", Collections.list(gn5Session.getAttributeNames())));
            if (gn5Session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
                log.trace("addGn4SecurityHeader: GN5 has security context!");
                SecurityContextImpl securityContext =
                        (SecurityContextImpl) gn5Session.getAttribute("SPRING_SECURITY_CONTEXT");
                var auth = securityContext.getAuthentication();
                if (auth != null) {
                    User user = (User) auth.getPrincipal();
                    log.trace(
                            "addGn4SecurityHeader: GN5 security context authentication type: {}",
                            auth.getClass().getSimpleName());
                    log.trace("addGn4SecurityHeader: GN5 principle username={}", user.getUsername());
                    var authorities = auth.getAuthorities();
                    var authoritiesNames =
                            authorities.stream().map(x -> x.getAuthority()).collect(Collectors.toList());
                    log.trace("addGn4SecurityHeader: GN5 authorities:{}", String.join(",", authoritiesNames));
                } else {
                    log.trace("addGn4SecurityHeader: GN5 security context has NO authentication!");
                }
            }
        }
    }
}
