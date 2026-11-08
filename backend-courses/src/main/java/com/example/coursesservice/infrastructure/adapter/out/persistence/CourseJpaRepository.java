package com.example.coursesservice.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data repository for {@link CourseEntity}.
 */
public interface CourseJpaRepository extends JpaRepository<CourseEntity, Long> {

    List<CourseEntity> findByActivoOrderByCreadoEnDesc(boolean activo);

    List<CourseEntity> findAllByOrderByCreadoEnDesc();
}
