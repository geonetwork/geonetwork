package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "metadatafiledownloads")
public class Metadatafiledownload {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "metadatafiledownloads_id_gen")
  @SequenceGenerator(
      name = "metadatafiledownloads_id_gen",
      sequenceName = "metadata_filedownload_id_seq",
      allocationSize = 1)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Size(max = 255)
  @NotNull
  @Column(name = "downloaddate", nullable = false)
  private String downloaddate;

  @Size(max = 255)
  @NotNull
  @Column(name = "filename", nullable = false)
  private String filename;

  @NotNull
  @Column(name = "fileuploadid", nullable = false)
  private Integer fileuploadid;

  @NotNull
  @Column(name = "metadataid", nullable = false)
  private Integer metadataid;

  @Size(max = 255)
  @Column(name = "requestercomments")
  private String requestercomments;

  @Size(max = 255)
  @Column(name = "requestermail")
  private String requestermail;

  @Size(max = 255)
  @Column(name = "requestername")
  private String requestername;

  @Size(max = 255)
  @Column(name = "requesterorg")
  private String requesterorg;

  @Size(max = 255)
  @Column(name = "username")
  private String username;
}
