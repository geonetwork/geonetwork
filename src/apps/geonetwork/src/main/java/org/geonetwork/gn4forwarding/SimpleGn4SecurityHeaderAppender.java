/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4forwarding;

import static org.geonetwork.gn4forwarding.Gn4SecurityHeaderAppender.log1;
import static org.springframework.web.servlet.function.HandlerFilterFunction.ofRequestProcessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.function.Consumer;
import java.util.function.Function;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.server.mvc.common.Shortcut;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * See Gn4SecurityHeaderAppender, which is a more complicated version of this filter.
 *
 * <p>This ONLY extracts the UserName (no rights or anything else) and sends that to GN4. GN4 needs to be setup to NOT
 * update profiles or groups. GN4 will get the rights from the DB.
 */
public class SimpleGn4SecurityHeaderAppender {
    private static final Object LOCK = new Object();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Gn4SecurityHeaderAppender.class);
    // debugging?  Set this to true.  This makes each request happen sequencially.
    // This is useful for debugging (all logging together)
    // but, has a performance impact.
    private static final boolean oneRequestAtATime = true;

    /**
     * main entry for the GN5->GN4 security token. JUST USERNAME
     *
     * @param request incomming gn4 request
     * @param gn4AuthHeaderName name of the header to set with security info
     * @return new request that has the gn4AuthHeaderName (with json security info - JUST USERNAME)
     */
    public static ServerRequest addSimpleGn4SecurityHeader_impl(ServerRequest request, String gn4AuthHeaderName) {
        log1(request);

        // remove the output header (or its a MAJOR security problem)
        var changedRequest = ServerRequest.from(request).headers(new Consumer<HttpHeaders>() {
            // security - remove this incoming header or it will be trusted in GN4!!
            @Override
            public void accept(HttpHeaders httpHeaders) {
                httpHeaders.remove(gn4AuthHeaderName);
            }
        });

        var user = getUser(request);
        if (user == null) {
            return null;
        }

        var username = user.getUsername();
        if (username == null) {
            return null;
        }
        var token = new Gn4SecurityToken(username);

        // convert to JSON
        String tokenJSON = "";
        try {
            tokenJSON = objectMapper.writeValueAsString(token);
        } catch (JsonProcessingException e) {
            logger.error("couldnt convert token to json", e);
        }

        // add header to GN4 request
        return changedRequest.header(gn4AuthHeaderName, tokenJSON).build();
    }

    /**
     * Gets the User from the gn5 request.
     *
     * @param request request that will be proxied to gn4
     * @return null or the User
     */
    private static User getUser(ServerRequest request) {
        var gn5Session = request.session();
        SecurityContextImpl securityContext = (SecurityContextImpl) gn5Session.getAttribute("SPRING_SECURITY_CONTEXT");
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
        User user = (User) auth.getPrincipal();
        if (user == null || user.getUsername() == null) {
            // not sure if this is possible, but if there's no user, there isn't really a correct authentication
            return null;
        }

        return user;
    }

    /**
     * this will handle the debugging for one-request-at-a-time. In general oneRequestAtATime will be false and this
     * will not by synchronized. However, during debugging, it can be nice to do the requests one-by-one.
     *
     * @param gn4AuthHeaderName name of the header to set in the GN4 request
     * @return new request that has the gn4AuthHeaderName (with json security info - JUST USERNAME)
     */
    public static Function<ServerRequest, ServerRequest> addSimpleGn4SecurityHeader_(String gn4AuthHeaderName) {
        return request -> {
            if (oneRequestAtATime) {
                synchronized (LOCK) {
                    return addSimpleGn4SecurityHeader_impl(request, gn4AuthHeaderName);
                }
            } else {
                return addSimpleGn4SecurityHeader_impl(request, gn4AuthHeaderName);
            }
        };
    }

    /**
     * For spring framework.
     *
     * @param gn4AuthHeaderName name of the header to set in the GN4 request
     * @return for spring to identify in application.yaml for the gateway (filters)
     */
    @Shortcut
    public static HandlerFilterFunction<ServerResponse, ServerResponse> addSimpleGn4SecurityHeader(
            String gn4AuthHeaderName) {
        return ofRequestProcessor(addSimpleGn4SecurityHeader_(gn4AuthHeaderName));
    }
}
