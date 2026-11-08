package com.example.studyplanservice.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link ReportEntity}. */
public interface ReportJpaRepository extends JpaRepository<ReportEntity, ReportId> {

    /**
     * Finds reports by user.
     *
     * @param userId user id
     * @return the reports
     */
    List<ReportEntity> findByUserId(UUID userId);
}
