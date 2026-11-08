package com.example.socialservice.infrastructure.adapter.in.web;

import com.example.socialservice.application.port.in.SocialCommands.CreateProfileCommand;
import com.example.socialservice.application.port.in.SocialCommands.UpdateProfileCommand;
import com.example.socialservice.application.port.in.SocialUseCases;
import com.example.socialservice.infrastructure.adapter.in.web.dto.SocialDtos.ProfileCreateRequest;
import com.example.socialservice.infrastructure.adapter.in.web.dto.SocialDtos.ProfileResponse;
import com.example.socialservice.infrastructure.adapter.in.web.dto.SocialDtos.ProfileUpdateRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter for public profiles under {@code /v1/social/public-profiles}.
 */
@RestController
@RequestMapping("/v1/social/public-profiles")
public class ProfileController {

    private final SocialUseCases useCases;

    /**
     * Creates the controller.
     *
     * @param useCases social use cases
     */
    public ProfileController(final SocialUseCases useCases) {
        this.useCases = useCases;
    }

    /**
     * Creates a public profile.
     *
     * @param request payload
     * @return 201 with the profile
     */
    @PostMapping
    public ResponseEntity<ProfileResponse> create(@Valid @RequestBody final ProfileCreateRequest request) {
        final ProfileResponse response = SocialWebMapper.toResponse(useCases.createProfile(
                new CreateProfileCommand(request.userId(), request.alias(), request.biografia(),
                        request.avatarEmoji())));
        return ResponseEntity.created(URI.create("/v1/social/public-profiles/" + response.userId()))
                .body(response);
    }

    /**
     * Gets a public profile.
     *
     * @param userId user id
     * @return 200 with the profile
     */
    @GetMapping("/{userId}")
    public ProfileResponse get(@PathVariable final UUID userId) {
        return SocialWebMapper.toResponse(useCases.getProfile(userId));
    }

    /**
     * Updates a public profile.
     *
     * @param userId  user id
     * @param request payload
     * @return 200 with the profile
     */
    @PutMapping("/{userId}")
    public ProfileResponse update(@PathVariable final UUID userId,
                                  @Valid @RequestBody final ProfileUpdateRequest request) {
        return SocialWebMapper.toResponse(useCases.updateProfile(userId,
                new UpdateProfileCommand(request.biografia(), request.avatarEmoji())));
    }
}
