package org.geonetwork.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "links")
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "links_id_gen")
    @SequenceGenerator(name = "links_id_gen", sequenceName = "link_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "laststate")
    private Integer laststate;

    @Size(max = 255)
    @NotNull
    @Column(name = "linktype", nullable = false)
    private String linktype;

    @Size(max = 255)
    @Column(name = "protocol")
    private String protocol;

    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;

    @Size(max = 255)
    @Column(name = "dateandtime")
    private String dateandtime;

    @OneToMany(mappedBy = "link")
    private Set<Linkstatus> linkstatuses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "linkid")
    private Set<Metadatalink> metadatalinks = new LinkedHashSet<>();

}
