package com.example.studyplanservice.infrastructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the composite id classes.
 */
class CompositeIdsTest {

    @Test
    void sessionId() {
        final UUID u = UUID.randomUUID();
        final LocalDate d = LocalDate.now();
        final SessionId a = new SessionId(u, d, "unidad");
        final SessionId b = new SessionId(u, d, "unidad");
        assertThat(a).isEqualTo(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(new SessionId(u, d, "repaso")).isNotEqualTo(null).isNotEqualTo("x");
        assertThat(a.getUserId()).isEqualTo(u);
        assertThat(a.getDia()).isEqualTo(d);
        assertThat(a.getTarea()).isEqualTo("unidad");
        assertThat(new SessionId().getUserId()).isNull();
    }

    @Test
    void reportId() {
        final UUID u = UUID.randomUUID();
        final LocalDate d = LocalDate.now();
        final ReportId a = new ReportId(u, d);
        final ReportId b = new ReportId(u, d);
        assertThat(a).isEqualTo(a).isEqualTo(b).hasSameHashCodeAs(b);
        assertThat(a).isNotEqualTo(new ReportId(UUID.randomUUID(), d)).isNotEqualTo(null).isNotEqualTo("x");
        assertThat(a.getUserId()).isEqualTo(u);
        assertThat(a.getSemanaInicio()).isEqualTo(d);
        assertThat(new ReportId().getUserId()).isNull();
    }
}
