/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.config;

import static java.nio.charset.StandardCharsets.UTF_8;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import javax.crypto.spec.SecretKeySpec;
import org.geonetwork.proxy.HttpProxyPolicyAgentAuthorizationManager;
import org.geonetwork.repository.UserRepository;
import org.geonetwork.security.DatabaseUserAuthProperties;
import org.geonetwork.security.DatabaseUserDetailsService;
import org.geonetwork.security.GeoNetworkUserService;
import org.geonetwork.security.OauthAuthoritiesMapperService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
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
      OauthAuthoritiesMapperService oauthAuthoritiesMapperService)
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
                                oauthAuthoritiesMapperService.userOauthAuthoritiesMapper())))
        .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()))
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

    //    http.sessionManagement(
    //        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }

  @Bean
  @SuppressWarnings("deprecation")
  public PasswordEncoder passwordEncoder(
      @Value("${geonetwork.security.passwordSalt}") String salt) {
    return new StandardPasswordEncoder(salt);
  }

  @Bean
  public JwtDecoder jwtDecoder(@Value("${geonetwork.security.jwt.key}") String jwtKey) {
    SecretKeySpec secretKey =
        new SecretKeySpec(
            jwtKey.getBytes(UTF_8), 0, jwtKey.getBytes(UTF_8).length, "RSA");
    return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
  }

  @Bean
  public JwtEncoder jwtEncoder(@Value("${geonetwork.security.jwt.key}") String jwtKey) {
    return new NimbusJwtEncoder(new ImmutableSecret<>(jwtKey.getBytes(UTF_8)));
  }

  @Bean
  public UserDetailsService userDetailsService(
      @Value("${geonetwork.security.checkUsernameOrEmail: 'USERNAME_OR_EMAIL'}")
          DatabaseUserAuthProperties checkUsernameOrEmail,
      PasswordEncoder passwordEncoder,
      UserRepository userRepository,
      GeoNetworkUserService geoNetworkUserService) {
    return new DatabaseUserDetailsService(
        checkUsernameOrEmail, passwordEncoder, geoNetworkUserService, userRepository);
  }
}
