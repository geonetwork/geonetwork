/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.security;

import java.util.Collections;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Me API. */
@RestController
@RequestMapping("/api/me")
public class MeApi {

  /** Get user details. */
  @GetMapping()
  public Map<String, Object> user(
      @AuthenticationPrincipal AuthenticationPrincipal principal,
      @AuthenticationPrincipal UserDetails userDetails) {
    String userName = "";
    if (principal instanceof OAuth2User) {
      userName = ((OAuth2User) principal).getAttribute("name");
    } else if (userDetails != null) {
      userName = userDetails.getUsername();
    }
    return Collections.singletonMap("name", userName);
  }
}
