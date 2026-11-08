package com.example.accessservice.domain.model;

import com.example.accessservice.domain.exception.InvalidAccessException;
import java.util.Objects;

/**
 * Immutable domain representation of a permission.
 */
public final class Permission {

    private final String codigo;
    private final String descripcion;

    /**
     * Creates a validated permission.
     *
     * @param codigo      permission code
     * @param descripcion description
     */
    public Permission(final String codigo, final String descripcion) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new InvalidAccessException("codigo is required");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new InvalidAccessException("descripcion is required");
        }
        this.codigo = codigo.trim();
        this.descripcion = descripcion.trim();
    }

    /** @return the code */
    public String getCodigo() {
        return codigo;
    }

    /** @return the description */
    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof Permission && Objects.equals(codigo, ((Permission) other).codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
