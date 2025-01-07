/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */

package org.geonetwork.domain.repository;

import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.stream.Stream;
import org.geonetwork.domain.Metadata;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;

public interface MetadataRepository extends JpaRepository<Metadata, Integer> {
    List<Metadata> findAllByUuidIn(List<String> uuid);

    @QueryHints(@QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "50"))
    Stream<Metadata> streamAllBy();

    @QueryHints(@QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "50"))
    Stream<Metadata> streamAllBy(Sort sortBy);

    @QueryHints(@QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "50"))
    Stream<Metadata> streamAllByUuidIn(List<String> uuid);

    @QueryHints(@QueryHint(name = AvailableHints.HINT_FETCH_SIZE, value = "50"))
    Stream<Metadata> streamAllByUuidIn(List<String> uuid, Sort sortBy);

    Metadata findOneByUuid(String uuid);
}
