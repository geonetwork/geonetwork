/*
 * SPDX-FileCopyrightText: 2001 FAO-UN and others <geonetwork@osgeo.org>
 * SPDX-License-Identifier: GPL-2.0-or-later
 */
package org.geonetwork.ogcapi.service.cql;

import java.util.List;
import org.geotools.api.filter.*;
import org.geotools.api.filter.expression.*;
import org.geotools.api.filter.spatial.*;
import org.geotools.api.filter.temporal.*;

public class IsSimpleFilterVisitor implements FilterVisitor, ExpressionVisitor {

    public IsSimpleFilterVisitor() {}

    @Override
    public Object visitNullFilter(Object o) {
        throw new UnsupportedOperationException("CQL null filter is not accepted");
    }

    @Override
    public Object visit(ExcludeFilter excludeFilter, Object o) {
        throw new UnsupportedOperationException("CQL exclude filter is not accepted");
    }

    @Override
    public Object visit(IncludeFilter includeFilter, Object o) {
        throw new UnsupportedOperationException("CQL include filter is not accepted");
    }

    @Override
    public Object visit(And filter, Object data) {
        List<Filter> childList = filter.getChildren();
        if (childList != null) {
            for (Filter child : childList) {
                if (child == null) continue;
                data = child.accept(this, data);
            }
        }
        return data;
    }

    @Override
    public Object visit(Id id, Object o) {
        throw new UnsupportedOperationException("CQL ID filter is not accepted");
    }

    @Override
    public Object visit(Not filter, Object data) {
        Filter child = filter.getFilter();
        if (child != null) {
            data = child.accept(this, data);
        }
        return data;
    }

    @Override
    public Object visit(Or filter, Object data) {
        List<Filter> childList = filter.getChildren();
        if (childList != null) {
            for (Filter child : childList) {
                if (child == null) continue;
                data = child.accept(this, data);
            }
        }
        return data;
    }

    @Override
    public Object visit(PropertyIsBetween filter, Object data) {
        data = filter.getLowerBoundary().accept(this, data);
        data = filter.getExpression().accept(this, data);
        data = filter.getUpperBoundary().accept(this, data);
        return data;
    }

