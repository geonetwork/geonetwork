package org.geonetwork.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "guf_userfeedback_keyword")
public class GufUserfeedbackKeyword {
    @SequenceGenerator(name = "guf_userfeedback_keyword_id_gen", sequenceName = "csw_server_capabilities_info_id_seq", allocationSize = 1)
    @EmbeddedId
    private GufUserfeedbackKeywordId id;

    @MapsId("keywordId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "keyword_id", nullable = false)
    private GufKeyword keyword;

}
