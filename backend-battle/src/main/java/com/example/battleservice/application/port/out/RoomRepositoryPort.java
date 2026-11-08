package com.example.battleservice.application.port.out;

import com.example.battleservice.domain.model.BattleRoom;
import com.example.battleservice.domain.model.RoomStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound port for battle room persistence.
 */
public interface RoomRepositoryPort {

    /**
     * Persists a room.
     *
     * @param room room
     * @return stored room
     */
    BattleRoom save(BattleRoom room);

    /**
     * Finds a room by id.
     *
     * @param id id
     * @return the room if present
     */
    Optional<BattleRoom> findById(UUID id);

    /**
     * Indicates whether a room exists.
     *
     * @param id id
     * @return {@code true} if present
     */
    boolean existsById(UUID id);

    /**
     * Lists rooms filtered by optional status.
     *
     * @param status optional status ({@code null} = all)
     * @return matching rooms
     */
    List<BattleRoom> findByStatus(RoomStatus status);
}
