package com.example.examsservice.infrastructure.adapter.out.persistence;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Spring Data repository for {@link ExamEntity}. */
public interface ExamJpaRepository extends JpaRepository<ExamEntity, Long> {

    /**
     * @param codigo code
     * @return {@code true} if taken
     */
    boolean existsByCodigo(String codigo);

    /**
     * Finds exams filtered by optional level/published.
     *
     * @param nivel     optional level
     * @param publicado optional published
     * @param pageable  pagination
     * @return matching entities
     */
    @Query("select e from ExamEntity e where (:nivel is null or e.nivel = :nivel) "
            + "and (:publicado is null or e.publicado = :publicado)")
    List<ExamEntity> findByFilter(@Param("nivel") String nivel, @Param("publicado") Boolean publicado,
                                  Pageable pageable);

    /**
     * Counts exams filtered by optional level/published.
     *
     * @param nivel     optional level
     * @param publicado optional published
     * @return count
     */
    @Query("select count(e) from ExamEntity e where (:nivel is null or e.nivel = :nivel) "
            + "and (:publicado is null or e.publicado = :publicado)")
    long countByFilter(@Param("nivel") String nivel, @Param("publicado") Boolean publicado);
}
