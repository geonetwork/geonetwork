/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.dataaccess;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.util.ArrayList;
import java.util.List;
import org.geonetwork.domain.Profile;
import org.geonetwork.domain.ReservedGroup;
import org.geonetwork.domain.ReservedOperation;
import org.geonetwork.domain.repository.UserRepository;
import org.geonetwork.domain.repository.UsergroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

/**
 * Given a query, create a new query that limits the results to items the user has permission for.
 *
 * <p>1. Get groups user is a member of 2. If op0 (read) has the `all` group or ANY of the groups the user is a member
 * of, they can see the record.
 *
 * <p>Original-Query AND (op0 CONTAINS `all` OR op0 CONTAINS `usergroup1` OR op0 CONTAINS `usergroup2` ...)
 *
 * <p>(where user is a member of usergroup1 and usergroup2)
 *
 * <p>NOTE: op1 contains the group id (integer)
 */
@Component
public class ElasticWithUserPermissions {

    @Autowired
    UsergroupRepository usergroupRepository;

    @Autowired
    UserRepository userRepository;

    /**
     * Creates a (original-query AND permission-Query) query.
     *
     * @param originalQuery original-query
     * @return (original - query AND permission - Query) query
     */
    public Query createPermissionQuery(Query originalQuery) {
        List<Query> queries = new ArrayList<Query>();
        queries.add(originalQuery); // original query
        queries.add(createPermissionQuery()); // permission query
        var queries2 = queries.stream().filter(x -> x != null).toList();
        return BoolQuery.of(bool -> bool.must(queries2))._toQuery();
    }

    /**
     * returns a (accessible-by-all-query OR (usergroup1) OR ... )
     *
     * @return (accessible - by - all - query OR ( usergroup1) OR ... )
     */
    public Query createPermissionQuery() {
        var user = getUser();

        if (user == null) {
            return createAllQuery();
        }
        if (user.getProfile() == Profile.Administrator) {
            return null;
        }

        var queries = new ArrayList<Query>();
        queries.add(createAllQuery());
        queries.addAll(createUserGroupsQueries(user));

        return BoolQuery.of(bool -> bool.should(queries).minimumShouldMatch("1"))
                ._toQuery();
    }

    /**
     * for each of the groups the user is in, create a query to see if the record is accessible by that group.
     *
     * @return set of queries to see if that group is allowed to see that record
     */
    protected List<Query> createUserGroupsQueries(org.geonetwork.domain.User user) {
        if (user == null) {
            return new ArrayList<>();
        }

        var groups = usergroupRepository.findAllByUserid_Id(user.getId());
        var groupQueries = groups.stream()
                .map(group -> createViewPermissionQuery(group.getGroupid().getId()))
                .toList();

        return groupQueries;
    }

    public org.geonetwork.domain.User getUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        String userName = "nobody";
        var principal = authentication.getPrincipal();
        if (principal instanceof User) {
            userName = ((User) principal).getUsername();
        }

        var userOptional = userRepository.findOptionalByUsername(userName);
        return userOptional.orElse(null);
    }

    /**
     * creates a query to see if the record is accessible to all user groups
     *
     * @return query - record is accessible to all user groups
     */
    protected Query createAllQuery() {
        return createViewPermissionQuery(ReservedGroup.all.getId());
    }

    protected Query createViewPermissionQuery(int groupNumber) {
        return MatchQuery.of(m -> {
                    m.field("op" + String.valueOf(ReservedOperation.view.getId()));
                    m.query(String.valueOf(groupNumber));
                    return m;
                })
                ._toQuery();
    }
}
