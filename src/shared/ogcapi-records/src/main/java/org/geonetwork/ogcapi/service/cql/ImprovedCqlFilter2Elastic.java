/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.cql;

import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.geotools.api.filter.*;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.filter.visitor.AbstractFilterVisitor;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unchecked")
public class ImprovedCqlFilter2Elastic extends AbstractFilterVisitor {

    ElasticBetweenSimplifier simplifier = new ElasticBetweenSimplifier();

    /*from CswFilter2Es (gn4)*/
    //    private static final Pattern SPECIAL_RE = Pattern.compile("([" + Pattern.quote("+-&|!(){}[]^\\\"~*?:/") +
    // "])");
    //    private static final Pattern SPECIAL_LIKE_RE =
    //            Pattern.compile("(?<!\\\\)([" + Pattern.quote("+-*?&|!(){}[]^\"~:/") + "])");

    private static final Pattern multiWildCardOgc = Pattern.compile("(?<!\\\\)%");
    private static final Pattern singleWildCardOgc = Pattern.compile("(?<!\\\\)_");

    IFieldMapper fieldMapper;

    public enum BinaryLogicOperatorType {
        AND,
        OR
    }

    public enum RangeOperatorType {
        GT,
        LT,
        GTE,
        LTE
    }

    Deque stack = new ArrayDeque<>();
    private final Expression2CswVisitor expressionVisitor;

    public ImprovedCqlFilter2Elastic(IFieldMapper fieldMapper) {
        this.fieldMapper = fieldMapper;
        this.expressionVisitor = new Expression2CswVisitor(stack, fieldMapper);
    }

    public Query getQuery() {
        return stack.isEmpty() ? null : (Query) stack.pop();
    }

    public static Query translate(Filter filter, IFieldMapper fieldMapper) {
        var translator = new ImprovedCqlFilter2Elastic(fieldMapper);

        if (filter != null) {
            filter.accept(translator, translator);
        }

        return translator.getQuery();
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object extraData) {
        checkFilterExpressionsInBinaryComparisonOperator(filter);

        filter.getExpression1().accept(expressionVisitor, extraData);
        filter.getExpression2().accept(expressionVisitor, extraData);

        String dataPropertyValue = (String) stack.pop();
        String dataPropertyName = (String) stack.pop();

        //        final var _dataPropertyValue = replaceAll(SPECIAL_RE, dataPropertyValue, "\\\\$1");
        final var _dataPropertyValue = dataPropertyValue;

        //        var query = Query.of(q -> q.term(tq -> tq.field(dataPropertyName).value(_dataPropertyValue)));
        var query = Query.of(q -> q.term(tq -> tq.field(dataPropertyName).value(_dataPropertyValue)));

        stack.push(query);
        return this;
    }

    @Override
    public Object visit(PropertyIsLike filter, Object extraData) {
        var searchString = filter.getLiteral();

        //        searchString = replaceAll(SPECIAL_LIKE_RE, searchString, "\\\\$1");

        searchString = multiWildCardOgc.matcher(searchString).replaceAll("*");
        searchString = singleWildCardOgc.matcher(searchString).replaceAll("?");

        searchString = searchString.replace("\\%", "%");
        searchString = searchString.replace("\\_", "_");
        searchString = searchString.replace("-", "\\-");

        filter.getExpression().accept(expressionVisitor, extraData);

        final String _searchString = searchString;

        //        var query =
        //                Query.of(q -> q.wildcard(tq -> tq.field((String) stack.pop()).value(_searchString)));
        var query = Query.of(q -> q.queryString(
                qs -> qs.query(_searchString).fields((String) stack.pop()).defaultOperator(Operator.And)));
        stack.push(query);

        return this;
    }

