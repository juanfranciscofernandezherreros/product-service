package com.example.socialservice.infrastructure.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link ProfileEntity}. */
public interface ProfileJpaRepository extends JpaRepository<ProfileEntity, UUID> {

    /**
     * Indicates whether an alias is taken.
     *
     * @param alias alias
     * @return {@code true} if present
     */
    boolean existsByAlias(String alias);
}
