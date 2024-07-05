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
@Table(name = "languages")
public class Language {
    @Id
    @Size(max = 5)
    @SequenceGenerator(name = "languages_id_gen", sequenceName = "harvester_setting_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false, length = 5)
    private String id;

    @Column(name = "isdefault", length = Integer.MAX_VALUE)
    private String isdefault;

    @Column(name = "isinspire", length = Integer.MAX_VALUE)
    private String isinspire;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

}
