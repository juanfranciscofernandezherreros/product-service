package com.example.studyplanservice.infrastructure.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link GoalEntity}. */
public interface GoalJpaRepository extends JpaRepository<GoalEntity, UUID> {
}
