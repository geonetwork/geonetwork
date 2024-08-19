/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.config;

import static org.geonetwork.security.DatabaseUserDetailsService.GN_AUTHORITY;
import static org.geonetwork.security.DatabaseUserDetailsService.HIGHEST_PROFILE;
import static org.geonetwork.security.DatabaseUserDetailsService.USER_ID;
import static org.geonetwork.security.DatabaseUserDetailsService.USER_NAME;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.proxy.HttpProxyPolicyAgentAuthorizationManager;
import org.geonetwork.repository.UserRepository;
import org.geonetwork.repository.UsergroupRepository;
import org.geonetwork.security.DatabaseUserAuthProperties;
import org.geonetwork.security.DatabaseUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      HttpProxyPolicyAgentAuthorizationManager proxyPolicyAgentAuthorizationManager,
      UserRepository userRepository,
      UsergroupRepository userGroupRepository)
      throws Exception {
    http.authorizeHttpRequests(
            requests ->
                requests
                    .requestMatchers("/", "/home", "/signin")
                    .permitAll()
                    .requestMatchers("/geonetwork/**")
                    .permitAll()
                    .requestMatchers("/api/proxy")
                    .access(proxyPolicyAgentAuthorizationManager)
                    .anyRequest()
                    .permitAll())
        .oauth2Login(
            oauth ->
                oauth
                    .permitAll()
                    .userInfoEndpoint(
                        userInfo ->
                            userInfo.userAuthoritiesMapper(
                                this.userOauthAuthoritiesMapper(
                                    userRepository, userGroupRepository))))
        .formLogin(
            form ->
                form.loginPage("/signin")
                    .loginProcessingUrl("/api/user/signin")
                    .defaultSuccessUrl("/home", true)
                    .permitAll())
        .httpBasic(
            basic ->
                // No popup in browsers
                basic.authenticationEntryPoint(
                    (request, response, authException) ->
                        response.sendError(
                            HttpStatus.UNAUTHORIZED.value(),
                            HttpStatus.UNAUTHORIZED.getReasonPhrase())))
        .logout(
            logout ->
                logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/api/user/signout"))
                    .logoutSuccessUrl("/"));

    return http.build();
  }

  @Bean
  @SuppressWarnings("deprecation")
  public PasswordEncoder passwordEncoder(
      @Value("${geonetwork.security.passwordSalt}") String salt) {
    return new StandardPasswordEncoder(salt);
  }

  @Bean
  public UserDetailsService userDetailsService(
      @Value("${geonetwork.security.checkUsernameOrEmail: 'USERNAME_OR_EMAIL'}")
          DatabaseUserAuthProperties checkUsernameOrEmail,
      PasswordEncoder passwordEncoder,
      UserRepository userRepository,
      UsergroupRepository userGroupRepository) {
    return new DatabaseUserDetailsService(
        checkUsernameOrEmail, passwordEncoder, userRepository, userGroupRepository);
  }

  private GrantedAuthoritiesMapper userOauthAuthoritiesMapper(
      UserRepository userRepository, UsergroupRepository userGroupRepository) {
    return (authorities) -> {
      Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

      authorities.forEach(
          authority -> {
            if (authority instanceof OidcUserAuthority) {
              // Handle OIDC user authority
            } else if (authority instanceof OAuth2UserAuthority oauth2UserAuthority) {
              Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();
              String email = userAttributes.get("email").toString();
              Optional<User> oauthUser = userRepository.findOptionalByEmail(email);

              User user =
                  oauthUser.orElseGet(
                      () -> {
                        String username =
                            Optional.ofNullable(userAttributes.get("login"))
                                .orElse(email)
                                .toString();
                        User newUser =
                            User.builder()
                                .isenabled("y")
                                .password("")
                                .username(username)
                                .name(
                                    Optional.ofNullable(userAttributes.get("name"))
                                        .orElse("")
                                        .toString())
                                .surname("")
                                .authtype(authority.getAuthority())
                                .email(Set.of(email))
                                .organisation(
                                    Optional.ofNullable(userAttributes.get("company"))
                                        .orElse("")
                                        .toString())
                                .build();
                        userRepository.save(newUser);
                        return newUser;
                      });

              user.setUsername(
                  Optional.ofNullable(userAttributes.get("name")).orElse(email).toString());
              userRepository.save(user);

              String mainUserProfile = user.getProfile().name();
              List<Usergroup> userGroups = userGroupRepository.findAllByUserid_Id(user.getId());

              Map<String, List<Integer>> attributesToCast =
                  userGroups.stream()
                      .collect(
                          Collectors.groupingBy(
                              ug -> Profile.values()[ug.getId().getProfile()].name(),
                              Collectors.mapping(
                                  ug -> ug.getGroupid().getId(), Collectors.toList())));

              Map<String, Object> attributes = new HashMap<>(attributesToCast);
              attributes.put(USER_ID, user.getId());
              attributes.put(USER_NAME, user.getUsername());
              attributes.put(HIGHEST_PROFILE, mainUserProfile);
              attributes.putIfAbsent(Profile.UserAdmin.name(), Collections.emptyList());
              attributes.putIfAbsent(Profile.Reviewer.name(), Collections.emptyList());
              attributes.putIfAbsent(Profile.RegisteredUser.name(), Collections.emptyList());
              attributes.putIfAbsent(Profile.Editor.name(), Collections.emptyList());

              mappedAuthorities.add(new OAuth2UserAuthority(GN_AUTHORITY, attributes));
            }
          });

      return mappedAuthorities;
    };
  }
}
