package com.example.contentservice.domain.model;

import com.example.contentservice.domain.exception.InvalidContentException;

import java.time.OffsetDateTime;

/**
 * Immutable domain model for a blog page.
 *
 * @param ruta          primary key path
 * @param titulo        title
 * @param descripcion   optional description
 * @param html          rendered HTML body
 * @param urlPublica    public URL
 * @param actualizadoEn optional last update timestamp
 */
public record Page(String ruta, String titulo, String descripcion, String html,
                   String urlPublica, OffsetDateTime actualizadoEn) {

    /**
     * Canonical constructor enforcing domain invariants.
     */
    public Page {
        if (ruta == null || ruta.strip().isEmpty()) {
            throw new InvalidContentException("ruta is required");
        }
        if (titulo == null || titulo.strip().isEmpty()) {
            throw new InvalidContentException("titulo is required");
        }
        if (html == null || html.strip().isEmpty()) {
            throw new InvalidContentException("html is required");
        }
        if (urlPublica == null || urlPublica.strip().isEmpty()) {
            throw new InvalidContentException("urlPublica is required");
        }
    }

    /**
     * Factory for a new page.
     *
     * @param ruta        path
     * @param titulo      title
     * @param descripcion description
     * @param html        HTML body
     * @param urlPublica  public URL
     * @return a new page
     */
    public static Page create(final String ruta, final String titulo, final String descripcion,
                              final String html, final String urlPublica) {
        return new Page(ruta, titulo, descripcion, html, urlPublica, null);
    }
}
