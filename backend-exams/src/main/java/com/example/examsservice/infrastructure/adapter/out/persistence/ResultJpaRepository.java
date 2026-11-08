package com.example.examsservice.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Spring Data repository for {@link ResultEntity}. */
public interface ResultJpaRepository extends JpaRepository<ResultEntity, Long> {

    /**
     * @param usuarioId user id
     * @return the results
     */
    List<ResultEntity> findByUsuarioId(UUID usuarioId);
}
