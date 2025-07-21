/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.cql;

import java.util.Deque;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.filter.expression.AbstractExpressionVisitor;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.WKTWriter;

public class Expression2CswVisitor extends AbstractExpressionVisitor {
    public static final WKTWriter WKT_WRITER = new WKTWriter();
    private final IFieldMapper fieldMapper;
    private final Deque<String> stack;

    public Expression2CswVisitor(Deque<String> stack, IFieldMapper fieldMapper) {
        this.stack = stack;
        this.fieldMapper = fieldMapper;
    }

    private static String quoteString(String text) {
        return text.replace("\"", "\\\"");
    }

    @Override
    public Object visit(PropertyName expr, Object extraData) {
        stack.push(fieldMapper.map(expr.getPropertyName()));
        return expr;
    }

    @Override
    public Object visit(Literal expr, Object extraData) {
        if (expr.getValue() instanceof Geometry) {
            Geometry geometry = (Geometry) expr.getValue();
            geometry2coordinates(geometry);
        } else {
            stack.push(quoteString(expr.getValue().toString()));
        }
        return expr;
    }

    private void geometry2coordinates(Geometry geometry) {
        try {
            final CoordinateReferenceSystem sourceCRS;
            if (geometry.getUserData() != null && geometry.getUserData() instanceof CoordinateReferenceSystem) {
                sourceCRS = (CoordinateReferenceSystem) geometry.getUserData();
            } else {
                sourceCRS = CRS.decode("EPSG:" + geometry.getSRID());
            }
            final CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");

            final MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
            geometry = JTS.transform(geometry, transform);
        } catch (FactoryException | TransformException e) {
            e.printStackTrace();
        }

        stack.push(geometry.toText());
    }
}
