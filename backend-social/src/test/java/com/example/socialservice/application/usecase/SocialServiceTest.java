package com.example.socialservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.socialservice.application.port.in.SocialCommands.CreateProfileCommand;
import com.example.socialservice.application.port.in.SocialCommands.CreateRequestCommand;
import com.example.socialservice.application.port.in.SocialCommands.SendMessageCommand;
import com.example.socialservice.application.port.in.SocialCommands.UpdateProfileCommand;
import com.example.socialservice.application.port.out.SocialOutPorts.FollowPort;
import com.example.socialservice.application.port.out.SocialOutPorts.MessagePort;
import com.example.socialservice.application.port.out.SocialOutPorts.ProfilePort;
import com.example.socialservice.application.port.out.SocialOutPorts.RequestPort;
import com.example.socialservice.domain.exception.DuplicateResourceException;
import com.example.socialservice.domain.exception.ResourceNotFoundException;
import com.example.socialservice.domain.model.AvatarEmoji;
import com.example.socialservice.domain.model.Follow;
import com.example.socialservice.domain.model.Message;
import com.example.socialservice.domain.model.MessageRequest;
import com.example.socialservice.domain.model.PublicProfile;
import com.example.socialservice.domain.model.RequestStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link SocialService}.
 */
@ExtendWith(MockitoExtension.class)
class SocialServiceTest {

    @Mock
    private ProfilePort profilePort;
    @Mock
    private FollowPort followPort;
    @Mock
    private MessagePort messagePort;
    @Mock
    private RequestPort requestPort;

    private SocialService service;
    private final UUID a = UUID.randomUUID();
    private final UUID b = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        service = new SocialService(profilePort, followPort, messagePort, requestPort);
    }

    @Test
    void createProfileOk() {
        when(profilePort.existsByUser(a)).thenReturn(false);
        when(profilePort.existsByAlias("alias")).thenReturn(false);
        when(profilePort.save(any(PublicProfile.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.createProfile(new CreateProfileCommand(a, "alias", "b", AvatarEmoji.BOOK))
                .getAlias()).isEqualTo("alias");
    }

    @Test
    void createProfileDuplicateUserOrAlias() {
        when(profilePort.existsByUser(a)).thenReturn(true);
        assertThatThrownBy(() -> service.createProfile(new CreateProfileCommand(a, "alias", null, null)))
                .isInstanceOf(DuplicateResourceException.class);
        when(profilePort.existsByUser(a)).thenReturn(false);
        when(profilePort.existsByAlias("alias")).thenReturn(true);
        assertThatThrownBy(() -> service.createProfile(new CreateProfileCommand(a, "alias", null, null)))
                .isInstanceOf(DuplicateResourceException.class);
        verify(profilePort, never()).save(any(PublicProfile.class));
    }

    @Test
    void getAndUpdateProfile() {
        final PublicProfile p = PublicProfile.create(a, "alias", "bio", AvatarEmoji.BOOK);
        when(profilePort.findByUser(a)).thenReturn(Optional.of(p));
        assertThat(service.getProfile(a).getAlias()).isEqualTo("alias");
        when(profilePort.save(any(PublicProfile.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.updateProfile(a, new UpdateProfileCommand("new", AvatarEmoji.STAR))
                .getBiografia()).isEqualTo("new");
    }

    @Test
    void getAndUpdateProfileMissing() {
        when(profilePort.findByUser(a)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getProfile(a)).isInstanceOf(ResourceNotFoundException.class);
        assertThatThrownBy(() -> service.updateProfile(a, new UpdateProfileCommand(null, null)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void followUnfollow() {
        when(followPort.findFollowing(a)).thenReturn(List.of(b));
        assertThat(service.listFollowing(a)).containsExactly(b);
        when(followPort.exists(a, b)).thenReturn(false);
        when(followPort.save(any(Follow.class))).thenAnswer(i -> i.getArgument(0));
        service.follow(a, b);
        verify(followPort).save(any(Follow.class));
        when(followPort.exists(a, b)).thenReturn(true);
        assertThatThrownBy(() -> service.follow(a, b)).isInstanceOf(DuplicateResourceException.class);
        service.unfollow(a, b);
        verify(followPort).delete(a, b);
        when(followPort.exists(a, b)).thenReturn(false);
        assertThatThrownBy(() -> service.unfollow(a, b)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void sendMessage() {
        when(messagePort.save(any(Message.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.sendMessage(new SendMessageCommand(a, b, "hi")).getContenido()).isEqualTo("hi");
    }

    @Test
    void requestCreateAndRespond() {
        when(requestPort.existsBySenderRecipient(a, b)).thenReturn(false);
        when(requestPort.save(any(MessageRequest.class))).thenAnswer(i -> i.getArgument(0));
        assertThat(service.createRequest(new CreateRequestCommand(a, b, "hi")).getEstado())
                .isEqualTo(RequestStatus.PENDIENTE);
        when(requestPort.existsBySenderRecipient(a, b)).thenReturn(true);
        assertThatThrownBy(() -> service.createRequest(new CreateRequestCommand(a, b, "hi")))
                .isInstanceOf(DuplicateResourceException.class);
        when(requestPort.findById(5L)).thenReturn(Optional.of(
                new MessageRequest(5L, a, b, "hi", RequestStatus.PENDIENTE, null, null)));
        assertThat(service.respondRequest(5L, RequestStatus.ACEPTADA).getEstado())
                .isEqualTo(RequestStatus.ACEPTADA);
        when(requestPort.findById(9L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.respondRequest(9L, RequestStatus.ACEPTADA))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
