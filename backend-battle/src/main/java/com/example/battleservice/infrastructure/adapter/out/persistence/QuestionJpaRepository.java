package com.example.battleservice.infrastructure.adapter.out.persistence;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data repository for {@link QuestionJpaEntity}.
 */
public interface QuestionJpaRepository extends JpaRepository<QuestionJpaEntity, Long> {

    /**
     * Indicates whether a question with the natural key exists.
     *
     * @param level  level name
     * @param topic  topic name
     * @param prompt prompt
     * @return {@code true} if present
     */
    boolean existsByLevelAndTopicAndPrompt(String level, String topic, String prompt);

    /**
     * Finds questions filtered by optional level/topic.
     *
     * @param level    optional level ({@code null} = ignore)
     * @param topic    optional topic ({@code null} = ignore)
     * @param pageable pagination
     * @return matching entities
     */
    @Query("select q from QuestionJpaEntity q where (:level is null or q.level = :level) "
            + "and (:topic is null or q.topic = :topic)")
    List<QuestionJpaEntity> findByFilter(@Param("level") String level, @Param("topic") String topic,
                                         Pageable pageable);

    /**
     * Counts questions filtered by optional level/topic.
     *
     * @param level optional level
     * @param topic optional topic
     * @return number of matching entities
     */
    @Query("select count(q) from QuestionJpaEntity q where (:level is null or q.level = :level) "
            + "and (:topic is null or q.topic = :topic)")
    long countByFilter(@Param("level") String level, @Param("topic") String topic);
}
