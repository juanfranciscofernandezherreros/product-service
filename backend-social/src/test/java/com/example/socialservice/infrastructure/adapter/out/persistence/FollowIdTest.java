package com.example.socialservice.infrastructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link FollowId}.
 */
class FollowIdTest {

    @Test
    void equalityAndAccessors() {
        final UUID a = UUID.randomUUID();
        final UUID b = UUID.randomUUID();
        final FollowId x = new FollowId(a, b);
        final FollowId y = new FollowId(a, b);
        assertThat(x).isEqualTo(x).isEqualTo(y).hasSameHashCodeAs(y);
        assertThat(x).isNotEqualTo(new FollowId(b, a)).isNotEqualTo(null).isNotEqualTo("x");
        assertThat(x.getSeguidor()).isEqualTo(a);
        assertThat(x.getSeguido()).isEqualTo(b);
        assertThat(new FollowId().getSeguidor()).isNull();
    }
}
