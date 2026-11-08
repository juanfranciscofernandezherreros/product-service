package com.example.examsservice.domain.model;

import com.example.examsservice.domain.exception.InvalidExamException;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable domain representation of a B2 simulation.
 */
public final class Simulation {

    private static final int MAX = 40;
    private static final int MAX_DURATION = 7200;

    private final Long id;
    private final UUID usuarioId;
    private final Modalidad modalidad;
    private final int aciertos;
    private final int total;
    private final String respuestas;
    private final int duracionSegundos;
    private final Instant creadoEn;

    /**
     * Creates a validated simulation.
     *
     * @param id               identifier (nullable before persistence)
     * @param usuarioId        user id
     * @param modalidad        modality
     * @param aciertos         correct count (0..40)
     * @param total            total (1..40)
     * @param respuestas       answers JSON
     * @param duracionSegundos duration (0..7200)
     * @param creadoEn         instant
     */
    public Simulation(final Long id, final UUID usuarioId, final Modalidad modalidad, final int aciertos,
                      final int total, final String respuestas, final int duracionSegundos,
                      final Instant creadoEn) {
        if (usuarioId == null) {
            throw new InvalidExamException("usuarioId is required");
        }
        if (modalidad == null) {
            throw new InvalidExamException("modalidad is required");
        }
        if (aciertos < 0 || aciertos > MAX) {
            throw new InvalidExamException("aciertos must be between 0 and 40");
        }
        if (total < 1 || total > MAX) {
            throw new InvalidExamException("total must be between 1 and 40");
        }
        if (duracionSegundos < 0 || duracionSegundos > MAX_DURATION) {
            throw new InvalidExamException("duracionSegundos must be between 0 and 7200");
        }
        this.id = id;
        this.usuarioId = usuarioId;
        this.modalidad = modalidad;
        this.aciertos = aciertos;
        this.total = total;
        this.respuestas = respuestas == null || respuestas.trim().isEmpty() ? "{}" : respuestas;
        this.duracionSegundos = duracionSegundos;
        this.creadoEn = creadoEn == null ? Instant.now() : creadoEn;
    }

    /**
     * Factory for a new simulation.
     *
     * @param usuarioId        user id
     * @param modalidad        modality
     * @param aciertos         correct count
     * @param total            total
     * @param respuestas       answers JSON
     * @param duracionSegundos duration
     * @return a new simulation
     */
    public static Simulation record(final UUID usuarioId, final Modalidad modalidad, final int aciertos,
                                    final int total, final String respuestas, final int duracionSegundos) {
        return new Simulation(null, usuarioId, modalidad, aciertos, total, respuestas, duracionSegundos,
                Instant.now());
    }

    /** @return id */
    public Long getId() {
        return id;
    }

    /** @return user id */
    public UUID getUsuarioId() {
        return usuarioId;
    }

    /** @return modality */
    public Modalidad getModalidad() {
        return modalidad;
    }

    /** @return correct count */
    public int getAciertos() {
        return aciertos;
    }

    /** @return total */
    public int getTotal() {
        return total;
    }

    /** @return answers JSON */
    public String getRespuestas() {
        return respuestas;
    }

    /** @return duration */
    public int getDuracionSegundos() {
        return duracionSegundos;
    }

    /** @return instant */
    public Instant getCreadoEn() {
        return creadoEn;
    }
}
