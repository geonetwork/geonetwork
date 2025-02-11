/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.config;

import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.security.GeoNetworkUserService;
import org.geonetwork.security.LdapAuthoritiesMapperService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.ldap.EmbeddedLdapServerContextSourceFactoryBean;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.ldap.DefaultSpringSecurityContextSource;
import org.springframework.security.ldap.server.UnboundIdContainer;

@Configuration
@ConditionalOnProperty(name = "geonetwork.security.provider", havingValue = "ldap")
public class WebSecurityLdapConfiguration {

    /** If no LDAP URL is provided, use the embedded LDAP server. */
    @Bean
    @ConditionalOnExpression("T(org.apache.commons.lang3.StringUtils).isEmpty('${geonetwork.security.ldap.url:}')")
    UnboundIdContainer ldapContainer(@Value("${geonetwork.security.ldap.base: ''}") String base) {
        UnboundIdContainer unboundIdContainer = new UnboundIdContainer(base, "classpath:users.ldif");
        return unboundIdContainer;
    }

    @Bean
    @ConditionalOnExpression("T(org.apache.commons.lang3.StringUtils).isEmpty('${geonetwork.security.ldap.url:}')")
    public EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean() {
        EmbeddedLdapServerContextSourceFactoryBean contextSourceFactoryBean =
                EmbeddedLdapServerContextSourceFactoryBean.fromEmbeddedLdapServer();
        contextSourceFactoryBean.setPort(0);
        return contextSourceFactoryBean;
    }

    /** Connection to LDAP server */
    @Bean
    @ConditionalOnExpression("T(org.apache.commons.lang3.StringUtils).isNotEmpty('${geonetwork.security.ldap.url:}')")
    ContextSource contextSource(
            @Value("${geonetwork.security.ldap.url: ''}") String url,
            @Value("${geonetwork.security.ldap.base: ''}") String base) {
        return new DefaultSpringSecurityContextSource(String.format("%s/%s", url, base));
    }

    @Bean
    AuthenticationManager authenticationManager(
            @Value("${geonetwork.security.ldap.user-search-dn: 'ou=people'}") String userSearchDn,
            @Value("${geonetwork.security.ldap.user-search-filter: 'uid={0}'}") String userSearchFilter,
            BaseLdapPathContextSource contextSource,
            LdapAuthoritiesMapperService ldapAuthoritiesMapperService,
            UserRepository userRepository,
            GeoNetworkUserService geoNetworkUserService) {
        LdapBindAuthenticationManagerFactory factory = new LdapBindAuthenticationManagerFactory(contextSource);
        factory.setUserSearchFilter(userSearchFilter);
        factory.setUserSearchBase(userSearchDn);
        //        factory.setUserDnPatterns(String.format("%s,%s", userSearchFilter, userSearchDn));
        factory.setUserDetailsContextMapper(ldapAuthoritiesMapperService.ldapUserContextMapper());
        return factory.createAuthenticationManager();
    }
}
