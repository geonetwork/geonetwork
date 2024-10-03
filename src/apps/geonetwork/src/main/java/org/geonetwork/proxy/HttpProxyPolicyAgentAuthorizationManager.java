/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.proxy;

import java.util.function.Supplier;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

@Component
public class HttpProxyPolicyAgentAuthorizationManager
    implements AuthorizationManager<RequestAuthorizationContext> {
  @Override
  public AuthorizationDecision check(Supplier authentication, RequestAuthorizationContext context) {
    // TODO: Implement authorization logic
    return new AuthorizationDecision(true);
  }
}
