package com.example.examsservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link DiagnosticEntity}. */
public interface DiagnosticJpaRepository extends JpaRepository<DiagnosticEntity, Long> {
}
