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
     * this is just for human-debugging friendly info.
     *
     * @return
     */
    String getName();

    /**
     * this is just for human-debugging friendly info.
     *
     * @return
     */
    String getDescription();

    /**
     * MIME Type that this Formatter handles.
     *
     * @return
     */
    MediaType getMimeType();

    /**
     * Quick name for the Mime type. i.e. for `?f=json` for `application/json`
     *
     * @return
     */
    String getMimeTypeQuickName();

    /**
     * What type of controller result does this handle?
     *
     * @return
     */
    Class<T> getInputType();

    /**
     * What profiles does this support.
     *
     * @return
     */
    List<String> getProvidedProfileNames();
}
