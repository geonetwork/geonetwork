/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.cql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.geotools.filter.text.cql2.CQLException;
import org.junit.jupiter.api.Test;

/**
 * We don't control every client of the OGC API - Records `filter` parameter (some are third-party, open-source
 * consumers we can't fix), and some of them percent-encode the CQL text twice before sending it. The servlet container
 * only undoes one layer of encoding, so a `LIKE '%foo%'` pattern sent that way arrives as the literal text
 * `LIKE%20%27%25foo%25%27`, which fails CQL parsing.
 *
 * <p>{@link CqlToElasticSearch#parseCql(String)} tolerates this by parsing as-is first and only attempting one extra
 * decode pass if that fails.
 */
public class CqlToElasticSearchTest {

    CqlToElasticSearch cqlToElasticSearch = new CqlToElasticSearch();

    @Test
    public void singleEncodedLikeFilterParsesDirectly() throws CQLException {
        var filter = cqlToElasticSearch.parseCql("name LIKE '%foo%'");
        assertEquals("[ name is like %foo% ]", filter.toString());
    }

    @Test
    public void doubleEncodedLikeFilterIsRecoveredOnRetry() throws CQLException {
        // what a client sends when it percent-encodes "name LIKE '%foo%'" twice, and the servlet
        // container only decodes it once
        var doubleEncoded = "name%20LIKE%20%27%25foo%25%27";
        var filter = cqlToElasticSearch.parseCql(doubleEncoded);
        assertEquals("[ name is like %foo% ]", filter.toString());
    }

    @Test
    public void genuinelyInvalidCqlStillFails() {
        assertThrows(CQLException.class, () -> cqlToElasticSearch.parseCql("this is not cql (("));
    }

    @Test
    public void plainEqualityFilterUnaffected() throws CQLException {
        var filter = cqlToElasticSearch.parseCql("name = 'foo'");
        assertEquals("[ name = foo ]", filter.toString());
    }
}
