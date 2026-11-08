package com.example.socialservice.infrastructure.adapter.out.persistence;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link FollowEntity}. */
public interface FollowJpaRepository extends JpaRepository<FollowEntity, FollowId> {

    /**
     * Finds follows by follower.
     *
     * @param seguidor follower
     * @return the follows
     */
    List<FollowEntity> findBySeguidor(UUID seguidor);

    /**
     * Indicates whether a follow exists.
     *
     * @param seguidor follower
     * @param seguido  followed
     * @return {@code true} if present
     */
    boolean existsBySeguidorAndSeguido(UUID seguidor, UUID seguido);

    /**
     * Deletes a follow.
     *
     * @param seguidor follower
     * @param seguido  followed
     */
    @Transactional
    void deleteBySeguidorAndSeguido(UUID seguidor, UUID seguido);
}
