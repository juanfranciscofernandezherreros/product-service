package com.example.socialservice.infrastructure.adapter.in.web;

import com.example.socialservice.application.port.in.SocialUseCases;
import com.example.socialservice.infrastructure.adapter.in.web.dto.SocialDtos.FollowRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter for follows under {@code /v1/social/users/{userId}/following}.
 */
@RestController
@RequestMapping("/v1/social/users/{userId}/following")
public class FollowController {

    private final SocialUseCases useCases;

    /**
     * Creates the controller.
     *
     * @param useCases social use cases
     */
    public FollowController(final SocialUseCases useCases) {
        this.useCases = useCases;
    }

    /**
     * Lists followed users.
     *
     * @param userId follower id
     * @return 200 with followed ids
     */
    @GetMapping
    public List<UUID> list(@PathVariable final UUID userId) {
        return useCases.listFollowing(userId);
    }

    /**
     * Follows a user.
     *
     * @param userId  follower id
     * @param request payload
     * @return 201 Created
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void follow(@PathVariable final UUID userId, @Valid @RequestBody final FollowRequest request) {
        useCases.follow(userId, request.seguido());
    }

    /**
     * Unfollows a user.
     *
     * @param userId  follower id
     * @param seguido followed id
     * @return 204 No Content
     */
    @DeleteMapping("/{seguido}")
    public ResponseEntity<Void> unfollow(@PathVariable final UUID userId, @PathVariable final UUID seguido) {
        useCases.unfollow(userId, seguido);
        return ResponseEntity.noContent().build();
    }
}
