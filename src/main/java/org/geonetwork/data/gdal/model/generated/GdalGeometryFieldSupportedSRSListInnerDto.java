/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.data.gdal.model.generated;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import java.util.*;

@JacksonXmlRootElement(localName = "GdalGeometryFieldSupportedSRSListInnerDto")
@XmlRootElement(name = "GdalGeometryFieldSupportedSRSListInnerDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
    value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-09-13T15:07:19.066965162+02:00[Europe/Paris]",
    comments = "Generator version: 7.7.0")
// FIXME abstract types either need to be mapped to concrete types,
//  have custom deserializer,
@JsonDeserialize(as = GdalGeometryFieldSupportedSRSListInnerOneOfDto.class)
public interface GdalGeometryFieldSupportedSRSListInnerDto {}
