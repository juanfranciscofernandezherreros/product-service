package com.example.coursesservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for {@link ActivityQuestionEntity}.
 */
public interface ActivityQuestionJpaRepository extends JpaRepository<ActivityQuestionEntity, Long> {

    boolean existsByActividadIdAndOrden(Long actividadId, int orden);

    List<ActivityQuestionEntity> findByActividadIdOrderByOrdenAsc(Long actividadId);
}
