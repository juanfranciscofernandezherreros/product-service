package com.example.socialservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link MessageEntity}. */
public interface MessageJpaRepository extends JpaRepository<MessageEntity, Long> {
}
