package com.example.accessservice.infrastructure.adapter.in.web;

import com.example.accessservice.application.port.in.AccessUseCases;
import com.example.accessservice.domain.model.UserRole;
import com.example.accessservice.infrastructure.adapter.in.web.dto.AccessDtos.AssignRoleRequest;
import com.example.accessservice.infrastructure.adapter.in.web.dto.AccessDtos.UserRoleResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter for user roles under {@code /v1/access/users/{userId}/roles}.
 */
@RestController
@RequestMapping("/v1/access/users/{userId}/roles")
public class UserRoleController {

    private final AccessUseCases useCases;

    /**
     * Creates the controller.
     *
     * @param useCases access use cases
     */
    public UserRoleController(final AccessUseCases useCases) {
        this.useCases = useCases;
    }

    private UserRoleResponse toResponse(final UserRole ur) {
        return new UserRoleResponse(ur.getUsuarioId(), ur.getRol(), ur.getAsignadoPor(), ur.getCreadoEn());
    }

    /**
     * Lists a user's roles.
     *
     * @param userId user id
     * @return 200 with assignments
     */
    @GetMapping
    public List<UserRoleResponse> list(@PathVariable final UUID userId) {
        return useCases.listUserRoles(userId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    /**
     * Assigns a role to a user.
     *
     * @param userId  user id
     * @param request payload
     * @return 201 with the assignment
     */
    @PostMapping
    public ResponseEntity<UserRoleResponse> assign(@PathVariable final UUID userId,
                                                   @Valid @RequestBody final AssignRoleRequest request) {
        final UserRole ur = useCases.assignUserRole(userId, request.rol(), request.asignadoPor());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(ur));
    }
}
