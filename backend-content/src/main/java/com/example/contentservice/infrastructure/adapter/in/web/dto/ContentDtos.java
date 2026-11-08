package com.example.contentservice.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Request and response DTOs for the Content web adapter.
 */
public final class ContentDtos {

    private ContentDtos() {
    }

    /** Create-article request. */
    public record CreateArticleRequest(
            @NotBlank String ruta, @NotBlank String slug, @NotBlank String titulo,
            String descripcion, @NotBlank String html, @NotBlank String urlPublica,
            String categoriaSlug, LocalDate fechaPublicacion, String nivel) {
    }

    /** Update-article request. */
    public record UpdateArticleRequest(String titulo, String descripcion, String html,
                                       String categoriaSlug, String nivel) {
    }

    /** Article response. */
    public record ArticleResponse(Long id, String ruta, String slug, String titulo, String descripcion,
                                  String html, String urlPublica, String origen, String categoriaSlug,
                                  LocalDate fechaPublicacion, String nivel, Integer palabras,
                                  boolean esArticuloIndice, OffsetDateTime actualizadoEn) {
    }

    /** Create-comment request. */
    public record CreateCommentRequest(Long padreId, UUID autorId,
                                       @NotBlank @Size(min = 1, max = 2000) String contenido) {
    }

    /** Comment response. */
    public record CommentResponse(Long id, String articuloSlug, Long padreId, UUID autorId,
                                  String contenido, OffsetDateTime creadoEn) {
    }

    /** Category request/response. */
    public record CategoryRequest(@NotBlank String slug, @NotBlank String nombre,
                                  String descripcion, Integer orden) {
    }

    /** Category response. */
    public record CategoryResponse(String slug, String nombre, String descripcion, int orden) {
    }

    /** Page request/response. */
    public record PageRequest(@NotBlank String ruta, @NotBlank String titulo, String descripcion,
                              @NotBlank String html, @NotBlank String urlPublica) {
    }

    /** Page response. */
    public record PageResponseDto(String ruta, String titulo, String descripcion, String html,
                                  String urlPublica, OffsetDateTime actualizadoEn) {
    }

    /** Create-collection request. */
    public record CreateCollectionRequest(@NotBlank String slug, @NotBlank String titulo,
                                          String descripcion, @NotBlank String tipo,
                                          Boolean incluirTodosArticulos, @NotBlank String nombreArchivo,
                                          LocalDate fechaPublicacion) {
    }

    /** Collection response. */
    public record CollectionResponse(Long id, String slug, String titulo, String descripcion, String tipo,
                                     boolean incluirTodosArticulos, String nombreArchivo,
                                     LocalDate fechaPublicacion, boolean publicado) {
    }

    /** Create-downloadable request. */
    public record CreateDownloadableRequest(@NotBlank String slug, @NotBlank String titulo,
                                            String descripcion, String categoria,
                                            LocalDate fechaPublicacion, @NotBlank String urlArchivo,
                                            String urlLectura) {
    }

    /** Downloadable response. */
    public record DownloadableResponse(Long id, String slug, String titulo, String descripcion,
                                       String categoria, LocalDate fechaPublicacion, String urlArchivo,
                                       String urlLectura, int orden, boolean publicado,
                                       OffsetDateTime creadoEn) {
    }

    /** Create-movie-phrase request. */
    public record CreateMoviePhraseRequest(@NotBlank String movieSlug, @NotBlank String movieTitle,
                                           @Min(1) int subtitleNumber, @NotBlank String timecode,
                                           @NotBlank String phrase, String tenseLabels, String notes) {
    }

    /** Movie-phrase response. */
    public record MoviePhraseResponse(Long id, String movieSlug, String movieTitle, int subtitleNumber,
                                      String timecode, String phrase, String tenseLabels, String notes,
                                      OffsetDateTime createdAt) {
    }

    /** Generic paginated wrapper. */
    public record PagedResponse<T>(List<T> items, long total, int offset, int limit) {
    }

    /** Error payload. */
    public record ErrorResponse(String code, String message) {
    }
}
