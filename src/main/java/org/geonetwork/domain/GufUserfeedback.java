package org.geonetwork.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "guf_userfeedbacks")
public class GufUserfeedback {
    @Id
    @Size(max = 255)
    @SequenceGenerator(name = "guf_userfeedbacks_id_gen", sequenceName = "csw_server_capabilities_info_id_seq", allocationSize = 1)
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Size(max = 255)
    @Column(name = "authoremail")
    private String authoremail;

    @Size(max = 255)
    @Column(name = "authorname")
    private String authorname;

    @Size(max = 255)
    @Column(name = "authororganization")
    private String authororganization;

    @Column(name = "authorprivacy")
    private Integer authorprivacy;

    @Size(max = 255)
    @Column(name = "commenttext")
    private String commenttext;

    @Column(name = "creationdate")
    private Instant creationdate;

    @Size(max = 255)
    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "metadata_uuid", referencedColumnName = "uuid")
    private org.geonetwork.domain.Metadata metadataUuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_uuid")
    private GufUserfeedback parentUuid;

}
