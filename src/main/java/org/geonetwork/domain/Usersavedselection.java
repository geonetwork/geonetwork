package org.geonetwork.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usersavedselections")
public class Usersavedselection {
    @SequenceGenerator(name = "usersavedselections_id_gen", sequenceName = "operation_id_seq", allocationSize = 1)
    @EmbeddedId
    private UsersavedselectionId id;

    @MapsId("selectionid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "selectionid", nullable = false)
    private Selection selectionid;

    @MapsId("userid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid", nullable = false)
    private User userid;

}
