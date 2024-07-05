package org.geonetwork.repository;

import org.geonetwork.domain.Usersavedselection;
import org.geonetwork.domain.UsersavedselectionId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersavedselectionRepository extends JpaRepository<Usersavedselection, UsersavedselectionId> {
}
