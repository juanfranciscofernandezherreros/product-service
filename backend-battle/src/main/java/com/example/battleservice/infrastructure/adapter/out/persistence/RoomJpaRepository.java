package com.example.battleservice.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Spring Data repository for {@link RoomJpaEntity}.
 */
public interface RoomJpaRepository extends JpaRepository<RoomJpaEntity, UUID> {

    /**
     * Finds rooms filtered by optional status.
     *
     * @param status optional status name ({@code null} = ignore)
     * @return matching entities
     */
    @Query("select r from RoomJpaEntity r where (:status is null or r.status = :status)")
    List<RoomJpaEntity> findByStatusFilter(@Param("status") String status);
}
