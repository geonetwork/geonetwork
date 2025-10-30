/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.security.GeoNetworkOAuth2UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            GeoNetworkOAuth2UserService geoNetworkOAuth2UserService,
            @Value("${geonetwork.home: '/'}") String homeUrl)
            throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests.requestMatchers("/", "/home", "/signin", "/test")
                        .permitAll())
                .authorizeHttpRequests(authz -> authz.requestMatchers(
                                "/ogcapi-records/**", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
                        .permitAll()
                        .anyRequest()
                        .authenticated())

                //                        .requestMatchers("/geonetwork/**")
                //                        .permitAll()
                //                        .requestMatchers("/api/proxy")
                //                        .access(proxyPolicyAgentAuthorizationManager)
                //                        .anyRequest()
                //                        .permitAll())
                //                .oauth2Login(oauth -> oauth.permitAll().userInfoEndpoint(userInfo ->
                // userInfo.oidcUserService(
                //                                geoNetworkOAuth2UserService.oidcUserService())
                //                        .userService(geoNetworkOAuth2UserService.userService())))
                .formLogin(form -> form.loginPage("/signin")
                        .loginProcessingUrl("/api/user/signin")
                        .successHandler((request, response, authentication) -> {
                            handleRedirectParam(request, response, homeUrl);
                        })
                        .permitAll())
                .httpBasic(basic ->
                        // No popup in browsers
                        basic.authenticationEntryPoint((request, response, authException) -> response.sendError(
                                HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase())))
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/api/user/signout"))
                        .logoutSuccessHandler((request, response, authentication) -> {
                            handleRedirectParam(request, response, homeUrl);
                        }));
        return http.build();
    }

    private static void handleRedirectParam(HttpServletRequest request, HttpServletResponse response, String homeUrl)
            throws IOException {
        String redirectUrl = request.getParameter("redirectUrl");
        response.sendRedirect(StringUtils.isNotEmpty(redirectUrl) ? redirectUrl : request.getContextPath() + homeUrl);
    }
}
