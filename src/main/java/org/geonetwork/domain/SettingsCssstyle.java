package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "settings_cssstyle")
public class SettingsCssstyle {
    @Id
    @Size(max = 255)
    @Column(name = "variable", nullable = false)
    private String variable;

    @Size(max = 255)
    @Column(name="\"value\"")
    private String value;

}
