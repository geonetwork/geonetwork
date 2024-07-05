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
@Table(name = "metadatastatus")
public class Metadatastatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metadatastatus_id_gen")
    @SequenceGenerator(name = "metadatastatus_id_gen", sequenceName = "metadatastatus_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "changedate", nullable = false, length = 30)
    private String changedate;

    @NotNull
    @Column(name = "metadataid", nullable = false)
    private Integer metadataid;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "statusid", nullable = false)
    private Statusvalue statusid;

    @NotNull
    @Column(name = "userid", nullable = false)
    private Integer userid;

    @Size(max = 2048)
    @NotNull
    @Column(name = "changemessage", nullable = false, length = 2048)
    private String changemessage;

    @Size(max = 30)
    @Column(name = "closedate", length = 30)
    private String closedate;

    @Column(name = "currentstate", length = Integer.MAX_VALUE)
    private String currentstate;

    @Size(max = 30)
    @Column(name = "duedate", length = 30)
    private String duedate;

    @Column(name = "owner")
    private Integer owner;

    @Column(name = "previousstate", length = Integer.MAX_VALUE)
    private String previousstate;

    @Column(name = "titles", length = Integer.MAX_VALUE)
    private String titles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relatedmetadatastatusid")
    private Metadatastatus relatedmetadatastatusid;

    @Size(max = 255)
    @NotNull
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @OneToMany(mappedBy = "relatedmetadatastatusid")
    private Set<Metadatastatus> metadatastatuses = new LinkedHashSet<>();

}
