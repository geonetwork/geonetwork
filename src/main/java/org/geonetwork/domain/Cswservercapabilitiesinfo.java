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
@Table(name = "cswservercapabilitiesinfo")
public class Cswservercapabilitiesinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cswservercapabilitiesinfo_id_gen")
    @SequenceGenerator(name = "cswservercapabilitiesinfo_id_gen", sequenceName = "csw_server_capabilities_info_id_seq", allocationSize = 1)
    @Column(name = "idfield", nullable = false)
    private Integer id;

    @Size(max = 32)
    @NotNull
    @Column(name = "field", nullable = false, length = 32)
    private String field;

    @Size(max = 5)
    @NotNull
    @Column(name = "langid", nullable = false, length = 5)
    private String langid;

    @Column(name = "label", length = Integer.MAX_VALUE)
    private String label;

}
