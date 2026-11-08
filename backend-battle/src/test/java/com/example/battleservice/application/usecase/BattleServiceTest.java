package com.example.battleservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.battleservice.application.port.in.BattleCommands.CreateRoomCommand;
import com.example.battleservice.application.port.in.BattleCommands.JoinRoomCommand;
import com.example.battleservice.application.port.in.BattleCommands.ListQuestionsQuery;
import com.example.battleservice.application.port.in.BattleCommands.PagedQuestions;
import com.example.battleservice.application.port.in.BattleCommands.QuestionCommand;
import com.example.battleservice.application.port.out.PlayerRepositoryPort;
import com.example.battleservice.application.port.out.QuestionRepositoryPort;
import com.example.battleservice.application.port.out.RoomRepositoryPort;
import com.example.battleservice.domain.exception.DuplicateResourceException;
import com.example.battleservice.domain.exception.ResourceNotFoundException;
import com.example.battleservice.domain.model.BattlePlayer;
import com.example.battleservice.domain.model.BattleQuestion;
import com.example.battleservice.domain.model.BattleRoom;
import com.example.battleservice.domain.model.Level;
import com.example.battleservice.domain.model.QuestionTopic;
import com.example.battleservice.domain.model.RoomStatus;
import com.example.battleservice.domain.model.RoomTopic;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link BattleService}.
 */
@ExtendWith(MockitoExtension.class)
class BattleServiceTest {

    @Mock
    private QuestionRepositoryPort questionRepository;
    @Mock
    private RoomRepositoryPort roomRepository;
    @Mock
    private PlayerRepositoryPort playerRepository;

    private BattleService service;

    @BeforeEach
    void setUp() {
        service = new BattleService(questionRepository, roomRepository, playerRepository);
    }

    private QuestionCommand qc() {
        return new QuestionCommand(Level.A1, QuestionTopic.DAILY, "prompt?",
                List.of("a", "b", "c", "d"), 1, "because");
    }

    @Test
    void createQuestionOk() {
        when(questionRepository.existsByLevelTopicPrompt(Level.A1, QuestionTopic.DAILY, "prompt?"))
                .thenReturn(false);
        when(questionRepository.save(any(BattleQuestion.class))).thenAnswer(inv -> inv.getArgument(0));
        assertThat(service.createQuestion(qc()).getPrompt()).isEqualTo("prompt?");
    }

    @Test
    void createQuestionDuplicate() {
        when(questionRepository.existsByLevelTopicPrompt(Level.A1, QuestionTopic.DAILY, "prompt?"))
                .thenReturn(true);
        assertThatThrownBy(() -> service.createQuestion(qc())).isInstanceOf(DuplicateResourceException.class);
        verify(questionRepository, never()).save(any(BattleQuestion.class));
    }

    @Test
    void getQuestionOkAndMissing() {
        final BattleQuestion q = new BattleQuestion(1L, Level.A1, QuestionTopic.DAILY, "p",
                List.of("a", "b", "c", "d"), 0, "e");
        when(questionRepository.findById(1L)).thenReturn(Optional.of(q));
        assertThat(service.getQuestion(1L)).isEqualTo(q);
        when(questionRepository.findById(9L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getQuestion(9L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listQuestionsReturnsPage() {
        final BattleQuestion q = new BattleQuestion(1L, Level.A1, QuestionTopic.DAILY, "p",
                List.of("a", "b", "c", "d"), 0, "e");
        when(questionRepository.findByFilter(eq(Level.A1), eq(QuestionTopic.DAILY), anyInt(), anyInt()))
                .thenReturn(List.of(q));
        when(questionRepository.countByFilter(Level.A1, QuestionTopic.DAILY)).thenReturn(1L);
        final PagedQuestions page = service.listQuestions(new ListQuestionsQuery(Level.A1, QuestionTopic.DAILY, 20, 0));
        assertThat(page.total()).isEqualTo(1L);
        assertThat(page.items()).hasSize(1);
    }

    @Test
    void updateQuestionOkAndMissing() {
        when(questionRepository.existsById(1L)).thenReturn(true);
        when(questionRepository.save(any(BattleQuestion.class))).thenAnswer(inv -> inv.getArgument(0));
        assertThat(service.updateQuestion(1L, qc()).getId()).isEqualTo(1L);
        when(questionRepository.existsById(9L)).thenReturn(false);
        assertThatThrownBy(() -> service.updateQuestion(9L, qc())).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteQuestionOkAndMissing() {
        when(questionRepository.existsById(1L)).thenReturn(true);
        service.deleteQuestion(1L);
        verify(questionRepository).deleteById(1L);
        when(questionRepository.existsById(9L)).thenReturn(false);
        assertThatThrownBy(() -> service.deleteQuestion(9L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createRoomAndGetAndList() {
        when(roomRepository.save(any(BattleRoom.class))).thenAnswer(inv -> inv.getArgument(0));
        final BattleRoom room = service.createRoom(new CreateRoomCommand(
                UUID.randomUUID(), Level.B2, RoomTopic.MIXED, 180, 4));
        assertThat(room.getStatus()).isEqualTo(RoomStatus.WAITING);
        when(roomRepository.findById(room.getId())).thenReturn(Optional.of(room));
        assertThat(service.getRoom(room.getId())).isEqualTo(room);
        when(roomRepository.findByStatus(RoomStatus.WAITING)).thenReturn(List.of(room));
        assertThat(service.listRooms(RoomStatus.WAITING)).hasSize(1);
    }

    @Test
    void getRoomMissing() {
        final UUID id = UUID.randomUUID();
        when(roomRepository.findById(id)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getRoom(id)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listPlayersOkAndRoomMissing() {
        final UUID id = UUID.randomUUID();
        when(roomRepository.existsById(id)).thenReturn(true);
        when(playerRepository.findByRoom(id)).thenReturn(List.of(
                BattlePlayer.join(id, UUID.randomUUID(), "al")));
        assertThat(service.listPlayers(id)).hasSize(1);
        final UUID missing = UUID.randomUUID();
        when(roomRepository.existsById(missing)).thenReturn(false);
        assertThatThrownBy(() -> service.listPlayers(missing)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void joinRoomOkAndRoomMissing() {
        final UUID id = UUID.randomUUID();
        when(roomRepository.existsById(id)).thenReturn(true);
        when(playerRepository.save(any(BattlePlayer.class))).thenAnswer(inv -> inv.getArgument(0));
        assertThat(service.joinRoom(new JoinRoomCommand(id, UUID.randomUUID(), "al")).getAlias()).isEqualTo("al");
        final UUID missing = UUID.randomUUID();
        when(roomRepository.existsById(missing)).thenReturn(false);
        assertThatThrownBy(() -> service.joinRoom(new JoinRoomCommand(missing, UUID.randomUUID(), "al")))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
