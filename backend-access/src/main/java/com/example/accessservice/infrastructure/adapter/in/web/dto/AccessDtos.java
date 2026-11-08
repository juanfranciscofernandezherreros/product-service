package com.example.accessservice.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

/**
 * Web DTOs for the access API.
 */
public final class AccessDtos {

    private AccessDtos() {
    }

    /**
     * Role payload/response.
     *
     * @param codigo      code
     * @param descripcion description
     */
    public record RoleDto(@NotBlank String codigo, @NotBlank String descripcion) {
    }

    /**
     * Permission payload/response.
     *
     * @param codigo      code
     * @param descripcion description
     */
    public record PermissionDto(@NotBlank String codigo, @NotBlank String descripcion) {
    }

    /**
     * Request to add a permission to a role.
     *
     * @param permiso permission code
     */
    public record AddPermissionRequest(@NotBlank String permiso) {
    }

    /**
     * Request to assign a role to a user.
     *
     * @param rol         role code
     * @param asignadoPor assigning user id (optional)
     */
    public record AssignRoleRequest(@NotBlank String rol, UUID asignadoPor) {
    }

    /**
     * User-role response.
     *
     * @param usuarioId   user id
     * @param rol         role code
     * @param asignadoPor assigning user id
     * @param creadoEn    creation instant
     */
    public record UserRoleResponse(UUID usuarioId, String rol, UUID asignadoPor, Instant creadoEn) {
    }

    /**
     * Error payload.
     *
     * @param code    code
     * @param message message
     */
    public record ErrorResponse(@NotNull String code, String message) {
    }
}
