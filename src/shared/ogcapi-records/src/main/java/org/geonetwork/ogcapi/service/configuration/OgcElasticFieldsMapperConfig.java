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

    @Getter
    @Setter
    public static class OgcFacetConfig {

        public enum FacetType {
            TERM,
            HISTOGRAM_FIXED_BUCKET_COUNT,
            HISTOGRAM_FIXED_INTERVAL,
            FILTER;

            public static String ogcString(FacetType type) {
                if (type == FacetType.TERM) {
                    return "term";
                } else if (type == FacetType.HISTOGRAM_FIXED_BUCKET_COUNT) {
                    return "histogram";
                } else if (type == FacetType.HISTOGRAM_FIXED_INTERVAL) {
                    return "histogram";
                } else if (type == FacetType.FILTER) {
                    return "filter";
                } else {
                    throw new RuntimeException("Unknown FacetType: " + type);
                }
            }
        }

        public enum BucketSorting {
            COUNT,
            VALUE
        }

        public enum CalendarIntervalUnit {
            year,
            month,
            week,
            day,
            hour,
            minute,
            second,
            quarter
        }

        public String facetName;
        public FacetType facetType;
        public BucketSorting bucketSorting = BucketSorting.COUNT;

        /** needed for FacetType.HISTOGRAM_FIXED_BUCKET_COUNT. For others, this will delete lower-priority buckets */
        public Integer bucketCount;

        /** buckets with <minimumDocumentCount will be removed. Usually 0 (dont remove) or 1 (remove empty buckets) */
        public int minimumDocumentCount = 1;

        /** For HISTOGRAM_FIXED_INTERVAL with a Number datatype */
        public Double numberBucketInterval;

        /** For HISTOGRAM_FIXED_INTERVAL with a Date datatype */
        public CalendarIntervalUnit calendarIntervalUnit;

        /** only valid for FILTER facets */
        public List<FilterFacetInfo> filters;

        /** often null - you might have to look at the parent */
        public OgcElasticFieldMapperConfig field;
    }

    @Getter
    @Setter
    public static class FilterFacetInfo {
        public String filterName;
        public String filterEquation;
    }

    @Getter
    @Setter
    public static class OgcElasticFieldMapperConfig {
        public enum SimpleType {
            DOUBLE,
            INTEGER,
            STRING,
            DATE,
            BOOLEAN
        }

        public enum OverrideType {
            LIST_STRING,
            LIST_INTEGER,
            LIST_DOUBLE,
            LIST_DATE,
            LIST_BOOLEAN,
            SINGLE_STRING,
            SINGLE_INTEGER,
            SINGLE_DOUBLE,
            SINGLE_DATE,
            SINGLE_BOOLEAN;

            public static boolean isList(OverrideType type) {
                return (type == LIST_STRING
                        || type == LIST_INTEGER
                        || type == LIST_DOUBLE
                        || type == LIST_DATE
                        || type == LIST_BOOLEAN);
            }

            public static SimpleType getSimpleType(OverrideType type) {
                if (type == LIST_STRING || type == SINGLE_STRING) {
                    return SimpleType.STRING;
                } else if (type == LIST_INTEGER || type == SINGLE_INTEGER) {
                    return SimpleType.INTEGER;
                } else if (type == LIST_DOUBLE || type == SINGLE_DOUBLE) {
                    return SimpleType.DOUBLE;
                } else if (type == LIST_DATE || type == SINGLE_DATE) {
                    return SimpleType.DATE;
                } else if (type == LIST_BOOLEAN || type == SINGLE_BOOLEAN) {
                    return SimpleType.BOOLEAN;
                }
                throw new IllegalArgumentException("Unsupported type " + type);
            }

            public static Class getJavaType(OverrideType type) {
                if (type == LIST_STRING || type == SINGLE_STRING) {
                    return String.class;
                } else if (type == LIST_INTEGER || type == SINGLE_INTEGER) {
                    return Integer.class;
                } else if (type == LIST_DOUBLE || type == SINGLE_DOUBLE) {
                    return Double.class;
                } else if (type == LIST_DATE || type == SINGLE_DATE) {
                    return String.class; // JSON treats dates like strings
                } else if (type == LIST_BOOLEAN || type == SINGLE_BOOLEAN) {
                    return Boolean.class;
                }
                throw new IllegalArgumentException("Unsupported type " + type);
            }

            public static Class getJavaType(SimpleType type) {
                if (type == SimpleType.STRING) {
                    return String.class;
                } else if (type == SimpleType.INTEGER) {
                    return Integer.class;
                } else if (type == SimpleType.DOUBLE) {
                    return Double.class;
                } else if (type == SimpleType.DATE) {
                    return String.class; // JSON treats dates like strings
                } else if (type == SimpleType.BOOLEAN) {
                    return Boolean.class;
                }
                throw new IllegalArgumentException("Unsupported type " + type);
            }
        }

        public String ogcProperty;
        public String elasticProperty;
        public String indexRecordProperty;

        public OverrideType typeOverride;

        /** add to the results (ie. already a pre-defined property that you want to use in facets) */
        public Boolean addProperty = true;

        public List<OgcFacetConfig> facetsConfig = new ArrayList<>();
    }
}
