package com.example.studyplanservice.infrastructure.adapter.out.persistence;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/** Composite id for {@link SessionEntity}. */
public class SessionId implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID userId;
    private LocalDate dia;
    private String tarea;

    /** Default constructor required by JPA. */
    public SessionId() {
    }

    /**
     * Creates the id.
     *
     * @param userId user id
     * @param dia    day
     * @param tarea  task
     */
    public SessionId(final UUID userId, final LocalDate dia, final String tarea) {
        this.userId = userId;
        this.dia = dia;
        this.tarea = tarea;
    }

    /** @return user id */
    public UUID getUserId() {
        return userId;
    }

    /** @return day */
    public LocalDate getDia() {
        return dia;
    }

    /** @return task */
    public String getTarea() {
        return tarea;
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SessionId)) {
            return false;
        }
        final SessionId that = (SessionId) other;
        return Objects.equals(userId, that.userId) && Objects.equals(dia, that.dia)
                && Objects.equals(tarea, that.tarea);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, dia, tarea);
    }
}