    @Override
    public Object visit(PropertyIsEqualTo filter, Object data) {
        data = filter.getExpression1().accept(this, data);
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    @Override
    public Object visit(PropertyIsNotEqualTo filter, Object data) {
        data = filter.getExpression1().accept(this, data);
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    @Override
    public Object visit(PropertyIsGreaterThan filter, Object data) {
        data = filter.getExpression1().accept(this, data);
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    @Override
    public Object visit(PropertyIsGreaterThanOrEqualTo filter, Object data) {
        data = filter.getExpression1().accept(this, data);
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    @Override
    public Object visit(PropertyIsLessThan filter, Object data) {
        data = filter.getExpression1().accept(this, data);
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    @Override
    public Object visit(PropertyIsLessThanOrEqualTo filter, Object data) {
        data = filter.getExpression1().accept(this, data);
        data = filter.getExpression2().accept(this, data);

        return data;
    }

    @Override
    public Object visit(PropertyIsLike filter, Object data) {
        data = filter.getExpression().accept(this, data);

        return data;
    }

    @Override
    public Object visit(PropertyIsNull propertyIsNull, Object o) {
        throw new UnsupportedOperationException("CQL Property is Null filter is not accepted");
    }

    @Override
    public Object visit(PropertyIsNil propertyIsNil, Object o) {
        throw new UnsupportedOperationException("CQL property is nil filter is not accepted");
    }

    @Override
    public Object visit(BBOX bbox, Object o) {
        throw new UnsupportedOperationException(
                "CQL bbbox filter is not accepted (use the normal ogcapi-records query parameter)");
    }

    @Override
    public Object visit(Beyond beyond, Object o) {
        throw new UnsupportedOperationException("CQL beyond filter is not accepted");
    }

    @Override
    public Object visit(Contains contains, Object o) {
        throw new UnsupportedOperationException("CQL contains filter is not accepted");
    }

    @Override
    public Object visit(Crosses crosses, Object o) {
        throw new UnsupportedOperationException("CQL crosses filter is not accepted");
    }

    @Override
    public Object visit(Disjoint disjoint, Object o) {
        throw new UnsupportedOperationException("CQL disjoint filter is not accepted");
    }

    @Override
    public Object visit(DWithin dWithin, Object o) {
        throw new UnsupportedOperationException("CQL distance within filter is not accepted");
    }

    @Override
    public Object visit(Equals equals, Object o) {
        throw new UnsupportedOperationException("CQL geometry equals filter is not accepted");
    }

    @Override
    public Object visit(Intersects intersects, Object o) {
        throw new UnsupportedOperationException("CQL intersects filter is not accepted");
    }

    @Override
    public Object visit(Overlaps overlaps, Object o) {
        throw new UnsupportedOperationException("CQL overlaps filter is not accepted");
    }

    @Override
    public Object visit(Touches touches, Object o) {
        throw new UnsupportedOperationException("CQL touches filter is not accepted");
    }

    @Override
    public Object visit(Within within, Object o) {
        throw new UnsupportedOperationException("CQL within filter is not accepted");
    }

    // todo: verify
    @Override
    public Object visit(After after, Object data) {
        throw new UnsupportedOperationException("CQL after filter is not accepted");
    }

    @Override
    public Object visit(AnyInteracts anyInteracts, Object o) {
        throw new UnsupportedOperationException("CQL any interacts filter is not accepted");
    }

    // todo: verify
    @Override
    public Object visit(Before before, Object data) {
        throw new UnsupportedOperationException("CQL before filter is not accepted");
    }

    @Override
    public Object visit(Begins begins, Object o) {
        throw new UnsupportedOperationException("CQL begins filter is not accepted");
    }

    @Override
    public Object visit(BegunBy begunBy, Object o) {
        throw new UnsupportedOperationException("CQL begun by filter is not accepted");
    }

    // todo: verify
    @Override
    public Object visit(During during, Object data) {
        throw new UnsupportedOperationException("CQL during filter is not accepted");
    }

    @Override
    public Object visit(EndedBy endedBy, Object o) {
        throw new UnsupportedOperationException("CQL ended by filter is not accepted");
    }

    @Override
    public Object visit(Ends ends, Object o) {
        throw new UnsupportedOperationException("CQL ends  filter is not accepted");
    }

    @Override
    public Object visit(Meets meets, Object o) {
        throw new UnsupportedOperationException("CQL meets filter is not accepted");
    }

    @Override
    public Object visit(MetBy metBy, Object o) {
        throw new UnsupportedOperationException("CQL met by filter is not accepted");
    }

    @Override
    public Object visit(OverlappedBy overlappedBy, Object o) {
        throw new UnsupportedOperationException("CQL overlapped by filter is not accepted");
    }

    @Override
    public Object visit(TContains tContains, Object o) {
        throw new UnsupportedOperationException("CQL contains filter is not accepted");
    }

    @Override
    public Object visit(TEquals equals, Object data) {
        data = equals.getExpression1().accept(this, data);
        data = equals.getExpression2().accept(this, data);
        return data;
    }

    // verify
    @Override
    public Object visit(TOverlaps contains, Object data) {
        throw new UnsupportedOperationException("CQL toverlaps filter is not accepted");
    }

    @Override
    public Object visit(NilExpression expression, Object extraData) {
        throw new UnsupportedOperationException("CQL nil expression is not accepted");
    }

    @Override
    public Object visit(Add expression, Object extraData) {
        throw new UnsupportedOperationException("CQL add expression is not accepted");
    }

    @Override
    public Object visit(Divide expression, Object extraData) {
        throw new UnsupportedOperationException("CQL divide expression is not accepted");
    }

    @Override
    public Object visit(Function expression, Object extraData) {
        throw new UnsupportedOperationException("CQL function expression is not accepted");
    }

    @Override
    public Object visit(Literal expression, Object data) {
        return data;
    }

    @Override
    public Object visit(Multiply expression, Object extraData) {
        throw new UnsupportedOperationException("CQL multiply expression is not accepted");
    }

    @Override
    public Object visit(PropertyName expression, Object extraData) {
        return null;
    }

    @Override
    public Object visit(Subtract expression, Object extraData) {
        throw new UnsupportedOperationException("CQL subtract expression is not accepted");
    }
}
