package org.geonetwork.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "inspireatomfeed_entrylist")
public class InspireatomfeedEntrylist {
    @Id
    @NotNull
    @SequenceGenerator(name = "inspireatomfeed_entrylist_id_gen", sequenceName = "harvester_setting_id_seq", allocationSize = 1)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "inspireatomfeed_id", nullable = false)
    private Inspireatomfeed inspireatomfeed;

    @Size(max = 255)
    @Column(name = "crs")
    private String crs;

    @NotNull
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 3)
    @Column(name = "lang", length = 3)
    private String lang;

    @Size(max = 255)
    @Column(name = "title")
    private String title;

    @Size(max = 255)
    @Column(name = "type")
    private String type;

    @Size(max = 255)
    @Column(name = "url")
    private String url;

}
