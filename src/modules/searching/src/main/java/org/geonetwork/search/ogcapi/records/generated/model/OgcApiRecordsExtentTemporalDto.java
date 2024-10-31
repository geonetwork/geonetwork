/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** The temporal extent of the features in the collection. */
@Schema(name = "extent_temporal", description = "The temporal extent of the features in the collection.")
@JsonTypeName("extent_temporal")
@JacksonXmlRootElement(localName = "OgcApiRecordsExtentTemporalDto")
@XmlRootElement(name = "OgcApiRecordsExtentTemporalDto")
@XmlAccessorType(XmlAccessType.FIELD)
@Generated(
        value = "org.openapitools.codegen.languages.SpringCodegen",
        date = "2024-08-01T19:35:56.379122136+02:00[Europe/Paris]",
        comments = "Generator version: 7.7.0")
public class OgcApiRecordsExtentTemporalDto {

    @Valid
    private List<List<OffsetDateTime>> interval = new ArrayList<>();

    private TrsEnum trs = TrsEnum.HTTP_WWW_OPENGIS_NET_DEF_UOM_ISO_8601_0_GREGORIAN;

    /** Create a builder with no initialized field (except for the default values). */
    public static Builder builder() {
        return new Builder();
    }

    public OgcApiRecordsExtentTemporalDto interval(List<List<OffsetDateTime>> interval) {
        this.interval = interval;
        return this;
    }

    public OgcApiRecordsExtentTemporalDto addIntervalItem(List<OffsetDateTime> intervalItem) {
        if (this.interval == null) {
            this.interval = new ArrayList<>();
        }
        this.interval.add(intervalItem);
        return this;
    }

    /**
     * One or more time intervals that describe the temporal extent of the dataset. In the Core only a single time
     * interval is supported. Extensions may support multiple intervals. The first time interval describes the overall
     * temporal extent of the data. All subsequent time intervals describe more precise time intervals, e.g., to
     * identify clusters of data. Clients only interested in the overall temporal extent will only need to access the
     * first time interval in the array (a pair of lower and upper bound instants).
     *
     * @return interval
     */
    @Valid
    @Size(min = 1)
    @Schema(
            name = "interval",
            description = "One or more time intervals that describe the temporal extent of the dataset. In the Core"
                    + " only a single time interval is supported.  Extensions may support multiple"
                    + " intervals. The first time interval describes the overall temporal extent of the"
                    + " data. All subsequent time intervals describe more precise time intervals, e.g.,"
                    + " to identify clusters of data. Clients only interested in the overall temporal"
                    + " extent will only need to access the first time interval in the array (a pair of"
                    + " lower and upper bound instants).",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("interval")
    @JacksonXmlProperty(localName = "interval")
    @JacksonXmlElementWrapper(useWrapping = false)
    @XmlElement(name = "interval")
    public List<List<OffsetDateTime>> getInterval() {
        return interval;
    }

    public void setInterval(List<List<OffsetDateTime>> interval) {
        this.interval = interval;
    }

    public OgcApiRecordsExtentTemporalDto trs(TrsEnum trs) {
        this.trs = trs;
        return this;
    }

    /**
     * Coordinate reference system of the coordinates in the temporal extent (property `interval`). The default
     * reference system is the Gregorian calendar. In the Core this is the only supported temporal coordinate reference
     * system. Extensions may support additional temporal coordinate reference systems and add additional enum values.
     *
     * @return trs
     */
    @Schema(
            name = "trs",
            description = "Coordinate reference system of the coordinates in the temporal extent (property"
                    + " `interval`). The default reference system is the Gregorian calendar. In the Core"
                    + " this is the only supported temporal coordinate reference system. Extensions may"
                    + " support additional temporal coordinate reference systems and add additional enum"
                    + " values.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("trs")
    @JacksonXmlProperty(localName = "trs")
    @XmlElement(name = "trs")
    public TrsEnum getTrs() {
        return trs;
    }

    public void setTrs(TrsEnum trs) {
        this.trs = trs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OgcApiRecordsExtentTemporalDto extentTemporal = (OgcApiRecordsExtentTemporalDto) o;
        return Objects.equals(this.interval, extentTemporal.interval) && Objects.equals(this.trs, extentTemporal.trs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(interval, trs);
    }

    @Override
    public String toString() {
        String sb = "class OgcApiRecordsExtentTemporalDto {\n"
                + "    interval: "
                + toIndentedString(interval)
                + "\n"
                + "    trs: "
                + toIndentedString(trs)
                + "\n"
                + "}";
        return sb;
    }

    /** Convert the given object to string with each line indented by 4 spaces (except the first line). */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    /** Create a builder with a shallow copy of this instance. */
    public Builder toBuilder() {
        Builder builder = new Builder();
        return builder.copyOf(this);
    }

    /**
     * Coordinate reference system of the coordinates in the temporal extent (property `interval`). The default
     * reference system is the Gregorian calendar. In the Core this is the only supported temporal coordinate reference
     * system. Extensions may support additional temporal coordinate reference systems and add additional enum values.
     */
    public enum TrsEnum {
        HTTP_WWW_OPENGIS_NET_DEF_UOM_ISO_8601_0_GREGORIAN("http://www.opengis.net/def/uom/ISO-8601/0/Gregorian");

        private final String value;

        TrsEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static TrsEnum fromValue(String value) {
            for (TrsEnum b : TrsEnum.values()) {
                if (b.value.equals(value)) {
                    return b;
                }
            }
            throw new IllegalArgumentException("Unexpected value '" + value + "'");
        }

        @JsonValue
        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public static class Builder {

        private OgcApiRecordsExtentTemporalDto instance;

        public Builder() {
            this(new OgcApiRecordsExtentTemporalDto());
        }

        protected Builder(OgcApiRecordsExtentTemporalDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(OgcApiRecordsExtentTemporalDto value) {
            this.instance.setInterval(value.interval);
            this.instance.setTrs(value.trs);
            return this;
        }

        public Builder interval(List<List<OffsetDateTime>> interval) {
            this.instance.interval(interval);
            return this;
        }

        public Builder trs(TrsEnum trs) {
            this.instance.trs(trs);
            return this;
        }

        /**
         * returns a built OgcApiRecordsExtentTemporalDto instance.
         *
         * <p>The builder is not reusable (NullPointerException)
         */
        public OgcApiRecordsExtentTemporalDto build() {
            try {
                return this.instance;
            } finally {
                // ensure that this.instance is not reused
                this.instance = null;
            }
        }

        @Override
        public String toString() {
            return getClass() + "=(" + instance + ")";
        }
    }
}
