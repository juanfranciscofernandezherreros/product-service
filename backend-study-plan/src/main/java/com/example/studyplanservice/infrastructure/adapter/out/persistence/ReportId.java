package com.example.studyplanservice.infrastructure.adapter.out.persistence;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/** Composite id for {@link ReportEntity}. */
public class ReportId implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID userId;
    private LocalDate semanaInicio;

    /** Default constructor required by JPA. */
    public ReportId() {
    }

    /**
     * Creates the id.
     *
     * @param userId       user id
     * @param semanaInicio week start
     */
    public ReportId(final UUID userId, final LocalDate semanaInicio) {
        this.userId = userId;
        this.semanaInicio = semanaInicio;
    }

    /** @return user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return week start */
    public LocalDate getSemanaInicio() {
        return semanaInicio;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ReportId)) {
            return false;
        }
        final ReportId that = (ReportId) other;
        return Objects.equals(userId, that.userId) && Objects.equals(semanaInicio, that.semanaInicio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, semanaInicio);
    }
}
