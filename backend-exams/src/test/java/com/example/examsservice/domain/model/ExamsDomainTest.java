package com.example.examsservice.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.examsservice.domain.exception.InvalidExamException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the exams domain models.
 */
class ExamsDomainTest {

    private final UUID user = UUID.randomUUID();

    @Test
    void examRequiresCodeTitleAndDefaultsLevel() {
        assertThat(Exam.create("EX", "T", null, null, null, false, null).getNivel()).isEqualTo("B1-B2");
        assertThatThrownBy(() -> Exam.create(" ", "T", null, null, null, false, null))
                .isInstanceOf(InvalidExamException.class);
    }

    @Test
    void questionValidation() {
        assertThat(new Question(null, 1L, 1, "p", List.of("a", "b"), null).getOrden()).isEqualTo(1);
        assertThatThrownBy(() -> new Question(null, 1L, 0, "p", List.of("a", "b"), null))
                .isInstanceOf(InvalidExamException.class);
        assertThatThrownBy(() -> new Question(null, 1L, 1, "p", List.of("a"), null))
                .isInstanceOf(InvalidExamException.class);
    }

    @Test
    void resultNoteRange() {
        assertThat(Result.record(user, 1L, "{}", 5, 10, new BigDecimal("50")).getNota())
                .isEqualByComparingTo("50");
        assertThatThrownBy(() -> Result.record(user, 1L, "{}", 5, 10, new BigDecimal("101")))
                .isInstanceOf(InvalidExamException.class);
        assertThatThrownBy(() -> Result.record(user, 1L, "{}", 5, 0, new BigDecimal("50")))
                .isInstanceOf(InvalidExamException.class);
    }

    @Test
    void diagnosticAndSimulationRanges() {
        assertThat(Diagnostic.record(user, 40, 40, null).getDesglose()).isEqualTo("{}");
        assertThatThrownBy(() -> Diagnostic.record(user, 41, 40, "{}"))
                .isInstanceOf(InvalidExamException.class);
        assertThat(Simulation.record(user, Modalidad.LISTENING, 0, 1, null, 0).getRespuestas()).isEqualTo("{}");
        assertThatThrownBy(() -> Simulation.record(user, Modalidad.LISTENING, 0, 1, "{}", 7201))
                .isInstanceOf(InvalidExamException.class);
    }
}
