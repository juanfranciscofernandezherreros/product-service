package com.example.contentservice.domain.model;

import com.example.contentservice.domain.exception.InvalidContentException;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Immutable domain model for a blog article.
 *
 * @param id               identifier (nullable before persistence)
 * @param ruta             storage path (must match {@code post/<slug>.html})
 * @param slug             unique slug
 * @param titulo           title
 * @param descripcion      optional description
 * @param html             rendered HTML body
 * @param urlPublica       public URL
 * @param origen           source system
 * @param categoriaSlug    optional category slug
 * @param fechaPublicacion optional publication date
 * @param nivel            optional difficulty level
 * @param palabras         optional word count
 * @param esArticuloIndice whether it is an index article
 * @param usuarioId        optional author id
 * @param actualizadoEn    last update timestamp
 */
public record Article(
        Long id, String ruta, String slug, String titulo, String descripcion, String html,
        String urlPublica, String origen, String categoriaSlug, LocalDate fechaPublicacion,
        String nivel, Integer palabras, boolean esArticuloIndice, UUID usuarioId,
        OffsetDateTime actualizadoEn) {

    private static final Pattern RUTA = Pattern.compile("^post/[a-z0-9-]+[.]html$");

    /**
     * Canonical constructor enforcing domain invariants.
     */
    public Article {
        if (ruta == null || !RUTA.matcher(ruta).matches()) {
            throw new InvalidContentException("ruta must match post/<slug>.html");
        }
        if (isBlank(slug)) {
            throw new InvalidContentException("slug is required");
        }
        if (isBlank(titulo)) {
            throw new InvalidContentException("titulo is required");
        }
        if (isBlank(html)) {
            throw new InvalidContentException("html is required");
        }
        if (isBlank(urlPublica)) {
            throw new InvalidContentException("urlPublica is required");
        }
    }

    /**
     * Factory for a new article (origin defaults to {@code github}).
     *
     * @param ruta             storage path
     * @param slug             slug
     * @param titulo           title
     * @param descripcion      description
     * @param html             HTML body
     * @param urlPublica       public URL
     * @param categoriaSlug    category slug
     * @param fechaPublicacion publication date
     * @param nivel            level
     * @return a new article
     */
    public static Article create(final String ruta, final String slug, final String titulo,
                                 final String descripcion, final String html, final String urlPublica,
                                 final String categoriaSlug, final LocalDate fechaPublicacion,
                                 final String nivel) {
        return new Article(null, ruta, slug, titulo, descripcion, html, urlPublica, "github",
                categoriaSlug, fechaPublicacion, nivel, null, false, null, null);
    }

    /**
     * Returns a copy with mutable fields updated (nulls keep current values).
     *
     * @param newTitulo      new title or null
     * @param newDescripcion new description or null
     * @param newHtml        new HTML or null
     * @param newCategoria   new category slug or null
     * @param newNivel       new level or null
     * @return an updated article
     */
    public Article update(final String newTitulo, final String newDescripcion, final String newHtml,
                          final String newCategoria, final String newNivel) {
        return new Article(id, ruta, slug,
                newTitulo != null ? newTitulo : titulo,
                newDescripcion != null ? newDescripcion : descripcion,
                newHtml != null ? newHtml : html,
                urlPublica, origen,
                newCategoria != null ? newCategoria : categoriaSlug,
                fechaPublicacion,
                newNivel != null ? newNivel : nivel,
                palabras, esArticuloIndice, usuarioId, actualizadoEn);
    }

    private static boolean isBlank(final String s) {
        return s == null || s.strip().isEmpty();
    }
}
