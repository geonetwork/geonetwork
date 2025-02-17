/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.config;

import java.util.Arrays;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.proxy.HttpProxyPolicyAgentAuthorizationManager;
import org.geonetwork.security.DatabaseUserAuthProperties;
import org.geonetwork.security.DatabaseUserDetailsService;
import org.geonetwork.security.GeoNetworkUserService;
import org.geonetwork.security.OauthAuthoritiesMapperService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
// @DependsOn("isGeoNetwork4RoutePredicateFactory")
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("*"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            HttpProxyPolicyAgentAuthorizationManager proxyPolicyAgentAuthorizationManager,
            OauthAuthoritiesMapperService oauthAuthoritiesMapperService)
            throws Exception {
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests.requestMatchers("/", "/home", "/signin")
                        .permitAll()
                        .requestMatchers("/geonetwork/**")
                        .permitAll()
                        .requestMatchers("/api/proxy")
                        .access(proxyPolicyAgentAuthorizationManager)
                        .anyRequest()
                        .permitAll())
                .oauth2Login(oauth -> oauth.permitAll()
                        .userInfoEndpoint(userInfo -> userInfo.userAuthoritiesMapper(
                                oauthAuthoritiesMapperService.userOauthAuthoritiesMapper())))
                .formLogin(form -> form.loginPage("/signin")
                        .loginProcessingUrl("/api/user/signin")
                        .defaultSuccessUrl("/home", true)
                        .permitAll())
                .httpBasic(AbstractHttpConfigurer::disable)
//                .httpBasic(basic ->
//                        // No popup in browsers
//                        basic.authenticationEntryPoint((request, response, authException) -> response.sendError(
//                                HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase())))
                .logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/api/user/signout"))
                        .logoutSuccessUrl("/"))
                .csrf(AbstractHttpConfigurer::disable);

        //    http.sessionManagement(
        //        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public PasswordEncoder passwordEncoder(@Value("${geonetwork.security.passwordSalt}") String salt) {
        return new StandardPasswordEncoder(salt);
    }

    @ConditionalOnProperty(name = "geonetwork.security.provider", havingValue = "database")
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

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        final RoleHierarchyImpl roleHierarchy = RoleHierarchyImpl.fromHierarchy(
                "ROLE_Administrator > ROLE_UserAdmin\n ROLE_UserAdmin > ROLE_Reviewer\n ROLE_Reviewer > ROLE_Editor\n ROLE_Editor > ROLE_RegisteredUser\n ROLE_RegisteredUser > ROLE_Guest");
        return roleHierarchy;
    }

    @Bean
    public DefaultWebSecurityExpressionHandler expressionHandler() {
        DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
        expressionHandler.setRoleHierarchy(roleHierarchy());
        return expressionHandler;
    }
}
