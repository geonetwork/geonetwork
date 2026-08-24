/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
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
@Table(name = "cswservercapabilitiesinfo")
public class Cswservercapabilitiesinfo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cswservercapabilitiesinfo_id_gen")
    @SequenceGenerator(
            name = "cswservercapabilitiesinfo_id_gen",
            sequenceName = "csw_server_capabilities_info_id_seq",
            allocationSize = 1)
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
