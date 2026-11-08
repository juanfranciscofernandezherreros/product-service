package com.example.coursesservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repository for {@link UnitEntity}.
 */
public interface UnitJpaRepository extends JpaRepository<UnitEntity, Long>,
        JpaSpecificationExecutor<UnitEntity> {

    boolean existsBySlug(String slug);
}
