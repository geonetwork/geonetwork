package org.geonetwork.repository;

import org.geonetwork.domain.Thesaurus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThesaurusRepository extends JpaRepository<Thesaurus, String> {}
