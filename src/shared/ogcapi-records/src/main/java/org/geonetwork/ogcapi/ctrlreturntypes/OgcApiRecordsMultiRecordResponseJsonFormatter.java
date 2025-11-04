package org.geonetwork.ogcapi.ctrlreturntypes;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import org.geonetwork.application.ctrlreturntypes.IControllerResultFormatter;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetRecords200ResponseDto;
import org.geonetwork.ogcapi.service.facets.FacetsResponseInjector;
import org.geonetwork.ogcapi.service.indexConvert.OgcApiGeoJsonConverter;
import org.geonetwork.ogcapi.service.links.FormatterApiRecordLinkAttacher;
import org.geonetwork.ogcapi.service.links.ItemPageLinks;
import org.geonetwork.ogcapi.service.links.ItemsPageLinks;
import org.geonetwork.utility.MediaTypeAndProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

@Component
public class OgcApiRecordsMultiRecordResponseJsonFormatter
        implements IControllerResultFormatter<OgcApiRecordsMultiRecordResponse> {

    @Autowired
    OgcApiGeoJsonConverter geoJsonConverter;

    @Autowired
    private ItemsPageLinks itemsPageLinks;

    @Autowired
    FormatterApiRecordLinkAttacher formatterApiRecordLinkAttacher;

    @Autowired
    private ItemPageLinks itemPageLinks;

    @Autowired
    FacetsResponseInjector facetsInjector;

    @Autowired
    ObjectMapper objectMapper;

    @Override
    public MediaType getMimeType() {
        return MediaType.parseMediaType("application/json");
    }

    @Override
    public String getMimeTypeQuickName() {
        return "json";
    }

    @Override
    public Class<OgcApiRecordsMultiRecordResponse> getInputType() {
        return OgcApiRecordsMultiRecordResponse.class;
    }

    @Override
    public List<String> getProvidedProfileNames() {
        //        return List.of("http://geonetwork.net/def/profile/ogc-items-json");
        return List.of(); // no profile
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        if (!clazz.equals(getInputType())) {
            return false;
        }
        return mediaType.equals(getMimeType());
    }

    @Override
    public List<MediaType> getSupportedMediaTypes() {
        return List.of(getMimeType());
    }

    @Override
    public OgcApiRecordsMultiRecordResponse read(
            Class<? extends OgcApiRecordsMultiRecordResponse> clazz, HttpInputMessage inputMessage)
            throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    public void write(
            OgcApiRecordsMultiRecordResponse ogcApiRecordsMultiRecordResponse,
            MediaType contentType,
            HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        var indexRecords = ogcApiRecordsMultiRecordResponse.getRecords();
        var ogcApiQuery = ogcApiRecordsMultiRecordResponse.getUserQuery();

        var features = indexRecords.stream()
                .map(ir -> ir.getIndexRecord())
                .map(x -> geoJsonConverter.convert(x, null))
                .toList();
        var response = new OgcApiRecordsGetRecords200ResponseDto();
        response.setType(OgcApiRecordsGetRecords200ResponseDto.TypeEnum.FEATURE_COLLECTION);
        response.setFeatures(features);
        response.numberMatched((int) ogcApiRecordsMultiRecordResponse.getTotalHits());
        response.numberReturned(features.size());
        response.setTimeStamp(OffsetDateTime.now(ZoneId.of("UTC")));
        response.setFacets(ogcApiRecordsMultiRecordResponse.getFacetInfo());

        var mediaAndProfile = new MediaTypeAndProfile(getMimeType(), getProvidedProfileNames());
        itemsPageLinks.addLinks(mediaAndProfile, ogcApiQuery.getCollectionId(), response, ogcApiQuery);
        features.stream().forEach(x -> itemPageLinks.addLinks(mediaAndProfile, ogcApiQuery.getCollectionId(), x));

        features.stream().forEach(x -> {
            try {
                formatterApiRecordLinkAttacher.attachLinks(x, ogcApiQuery.getCollectionId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        outputMessage.getHeaders().setContentType(getMimeType()); // this must be done first

        objectMapper.writeValue(outputMessage.getBody(), response);
    }
}
