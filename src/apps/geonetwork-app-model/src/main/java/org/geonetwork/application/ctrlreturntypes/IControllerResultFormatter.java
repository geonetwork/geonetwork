/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.application.ctrlreturntypes;

import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;

/**
 * This is
 *
 * @param <T> This should be one of the small objects returned by the controller.
 */
public interface IControllerResultFormatter<T extends IControllerResponseObject> extends HttpMessageConverter<T> {

    /**
     * MIME Type that this Formatter handles.
     *
     * @return mime type that this formats to
     */
    MediaType getMimeType();

    /**
     * Quick name for the Mime type. i.e. for `?f=json` for `application/json`
     *
     * @return quick name of the mime type
     */
    String getMimeTypeQuickName();

    /**
     * What type of controller result does this handle?
     *
     * @return class that this formats
     */
    Class<T> getInputType();

    /**
     * What profiles does this support.
     *
     * @return profiles that this formatter supports
     */
    List<String> getProvidedProfileNames();
}
