package com.example.examsservice.infrastructure.adapter.out.persistence;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link QuestionEntity}. */
public interface QuestionJpaRepository extends JpaRepository<QuestionEntity, Long> {

    /**
     * @param examenId exam id
     * @return the questions ordered by order
     */
    List<QuestionEntity> findByExamenIdOrderByOrden(Long examenId);

    /**
     * @param examenId exam id
     * @param orden    order
     * @return {@code true} if present
     */
    boolean existsByExamenIdAndOrden(Long examenId, int orden);
}
