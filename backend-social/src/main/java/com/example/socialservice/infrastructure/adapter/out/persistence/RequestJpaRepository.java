package com.example.socialservice.infrastructure.adapter.out.persistence;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link RequestEntity}. */
public interface RequestJpaRepository extends JpaRepository<RequestEntity, Long> {

    /**
     * Indicates whether a request exists between two users.
     *
     * @param remitente    sender
     * @param destinatario recipient
     * @return {@code true} if present
     */
    boolean existsByRemitenteAndDestinatario(UUID remitente, UUID destinatario);
}
