package com.example.contentservice.application.port.in;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Command records carrying validated input into the use cases.
 */
public final class ContentCommands {

    private ContentCommands() {
    }

    /** Command to create an article. */
    public record CreateArticle(String ruta, String slug, String titulo, String descripcion,
                                String html, String urlPublica, String categoriaSlug,
                                LocalDate fechaPublicacion, String nivel) {
    }

    /** Command to update an article by slug. */
    public record UpdateArticle(String slug, String titulo, String descripcion, String html,
                                String categoriaSlug, String nivel) {
    }

    /** Command to create a comment. */
    public record CreateComment(String articuloSlug, Long padreId, UUID autorId, String contenido) {
    }

    /** Command to create a category. */
    public record CreateCategory(String slug, String nombre, String descripcion, Integer orden) {
    }

    /** Command to create a page. */
    public record CreatePage(String ruta, String titulo, String descripcion, String html,
                             String urlPublica) {
    }

    /** Command to create a collection. */
    public record CreateCollection(String slug, String titulo, String descripcion, String tipo,
                                   Boolean incluirTodosArticulos, String nombreArchivo,
                                   LocalDate fechaPublicacion) {
    }

    /** Command to create a downloadable. */
    public record CreateDownloadable(String slug, String titulo, String descripcion, String categoria,
                                     LocalDate fechaPublicacion, String urlArchivo, String urlLectura) {
    }

    /** Command to create a movie phrase. */
    public record CreateMoviePhrase(String movieSlug, String movieTitle, int subtitleNumber,
                                    String timecode, String phrase, String tenseLabels, String notes) {
    }
}
