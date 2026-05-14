/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.index.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.geonetwork.domain.Group;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.User;
import org.geonetwork.domain.Usergroup;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsergroupRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

public class ElasticWithUserPermissionsTest {

    @Test
    public void testAnonymous() {
        ElasticWithUserPermissions elasticWithUserPermissions = new ElasticWithUserPermissions();
        elasticWithUserPermissions.userRepository = createUserRepository(null);
        elasticWithUserPermissions.usergroupRepository = createUserGroupRepository(null);

        setUserAnonymous();
        var query = elasticWithUserPermissions.createPermissionQuery(createTrivialQuery());
        var boolquery = (BoolQuery) query._get();

        // user query - see createTrivialQuery()
        var userQuery = boolquery.must().get(0);
        assertEquals(2, boolquery.must().size());
        assertEquals("Query: {\"query_string\":{\"query\":\"*:*\"}}", userQuery.toString());

        var permissionQuery = (MatchQuery) boolquery.must().get(1)._get();
        // "1" is the "all" group, "op0"=read
        assertEquals("MatchQuery: {\"op0\":{\"query\":\"1\"}}", permissionQuery.toString());
    }

    @Test
    public void testUser() {
        var user = new User();
        user.setId(200);
        user.setUsername("testcase name");

        var usergroup1 = new Usergroup();
        var group500 = new Group();
        group500.setId(500);
        usergroup1.setGroupid(group500);
        usergroup1.setUserid(user);

        var usergroup2 = new Usergroup();
        var group501 = new Group();
        group501.setId(501);
        usergroup2.setGroupid(group501);
        usergroup2.setUserid(user);

        var userGroups = Arrays.asList(usergroup1, usergroup2);

        ElasticWithUserPermissions elasticWithUserPermissions = new ElasticWithUserPermissions();
        elasticWithUserPermissions.userRepository = createUserRepository(user);
        elasticWithUserPermissions.usergroupRepository = createUserGroupRepository(userGroups);

        setUser(user);
        var query = elasticWithUserPermissions.createPermissionQuery(createTrivialQuery());
        var boolquery = (BoolQuery) query._get();

        // user query - see createTrivialQuery()
        var userQuery = boolquery.must().get(0);
        assertEquals(2, boolquery.must().size());
        assertEquals("Query: {\"query_string\":{\"query\":\"*:*\"}}", userQuery.toString());

        var permissionQuery = (BoolQuery) boolquery.must().get(1)._get();
        // "1" is the "all" group, "op0"=read
        assertEquals(
                "BoolQuery: {\"minimum_should_match\":\"1\",\"should\":[{\"match\":{\"op0\":{\"query\":\"1\"}}},{\"match\":{\"op0\":{\"query\":\"500\"}}},{\"match\":{\"op0\":{\"query\":\"501\"}}}]}",
                permissionQuery.toString());
    }

    @Test
    public void testAdmin() {
        var user = new User();
        user.setId(200);
        user.setUsername("admin name");
        user.setProfile(Profile.Administrator);

        ElasticWithUserPermissions elasticWithUserPermissions = new ElasticWithUserPermissions();
        elasticWithUserPermissions.userRepository = createUserRepository(user);
        elasticWithUserPermissions.usergroupRepository = createUserGroupRepository(null);

        setUser(user);
        var query = elasticWithUserPermissions.createPermissionQuery(createTrivialQuery());
        var boolquery = (BoolQuery) query._get();

        // user query - see createTrivialQuery()
        var userQuery = boolquery.must().get(0);
        assertEquals(1, boolquery.must().size());
        assertEquals("Query: {\"query_string\":{\"query\":\"*:*\"}}", userQuery.toString());

        // no permission query
    }

    public Query createTrivialQuery() {
        return Query.of(x -> x.queryString(y -> y.query("*:*")));
    }

    public UsergroupRepository createUserGroupRepository(List<Usergroup> groups) {
        var usergroupRepo = mock(UsergroupRepository.class);
        if (groups != null) {
            var userid = groups.get(0).getUserid().getId();
            when(usergroupRepo.findAllByUserid_Id(userid)).thenReturn(groups);
        }

        return usergroupRepo;
    }

    public UserRepository createUserRepository(User user) {
        var userRepo = mock(UserRepository.class);
        if (user != null) {
            when(userRepo.findOptionalByUsername(user.getUsername())).thenReturn(Optional.of(user));
        }
        return userRepo;
    }

    public void setUserAnonymous() {
        var auth = new AnonymousAuthenticationToken(
                "anon", "anonymousUser", List.of(new SimpleGrantedAuthority("ROLE_ANONYMOUS")));
        var securityContext = new SecurityContextImpl(auth);
        SecurityContextHolder.setContext(securityContext);
    }

    public void setUser(User user) {
        var authUser = new org.springframework.security.core.userdetails.User(
                user.getUsername(), "password", Arrays.asList(new SimpleGrantedAuthority("ROLE_RegisteredUser")));
        var auth = new UsernamePasswordAuthenticationToken(
                authUser, null, List.of(new SimpleGrantedAuthority("ROLE_RegisteredUser")));
        var securityContext = new SecurityContextImpl(auth);
        SecurityContextHolder.setContext(securityContext);
    }
}
