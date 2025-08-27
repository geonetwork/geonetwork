/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.facets;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetHistogramDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsFacetResultBucketDto;
import org.geonetwork.ogcapi.service.configuration.OgcFacetConfig;
import org.geonetwork.ogcapi.service.configuration.SimpleType;
import org.geonetwork.ogcapi.service.indexConvert.dynamic.ExtraElasticPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This takes a list of buckets (with min/max) and re-works them so that the min/max form groups with no holes.
 *
 * <p>For example,
 *
 * <p>{min=0,max=10}, {min=20, max=55}
 *
 * <p>The 1st bucket will be converted to {min=0, max=20} to close the gap.
 *
 * <p>This is typically only needed for fixed-number-of-buckets since their min/max aren't quite right (with respect to
 * the fixed-interval buckets)
 */
@Component
public class HistogramBucketReJiggler {

    @Autowired
    ExtraElasticPropertiesService extraElasticPropertiesService;

    public List<OgcApiRecordsFacetResultBucketDto> reJiggle(
            List<OgcApiRecordsFacetResultBucketDto> buckets, OgcFacetConfig histogramInfo) {

        var dataType = OgcApiRecordsFacetHistogramDto.XElasticDatatypeEnum.NUMBER;
        var ogcProperty = histogramInfo.getField().getOgcProperty();
        var type = this.extraElasticPropertiesService
                .getElasticTypingSystem()
                .getFinalElasticTypes()
                .get(ogcProperty)
                .getType();
        if (type == SimpleType.DATE) {
            dataType = OgcApiRecordsFacetHistogramDto.XElasticDatatypeEnum.DATE;
        }

        buckets = sortByValue(buckets, dataType);

        for (int i = 0; i < buckets.size() - 1; i++) {
            var bucket = buckets.get(i);
            var nextBucket = buckets.get(i + 1);
            bucket.setMax(nextBucket.getMin());
        }

        if (!buckets.isEmpty()) {
            buckets.getLast().setxHighestBucket(true);
        }

        return buckets;
    }

    protected List<OgcApiRecordsFacetResultBucketDto> sortByValue(
            List<OgcApiRecordsFacetResultBucketDto> buckets, OgcApiRecordsFacetHistogramDto.XElasticDatatypeEnum type) {
        if (type.equals(OgcApiRecordsFacetHistogramDto.XElasticDatatypeEnum.NUMBER)) {
            return sortByValue_number(buckets);
        } else if (type.equals(OgcApiRecordsFacetHistogramDto.XElasticDatatypeEnum.DATE)) {
            return sortByValue_date(buckets);
        } else {
            throw new RuntimeException("Unsupported XElasticDatatypeEnum: " + type);
        }
    }

    protected List<OgcApiRecordsFacetResultBucketDto> sortByValue_number(
            List<OgcApiRecordsFacetResultBucketDto> buckets) {
        buckets.sort(Comparator.comparingDouble(x -> Double.parseDouble(x.getMin())));
        return buckets;
    }

    public List<OgcApiRecordsFacetResultBucketDto> sortByValue_date(List<OgcApiRecordsFacetResultBucketDto> buckets) {
        buckets.sort(Comparator.comparing(x -> Instant.parse(x.getMin())));
        return buckets;
    }
}
