/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.cql;

import java.util.HashSet;
import org.geotools.api.data.Query;
import org.geotools.data.DataUtilities;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@SuppressWarnings("UnusedVariable")
public class CqlTest {

    /**
     * these are cql expression that will properly translate to Elastic.
     *
     * @throws Exception expression is invalid or major parsing issue
     */
    @Test
    public void testValid() throws Exception {
        String elastic;
        elastic = assertValid("str = 'dave'");
        elastic = assertValid("number BETWEEN 1 AND 2");
        elastic = assertValid("number >1");
        elastic = assertValid("number =1");
        elastic = assertValid("number >=1");
        elastic = assertValid("number <1");
        elastic = assertValid("number <=1");
        elastic = assertValid("number >=1 AND number <=2");
        elastic = assertValid("str = '1' OR str = '2'");
    }

    /**
     * these are cql expression that will not properly translate to Elastic. Might be fixable in the future, but most
     * unlikely.
     *
     * @throws Exception expression is valid or major parsing issue
     */
    @Test
    public void testInvalid() throws Exception {
        assertInvalid("geometry IS NULL");
        assertInvalid("geometry IS NOT NULL");
        // todo - add more
    }

    /**
     * parsing the cql should throw an exception
     *
     * @param cql cql expression to parse
     * @throws Exception unexpected exception
     */
    public void assertInvalid(String cql) throws Exception {
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            var es = getEs(cql);
        });
    }

    public String assertValid(String cql) throws Exception {
        var es = getEs(cql);
        return es;
    }

    public String getEs(String cql) throws CQLException {
        var fieldMapper = new TrivialFieldMapper();

        var filter = CQL.toFilter(cql);
        filter = DataUtilities.simplifyFilter(new Query("gn", filter)).getFilter();
        var validator = new IsSimpleFilterVisitor();
        filter.accept(validator, new HashSet<>());
        var result = CswFilter2Es.translate(filter, fieldMapper);
        return result;
    }

    public static class TrivialFieldMapper implements IFieldMapper {

        @Override
        public String map(String field) {
            return field.replaceAll("/", ".");
        }

        @Override
        public String mapSort(String field) {
            return "";
        }
    }
}
