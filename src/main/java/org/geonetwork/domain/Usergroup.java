package org.geonetwork.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usergroups")
public class Usergroup {
    @EmbeddedId
    private UsergroupId id;

    @MapsId("groupid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "groupid", nullable = false)
    private Group groupid;

    @MapsId("userid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid", nullable = false)
    private User userid;

}
