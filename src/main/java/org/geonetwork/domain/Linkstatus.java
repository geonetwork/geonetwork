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
@Table(name = "linkstatus")
public class Linkstatus {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "linkstatus_id_gen")
    @SequenceGenerator(name = "linkstatus_id_gen", sequenceName = "linkstatus_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "batchkey")
    private String batchkey;

    @Size(max = 30)
    @Column(name = "checkdate", length = 30)
    private String checkdate;

    @NotNull
    @Column(name = "failing", nullable = false, length = Integer.MAX_VALUE)
    private String failing;

    @Column(name = "statusinfo", length = Integer.MAX_VALUE)
    private String statusinfo;

    @Size(max = 255)
    @NotNull
    @Column(name = "statusvalue", nullable = false)
    private String statusvalue;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "link", nullable = false)
    private Link link;

}
