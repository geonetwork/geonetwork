package org.geonetwork.repository;

import org.geonetwork.domain.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, String> {}
