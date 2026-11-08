package com.example.battleservice.infrastructure.adapter.in.web;

import com.example.battleservice.application.port.in.BattleCommands.CreateRoomCommand;
import com.example.battleservice.application.port.in.BattleCommands.JoinRoomCommand;
import com.example.battleservice.application.port.in.BattleUseCases;
import com.example.battleservice.domain.model.RoomStatus;
import com.example.battleservice.infrastructure.adapter.in.web.dto.BattleDtos.JoinRequest;
import com.example.battleservice.infrastructure.adapter.in.web.dto.BattleDtos.PlayerResponse;
import com.example.battleservice.infrastructure.adapter.in.web.dto.BattleDtos.RoomRequest;
import com.example.battleservice.infrastructure.adapter.in.web.dto.BattleDtos.RoomResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter for battle rooms under {@code /v1/battle/rooms}.
 */
@RestController
@RequestMapping("/v1/battle/rooms")
public class RoomController {

    private final BattleUseCases useCases;

    /**
     * Creates the controller.
     *
     * @param useCases battle use cases
     */
    public RoomController(final BattleUseCases useCases) {
        this.useCases = useCases;
    }

    /**
     * Creates a room.
     *
     * @param request payload
     * @return 201 with the room
     */
    @PostMapping
    public ResponseEntity<RoomResponse> create(@Valid @RequestBody final RoomRequest request) {
        final RoomResponse response = BattleWebMapper.toResponse(useCases.createRoom(new CreateRoomCommand(
                request.hostId(), request.level(), request.topic(),
                request.durationSeconds(), request.capacity())));
        return ResponseEntity.created(URI.create("/v1/battle/rooms/" + response.id())).body(response);
    }

    /**
     * Lists rooms by optional status.
     *
     * @param status optional status
     * @return 200 with rooms
     */
    @GetMapping
    public List<RoomResponse> list(@RequestParam(required = false) final String status) {
        final RoomStatus parsed = status == null || status.isBlank()
                ? null : RoomStatus.valueOf(status.trim().toUpperCase(Locale.ROOT));
        return useCases.listRooms(parsed).stream().map(BattleWebMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * Gets a room.
     *
     * @param roomId room id
     * @return 200 with the room
     */
    @GetMapping("/{roomId}")
    public RoomResponse get(@PathVariable final UUID roomId) {
        return BattleWebMapper.toResponse(useCases.getRoom(roomId));
    }

    /**
     * Lists players of a room.
     *
     * @param roomId room id
     * @return 200 with players
     */
    @GetMapping("/{roomId}/players")
    public List<PlayerResponse> listPlayers(@PathVariable final UUID roomId) {
        return useCases.listPlayers(roomId).stream().map(BattleWebMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * Joins a room.
     *
     * @param roomId  room id
     * @param request payload
     * @return 201 with the player
     */
    @PostMapping("/{roomId}/players")
    public ResponseEntity<PlayerResponse> join(@PathVariable final UUID roomId,
                                               @Valid @RequestBody final JoinRequest request) {
        final PlayerResponse response = BattleWebMapper.toResponse(useCases.joinRoom(
                new JoinRoomCommand(roomId, request.userId(), request.alias())));
        return ResponseEntity.created(URI.create("/v1/battle/rooms/" + roomId + "/players/" + response.userId()))
                .body(response);
    }
}
