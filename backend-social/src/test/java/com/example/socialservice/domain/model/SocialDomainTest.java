package com.example.socialservice.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.socialservice.domain.exception.InvalidSocialException;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the social domain models.
 */
class SocialDomainTest {

    private final UUID a = UUID.randomUUID();
    private final UUID b = UUID.randomUUID();

    @Test
    void profileAliasValidation() {
        assertThat(PublicProfile.create(a, "valid_1", null, null).getAvatarEmoji()).isEqualTo(AvatarEmoji.BOOK);
        assertThatThrownBy(() -> PublicProfile.create(a, "AB", null, null))
                .isInstanceOf(InvalidSocialException.class);
        assertThatThrownBy(() -> PublicProfile.create(a, "has space", null, null))
                .isInstanceOf(InvalidSocialException.class);
    }

    @Test
    void followSelfRejected() {
        assertThatThrownBy(() -> new Follow(a, a, null)).isInstanceOf(InvalidSocialException.class);
    }

    @Test
    void messageValidation() {
        assertThat(Message.send(a, b, "hi").getContenido()).isEqualTo("hi");
        assertThatThrownBy(() -> Message.send(a, a, "hi")).isInstanceOf(InvalidSocialException.class);
        assertThatThrownBy(() -> Message.send(a, b, " ")).isInstanceOf(InvalidSocialException.class);
    }

    @Test
    void requestRespondValidation() {
        final MessageRequest r = MessageRequest.create(a, b, "hi");
        assertThat(r.respond(RequestStatus.ACEPTADA).getRespondidoEn()).isNotNull();
        assertThatThrownBy(() -> r.respond(RequestStatus.PENDIENTE)).isInstanceOf(InvalidSocialException.class);
    }
}
