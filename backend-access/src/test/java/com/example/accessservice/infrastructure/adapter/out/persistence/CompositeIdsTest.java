package com.example.accessservice.infrastructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the composite id classes.
 */
class CompositeIdsTest {

    @Test
    void rolePermissionIdEqualityAndAccessors() {
        final RolePermissionId a = new RolePermissionId("ADMIN", "READ");
        final RolePermissionId b = new RolePermissionId("ADMIN", "READ");
        final RolePermissionId c = new RolePermissionId("ADMIN", "WRITE");

        assertThat(a).isEqualTo(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(c).isNotEqualTo(null).isNotEqualTo("x");
        assertThat(a.getRol()).isEqualTo("ADMIN");
        assertThat(a.getPermiso()).isEqualTo("READ");
        assertThat(new RolePermissionId().getRol()).isNull();
    }

    @Test
    void userRoleIdEqualityAndAccessors() {
        final UUID user = UUID.randomUUID();
        final UserRoleId a = new UserRoleId(user, "ADMIN");
        final UserRoleId b = new UserRoleId(user, "ADMIN");
        final UserRoleId c = new UserRoleId(UUID.randomUUID(), "ADMIN");

        assertThat(a).isEqualTo(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(c).isNotEqualTo(null).isNotEqualTo("x");
        assertThat(a.getUsuarioId()).isEqualTo(user);
        assertThat(a.getRol()).isEqualTo("ADMIN");
        assertThat(new UserRoleId().getRol()).isNull();
    }
}
