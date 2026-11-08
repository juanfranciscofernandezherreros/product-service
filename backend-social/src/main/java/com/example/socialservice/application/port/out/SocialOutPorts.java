package com.example.socialservice.application.port.out;

import com.example.socialservice.domain.model.Follow;
import com.example.socialservice.domain.model.Message;
import com.example.socialservice.domain.model.MessageRequest;
import com.example.socialservice.domain.model.PublicProfile;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Outbound ports for the social domain.
 */
public final class SocialOutPorts {

    private SocialOutPorts() {
    }

    /** Public profile port. */
    public interface ProfilePort {
        /** @param profile profile @return stored profile */
        PublicProfile save(PublicProfile profile);

        /** @param userId user id @return the profile if present */
        Optional<PublicProfile> findByUser(UUID userId);

        /** @param userId user id @return {@code true} if present */
        boolean existsByUser(UUID userId);

        /** @param alias alias @return {@code true} if taken */
        boolean existsByAlias(String alias);
    }

    /** Follow port. */
    public interface FollowPort {
        /** @param follow follow @return stored follow */
        Follow save(Follow follow);

        /** @param seguidor follower id @return followed ids */
        List<UUID> findFollowing(UUID seguidor);

        /** @param seguidor follower @param seguido followed @return {@code true} if present */
        boolean exists(UUID seguidor, UUID seguido);

        /** @param seguidor follower @param seguido followed */
        void delete(UUID seguidor, UUID seguido);
    }

    /** Message port. */
    public interface MessagePort {
        /** @param message message @return stored message */
        Message save(Message message);
    }

    /** Message request port. */
    public interface RequestPort {
        /** @param request request @return stored request */
        MessageRequest save(MessageRequest request);

        /** @param id id @return the request if present */
        Optional<MessageRequest> findById(long id);

        /** @param remitente sender @param destinatario recipient @return {@code true} if present */
        boolean existsBySenderRecipient(UUID remitente, UUID destinatario);
    }
}
