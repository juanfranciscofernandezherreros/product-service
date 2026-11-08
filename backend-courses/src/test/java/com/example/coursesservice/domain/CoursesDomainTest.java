package com.example.coursesservice.domain;

import com.example.coursesservice.domain.exception.InvalidCourseException;
import com.example.coursesservice.domain.model.Activity;
import com.example.coursesservice.domain.model.ActivityAttempt;
import com.example.coursesservice.domain.model.ActivityQuestion;
import com.example.coursesservice.domain.model.Course;
import com.example.coursesservice.domain.model.Destreza;
import com.example.coursesservice.domain.model.QuestionType;
import com.example.coursesservice.domain.model.Unit;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CoursesDomainTest {

    @Test
    void courseRequiresName() {
        assertThatThrownBy(() -> Course.create("  ", "d", true))
                .isInstanceOf(InvalidCourseException.class);
    }

    @Test
    void courseUpdateKeepsActivoWhenNull() {
        final Course c = Course.create("Curso", "d", false).update("Nuevo", "d2", null);
        assertThat(c.activo()).isFalse();
        assertThat(c.nombre()).isEqualTo("Nuevo");
    }

    @Test
    void unitDefaultsOrderAndPublished() {
        final Unit u = Unit.create("slug", "T", null, "B2", null, null);
        assertThat(u.orden()).isEqualTo(100);
        assertThat(u.publicado()).isTrue();
    }

    @Test
    void activityRejectsBadSlug() {
        assertThatThrownBy(() -> Activity.create("A B", "T", null, Destreza.READING, null, null, null, null))
                .isInstanceOf(InvalidCourseException.class);
    }

    @Test
    void activityDefaults() {
        final Activity a = Activity.create("act-slug", "T", null, Destreza.WRITING, null, null, null, null);
        assertThat(a.orden()).isEqualTo(1);
        assertThat(a.publicado()).isFalse();
    }

    @Test
    void destrezaFromValueInvalid() {
        assertThatThrownBy(() -> Destreza.fromValue("dancing"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void questionRejectsNonPositiveOrder() {
        assertThatThrownBy(() -> ActivityQuestion.create(1L, 0, QuestionType.SELECCION, "e", null,
                null, null, null)).isInstanceOf(InvalidCourseException.class);
    }

    @Test
    void questionDefaultsConfiguration() {
        final ActivityQuestion q = ActivityQuestion.create(1L, 1, QuestionType.HUECOS, "e", null,
                null, null, null);
        assertThat(q.configuracion()).isEqualTo("{}");
    }

    @Test
    void questionRejectsMediaWithoutUrl() {
        assertThatThrownBy(() -> ActivityQuestion.create(1L, 1, QuestionType.SELECCION, "e", "{}",
                "audio", null, null)).isInstanceOf(InvalidCourseException.class);
    }

    @Test
    void questionRejectsUrlWithoutType() {
        assertThatThrownBy(() -> ActivityQuestion.create(1L, 1, QuestionType.SELECCION, "e", "{}",
                null, "https://x/a.mp3", null)).isInstanceOf(InvalidCourseException.class);
    }

    @Test
    void questionTypeFromValueInvalid() {
        assertThatThrownBy(() -> QuestionType.fromValue("ensayo"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void attemptRejectsNegativeCounters() {
        assertThatThrownBy(() -> ActivityAttempt.create(UUID.randomUUID(), 1L, "{}", "{}", -1, 0, 0))
                .isInstanceOf(InvalidCourseException.class);
    }

    @Test
    void attemptCreateGeneratesId() {
        final ActivityAttempt a = ActivityAttempt.create(UUID.randomUUID(), 1L, "{}", "{}", 1, 2, 0);
        assertThat(a.id()).isNotNull();
    }
}
