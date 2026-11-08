package com.example.battleservice.infrastructure.adapter.in.web;

import com.example.battleservice.domain.model.BattlePlayer;
import com.example.battleservice.domain.model.BattleQuestion;
import com.example.battleservice.domain.model.BattleRoom;
import com.example.battleservice.infrastructure.adapter.in.web.dto.BattleDtos.PlayerResponse;
import com.example.battleservice.infrastructure.adapter.in.web.dto.BattleDtos.QuestionResponse;
import com.example.battleservice.infrastructure.adapter.in.web.dto.BattleDtos.RoomResponse;
import java.util.Locale;

/**
 * Maps domain models to web responses.
 */
public final class BattleWebMapper {

    private BattleWebMapper() {
    }

    /**
     * Maps a question to its response.
     *
     * @param q domain question
     * @return the response
     */
    public static QuestionResponse toResponse(final BattleQuestion q) {
        return new QuestionResponse(q.getId(), q.getLevel(), q.getTopic(), q.getPrompt(),
                q.getOptions(), q.getCorrectIndex(), q.getExplanation());
    }

    /**
     * Maps a room to its response.
     *
     * @param r domain room
     * @return the response
     */
    public static RoomResponse toResponse(final BattleRoom r) {
        return new RoomResponse(r.getId(), r.getCode(), r.getHostId(), r.getLevel(), r.getTopic(),
                r.getDurationSeconds(), r.getCapacity(), r.getStatus().name().toLowerCase(Locale.ROOT),
                r.getCreatedAt());
    }

    /**
     * Maps a player to its response.
     *
     * @param p domain player
     * @return the response
     */
    public static PlayerResponse toResponse(final BattlePlayer p) {
        return new PlayerResponse(p.getRoomId(), p.getUserId(), p.getAlias(),
                p.getStatus().name().toLowerCase(Locale.ROOT), p.getJoinedAt());
    }
}
