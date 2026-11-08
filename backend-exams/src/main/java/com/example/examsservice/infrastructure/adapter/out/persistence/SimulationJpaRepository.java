package com.example.examsservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link SimulationEntity}. */
public interface SimulationJpaRepository extends JpaRepository<SimulationEntity, Long> {
}
