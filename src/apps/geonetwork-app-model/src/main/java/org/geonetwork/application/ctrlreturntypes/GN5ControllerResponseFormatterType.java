/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application.ctrlreturntypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Attach this to OpenAPI response objects that use "special" formatters (i.e. not json/xml).
 *
 * <p>This overcomes a spring openapi codegen problem - the controller is setup to return the
 * defined-in-the-openapi-yaml "JSON" (DTO) object.
 *
 * <p>However, for endpoints that support special formatters (i.e. GN `FormatterApi`), do NOT return the normal
 * defined-in-the-openapi-yaml "JSON" (DTO) object, instead they return a simple `IControllerResponse` object that is
 * used by the message converters to create the final result.
 *
 * <p>However, to do some reflection analysis, we need to know that the controller endpoint actually creates an
 * `IControllerResponse` (for formatting later in the content negotiation system).
 *
 * <p>This creates that link.
 *
 * <p>Put this in your model object (i.e. defined-in-the-openapi-yaml "JSON" (DTO) object)
 *
 * <p>x-class-extra-annotation:
 * "@org.geonetwork.application.ctrlreturntypes.GN5ControllerResponseFormatterType(fullTypeName=\"OgcApiRecordsSingleRecordResponse\")"
 *
 * <p>This indicates that there's a link from `OgcApiRecordsRecordGeoJSONDto` (defined in yaml) and
 * `OgcApiRecordsSingleRecordResponse` (the actual result that will be formatted later by spring and the message
 * converters).
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GN5ControllerResponseFormatterType {

    /** name of the correlated class */
    public String fullTypeName();
}
