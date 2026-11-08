package com.example.battleservice.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.battleservice.domain.exception.InvalidBattleException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the battle domain models.
 */
class BattleDomainTest {

    @Test
    void createsValidQuestion() {
        final BattleQuestion q = new BattleQuestion(null, Level.A1, QuestionTopic.DAILY, "p",
                List.of("a", "b", "c", "d"), 2, "e");
        assertThat(q.getOptions()).hasSize(4);
    }

    @Test
    void rejectsWrongOptions() {
        assertThatThrownBy(() -> new BattleQuestion(null, Level.A1, QuestionTopic.DAILY, "p",
                List.of("a", "b"), 0, "e")).isInstanceOf(InvalidBattleException.class);
    }

    @Test
    void rejectsIndexOutOfRange() {
        assertThatThrownBy(() -> new BattleQuestion(null, Level.A1, QuestionTopic.DAILY, "p",
                List.of("a", "b", "c", "d"), 4, "e")).isInstanceOf(InvalidBattleException.class);
    }

    @Test
    void roomOpenDefaults() {
        final BattleRoom r = BattleRoom.open(UUID.randomUUID(), Level.B1, RoomTopic.DAILY, 300, 6);
        assertThat(r.getStatus()).isEqualTo(RoomStatus.WAITING);
        assertThat(r.getCode()).isNotBlank();
    }

    @Test
    void roomRejectsBadDurationAndCapacity() {
        assertThatThrownBy(() -> new BattleRoom(UUID.randomUUID(), "C", UUID.randomUUID(), Level.A1,
                RoomTopic.MIXED, 999, 4, RoomStatus.WAITING, null, null, null))
                .isInstanceOf(InvalidBattleException.class);
        assertThatThrownBy(() -> BattleRoom.open(UUID.randomUUID(), Level.A1, RoomTopic.MIXED, 120, 1))
                .isInstanceOf(InvalidBattleException.class);
    }

    @Test
    void playerRejectsShortAlias() {
        assertThatThrownBy(() -> BattlePlayer.join(UUID.randomUUID(), UUID.randomUUID(), "a"))
                .isInstanceOf(InvalidBattleException.class);
        assertThat(BattlePlayer.join(UUID.randomUUID(), UUID.randomUUID(), "ok").getStatus())
                .isEqualTo(PlayerStatus.PENDING);
    }
}
