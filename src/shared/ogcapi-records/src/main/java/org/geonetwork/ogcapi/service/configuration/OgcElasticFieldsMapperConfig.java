/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.configuration;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "geonetwork.openapi-records.ogcapi-property-mapping")
@Getter
@Setter
public class OgcElasticFieldsMapperConfig {

    public List<OgcElasticFieldMapperConfig> fields = new ArrayList<>();

    /** default number of buckets in results */
    public int defaultBucketCount = 10;

    public OgcElasticFieldMapperConfig findByOgc(String ogcFieldName) {
        for (OgcElasticFieldMapperConfig field : fields) {
            if (field.ogcProperty.equals(ogcFieldName)) {
                return field;
            }
        }
        return null;
    }
}
