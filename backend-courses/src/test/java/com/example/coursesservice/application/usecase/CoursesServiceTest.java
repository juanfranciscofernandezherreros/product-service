package com.example.coursesservice.application.usecase;

import com.example.coursesservice.application.port.in.CoursesCommands;
import com.example.coursesservice.application.port.out.CoursesOutPorts.ActivityAttemptPort;
import com.example.coursesservice.application.port.out.CoursesOutPorts.ActivityPort;
import com.example.coursesservice.application.port.out.CoursesOutPorts.ActivityQuestionPort;
import com.example.coursesservice.application.port.out.CoursesOutPorts.CoursePort;
import com.example.coursesservice.application.port.out.CoursesOutPorts.UnitPort;
import com.example.coursesservice.domain.exception.DuplicateResourceException;
import com.example.coursesservice.domain.exception.ResourceNotFoundException;
import com.example.coursesservice.domain.model.Activity;
import com.example.coursesservice.domain.model.Course;
import com.example.coursesservice.domain.model.Destreza;
import com.example.coursesservice.domain.model.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CoursesServiceTest {

    private CoursePort coursePort;
    private UnitPort unitPort;
    private ActivityPort activityPort;
    private ActivityQuestionPort questionPort;
    private ActivityAttemptPort attemptPort;
    private CoursesService service;

    @BeforeEach
    void setUp() {
        coursePort = mock(CoursePort.class);
        unitPort = mock(UnitPort.class);
        activityPort = mock(ActivityPort.class);
        questionPort = mock(ActivityQuestionPort.class);
        attemptPort = mock(ActivityAttemptPort.class);
        service = new CoursesService(coursePort, unitPort, activityPort, questionPort, attemptPort);
    }

    private Activity sampleActivity() {
        return new Activity(5L, "act-uno", "Actividad", "d", Destreza.READING, null, null, 1,
                true, OffsetDateTime.now());
    }

    @Test
    void createCourseActiveByDefault() {
        when(coursePort.save(any())).thenAnswer(i -> i.getArgument(0));
        final Course c = service.createCourse(new CoursesCommands.SaveCourse(null, "Curso", "d", null));
        assertThat(c.activo()).isTrue();
    }

    @Test
    void getCourseMissingThrows() {
        when(coursePort.findById(9L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getCourse(9L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listCoursesDelegates() {
        when(coursePort.findAll(true)).thenReturn(List.of());
        assertThat(service.listCourses(true)).isEmpty();
    }

    @Test
    void updateCourseChangesName() {
        when(coursePort.findById(1L)).thenReturn(Optional.of(
                new Course(1L, "Viejo", "d", true, OffsetDateTime.now())));
        when(coursePort.save(any())).thenAnswer(i -> i.getArgument(0));
        final Course c = service.updateCourse(new CoursesCommands.SaveCourse(1L, "Nuevo", "d", false));
        assertThat(c.nombre()).isEqualTo("Nuevo");
        assertThat(c.activo()).isFalse();
    }

    @Test
    void deleteCourseVerifiesExistence() {
        when(coursePort.findById(1L)).thenReturn(Optional.of(
                new Course(1L, "C", null, true, OffsetDateTime.now())));
        service.deleteCourse(1L);
        verify(coursePort).deleteById(1L);
    }

    @Test
    void createUnitDuplicateThrows() {
        when(unitPort.existsBySlug("u")).thenReturn(true);
        assertThatThrownBy(() -> service.createUnit(new CoursesCommands.CreateUnit(
                "u", "T", null, "B2", null, null))).isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void createUnitAndGetAndList() {
        when(unitPort.existsBySlug("u")).thenReturn(false);
        when(unitPort.save(any())).thenAnswer(i -> i.getArgument(0));
        when(unitPort.findById(1L)).thenReturn(Optional.of(new Unit(1L, "u", "T", null, "B2", 100, true)));
        when(unitPort.findAll("B2", true)).thenReturn(List.of());
        assertThat(service.createUnit(new CoursesCommands.CreateUnit("u", "T", null, "B2", null, null))
                .slug()).isEqualTo("u");
        assertThat(service.getUnit(1L).id()).isEqualTo(1L);
        assertThat(service.listUnits("B2", true)).isEmpty();
    }

    @Test
    void createActivityDuplicateThrows() {
        when(activityPort.existsBySlug("act-uno")).thenReturn(true);
        assertThatThrownBy(() -> service.createActivity(new CoursesCommands.CreateActivity(
                "act-uno", "T", null, "reading", null, null, null, null)))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void createActivityAndListCount() {
        when(activityPort.existsBySlug("act-uno")).thenReturn(false);
        when(activityPort.save(any())).thenAnswer(i -> i.getArgument(0));
        when(activityPort.findAll(Destreza.READING, true, 0, 10)).thenReturn(List.of());
        when(activityPort.count(Destreza.READING, true)).thenReturn(3L);
        assertThat(service.createActivity(new CoursesCommands.CreateActivity(
                "act-uno", "T", "d", "reading", null, null, null, true)).slug()).isEqualTo("act-uno");
        assertThat(service.listActivities(Destreza.READING, true, 0, 10)).isEmpty();
        assertThat(service.countActivities(Destreza.READING, true)).isEqualTo(3L);
    }

    @Test
    void addQuestionRequiresActivity() {
        when(activityPort.findById(5L)).thenReturn(Optional.of(sampleActivity()));
        when(questionPort.existsByActivityAndOrden(5L, 1)).thenReturn(false);
        when(questionPort.save(any())).thenAnswer(i -> i.getArgument(0));
        assertThat(service.addQuestion(new CoursesCommands.CreateQuestion(
                5L, 1, "seleccion", "Pregunta?", null, null, null, null)).orden()).isEqualTo(1);
    }

    @Test
    void addQuestionDuplicateOrdenThrows() {
        when(activityPort.findById(5L)).thenReturn(Optional.of(sampleActivity()));
        when(questionPort.existsByActivityAndOrden(5L, 1)).thenReturn(true);
        assertThatThrownBy(() -> service.addQuestion(new CoursesCommands.CreateQuestion(
                5L, 1, "seleccion", "Pregunta?", null, null, null, null)))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void addQuestionMissingActivityThrows() {
        when(activityPort.findById(9L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.addQuestion(new CoursesCommands.CreateQuestion(
                9L, 1, "seleccion", "Pregunta?", null, null, null, null)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void listQuestionsRequiresActivity() {
        when(activityPort.findById(5L)).thenReturn(Optional.of(sampleActivity()));
        when(questionPort.findByActivity(5L)).thenReturn(List.of());
        assertThat(service.listQuestions(5L)).isEmpty();
    }

    @Test
    void createAttemptRequiresActivity() {
        when(activityPort.findById(5L)).thenReturn(Optional.of(sampleActivity()));
        when(attemptPort.save(any())).thenAnswer(i -> i.getArgument(0));
        assertThat(service.createAttempt(new CoursesCommands.CreateAttempt(
                5L, UUID.randomUUID(), "{}", "{}", 2, 3, 1)).aciertos()).isEqualTo(2);
    }

    @Test
    void getActivityMissingThrows() {
        when(activityPort.findById(anyLong())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getActivity(1L)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void activityFilterNullsAreSupported() {
        when(activityPort.findAll(any(), any(), anyInt(), anyInt())).thenReturn(List.of());
        when(activityPort.existsBySlug(anyString())).thenReturn(false);
        assertThat(service.listActivities(null, null, 0, 20)).isEmpty();
    }
}
