package org.geonetwork.repository;

import org.geonetwork.domain.CategoriesDes;
import org.geonetwork.domain.CategoriesDesId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesDesRepository extends JpaRepository<CategoriesDes, CategoriesDesId> {
}
