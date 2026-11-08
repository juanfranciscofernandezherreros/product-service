package com.example.examsservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

/** JPA entity for B2 diagnostics. */
@Entity
@Table(name = "diagnosticos_b2")
public class DiagnosticEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "aciertos", nullable = false)
    private int aciertos;

    @Column(name = "total", nullable = false)
    private int total;

    @Column(name = "desglose", nullable = false, length = 8000)
    private String desglose;

    @Column(name = "creado_en", nullable = false)
    private Instant creadoEn;

    /** Default constructor required by JPA. */
    protected DiagnosticEntity() {
    }

    /**
     * Creates an entity.
     *
     * @param id        id
     * @param usuarioId user id
     * @param aciertos  correct count
     * @param total     total
     * @param desglose  breakdown JSON
     * @param creadoEn  instant
     */
    public DiagnosticEntity(final Long id, final UUID usuarioId, final int aciertos, final int total,
                            final String desglose, final Instant creadoEn) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.aciertos = aciertos;
        this.total = total;
        this.desglose = desglose;
        this.creadoEn = creadoEn;
    }

    /** @return id */
    public Long getId() {
        return id;
    }

    /** @return user id */
    public UUID getUsuarioId() {
        return usuarioId;
    }

    /** @return correct count */
    public int getAciertos() {
        return aciertos;
    }

    /** @return total */
    public int getTotal() {
        return total;
    }

    /** @return breakdown JSON */
    public String getDesglose() {
        return desglose;
    }

    /** @return instant */
    public Instant getCreadoEn() {
        return creadoEn;
    }
}
