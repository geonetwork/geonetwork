package org.geonetwork.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customelementset")
public class Customelementset {
    @Id
    @Column(name = "xpathhashcode", nullable = false)
    private Integer id;

    @Size(max = 1000)
    @NotNull
    @Column(name = "xpath", nullable = false, length = 1000)
    private String xpath;

}
