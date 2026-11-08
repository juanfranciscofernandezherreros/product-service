package com.example.accessservice.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.accessservice.domain.exception.InvalidAccessException;
import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the access domain models.
 */
class RoleTest {

    @Test
    void createsValidRoleAndPermission() {
        assertThat(new Role("ADMIN", "Administrador").getCodigo()).isEqualTo("ADMIN");
        assertThat(new Permission("READ", "Lectura").getDescripcion()).isEqualTo("Lectura");
    }

    @Test
    void rejectsBlankCode() {
        assertThatThrownBy(() -> new Role(" ", "d")).isInstanceOf(InvalidAccessException.class);
        assertThatThrownBy(() -> new Permission(" ", "d")).isInstanceOf(InvalidAccessException.class);
    }

    @Test
    void rejectsBlankDescription() {
        assertThatThrownBy(() -> new Role("ADMIN", " ")).isInstanceOf(InvalidAccessException.class);
    }

    @Test
    void rolesEqualByCode() {
        assertThat(new Role("ADMIN", "a")).isEqualTo(new Role("ADMIN", "b"));
        assertThat(new Permission("READ", "a")).isEqualTo(new Permission("READ", "b"));
    }

    @Test
    void userRoleValidation() {
        final UUID user = UUID.randomUUID();
        final UserRole ur = new UserRole(user, "ADMIN", null, Instant.now());
        assertThat(ur.getRol()).isEqualTo("ADMIN");
        assertThatThrownBy(() -> new UserRole(null, "ADMIN", null, null))
                .isInstanceOf(InvalidAccessException.class);
        assertThatThrownBy(() -> new UserRole(user, " ", null, null))
                .isInstanceOf(InvalidAccessException.class);
    }
}
