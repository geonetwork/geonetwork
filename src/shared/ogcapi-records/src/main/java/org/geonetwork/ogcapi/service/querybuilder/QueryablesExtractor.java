/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.querybuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.ogcapi.service.queryables.QueryablesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Many clients do not support Queryables. This makes it easier to do queryables searches just from the `q` (full text).
 *
 * <p>Given a query, look at the `q` and if it has sections like 'id:abc', then; A) remove that from the `q` section B)
 * add it as a queryable
 */
@Component
public class QueryablesExtractor {

    @Autowired
    QueryablesService queryablesService;

    public void extractQueryables(OgcApiQuery query) {
        if (query == null || query.getQ() == null || query.getQ().isEmpty()) {
            return;
        }

        var updated = parseWithQueryables(queryablesService.queryableProperties(null), query.getQ());
        var qs = updated.getQs().stream().filter(x -> !StringUtils.isBlank(x)).toList();
        query.setQ(qs);
        if (updated.getQueryables() != null) {
            if (query.getPropValues() == null) {
                query.setPropValues(new HashMap<>());
            }
            updated.getQueryables().entrySet().stream().forEach(x -> query.addProp(x.getKey(), x.getValue()));
        }
    }

    /**
     * This looks for things like:
     *
     * <p>queryable:<word> and queryable:"<words>"
     *
     * <p>and put in the Query object as "queryables" (map representing the queryableName=>user search text)
     *
     * @param queryablesProperyNames - all the queryable properties
     * @param qs - texts from user
     */
    public QueryChanges parseWithQueryables(List<String> queryablesProperyNames, List<String> qs) {
        var map = new HashMap<String, String>();
        var updatedQs = new ArrayList<String>();
        for (var q : qs) {
            for (var queryableName : queryablesProperyNames) {
                var match = this.queryableInString(q, queryableName);
                if (match != null && !StringUtils.isBlank(match.getQueryableText())) {
                    map.put(match.queryableName, match.queryableText);
                    q = q.replace(match.fullText, "").trim();
                }
            }
            updatedQs.add(q.trim());
        }

        var result = QueryChanges.builder().qs(updatedQs).queryables(map).build();

        return result;
    }

    /**
     * Looks for a single queryable in the user string. If not present, return null.
     *
     * <p>Otherwise return:
     *
     * <p>QueryableTextAndValue { queryableName:string, // name of the queryable (from arguments) queryableText: string,
     * // what the user is searching for (right of ":", excluding quotes if used) fullText:string // full text of what
     * the user typed &lt;queryableName&gt;:&lt;search text, including quote&gt; }
     *
     * <p>example - id:abc queryableName id queryableText abc fullText id:abc
     *
     * <p>example = title:"dave was here" queryableName=title queryableText=dave was here" fullText title:"dave was
     * here"
     *
     * @param text text from user
     * @param queryableName name of the queryable we are looking for
     */
    public QueryableTextAndValue queryableInString(String text, String queryableName) {
        var indx = text.indexOf(queryableName + ":");
        if (indx != -1) {
            var regex = Pattern.compile(queryableName + ":" + "([^ \"]+)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = regex.matcher(text);
            boolean matchFound = matcher.find();
            if (matchFound) {
                return QueryableTextAndValue.builder()
                        .queryableName(queryableName)
                        .queryableText(matcher.group(1).trim())
                        .fullText(matcher.group(0))
                        .build();
            }
            var regex1 = Pattern.compile(queryableName + ":" + "\"([^\"]+)\"", Pattern.CASE_INSENSITIVE);
            matcher = regex1.matcher(text);
            matchFound = matcher.find();
            if (matchFound) {
                return QueryableTextAndValue.builder()
                        .queryableName(queryableName)
                        .queryableText(matcher.group(1).trim())
                        .fullText(matcher.group(0))
                        .build();
            }
        }
        return null;
    }

    /** parses an input query string so that it is broken in a full-text search + all the queryables. */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryChanges {
        List<String> qs;
        Map<String, String> queryables;

        public List<String> getQs() {
            return qs == null ? null : Collections.unmodifiableList(qs);
        }

        public Map<String, String> getQueryables() {
            return queryables == null ? null : Collections.unmodifiableMap(queryables);
        }
    }

    /**
     * Simple representation of the result of parsing a query string so the queryables are extracted.
     *
     * <p>example = title:"dave was here" queryableName=title queryableText="dave was here" fullText=title:"dave was
     * here"
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QueryableTextAndValue {
        String fullText;
        String queryableName;
        String queryableText;
    }
}
