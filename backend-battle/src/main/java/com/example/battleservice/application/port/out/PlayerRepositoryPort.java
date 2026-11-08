package com.example.battleservice.application.port.out;

import com.example.battleservice.domain.model.BattlePlayer;
import java.util.List;
import java.util.UUID;

/**
 * Outbound port for battle player persistence.
 */
public interface PlayerRepositoryPort {

    /**
     * Persists a player.
     *
     * @param player player
     * @return stored player
     */
    BattlePlayer save(BattlePlayer player);

    /**
     * Lists players of a room.
     *
     * @param roomId room id
     * @return the players
     */
    List<BattlePlayer> findByRoom(UUID roomId);
}
