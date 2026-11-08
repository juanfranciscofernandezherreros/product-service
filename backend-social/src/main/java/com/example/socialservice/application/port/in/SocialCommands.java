package com.example.socialservice.application.port.in;

import com.example.socialservice.domain.model.AvatarEmoji;
import java.util.UUID;

/**
 * Input records for social operations.
 */
public final class SocialCommands {

    private SocialCommands() {
    }

    /**
     * Create profile command.
     *
     * @param userId      user id
     * @param alias       alias
     * @param biografia   biography
     * @param avatarEmoji avatar
     */
    public record CreateProfileCommand(UUID userId, String alias, String biografia, AvatarEmoji avatarEmoji) {
    }

    /**
     * Update profile command.
     *
     * @param biografia   biography
     * @param avatarEmoji avatar
     */
    public record UpdateProfileCommand(String biografia, AvatarEmoji avatarEmoji) {
    }

    /**
     * Send message command.
     *
     * @param remitente    sender
     * @param destinatario recipient
     * @param contenido    content
     */
    public record SendMessageCommand(UUID remitente, UUID destinatario, String contenido) {
    }

    /**
     * Create message request command.
     *
     * @param remitente      sender
     * @param destinatario   recipient
     * @param mensajeInicial initial message
     */
    public record CreateRequestCommand(UUID remitente, UUID destinatario, String mensajeInicial) {
    }
}
