package com.example.socialservice.application.port.in;

import com.example.socialservice.application.port.in.SocialCommands.CreateProfileCommand;
import com.example.socialservice.application.port.in.SocialCommands.CreateRequestCommand;
import com.example.socialservice.application.port.in.SocialCommands.SendMessageCommand;
import com.example.socialservice.application.port.in.SocialCommands.UpdateProfileCommand;
import com.example.socialservice.domain.model.Message;
import com.example.socialservice.domain.model.MessageRequest;
import com.example.socialservice.domain.model.PublicProfile;
import com.example.socialservice.domain.model.RequestStatus;
import java.util.List;
import java.util.UUID;

/**
 * Inbound port exposing the social use cases.
 */
public interface SocialUseCases {

    /** @param command attributes @return created profile */
    PublicProfile createProfile(CreateProfileCommand command);

    /** @param userId user id @return the profile */
    PublicProfile getProfile(UUID userId);

    /** @param userId user id @param command attributes @return the updated profile */
    PublicProfile updateProfile(UUID userId, UpdateProfileCommand command);

    /** @param seguidor follower @return followed ids */
    List<UUID> listFollowing(UUID seguidor);

    /** @param seguidor follower @param seguido followed */
    void follow(UUID seguidor, UUID seguido);

    /** @param seguidor follower @param seguido followed */
    void unfollow(UUID seguidor, UUID seguido);

    /** @param command attributes @return the message */
    Message sendMessage(SendMessageCommand command);

    /** @param command attributes @return the request */
    MessageRequest createRequest(CreateRequestCommand command);

    /** @param id request id @param status new status @return the updated request */
    MessageRequest respondRequest(long id, RequestStatus status);
}
