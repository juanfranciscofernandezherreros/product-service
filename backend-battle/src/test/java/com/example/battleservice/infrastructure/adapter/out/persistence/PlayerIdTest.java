package com.example.battleservice.infrastructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link PlayerId}.
 */
class PlayerIdTest {

    @Test
    void equalityAndAccessors() {
        final UUID room = UUID.randomUUID();
        final UUID user = UUID.randomUUID();
        final PlayerId a = new PlayerId(room, user);
        final PlayerId b = new PlayerId(room, user);
        final PlayerId c = new PlayerId(UUID.randomUUID(), user);

        assertThat(a).isEqualTo(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(c).isNotEqualTo(null).isNotEqualTo("x");
        assertThat(a.getRoomId()).isEqualTo(room);
        assertThat(a.getUserId()).isEqualTo(user);
        assertThat(new PlayerId().getRoomId()).isNull();
    }
}
