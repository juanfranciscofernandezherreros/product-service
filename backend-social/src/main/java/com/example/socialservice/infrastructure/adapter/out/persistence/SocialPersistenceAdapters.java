package com.example.socialservice.infrastructure.adapter.out.persistence;

import com.example.socialservice.application.port.out.SocialOutPorts.FollowPort;
import com.example.socialservice.application.port.out.SocialOutPorts.MessagePort;
import com.example.socialservice.application.port.out.SocialOutPorts.ProfilePort;
import com.example.socialservice.application.port.out.SocialOutPorts.RequestPort;
import com.example.socialservice.domain.model.AvatarEmoji;
import com.example.socialservice.domain.model.Follow;
import com.example.socialservice.domain.model.Message;
import com.example.socialservice.domain.model.MessageRequest;
import com.example.socialservice.domain.model.PublicProfile;
import com.example.socialservice.domain.model.RequestStatus;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Outbound persistence adapters for the social domain.
 */
public final class SocialPersistenceAdapters {

    private SocialPersistenceAdapters() {
    }

    /** Profile adapter. */
    public static final class ProfileAdapter implements ProfilePort {
        private final ProfileJpaRepository repository;

        /** @param repository repository */
        public ProfileAdapter(final ProfileJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public PublicProfile save(final PublicProfile p) {
            final ProfileEntity saved = repository.save(new ProfileEntity(p.getUserId(), p.getAlias(),
                    p.getCreadoEn(), p.getBiografia(), p.getAvatarEmoji().name()));
            return toDomain(saved);
        }

        @Override
        public Optional<PublicProfile> findByUser(final UUID userId) {
            return repository.findById(userId).map(ProfileAdapter::toDomain);
        }

        @Override
        public boolean existsByUser(final UUID userId) {
            return repository.existsById(userId);
        }

        @Override
        public boolean existsByAlias(final String alias) {
            return repository.existsByAlias(alias);
        }

        private static PublicProfile toDomain(final ProfileEntity e) {
            return new PublicProfile(e.getUserId(), e.getAlias(), e.getCreadoEn(), e.getBiografia(),
                    AvatarEmoji.valueOf(e.getAvatarEmoji().toUpperCase(Locale.ROOT)));
        }
    }

    /** Follow adapter. */
    public static final class FollowAdapter implements FollowPort {
        private final FollowJpaRepository repository;

        /** @param repository repository */
        public FollowAdapter(final FollowJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public Follow save(final Follow f) {
            final FollowEntity saved = repository.save(new FollowEntity(f.getSeguidor(), f.getSeguido(),
                    f.getCreadoEn()));
            return new Follow(saved.getSeguidor(), saved.getSeguido(), saved.getCreadoEn());
        }

        @Override
        public List<UUID> findFollowing(final UUID seguidor) {
            return repository.findBySeguidor(seguidor).stream()
                    .map(FollowEntity::getSeguido).collect(Collectors.toList());
        }

        @Override
        public boolean exists(final UUID seguidor, final UUID seguido) {
            return repository.existsBySeguidorAndSeguido(seguidor, seguido);
        }

        @Override
        public void delete(final UUID seguidor, final UUID seguido) {
            repository.deleteBySeguidorAndSeguido(seguidor, seguido);
        }
    }

    /** Message adapter. */
    public static final class MessageAdapter implements MessagePort {
        private final MessageJpaRepository repository;

        /** @param repository repository */
        public MessageAdapter(final MessageJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public Message save(final Message m) {
            final MessageEntity saved = repository.save(new MessageEntity(m.getId(), m.getRemitente(),
                    m.getDestinatario(), m.getContenido(), m.getCreadoEn()));
            return new Message(saved.getId(), saved.getRemitente(), saved.getDestinatario(),
                    saved.getContenido(), saved.getCreadoEn());
        }
    }

    /** Request adapter. */
    public static final class RequestAdapter implements RequestPort {
        private final RequestJpaRepository repository;

        /** @param repository repository */
        public RequestAdapter(final RequestJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public MessageRequest save(final MessageRequest r) {
            final RequestEntity saved = repository.save(new RequestEntity(r.getId(), r.getRemitente(),
                    r.getDestinatario(), r.getMensajeInicial(), r.getEstado().name().toLowerCase(Locale.ROOT),
                    r.getCreadoEn(), r.getRespondidoEn()));
            return toDomain(saved);
        }

        @Override
        public Optional<MessageRequest> findById(final long id) {
            return repository.findById(id).map(RequestAdapter::toDomain);
        }

        @Override
        public boolean existsBySenderRecipient(final UUID remitente, final UUID destinatario) {
            return repository.existsByRemitenteAndDestinatario(remitente, destinatario);
        }

        private static MessageRequest toDomain(final RequestEntity e) {
            return new MessageRequest(e.getId(), e.getRemitente(), e.getDestinatario(), e.getMensajeInicial(),
                    RequestStatus.valueOf(e.getEstado().toUpperCase(Locale.ROOT)), e.getCreadoEn(),
                    e.getRespondidoEn());
        }
    }
}
