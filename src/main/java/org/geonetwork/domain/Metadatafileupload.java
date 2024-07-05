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
@Table(name = "metadatafileuploads")
public class Metadatafileupload {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metadatafileuploads_id_gen")
    @SequenceGenerator(name = "metadatafileuploads_id_gen", sequenceName = "metadata_fileupload_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "deleteddate")
    private String deleteddate;

    @Size(max = 255)
    @NotNull
    @Column(name = "filename", nullable = false)
    private String filename;

    @NotNull
    @Column(name = "filesize", nullable = false)
    private Double filesize;

    @NotNull
    @Column(name = "metadataid", nullable = false)
    private Integer metadataid;

    @Size(max = 255)
    @NotNull
    @Column(name = "uploaddate", nullable = false)
    private String uploaddate;

    @Size(max = 255)
    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

}
