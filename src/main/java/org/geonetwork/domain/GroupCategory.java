package org.geonetwork.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "group_category")
public class GroupCategory {
    @SequenceGenerator(name = "group_category_id_gen", sequenceName = "address_id_seq", allocationSize = 1)
    @EmbeddedId
    private GroupCategoryId id;

}
