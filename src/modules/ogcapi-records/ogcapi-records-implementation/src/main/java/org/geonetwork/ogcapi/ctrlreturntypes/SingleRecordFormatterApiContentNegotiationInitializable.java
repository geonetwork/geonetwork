/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.ctrlreturntypes;

import java.util.ArrayList;
import java.util.List;
import org.geonetwork.application.ctrlreturntypes.IControllerResultFormatter;
import org.geonetwork.application.formatters.IContentNegotiationInitializable;
import org.geonetwork.formatting.FormatterApi;
import org.geonetwork.ogcapi.service.configuration.ItemPageLinksConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

/**
 * creates a OgcApiRecordsSingleRecordResponseFormatter (a formatterApi profile-aware formatter) for each of the mime
 * type that the formatterApi supports.
 */
@Component
public class SingleRecordFormatterApiContentNegotiationInitializable implements IContentNegotiationInitializable {

    @Autowired
    private FormatterApi formatterApi;

    @Autowired
    ItemPageLinksConfiguration itemPageLinksConfiguration; // for default profile

    @Override
    public List<IControllerResultFormatter> initialize() throws Exception {
        var result = new ArrayList<IControllerResultFormatter>();
        var formatters = formatterApi.getAllFormatters();
        for (var mimeTypeFormatters : formatters.entrySet()) {
            var mimeType = MediaType.valueOf(mimeTypeFormatters.getKey());

            var formatter = new OgcApiRecordsSingleRecordResponseFormatter(
                    mimeType,
                    null,
                    this.itemPageLinksConfiguration.getDefaultProfile(mimeType),
                    mimeTypeFormatters.getValue(),
                    formatterApi);

            result.add(0, formatter);
        }
        return result;
    }
}
