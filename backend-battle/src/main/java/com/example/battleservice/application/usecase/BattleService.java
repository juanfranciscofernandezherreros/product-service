package com.example.battleservice.application.usecase;

import com.example.battleservice.application.port.in.BattleCommands.CreateRoomCommand;
import com.example.battleservice.application.port.in.BattleCommands.JoinRoomCommand;
import com.example.battleservice.application.port.in.BattleCommands.ListQuestionsQuery;
import com.example.battleservice.application.port.in.BattleCommands.PagedQuestions;
import com.example.battleservice.application.port.in.BattleCommands.QuestionCommand;
import com.example.battleservice.application.port.in.BattleUseCases;
import com.example.battleservice.application.port.out.PlayerRepositoryPort;
import com.example.battleservice.application.port.out.QuestionRepositoryPort;
import com.example.battleservice.application.port.out.RoomRepositoryPort;
import com.example.battleservice.domain.exception.DuplicateResourceException;
import com.example.battleservice.domain.exception.ResourceNotFoundException;
import com.example.battleservice.domain.model.BattlePlayer;
import com.example.battleservice.domain.model.BattleQuestion;
import com.example.battleservice.domain.model.BattleRoom;
import com.example.battleservice.domain.model.RoomStatus;
import java.util.List;
import java.util.UUID;

/**
 * Application service implementing the battle use cases.
 */
public class BattleService implements BattleUseCases {

    private final QuestionRepositoryPort questionRepository;
    private final RoomRepositoryPort roomRepository;
    private final PlayerRepositoryPort playerRepository;

    /**
     * Creates the service.
     *
     * @param questionRepository question port
     * @param roomRepository     room port
     * @param playerRepository   player port
     */
    public BattleService(final QuestionRepositoryPort questionRepository,
                         final RoomRepositoryPort roomRepository,
                         final PlayerRepositoryPort playerRepository) {
        this.questionRepository = questionRepository;
        this.roomRepository = roomRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public BattleQuestion createQuestion(final QuestionCommand command) {
        if (command.prompt() != null && questionRepository.existsByLevelTopicPrompt(
                command.level(), command.topic(), command.prompt().trim())) {
            throw new DuplicateResourceException("Question already exists for level/topic/prompt");
        }
        return questionRepository.save(new BattleQuestion(null, command.level(), command.topic(),
                command.prompt(), command.options(), command.correctIndex(), command.explanation()));
    }

    @Override
    public BattleQuestion getQuestion(final long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", String.valueOf(id)));
    }

    @Override
    public PagedQuestions listQuestions(final ListQuestionsQuery query) {
        final List<BattleQuestion> items = questionRepository.findByFilter(
                query.level(), query.topic(), query.limit(), query.offset());
        return new PagedQuestions(questionRepository.countByFilter(query.level(), query.topic()), items);
    }

    @Override
    public BattleQuestion updateQuestion(final long id, final QuestionCommand command) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question", String.valueOf(id));
        }
        return questionRepository.save(new BattleQuestion(id, command.level(), command.topic(),
                command.prompt(), command.options(), command.correctIndex(), command.explanation()));
    }

    @Override
    public void deleteQuestion(final long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question", String.valueOf(id));
        }
        questionRepository.deleteById(id);
    }

    @Override
    public BattleRoom createRoom(final CreateRoomCommand command) {
        return roomRepository.save(BattleRoom.open(command.hostId(), command.level(), command.topic(),
                command.durationSeconds(), command.capacity()));
    }

    @Override
    public BattleRoom getRoom(final UUID id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", String.valueOf(id)));
    }

    @Override
    public List<BattleRoom> listRooms(final RoomStatus status) {
        return roomRepository.findByStatus(status);
    }

    @Override
    public List<BattlePlayer> listPlayers(final UUID roomId) {
        if (!roomRepository.existsById(roomId)) {
            throw new ResourceNotFoundException("Room", String.valueOf(roomId));
        }
        return playerRepository.findByRoom(roomId);
    }

    @Override
    public BattlePlayer joinRoom(final JoinRoomCommand command) {
        if (!roomRepository.existsById(command.roomId())) {
            throw new ResourceNotFoundException("Room", String.valueOf(command.roomId()));
        }
        return playerRepository.save(BattlePlayer.join(command.roomId(), command.userId(), command.alias()));
    }
}
