package org.geonetwork.repository;

import org.geonetwork.domain.Source;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceRepository extends JpaRepository<Source, String> {
}
