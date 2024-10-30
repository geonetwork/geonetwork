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
@Table(name = "messageproducerentity")
public class Messageproducerentity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messageproducerentity_id_gen")
    @SequenceGenerator(
            name = "messageproducerentity_id_gen",
            sequenceName = "message_producer_entity_id_seq",
            allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 255)
    @Column(name = "cronexpression")
    private String cronexpression;

    @Size(max = 255)
    @Column(name = "metadatauuid")
    private String metadatauuid;

    @Size(max = 255)
    @Column(name = "strategy")
    private String strategy;

    @Size(max = 255)
    @Column(name = "typename")
    private String typename;

    @Size(max = 255)
    @Column(name = "url")
    private String url;
}
