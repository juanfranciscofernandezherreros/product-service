package com.example.contentservice.infrastructure.adapter.out.persistence;

import com.example.contentservice.application.port.out.ContentOutPorts.ArticlePort;
import com.example.contentservice.domain.model.Article;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Persistence adapter implementing {@link ArticlePort}.
 */
public class ArticlePersistenceAdapter implements ArticlePort {

    private final ArticleJpaRepository repository;

    public ArticlePersistenceAdapter(final ArticleJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Article save(final Article article) {
        final ArticleEntity entity = new ArticleEntity();
        entity.setId(article.id());
        entity.setRuta(article.ruta());
        entity.setSlug(article.slug());
        entity.setTitulo(article.titulo());
        entity.setDescripcion(article.descripcion());
        entity.setHtml(article.html());
        entity.setUrlPublica(article.urlPublica());
        entity.setOrigen(article.origen());
        entity.setActualizadoEn(article.actualizadoEn() != null ? article.actualizadoEn() : OffsetDateTime.now());
        entity.setCategoriaSlug(article.categoriaSlug());
        entity.setFechaPublicacion(article.fechaPublicacion());
        entity.setNivel(article.nivel());
        entity.setPalabras(article.palabras());
        entity.setEsArticuloIndice(article.esArticuloIndice());
        entity.setUsuarioId(article.usuarioId());
        return ContentPersistenceMapper.toDomain(repository.save(entity));
    }

    @Override
    public Optional<Article> findBySlug(final String slug) {
        return repository.findBySlug(slug).map(ContentPersistenceMapper::toDomain);
    }

    @Override
    public boolean existsByRutaOrSlug(final String ruta, final String slug) {
        return repository.existsByRutaOrSlug(ruta, slug);
    }

    @Override
    public List<Article> findAll(final String categoriaSlug, final String nivel,
                                 final int offset, final int limit) {
        final Sort sort = Sort.by(Sort.Direction.DESC, "actualizadoEn");
        return repository.findAll(filter(categoriaSlug, nivel),
                        new OffsetBasedPageRequest(offset, limit, sort))
                .map(ContentPersistenceMapper::toDomain).getContent();
    }

    @Override
    public long count(final String categoriaSlug, final String nivel) {
        return repository.count(filter(categoriaSlug, nivel));
    }

    @Override
    @Transactional
    public void deleteBySlug(final String slug) {
        repository.deleteBySlug(slug);
    }

    private static Specification<ArticleEntity> filter(final String categoriaSlug, final String nivel) {
        return (root, query, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (categoriaSlug != null && !categoriaSlug.isBlank()) {
                predicates.add(cb.equal(root.get("categoriaSlug"), categoriaSlug));
            }
            if (nivel != null && !nivel.isBlank()) {
                predicates.add(cb.equal(root.get("nivel"), nivel));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
