package com.example.socialservice.infrastructure.adapter.in.web.dto;

import com.example.socialservice.domain.model.AvatarEmoji;
import com.example.socialservice.domain.model.RequestStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

/**
 * Web DTOs for the social API.
 */
public final class SocialDtos {

    private SocialDtos() {
    }

    /**
     * Profile create request.
     *
     * @param userId      user id
     * @param alias       alias
     * @param biografia   biography
     * @param avatarEmoji avatar
     */
    public record ProfileCreateRequest(@NotNull UUID userId, @NotBlank String alias, String biografia,
                                       AvatarEmoji avatarEmoji) {
    }

    /**
     * Profile update request.
     *
     * @param biografia   biography
     * @param avatarEmoji avatar
     */
    public record ProfileUpdateRequest(String biografia, AvatarEmoji avatarEmoji) {
    }

    /**
     * Profile response.
     *
     * @param userId      user id
     * @param alias       alias
     * @param creadoEn    creation instant
     * @param biografia   biography
     * @param avatarEmoji avatar
     */
    public record ProfileResponse(UUID userId, String alias, Instant creadoEn, String biografia,
                                  AvatarEmoji avatarEmoji) {
    }

    /**
     * Follow request.
     *
     * @param seguido followed id
     */
    public record FollowRequest(@NotNull UUID seguido) {
    }

    /**
     * Message send request.
     *
     * @param remitente    sender
     * @param destinatario recipient
     * @param contenido    content
     */
    public record MessageSendRequest(@NotNull UUID remitente, @NotNull UUID destinatario,
                                     @NotBlank String contenido) {
    }

    /**
     * Message response.
     *
     * @param id           id
     * @param remitente    sender
     * @param destinatario recipient
     * @param contenido    content
     * @param creadoEn     creation instant
     */
    public record MessageResponse(Long id, UUID remitente, UUID destinatario, String contenido,
                                  Instant creadoEn) {
    }

    /**
     * Message request create.
     *
     * @param remitente      sender
     * @param destinatario   recipient
     * @param mensajeInicial initial message
     */
    public record RequestCreateRequest(@NotNull UUID remitente, @NotNull UUID destinatario,
                                       @NotBlank String mensajeInicial) {
    }

    /**
     * Respond request.
     *
     * @param estado status
     */
    public record RespondRequest(@NotNull RequestStatus estado) {
    }

    /**
     * Message request response.
     *
     * @param id             id
     * @param remitente      sender
     * @param destinatario   recipient
     * @param mensajeInicial initial message
     * @param estado         status
     * @param creadoEn       creation instant
     * @param respondidoEn   response instant
     */
    public record RequestResponse(Long id, UUID remitente, UUID destinatario, String mensajeInicial,
                                  RequestStatus estado, Instant creadoEn, Instant respondidoEn) {
    }

    /**
     * Error payload.
     *
     * @param code    code
     * @param message message
     */
    public record ErrorResponse(String code, String message) {
    }
}
