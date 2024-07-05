package org.geonetwork.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "useraddress")
public class Useraddress {
    @SequenceGenerator(name = "useraddress_id_gen", sequenceName = "user_search_id_seq", allocationSize = 1)
    @EmbeddedId
    private UseraddressId id;

    @MapsId("userid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid", nullable = false)
    private User userid;

    @MapsId("addressid")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "addressid", nullable = false)
    private Address addressid;

}
