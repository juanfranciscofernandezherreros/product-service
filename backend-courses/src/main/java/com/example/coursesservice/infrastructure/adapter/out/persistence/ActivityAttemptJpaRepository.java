package com.example.coursesservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data repository for {@link ActivityAttemptEntity}.
 */
public interface ActivityAttemptJpaRepository extends JpaRepository<ActivityAttemptEntity, UUID> {
}
