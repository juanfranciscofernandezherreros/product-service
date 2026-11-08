package com.example.socialservice.application.usecase;

import com.example.socialservice.application.port.in.SocialCommands.CreateProfileCommand;
import com.example.socialservice.application.port.in.SocialCommands.CreateRequestCommand;
import com.example.socialservice.application.port.in.SocialCommands.SendMessageCommand;
import com.example.socialservice.application.port.in.SocialCommands.UpdateProfileCommand;
import com.example.socialservice.application.port.in.SocialUseCases;
import com.example.socialservice.application.port.out.SocialOutPorts.FollowPort;
import com.example.socialservice.application.port.out.SocialOutPorts.MessagePort;
import com.example.socialservice.application.port.out.SocialOutPorts.ProfilePort;
import com.example.socialservice.application.port.out.SocialOutPorts.RequestPort;
import com.example.socialservice.domain.exception.DuplicateResourceException;
import com.example.socialservice.domain.exception.ResourceNotFoundException;
import com.example.socialservice.domain.model.Follow;
import com.example.socialservice.domain.model.Message;
import com.example.socialservice.domain.model.MessageRequest;
import com.example.socialservice.domain.model.PublicProfile;
import com.example.socialservice.domain.model.RequestStatus;
import java.util.List;
import java.util.UUID;

/**
 * Application service implementing the social use cases.
 */
public class SocialService implements SocialUseCases {

    private final ProfilePort profilePort;
    private final FollowPort followPort;
    private final MessagePort messagePort;
    private final RequestPort requestPort;

    /**
     * Creates the service.
     *
     * @param profilePort profile port
     * @param followPort  follow port
     * @param messagePort message port
     * @param requestPort request port
     */
    public SocialService(final ProfilePort profilePort, final FollowPort followPort,
                         final MessagePort messagePort, final RequestPort requestPort) {
        this.profilePort = profilePort;
        this.followPort = followPort;
        this.messagePort = messagePort;
        this.requestPort = requestPort;
    }

    @Override
    public PublicProfile createProfile(final CreateProfileCommand c) {
        if (profilePort.existsByUser(c.userId())) {
            throw new DuplicateResourceException("Profile already exists for user " + c.userId());
        }
        if (c.alias() != null && profilePort.existsByAlias(c.alias())) {
            throw new DuplicateResourceException("Alias already taken: " + c.alias());
        }
        return profilePort.save(PublicProfile.create(c.userId(), c.alias(), c.biografia(), c.avatarEmoji()));
    }

    @Override
    public PublicProfile getProfile(final UUID userId) {
        return profilePort.findByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", String.valueOf(userId)));
    }

    @Override
    public PublicProfile updateProfile(final UUID userId, final UpdateProfileCommand c) {
        final PublicProfile existing = profilePort.findByUser(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", String.valueOf(userId)));
        return profilePort.save(existing.withUpdate(c.biografia(), c.avatarEmoji()));
    }

    @Override
    public List<UUID> listFollowing(final UUID seguidor) {
        return followPort.findFollowing(seguidor);
    }

    @Override
    public void follow(final UUID seguidor, final UUID seguido) {
        if (followPort.exists(seguidor, seguido)) {
            throw new DuplicateResourceException("Already following");
        }
        followPort.save(new Follow(seguidor, seguido, null));
    }

    @Override
    public void unfollow(final UUID seguidor, final UUID seguido) {
        if (!followPort.exists(seguidor, seguido)) {
            throw new ResourceNotFoundException("Follow", seguidor + "->" + seguido);
        }
        followPort.delete(seguidor, seguido);
    }

    @Override
    public Message sendMessage(final SendMessageCommand c) {
        return messagePort.save(Message.send(c.remitente(), c.destinatario(), c.contenido()));
    }

    @Override
    public MessageRequest createRequest(final CreateRequestCommand c) {
        if (requestPort.existsBySenderRecipient(c.remitente(), c.destinatario())) {
            throw new DuplicateResourceException("Request already exists between users");
        }
        return requestPort.save(MessageRequest.create(c.remitente(), c.destinatario(), c.mensajeInicial()));
    }

    @Override
    public MessageRequest respondRequest(final long id, final RequestStatus status) {
        final MessageRequest existing = requestPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MessageRequest", String.valueOf(id)));
        return requestPort.save(existing.respond(status));
    }
}
