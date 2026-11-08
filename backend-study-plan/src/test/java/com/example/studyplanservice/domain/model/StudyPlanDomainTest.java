package com.example.studyplanservice.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.studyplanservice.domain.exception.InvalidStudyPlanException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the study-plan domain models.
 */
class StudyPlanDomainTest {

    private final UUID user = UUID.randomUUID();

    @Test
    void planValidAndInvalid() {
        assertThat(new StudyPlan(user, null, 30, List.of(1, 2), Prioridad.AUTO, null, false, null)
                .getMinutosDia()).isEqualTo(30);
        assertThatThrownBy(() -> new StudyPlan(user, null, 5, List.of(1), Prioridad.AUTO, null, false, null))
                .isInstanceOf(InvalidStudyPlanException.class);
        assertThatThrownBy(() -> new StudyPlan(user, null, 30, List.of(9), Prioridad.AUTO, null, false, null))
                .isInstanceOf(InvalidStudyPlanException.class);
        assertThatThrownBy(() -> new StudyPlan(user, null, 30, List.of(), Prioridad.AUTO, null, false, null))
                .isInstanceOf(InvalidStudyPlanException.class);
    }

    @Test
    void aiPlanValidAndInvalid() {
        assertThat(new AiPlan(user, 8, "{}", null).getSemanas()).isEqualTo(8);
        assertThatThrownBy(() -> new AiPlan(user, 7, "{}", null)).isInstanceOf(InvalidStudyPlanException.class);
        assertThatThrownBy(() -> new AiPlan(user, 8, " ", null)).isInstanceOf(InvalidStudyPlanException.class);
    }

    @Test
    void goalValidAndInvalid() {
        assertThat(new WeeklyGoal(user, GoalType.UNIDADES, 3, null).getCantidad()).isEqualTo(3);
        assertThatThrownBy(() -> new WeeklyGoal(user, GoalType.UNIDADES, 0, null))
                .isInstanceOf(InvalidStudyPlanException.class);
    }

    @Test
    void reportRejectsBadWeek() {
        assertThatThrownBy(() -> new WeeklyReport(user, LocalDate.of(2026, 1, 10),
                LocalDate.of(2026, 1, 1), "{}", null)).isInstanceOf(InvalidStudyPlanException.class);
    }

    @Test
    void sessionDefaults() {
        assertThat(new StudySession(user, null, Tarea.UNIDAD, null).getDia()).isNotNull();
    }
}
