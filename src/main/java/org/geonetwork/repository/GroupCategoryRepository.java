package org.geonetwork.repository;

import org.geonetwork.domain.GroupCategory;
import org.geonetwork.domain.GroupCategoryId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupCategoryRepository extends JpaRepository<GroupCategory, GroupCategoryId> {}
