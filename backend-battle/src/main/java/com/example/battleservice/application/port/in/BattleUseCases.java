package com.example.battleservice.application.port.in;

import com.example.battleservice.application.port.in.BattleCommands.CreateRoomCommand;
import com.example.battleservice.application.port.in.BattleCommands.JoinRoomCommand;
import com.example.battleservice.application.port.in.BattleCommands.ListQuestionsQuery;
import com.example.battleservice.application.port.in.BattleCommands.PagedQuestions;
import com.example.battleservice.application.port.in.BattleCommands.QuestionCommand;
import com.example.battleservice.domain.model.BattlePlayer;
import com.example.battleservice.domain.model.BattleQuestion;
import com.example.battleservice.domain.model.BattleRoom;
import com.example.battleservice.domain.model.RoomStatus;
import java.util.List;
import java.util.UUID;

/**
 * Inbound port exposing the battle use cases.
 */
public interface BattleUseCases {

    /**
     * Creates a question.
     *
     * @param command attributes
     * @return created question
     */
    BattleQuestion createQuestion(QuestionCommand command);

    /**
     * Gets a question.
     *
     * @param id id
     * @return the question
     */
    BattleQuestion getQuestion(long id);

    /**
     * Lists questions.
     *
     * @param query pagination and filter
     * @return a page
     */
    PagedQuestions listQuestions(ListQuestionsQuery query);

    /**
     * Updates a question.
     *
     * @param id      id
     * @param command attributes
     * @return updated question
     */
    BattleQuestion updateQuestion(long id, QuestionCommand command);

    /**
     * Deletes a question.
     *
     * @param id id
     */
    void deleteQuestion(long id);

    /**
     * Creates a room.
     *
     * @param command attributes
     * @return created room
     */
    BattleRoom createRoom(CreateRoomCommand command);

    /**
     * Gets a room.
     *
     * @param id id
     * @return the room
     */
    BattleRoom getRoom(UUID id);

    /**
     * Lists rooms by optional status.
     *
     * @param status optional status
     * @return the rooms
     */
    List<BattleRoom> listRooms(RoomStatus status);

    /**
     * Lists players of a room.
     *
     * @param roomId room id
     * @return the players
     */
    List<BattlePlayer> listPlayers(UUID roomId);

    /**
     * Adds a player to a room.
     *
     * @param command attributes
     * @return the player
     */
    BattlePlayer joinRoom(JoinRoomCommand command);
}
