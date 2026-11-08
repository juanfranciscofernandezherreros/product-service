package com.example.accessservice.infrastructure.adapter.in.web;

import com.example.accessservice.application.port.in.AccessUseCases;
import com.example.accessservice.domain.model.Role;
import com.example.accessservice.infrastructure.adapter.in.web.dto.AccessDtos.AddPermissionRequest;
import com.example.accessservice.infrastructure.adapter.in.web.dto.AccessDtos.RoleDto;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
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
 * REST adapter for roles under {@code /v1/access/roles}.
 */
@RestController
@RequestMapping("/v1/access/roles")
public class RoleController {

    private final AccessUseCases useCases;

    /**
     * Creates the controller.
     *
     * @param useCases access use cases
     */
    public RoleController(final AccessUseCases useCases) {
        this.useCases = useCases;
    }

    /**
     * Lists roles.
     *
     * @return 200 with roles
     */
    @GetMapping
    public List<RoleDto> list() {
        return useCases.listRoles().stream()
                .map(r -> new RoleDto(r.getCodigo(), r.getDescripcion()))
                .collect(Collectors.toList());
    }

    /**
     * Creates a role.
     *
     * @param request payload
     * @return 201 with the role
     */
    @PostMapping
    public ResponseEntity<RoleDto> create(@Valid @RequestBody final RoleDto request) {
        final Role role = useCases.createRole(new Role(request.codigo(), request.descripcion()));
        return ResponseEntity.created(URI.create("/v1/access/roles/" + role.getCodigo()))
                .body(new RoleDto(role.getCodigo(), role.getDescripcion()));
    }

    /**
     * Gets a role.
     *
     * @param codigo role code
     * @return 200 with the role
     */
    @GetMapping("/{codigo}")
    public RoleDto get(@PathVariable final String codigo) {
        final Role role = useCases.getRole(codigo);
        return new RoleDto(role.getCodigo(), role.getDescripcion());
    }

    /**
     * Deletes a role.
     *
     * @param codigo role code
     */
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final String codigo) {
        useCases.deleteRole(codigo);
    }

    /**
     * Lists a role's permissions.
     *
     * @param codigo role code
     * @return 200 with permission codes
     */
    @GetMapping("/{codigo}/permissions")
    public List<String> listPermissions(@PathVariable final String codigo) {
        return useCases.listRolePermissions(codigo);
    }

    /**
     * Assigns a permission to a role.
     *
     * @param codigo  role code
     * @param request payload
     * @return 201 Created
     */
    @PostMapping("/{codigo}/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPermission(@PathVariable final String codigo,
                              @Valid @RequestBody final AddPermissionRequest request) {
        useCases.addRolePermission(codigo, request.permiso());
    }
}
