package org.geonetwork.search.ogcapi.records;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import jakarta.annotation.Generated;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.geonetwork.index.IndexClient;
import org.geonetwork.index.model.record.IndexRecord;
import org.geonetwork.search.ogcapi.records.generated.CollectionsApi;
import org.geonetwork.search.ogcapi.records.generated.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.geonetwork.index.model.record.IndexRecordFieldNames.CommonField.DEFAULT_TEXT;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]", comments = "Generator version: 7.6.0")
@Controller
@RequestMapping("${openapi.oGCAPIRecordsPart1Core.base-path:/data}")
public class CollectionsApiController implements CollectionsApi {

    private final IndexClient client;

    @Autowired
    public CollectionsApiController(IndexClient client) {
        this.client = client;
    }

    @Override
    public ResponseEntity<GetCollections200ResponseDto> getCollections() {
        GetCollections200ResponseDto collections = GetCollections200ResponseDto.builder()
            .collections(List.of(CollectionDto.builder()
                .id("srv")
                .title("Demo").build()
            )).build();
        return ResponseEntity.ok(collections);
    }

    @SneakyThrows
    @Override
    public ResponseEntity<GetRecords200ResponseDto> getRecords(String catalogId,
                                                               GetRecordsBboxDto bbox,
                                                               String datetime,
                                                               Integer limit,
                                                               List<String> q,
                                                               List<String> type,
                                                               List<String> externalId,
                                                               List<String> ids,
                                                               List<String> sortby) {

        SearchResponse<IndexRecord> searchResponse = client.getEsClient().search(s -> {
            s.index("gn-records");
            if (limit != null) {
                s.size(limit);
            }
            if (q != null && StringUtils.isNoneEmpty(q.getFirst())) {
                s.q(q.getFirst());
            }
            return s;
        }, IndexRecord.class);

return ResponseEntity.ok(
    GetRecords200ResponseDto.builder()
        .numberMatched(Math.toIntExact(searchResponse.hits().total().value()))
        .numberReturned(searchResponse.hits().hits().size())
        .features(searchResponse.hits().hits().stream().map(h -> {
            var title = Optional.ofNullable(h.source().getResourceTitle()).map(t -> t.get(DEFAULT_TEXT)).orElse("");
            var description = Optional.ofNullable(h.source().getResourceAbstract()).map(a -> a.get(DEFAULT_TEXT)).orElse("");
            var keywords = Optional.ofNullable(h.source().getTag()).orElseGet(ArrayList::new).stream()
                .flatMap(Stream::ofNullable).map(tag -> tag.get(DEFAULT_TEXT)).collect(Collectors.toList());

            return RecordGeoJSONDto.builder()
                .id(h.id())
                .properties(RecordGeoJSONPropertiesDto.builder().title(title).description(description).keywords(keywords).build())
                .build();
        }).collect(Collectors.toList())).build());
    }
}
