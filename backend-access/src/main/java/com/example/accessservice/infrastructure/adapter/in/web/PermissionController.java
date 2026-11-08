package com.example.accessservice.infrastructure.adapter.in.web;

import com.example.accessservice.application.port.in.AccessUseCases;
import com.example.accessservice.domain.model.Permission;
import com.example.accessservice.infrastructure.adapter.in.web.dto.AccessDtos.PermissionDto;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter for permissions under {@code /v1/access/permissions}.
 */
@RestController
@RequestMapping("/v1/access/permissions")
public class PermissionController {

    private final AccessUseCases useCases;

    /**
     * Creates the controller.
     *
     * @param useCases access use cases
     */
    public PermissionController(final AccessUseCases useCases) {
        this.useCases = useCases;
    }

    /**
     * Lists permissions.
     *
     * @return 200 with permissions
     */
    @GetMapping
    public List<PermissionDto> list() {
        return useCases.listPermissions().stream()
                .map(p -> new PermissionDto(p.getCodigo(), p.getDescripcion()))
                .collect(Collectors.toList());
    }

    /**
     * Creates a permission.
     *
     * @param request payload
     * @return 201 with the permission
     */
    @PostMapping
    public ResponseEntity<PermissionDto> create(@Valid @RequestBody final PermissionDto request) {
        final Permission permission = useCases.createPermission(
                new Permission(request.codigo(), request.descripcion()));
        return ResponseEntity.created(URI.create("/v1/access/permissions/" + permission.getCodigo()))
                .body(new PermissionDto(permission.getCodigo(), permission.getDescripcion()));
    }
}
