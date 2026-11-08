package com.example.coursesservice.infrastructure.adapter.out.persistence;

import com.example.coursesservice.application.port.out.CoursesOutPorts.UnitPort;
import com.example.coursesservice.domain.model.Unit;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Persistence adapter implementing {@link UnitPort}.
 */
public class UnitPersistenceAdapter implements UnitPort {

    private final UnitJpaRepository repository;

    public UnitPersistenceAdapter(final UnitJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Unit save(final Unit unit) {
        final UnitEntity entity = new UnitEntity();
        entity.setId(unit.id());
        entity.setSlug(unit.slug());
        entity.setTitulo(unit.titulo());
        entity.setDescripcion(unit.descripcion());
        entity.setNivel(unit.nivel());
        entity.setOrden(unit.orden());
        entity.setPublicado(unit.publicado());
        return CoursesPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Unit> findById(final Long id) {
        return repository.findById(id).map(CoursesPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsBySlug(final String slug) {
        return repository.existsBySlug(slug);
    }

    @Override
    public List<Unit> findAll(final String nivel, final Boolean publicado) {
        return repository.findAll(filter(nivel, publicado), Sort.by(Sort.Direction.ASC, "orden"))
                .stream().map(CoursesPersistenceMapper::toDomain).toList();
    }

    private static Specification<UnitEntity> filter(final String nivel, final Boolean publicado) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (nivel != null && !nivel.isBlank()) {
                predicates.add(cb.equal(root.get("nivel"), nivel));
            }
            if (publicado != null) {
                predicates.add(cb.equal(root.get("publicado"), publicado));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
