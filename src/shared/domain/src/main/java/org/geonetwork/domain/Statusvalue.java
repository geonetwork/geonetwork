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
@Table(name = "statusvalues")
public class Statusvalue {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statusvalues_id_gen")
    @SequenceGenerator(name = "statusvalues_id_gen", sequenceName = "status_value_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "displayorder")
    private Integer displayorder;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "reserved", nullable = false, length = Integer.MAX_VALUE)
    private String reserved;

    @Size(max = 255)
    @Column(name = "notificationlevel")
    private String notificationlevel;

    @Size(max = 255)
    @Column(name = "type")
    private String type;

    /**
     * List of predefined status part of the workflow. Those values are the default one for GeoNetwork and may be
     * modified in the database.
     */
    public static final class Status {
        private Status() {}

        public static final String DRAFT = "1";
        public static final String APPROVED = "2";
        public static final String RETIRED = "3";
        public static final String SUBMITTED = "4";
    }

    /**
     * List of predefined status part of the events. Those values are the default one for GeoNetwork and may be modified
     * in the database.
     */
    public static final class Events {
        private Events() {}

        public static final String RECORDCREATED = "50";
        public static final String RECORDUPDATED = "51";
        public static final String ATTACHMENTADDED = "52";
        public static final String ATTACHMENTDELETED = "53";
        public static final String RECORDOWNERCHANGE = "54";
        public static final String RECORDGROUPOWNERCHANGE = "55";
        public static final String RECORDPRIVILEGESCHANGE = "56";
        public static final String RECORDCATEGORYCHANGE = "57";
        public static final String RECORDVALIDATIONTRIGGERED = "58";
        public static final String RECORDSTATUSCHANGE = "59";
        public static final String RECORDPROCESSINGCHANGE = "60";
        public static final String RECORDDELETED = "61";
        public static final String RECORDIMPORTED = "62";
        public static final String RECORDRESTORED = "63";
    }
}
