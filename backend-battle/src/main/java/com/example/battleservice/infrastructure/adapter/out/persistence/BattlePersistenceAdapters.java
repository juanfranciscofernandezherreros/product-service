package com.example.battleservice.infrastructure.adapter.out.persistence;

import com.example.battleservice.application.port.out.PlayerRepositoryPort;
import com.example.battleservice.application.port.out.QuestionRepositoryPort;
import com.example.battleservice.application.port.out.RoomRepositoryPort;
import com.example.battleservice.domain.model.BattlePlayer;
import com.example.battleservice.domain.model.BattleQuestion;
import com.example.battleservice.domain.model.BattleRoom;
import com.example.battleservice.domain.model.Level;
import com.example.battleservice.domain.model.QuestionTopic;
import com.example.battleservice.domain.model.RoomStatus;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Outbound persistence adapters for the battle domain.
 */
public final class BattlePersistenceAdapters {

    private BattlePersistenceAdapters() {
    }

    /** Question persistence adapter. */
    public static final class QuestionPersistenceAdapter implements QuestionRepositoryPort {

        private final QuestionJpaRepository repository;

        /**
         * Creates the adapter.
         *
         * @param repository repository
         */
        public QuestionPersistenceAdapter(final QuestionJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public BattleQuestion save(final BattleQuestion question) {
            return BattlePersistenceMapper.toDomain(repository.save(BattlePersistenceMapper.toEntity(question)));
        }

        @Override
        public Optional<BattleQuestion> findById(final long id) {
            return repository.findById(id).map(BattlePersistenceMapper::toDomain);
        }

        @Override
        public boolean existsById(final long id) {
            return repository.existsById(id);
        }

        @Override
        public boolean existsByLevelTopicPrompt(final Level level, final QuestionTopic topic, final String prompt) {
            return repository.existsByLevelAndTopicAndPrompt(level.name(),
                    topic.name().toLowerCase(Locale.ROOT), prompt);
        }

        @Override
        public List<BattleQuestion> findByFilter(final Level level, final QuestionTopic topic,
                                                 final int limit, final int offset) {
            final Pageable pageable = new OffsetBasedPageRequest(offset, limit, Sort.by("id").ascending());
            return repository.findByFilter(level == null ? null : level.name(),
                            topic == null ? null : topic.name().toLowerCase(Locale.ROOT), pageable).stream()
                    .map(BattlePersistenceMapper::toDomain).collect(Collectors.toList());
        }

        @Override
        public long countByFilter(final Level level, final QuestionTopic topic) {
            return repository.countByFilter(level == null ? null : level.name(),
                    topic == null ? null : topic.name().toLowerCase(Locale.ROOT));
        }

        @Override
        public void deleteById(final long id) {
            repository.deleteById(id);
        }
    }

    /** Room persistence adapter. */
    public static final class RoomPersistenceAdapter implements RoomRepositoryPort {

        private final RoomJpaRepository repository;

        /**
         * Creates the adapter.
         *
         * @param repository repository
         */
        public RoomPersistenceAdapter(final RoomJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public BattleRoom save(final BattleRoom room) {
            return BattlePersistenceMapper.toDomain(repository.save(BattlePersistenceMapper.toEntity(room)));
        }

        @Override
        public Optional<BattleRoom> findById(final UUID id) {
            return repository.findById(id).map(BattlePersistenceMapper::toDomain);
        }

        @Override
        public boolean existsById(final UUID id) {
            return repository.existsById(id);
        }

        @Override
        public List<BattleRoom> findByStatus(final RoomStatus status) {
            final String name = status == null ? null : status.name().toLowerCase(Locale.ROOT);
            return repository.findByStatusFilter(name).stream()
                    .map(BattlePersistenceMapper::toDomain).collect(Collectors.toList());
        }
    }

    /** Player persistence adapter. */
    public static final class PlayerPersistenceAdapter implements PlayerRepositoryPort {

        private final PlayerJpaRepository repository;

        /**
         * Creates the adapter.
         *
         * @param repository repository
         */
        public PlayerPersistenceAdapter(final PlayerJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public BattlePlayer save(final BattlePlayer player) {
            return BattlePersistenceMapper.toDomain(repository.save(BattlePersistenceMapper.toEntity(player)));
        }

        @Override
        public List<BattlePlayer> findByRoom(final UUID roomId) {
            return repository.findByRoomId(roomId).stream()
                    .map(BattlePersistenceMapper::toDomain).collect(Collectors.toList());
        }
    }
}
