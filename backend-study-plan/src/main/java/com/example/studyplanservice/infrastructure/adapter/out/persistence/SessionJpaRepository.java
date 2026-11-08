package com.example.studyplanservice.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link SessionEntity}. */
public interface SessionJpaRepository extends JpaRepository<SessionEntity, SessionId> {

    /**
     * Finds sessions by user.
     *
     * @param userId user id
     * @return the sessions
     */
    List<SessionEntity> findByUserId(UUID userId);
}
