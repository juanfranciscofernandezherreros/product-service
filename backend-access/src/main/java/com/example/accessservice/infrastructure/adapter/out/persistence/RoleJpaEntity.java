package com.example.accessservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity for roles.
 */
@Entity
@Table(name = "roles")
public class RoleJpaEntity {

    @Id
    @Column(name = "codigo", nullable = false, updatable = false)
    private String codigo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    /** Default constructor required by JPA. */
    protected RoleJpaEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param codigo      code
     * @param descripcion description
     */
    public RoleJpaEntity(final String codigo, final String descripcion) {
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
