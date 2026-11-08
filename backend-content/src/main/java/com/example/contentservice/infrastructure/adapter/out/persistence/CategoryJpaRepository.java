package com.example.contentservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for {@link CategoryEntity}.
 */
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, String> {

    List<CategoryEntity> findAllByOrderByOrdenAsc();
}
