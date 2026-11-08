package com.example.accessservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity for permissions.
 */
@Entity
@Table(name = "permisos")
public class PermissionJpaEntity {

    @Id
    @Column(name = "codigo", nullable = false, updatable = false)
    private String codigo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    /** Default constructor required by JPA. */
    protected PermissionJpaEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param codigo      code
     * @param descripcion description
     */
    public PermissionJpaEntity(final String codigo, final String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    /** @return the code */
    public String getCodigo() {
        return codigo;
    }

    /** @return the description */
    public String getDescripcion() {
        return descripcion;
    }
}
