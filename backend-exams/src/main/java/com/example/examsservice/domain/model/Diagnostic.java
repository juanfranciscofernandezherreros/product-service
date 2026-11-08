package com.example.examsservice.domain.model;

import com.example.examsservice.domain.exception.InvalidExamException;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable domain representation of a B2 diagnostic.
 */
public final class Diagnostic {

    private static final int MAX = 40;

    private final Long id;
    private final UUID usuarioId;
    private final int aciertos;
    private final int total;
    private final String desglose;
    private final Instant creadoEn;

    /**
     * Creates a validated diagnostic.
     *
     * @param id        identifier (nullable before persistence)
     * @param usuarioId user id
     * @param aciertos  correct count (0..40)
     * @param total     total (1..40)
     * @param desglose  breakdown JSON
     * @param creadoEn  instant
     */
    public Diagnostic(final Long id, final UUID usuarioId, final int aciertos, final int total,
                      final String desglose, final Instant creadoEn) {
        if (usuarioId == null) {
            throw new InvalidExamException("usuarioId is required");
        }
        if (aciertos < 0 || aciertos > MAX) {
            throw new InvalidExamException("aciertos must be between 0 and 40");
        }
        if (total < 1 || total > MAX) {
            throw new InvalidExamException("total must be between 1 and 40");
        }
        this.id = id;
        this.usuarioId = usuarioId;
        this.aciertos = aciertos;
        this.total = total;
        this.desglose = desglose == null || desglose.trim().isEmpty() ? "{}" : desglose;
        this.creadoEn = creadoEn == null ? Instant.now() : creadoEn;
    }

    /**
     * Factory for a new diagnostic.
     *
     * @param usuarioId user id
     * @param aciertos  correct count
     * @param total     total
     * @param desglose  breakdown JSON
     * @return a new diagnostic
     */
    public static Diagnostic record(final UUID usuarioId, final int aciertos, final int total,
                                    final String desglose) {
        return new Diagnostic(null, usuarioId, aciertos, total, desglose, Instant.now());
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
