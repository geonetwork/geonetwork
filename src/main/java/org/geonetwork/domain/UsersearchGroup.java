package org.geonetwork.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usersearch_group")
public class UsersearchGroup {
    @SequenceGenerator(name = "usersearch_group_id_gen", sequenceName = "user_search_id_seq", allocationSize = 1)
    @EmbeddedId
    private UsersearchGroupId id;

    @MapsId("usersearchId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usersearch_id", nullable = false)
    private Usersearch usersearch;

    @MapsId("groupId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

}
