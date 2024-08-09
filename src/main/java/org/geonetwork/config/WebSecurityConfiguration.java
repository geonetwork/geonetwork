/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.config;

import org.geonetwork.proxy.HttpProxyPolicyAgentAuthorizationManager;
import org.geonetwork.repository.UserRepository;
import org.geonetwork.repository.UsergroupRepository;
import org.geonetwork.security.DatabaseUserAuthProperties;
import org.geonetwork.security.DatabaseUserDetailsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      HttpProxyPolicyAgentAuthorizationManager proxyPolicyAgentAuthorizationManager)
      throws Exception {
    http.authorizeHttpRequests(
            (requests) ->
                requests
                    .requestMatchers("/", "/home")
                    .permitAll()
                    .requestMatchers("/geonetwork/**")
                    .permitAll()
                    .requestMatchers("/api/proxy")
                    .access(proxyPolicyAgentAuthorizationManager)
                    .anyRequest()
                    .authenticated())
        .formLogin((form) -> form.loginPage("/login").defaultSuccessUrl("/home", true).permitAll())
        .httpBasic(
            (basic) ->
                // No popup in browsers
                basic.authenticationEntryPoint(
                    (request, response, authException) ->
                        response.sendError(
                            HttpStatus.UNAUTHORIZED.value(),
                            HttpStatus.UNAUTHORIZED.getReasonPhrase())))
        .logout(LogoutConfigurer::permitAll);

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
}
