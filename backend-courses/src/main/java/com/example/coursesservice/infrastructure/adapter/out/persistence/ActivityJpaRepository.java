package com.example.coursesservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Spring Data repository for {@link ActivityEntity}.
 */
public interface ActivityJpaRepository extends JpaRepository<ActivityEntity, Long>,
        JpaSpecificationExecutor<ActivityEntity> {

    boolean existsBySlug(String slug);
}