    public Object visitRange(BinaryComparisonOperator filter, RangeOperatorType operator, Object extraData) {

        checkFilterExpressionsInBinaryComparisonOperator(filter);

        filter.getExpression1().accept(expressionVisitor, extraData);
        filter.getExpression2().accept(expressionVisitor, extraData);

        String dataPropertyValue = (String) stack.pop();
        String dataPropertyName = (String) stack.pop();

        //        boolean isDate = (DateUtil.parseBasicOrFullDateTime(dataPropertyValue) != null);

        Query query =
                switch (operator) {
                    case GT -> Query.of(
                            q -> q.range(r -> r.field(dataPropertyName).gt(JsonData.of(dataPropertyValue))));
                    case GTE -> Query.of(
                            q -> q.range(r -> r.field(dataPropertyName).gte(JsonData.of(dataPropertyValue))));
                    case LT -> Query.of(
                            q -> q.range(r -> r.field(dataPropertyName).lt(JsonData.of(dataPropertyValue))));
                    case LTE -> Query.of(
                            q -> q.range(r -> r.field(dataPropertyName).lte(JsonData.of(dataPropertyValue))));
                };

        stack.push(query);

        return this;
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object extraData) {

        if (!(filter.getExpression() instanceof PropertyName)) {
            throw new IllegalArgumentException("Invalid expression property provided");
        }

        if (!(filter.getLowerBoundary() instanceof Literal)) {
            throw new IllegalArgumentException("Invalid expression lower boundary literal provided");
        }

        if (!(filter.getUpperBoundary() instanceof Literal)) {
            throw new IllegalArgumentException("Invalid expression upper boundary literal provided");
        }

        filter.getExpression().accept(expressionVisitor, extraData);
        filter.getLowerBoundary().accept(expressionVisitor, extraData);
        filter.getUpperBoundary().accept(expressionVisitor, extraData);

        var dataPropertyUpperValue = (String) stack.pop();
        var dataPropertyLowerValue = (String) stack.pop();
        var dataPropertyName = (String) stack.pop();

        if (!NumberUtils.isCreatable(dataPropertyUpperValue)) {
            dataPropertyUpperValue = StringEscapeUtils.escapeJson(quoteString(dataPropertyUpperValue));
        }

        if (!NumberUtils.isCreatable(dataPropertyLowerValue)) {
            dataPropertyLowerValue = StringEscapeUtils.escapeJson(quoteString(dataPropertyLowerValue));
        }

        var _dataPropertyUpperValue = dataPropertyUpperValue;
        var _dataPropertyLowerValue = dataPropertyLowerValue;

        Query query = Query.of(q -> q.range(r -> r.field(dataPropertyName)
                .gte(JsonData.of(_dataPropertyLowerValue))
                .lte(JsonData.of(_dataPropertyUpperValue))));

        stack.push(query);

        return this;
    }

    protected static String quoteString(String text) {
        return String.format("\"%s\"", text);
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object extraData) {
        return visitRange(filter, RangeOperatorType.GT, extraData);
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object extraData) {
        return visitRange(filter, RangeOperatorType.GTE, extraData);
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object extraData) {
        return visitRange(filter, RangeOperatorType.LT, extraData);
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object extraData) {
        return visitRange(filter, RangeOperatorType.LTE, extraData);
    }

    @Override
    public Object visit(Or filter, Object extraData) {
        return visitBinaryLogic(filter, BinaryLogicOperatorType.OR, extraData);
    }

    @Override
    public Object visit(And filter, Object extraData) {
        return visitBinaryLogic(filter, BinaryLogicOperatorType.AND, extraData);
    }

    private Object visitBinaryLogic(BinaryLogicOperator filter, BinaryLogicOperatorType operator, Object extraData) {
        // Visit children adding the conditions to the stack as query
        for (Filter sub : filter.getChildren()) {
            sub.accept(this, extraData);
        }

        var qs = getNFromStack(filter.getChildren().size());

        Query query;
        if (operator == BinaryLogicOperatorType.AND) {
            var _qs = simplifier.simplify(qs);
            query = Query.of(q -> q.bool(b -> b.must(_qs)));
        } else {
            query = Query.of(q -> q.bool(b -> b.should(qs).minimumShouldMatch("1")));
        }

        stack.push(query);
        return this;
    }

    public String replaceAll(Pattern pattern, String src, String replacement) {
        return pattern.matcher(src).replaceAll(replacement);
    }

    public List<Query> getNFromStack(int n) {
        List<Query> result = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            result.add((Query) stack.pop());
        }
        return result;
    }

    private void checkFilterExpressionsInBinaryComparisonOperator(BinaryComparisonOperator filter) {
        if (!(filter.getExpression1() instanceof PropertyName)) {
            throw new IllegalArgumentException("Invalid expression property provided");
        }

        if (!(filter.getExpression2() instanceof Literal)) {
            throw new IllegalArgumentException("Invalid expression literal provided");
        }
    }
}
