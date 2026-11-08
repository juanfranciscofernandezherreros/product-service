package com.example.coursesservice.application.usecase;

import com.example.coursesservice.application.port.in.CoursesCommands;
import com.example.coursesservice.application.port.in.CoursesUseCase;
import com.example.coursesservice.application.port.out.CoursesOutPorts.ActivityAttemptPort;
import com.example.coursesservice.application.port.out.CoursesOutPorts.ActivityPort;
import com.example.coursesservice.application.port.out.CoursesOutPorts.ActivityQuestionPort;
import com.example.coursesservice.application.port.out.CoursesOutPorts.CoursePort;
import com.example.coursesservice.application.port.out.CoursesOutPorts.UnitPort;
import com.example.coursesservice.domain.exception.DuplicateResourceException;
import com.example.coursesservice.domain.exception.ResourceNotFoundException;
import com.example.coursesservice.domain.model.Activity;
import com.example.coursesservice.domain.model.ActivityAttempt;
import com.example.coursesservice.domain.model.ActivityQuestion;
import com.example.coursesservice.domain.model.Course;
import com.example.coursesservice.domain.model.Destreza;
import com.example.coursesservice.domain.model.QuestionType;
import com.example.coursesservice.domain.model.Unit;

import java.util.List;

/**
 * Application service orchestrating the Courses domain use cases.
 */
public class CoursesService implements CoursesUseCase {

    private final CoursePort coursePort;
    private final UnitPort unitPort;
    private final ActivityPort activityPort;
    private final ActivityQuestionPort questionPort;
    private final ActivityAttemptPort attemptPort;

    /**
     * Constructor injection of all output ports.
     *
     * @param coursePort   courses port
     * @param unitPort     units port
     * @param activityPort activities port
     * @param questionPort questions port
     * @param attemptPort  attempts port
     */
    public CoursesService(final CoursePort coursePort, final UnitPort unitPort,
                          final ActivityPort activityPort, final ActivityQuestionPort questionPort,
                          final ActivityAttemptPort attemptPort) {
        this.coursePort = coursePort;
        this.unitPort = unitPort;
        this.activityPort = activityPort;
        this.questionPort = questionPort;
        this.attemptPort = attemptPort;
    }

    @Override
    public Course createCourse(final CoursesCommands.SaveCourse c) {
        return coursePort.save(Course.create(c.nombre(), c.descripcion(), c.activo()));
    }

    @Override
    public Course getCourse(final Long id) {
        return coursePort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found: " + id));
    }

    @Override
    public List<Course> listCourses(final Boolean activo) {
        return coursePort.findAll(activo);
    }

    @Override
    public Course updateCourse(final CoursesCommands.SaveCourse c) {
        final Course existing = getCourse(c.id());
        return coursePort.save(existing.update(c.nombre(), c.descripcion(), c.activo()));
    }

    @Override
    public void deleteCourse(final Long id) {
        getCourse(id);
        coursePort.deleteById(id);
    }

    @Override
    public Unit createUnit(final CoursesCommands.CreateUnit c) {
        if (unitPort.existsBySlug(c.slug())) {
            throw new DuplicateResourceException("Unit slug already exists: " + c.slug());
        }
        return unitPort.save(Unit.create(c.slug(), c.titulo(), c.descripcion(), c.nivel(),
                c.orden(), c.publicado()));
    }

    @Override
    public Unit getUnit(final Long id) {
        return unitPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found: " + id));
    }

    @Override
    public List<Unit> listUnits(final String nivel, final Boolean publicado) {
        return unitPort.findAll(nivel, publicado);
    }

    @Override
    public Activity createActivity(final CoursesCommands.CreateActivity c) {
        if (activityPort.existsBySlug(c.slug())) {
            throw new DuplicateResourceException("Activity slug already exists: " + c.slug());
        }
        return activityPort.save(Activity.create(c.slug(), c.titulo(), c.descripcion(),
                Destreza.fromValue(c.destreza()), c.unidadId(), c.articuloSlug(), c.orden(),
                c.publicado()));
    }

    @Override
    public Activity getActivity(final Long id) {
        return activityPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Activity not found: " + id));
    }

    @Override
    public List<Activity> listActivities(final Destreza destreza, final Boolean publicado,
                                         final int offset, final int limit) {
        return activityPort.findAll(destreza, publicado, offset, limit);
    }

    @Override
    public long countActivities(final Destreza destreza, final Boolean publicado) {
        return activityPort.count(destreza, publicado);
    }

    @Override
    public ActivityQuestion addQuestion(final CoursesCommands.CreateQuestion c) {
        getActivity(c.actividadId());
        if (questionPort.existsByActivityAndOrden(c.actividadId(), c.orden())) {
            throw new DuplicateResourceException("Question order already exists in activity");
        }
        return questionPort.save(ActivityQuestion.create(c.actividadId(), c.orden(),
                QuestionType.fromValue(c.tipo()), c.enunciado(), c.configuracion(),
                c.medioTipo(), c.medioUrl(), c.medioAlt()));
    }

    @Override
    public List<ActivityQuestion> listQuestions(final Long actividadId) {
        getActivity(actividadId);
        return questionPort.findByActivity(actividadId);
    }

    @Override
    public ActivityAttempt createAttempt(final CoursesCommands.CreateAttempt c) {
        getActivity(c.actividadId());
        return attemptPort.save(ActivityAttempt.create(c.usuarioId(), c.actividadId(),
                c.respuestas(), c.detalle(), c.aciertos(), c.evaluables(), c.pendientesRevision()));
    }
}
