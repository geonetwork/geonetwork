/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4forwarding;

import static org.springframework.web.servlet.function.HandlerFilterFunction.ofRequestProcessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.geonetwork.domain.repository.GroupRepository;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsergroupRepository;
import org.geonetwork.utility.ApplicationContextProvider;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.server.mvc.common.Shortcut;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

/**
 * This is the complicated version that does full profile-groups and profile. This would be used for users managed
 * OUTSIDE the database - like an oauth2 user with the roles in the remote IDP.
 *
 * <p>If you are managing the roles in the GN DB, then use SimpleGn4SecurityHeaderAppender
 */
public class Gn4SecurityHeaderAppender {

    // see oneRequestAtATime (for syncronizing single access)
    private static final Object LOCK = new Object();
    // JSON converter
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Gn4SecurityHeaderAppender.class);
    // debugging?  Set this to true.  This makes each request happen sequencially.
    // This is useful for debugging (all logging together)
    // but, has a performance impact.
    private static final boolean oneRequestAtATime = true;

    /**
     * logs the incomming requests (GN5 that's going to GN4). Logs as trace
     *
     * @param request - incomming gn5 request
     */
    public static void log1(ServerRequest request) {
        if (!logger.isTraceEnabled()) {
            return;
        }
        logger.trace("addGn4SecurityHeader: start");
        logger.trace("addGn4SecurityHeader: incoming GN5 URL:" + request.uri());

        var gn5Session = request.session();
        if (gn5Session != null) {
            logger.trace("addGn4SecurityHeader: has  GN5 session, id=" + gn5Session.getId());
            // logger.trace("addGn4SecurityHeader: has  GN5 session, base64 decoded id="+
            //       new String(Base64.getDecoder().decode(gn5Session.getId())));
            logger.trace("addGn4SecurityHeader: session has attributes: "
                    + String.join(",", Collections.list(gn5Session.getAttributeNames())));
            if (gn5Session.getAttribute("SPRING_SECURITY_CONTEXT") != null) {
                logger.trace("addGn4SecurityHeader: GN5 has security context!");
                SecurityContextImpl securityContext =
                        (SecurityContextImpl) gn5Session.getAttribute("SPRING_SECURITY_CONTEXT");
                var auth = securityContext.getAuthentication();
                if (auth != null) {
                    User user = (User) auth.getPrincipal();
                    logger.trace("addGn4SecurityHeader: GN5 security context authentication type: "
                            + auth.getClass().getSimpleName());
                    logger.trace("addGn4SecurityHeader: GN5 principle username=" + user.getUsername());
                    var authorities = auth.getAuthorities();
                    var authoritiesNames =
                            authorities.stream().map(x -> x.getAuthority()).collect(Collectors.toList());
                    logger.trace("addGn4SecurityHeader: GN5 authorities:" + String.join(",", authoritiesNames));
                } else {
                    logger.trace("addGn4SecurityHeader: GN5 security context has NO authentication!");
                }
            }
        }
    }

    /**
     * this does DB access to get the usergroups associated with a user
     *
     * @param userName username of the user to get groups from
     * @return list of Profile-Groups for the user
     */
    public static List<UserGroupProfile> userGroups(String userName) {
        var userRepo = ApplicationContextProvider.getApplicationContext().getBean(UserRepository.class);

        var user = userRepo.findOptionalByUsername(userName);
        if (user.isEmpty()) {
            return null;
        }
        var userid = user.get().getId();

        var userGroupRepo = ApplicationContextProvider.getApplicationContext().getBean(UsergroupRepository.class);

        var groups = userGroupRepo.findAllByUserid_Id(userid);
        if (groups.isEmpty()) {
            return null;
        }

        var groupRepo = ApplicationContextProvider.getApplicationContext().getBean(GroupRepository.class);

        var result = groups.stream()
                .map(x -> new UserGroupProfile(
                        groupRepo.findById(x.getId().getGroupid()).get().getName(),
                        x.getId().getProfile()))
                .collect(Collectors.toList());

        return result;
    }

    /**
     * main entry for the GN5->GN4 security token.
     *
     * @param request incomming gn4 request
     * @param gn4AuthHeaderName name of the header to set with security info
     * @return new request that has the gn4AuthHeaderName (with json security info)
     */
    public static ServerRequest addGn4SecurityHeader_impl(ServerRequest request, String gn4AuthHeaderName) {
        log1(request);
        var changedRequest = ServerRequest.from(request).headers(new Consumer<HttpHeaders>() {
            // security - remove this incoming header or it will be trusted in GN4!!
            @Override
            public void accept(HttpHeaders httpHeaders) {
                httpHeaders.remove(gn4AuthHeaderName);
            }
        });

        var gn5Session = request.session();
        SecurityContextImpl securityContext = (SecurityContextImpl) gn5Session.getAttribute("SPRING_SECURITY_CONTEXT");
        if (securityContext == null) {
            // no security context - let GN4 create it own
            return changedRequest.build();
        }
        var auth = securityContext.getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            //   security context doesn't have an auth- let GN4 create it own
            // (I dont think this should happen - if there's a security context, there should be an auth (might be
            // anonymous)
            return changedRequest.build();
        }
        User user = (User) auth.getPrincipal();
        if (user == null || user.getUsername() == null) {
            // not sure if this is possible, but if there's no user, there isn't really a correct authentication
            return changedRequest.build();
        }

        var username = user.getUsername();
        var authorities = auth.getAuthorities();
        var groups = userGroups(username);

        var token = new Gn4SecurityToken(username, authorities, groups);

        String tokenJSON = "";
        try {
            tokenJSON = objectMapper.writeValueAsString(token);
        } catch (JsonProcessingException e) {
            logger.error("couldnt convert token to json", e);
        }

        return changedRequest.header(gn4AuthHeaderName, tokenJSON).build();
    }

    /**
     * wrapper than handles debugging synchro. If oneRequestAtATime=true - one-request-at-a-time Otherwise, handle in
     * parallel (by spring gateway)
     *
     * @param gn4AuthHeaderName name of the header to set with security info
     * @return new request that has the gn4AuthHeaderName (with json security info)
     */
    public static Function<ServerRequest, ServerRequest> addGn4SecurityHeader_(String gn4AuthHeaderName) {
        return request -> {
            if (oneRequestAtATime) {
                synchronized (LOCK) {
                    return addGn4SecurityHeader_impl(request, gn4AuthHeaderName);
                }
            } else {
                return addGn4SecurityHeader_impl(request, gn4AuthHeaderName);
            }
        };
    }

    /**
     * for registration in the application.yaml.
     *
     * @param gn4AuthHeaderName name of the header to set with security info
     * @return configured addGn4SecurityHeader_
     */
    @Shortcut
    public static HandlerFilterFunction<ServerResponse, ServerResponse> addGn4SecurityHeader(String gn4AuthHeaderName) {
        return ofRequestProcessor(addGn4SecurityHeader_(gn4AuthHeaderName));
    }
}
