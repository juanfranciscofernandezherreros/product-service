package com.example.coursesservice.infrastructure.adapter.out.persistence;

import com.example.coursesservice.application.port.out.CoursesOutPorts.ActivityPort;
import com.example.coursesservice.domain.model.Activity;
import com.example.coursesservice.domain.model.Destreza;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Persistence adapter implementing {@link ActivityPort}.
 */
public class ActivityPersistenceAdapter implements ActivityPort {

    private final ActivityJpaRepository repository;

    public ActivityPersistenceAdapter(final ActivityJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Activity save(final Activity activity) {
        final ActivityEntity entity = new ActivityEntity();
        entity.setId(activity.id());
        entity.setSlug(activity.slug());
        entity.setTitulo(activity.titulo());
        entity.setDescripcion(activity.descripcion());
        entity.setDestreza(activity.destreza().value());
        entity.setUnidadId(activity.unidadId());
        entity.setArticuloSlug(activity.articuloSlug());
        entity.setOrden(activity.orden());
        entity.setPublicado(activity.publicado());
        entity.setCreadoEn(activity.creadoEn() != null ? activity.creadoEn() : OffsetDateTime.now());
        return CoursesPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Activity> findById(final Long id) {
        return repository.findById(id).map(CoursesPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsBySlug(final String slug) {
        return repository.existsBySlug(slug);
    }

    @Override
    public List<Activity> findAll(final Destreza destreza, final Boolean publicado,
                                  final int offset, final int limit) {
        final Sort sort = Sort.by(Sort.Direction.ASC, "orden");
        return repository.findAll(filter(destreza, publicado),
                        new OffsetBasedPageRequest(offset, limit, sort))
                .map(CoursesPersistenceMapper::toDomain).getContent();
    }

    @Override
    public long count(final Destreza destreza, final Boolean publicado) {
        return repository.count(filter(destreza, publicado));
    }

    private static Specification<ActivityEntity> filter(final Destreza destreza, final Boolean publicado) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (destreza != null) {
                predicates.add(cb.equal(root.get("destreza"), destreza.value()));
            }
            if (publicado != null) {
                predicates.add(cb.equal(root.get("publicado"), publicado));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
