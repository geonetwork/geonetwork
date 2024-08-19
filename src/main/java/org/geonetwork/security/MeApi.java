/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.security;

import static org.geonetwork.security.DatabaseUserDetailsService.USER_NAME;

import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Me API. */
@RestController
@RequestMapping("/api/me")
public class MeApi {

  /** Get user details. */
  @GetMapping()
  @PreAuthorize("permitAll")
  public ResponseEntity<Map<String, Object>> user(
      @AuthenticationPrincipal AuthenticationPrincipal principal,
      Authentication authentication,
      @AuthenticationPrincipal UserDetails userDetails) {
    String userName = "";
    if (authentication instanceof OAuth2AuthenticationToken) {
      userName =
          ((OAuth2UserAuthority) authentication.getAuthorities().toArray()[0])
              .getAttributes()
              .get(USER_NAME)
              .toString();
    } else if (userDetails != null) {
      userName = userDetails.getUsername();
    }
    if (StringUtils.isNotEmpty(userName)) {
      return ResponseEntity.ok(Collections.singletonMap("username", userName));
    } else {
      return ResponseEntity.noContent().build();
    }
  }
}
