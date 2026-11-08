package com.example.battleservice.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data repository for {@link PlayerJpaEntity}.
 */
public interface PlayerJpaRepository extends JpaRepository<PlayerJpaEntity, PlayerId> {

    /**
     * Finds players by room.
     *
     * @param roomId room id
     * @return the players
     */
    List<PlayerJpaEntity> findByRoomId(UUID roomId);
}
