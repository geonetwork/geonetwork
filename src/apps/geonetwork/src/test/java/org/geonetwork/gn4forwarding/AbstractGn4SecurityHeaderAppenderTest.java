/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.gn4forwarding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.Cookie;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.servlet.function.ServerRequest;

public class AbstractGn4SecurityHeaderAppenderTest {

    /** very simple test to make sure its getting the user from the security context. */
    @Test
    public void testGetUser() {
        var abstractGn4SecurityHeaderAppender = new AbstractGn4SecurityHeaderAppender() {
            @Override
            protected String encodeToken(Gn4SecurityToken token, Map config) {
                return "";
            }
        };
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        var user = new org.springframework.security.core.userdetails.User("testcase_dave", "password", authorities);
        var auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
        var securityContext = new SecurityContextImpl(auth);

        var user2 = abstractGn4SecurityHeaderAppender.getUserName(securityContext);

        assertSame(user.getUsername(), user2);
    }

    /**
     * This tests most of the system. 1. constructs a request with a User 2. verifies that the Gn4SecurityToken has the
     * correct username (from the mocked request) 3. verifies that the token is set on the correct header in the new
     * request
     *
     * @throws Exception error - test fail
     */
    @Test
    public void testToken() throws Exception {
        final Gn4SecurityToken[] constructedToken = new Gn4SecurityToken[1];
        var abstractGn4SecurityHeaderAppender = new AbstractGn4SecurityHeaderAppender() {
            @Override
            protected String encodeToken(Gn4SecurityToken token, Map config) {
                constructedToken[0] = token;
                return "TESTCASETOKEN";
            }
        };

        var request = mockServerRequest();
        var config = new HashMap<String, String>();
        config.put("headerName", "testcaseheadername");

        var newrequest = abstractGn4SecurityHeaderAppender.execute_impl(request, config);

        assertNotNull(newrequest);
        assertNotNull(constructedToken);
        assertEquals("TESTCASETOKEN", newrequest.headers().firstHeader("testcaseheadername"));
        assertEquals("testcase_dave", constructedToken[0].getUsername());
    }

    /**
     * This tests to make sure the security header is removed for incoming requests.
     *
     * @throws Exception error - test fail
     */
    @Test
    public void testMaliciousHeader() throws Exception {
        final Gn4SecurityToken[] constructedToken = new Gn4SecurityToken[1];
        var abstractGn4SecurityHeaderAppender = new AbstractGn4SecurityHeaderAppender() {
            @Override
            protected String encodeToken(Gn4SecurityToken token, Map config) {
                constructedToken[0] = token;
                return "TESTCASETOKEN";
            }
        };

        var request = mockServerRequestMalicious();
        var config = new HashMap<String, String>();
        config.put("headerName", "testcaseheadername");

        assertFalse(request.headers().header("testcaseheadername").isEmpty());

        var newrequest = abstractGn4SecurityHeaderAppender.execute_impl(request, config);

        assertNotNull(newrequest);
        assertNull(constructedToken[0]);
        assertTrue(newrequest.headers().header("testcaseheadername").isEmpty());
    }

    /**
     * creates a mock request with a User (username = "testcase_dave").
     *
     * @return mocked request GN5 user "testcase_dave"
     * @throws Exception error - test fail
     */
    public ServerRequest mockServerRequest() throws Exception {
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        var user = new org.springframework.security.core.userdetails.User("testcase_dave", "password", authorities);
        var auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
        var securityContext = new SecurityContextImpl(auth);

        var mockSession = new MockHttpSession();
        mockSession.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        SecurityContextHolder.setContext(securityContext);

        var request = mock(ServerRequest.class);
        when(request.session()).thenReturn(mockSession);
        when(request.uri()).thenReturn(new URI("http://localhost"));

        var mockHeaders = mock(org.springframework.web.servlet.function.ServerRequest.Headers.class);
        when(mockHeaders.asHttpHeaders()).thenReturn(new HttpHeaders());

        when(request.headers()).thenReturn(mockHeaders);

        when(request.cookies()).thenReturn(new LinkedMultiValueMap<String, Cookie>());
        when(request.attributes()).thenReturn(new HashMap<String, Object>());
        when(request.params()).thenReturn(new LinkedMultiValueMap<String, String>());

        return request;
    }

    /**
     * creates a mock request without a user, but with the security header already set. This is a bit complex because we
     * need to add the header (not easy) as well as keep the session.
     *
     * @return mocked request with illegal header from user
     * @throws Exception error - test fail
     */
    public ServerRequest mockServerRequestMalicious() throws Exception {

        var securityContext = new SecurityContextImpl(null);

        var mockSession = new MockHttpSession();
        mockSession.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
        SecurityContextHolder.setContext(securityContext);

        var request = mock(ServerRequest.class);
        when(request.session()).thenReturn(mockSession);
        when(request.uri()).thenReturn(new URI("http://localhost"));

        var mockHeaders = mock(org.springframework.web.servlet.function.ServerRequest.Headers.class);
        when(mockHeaders.asHttpHeaders()).thenReturn(new HttpHeaders());

        when(request.headers()).thenReturn(mockHeaders);

        when(request.cookies()).thenReturn(new LinkedMultiValueMap<String, Cookie>());
        when(request.attributes()).thenReturn(new HashMap<String, Object>());
        when(request.params()).thenReturn(new LinkedMultiValueMap<String, String>());

        var changedRequest = ServerRequest.from(request);
        changedRequest.header("testcaseheadername", "BAD BAD");

        // need to have the session in the modified request
        var changedRequest2 = changedRequest.build();
        var spychangedRequest2 = spy(changedRequest2);
        // when(spychangedRequest2.session()).thenReturn(mockSession);
        doReturn(mockSession).when(spychangedRequest2).session();
        return spychangedRequest2;
    }
}
