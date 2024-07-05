package org.geonetwork.repository;

import org.geonetwork.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Integer> {}
