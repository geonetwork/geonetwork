package org.geonetwork.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "operationallowed")
public class Operationallowed {
    @EmbeddedId
    private OperationallowedId id;
}
