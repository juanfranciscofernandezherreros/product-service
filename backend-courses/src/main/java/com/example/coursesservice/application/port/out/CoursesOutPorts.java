package com.example.coursesservice.application.port.out;

import com.example.coursesservice.domain.model.Activity;
import com.example.coursesservice.domain.model.ActivityAttempt;
import com.example.coursesservice.domain.model.ActivityQuestion;
import com.example.coursesservice.domain.model.Course;
import com.example.coursesservice.domain.model.Destreza;
import com.example.coursesservice.domain.model.Unit;

import java.util.List;
import java.util.Optional;

/**
 * Output ports (persistence contracts) for the Courses domain.
 */
public final class CoursesOutPorts {

    private CoursesOutPorts() {
    }

    /** Persistence port for courses. */
    public interface CoursePort {
        Course save(Course course);

        Optional<Course> findById(Long id);

        List<Course> findAll(Boolean activo);

        void deleteById(Long id);
    }

    /** Persistence port for units. */
    public interface UnitPort {
        Unit save(Unit unit);

        Optional<Unit> findById(Long id);

        boolean existsBySlug(String slug);

        List<Unit> findAll(String nivel, Boolean publicado);
    }

    /** Persistence port for activities. */
    public interface ActivityPort {
        Activity save(Activity activity);

        Optional<Activity> findById(Long id);

        boolean existsBySlug(String slug);

        List<Activity> findAll(Destreza destreza, Boolean publicado, int offset, int limit);

        long count(Destreza destreza, Boolean publicado);
    }

    /** Persistence port for activity questions. */
    public interface ActivityQuestionPort {
        ActivityQuestion save(ActivityQuestion question);

        boolean existsByActivityAndOrden(Long actividadId, int orden);

        List<ActivityQuestion> findByActivity(Long actividadId);
    }

    /** Persistence port for activity attempts. */
    public interface ActivityAttemptPort {
        ActivityAttempt save(ActivityAttempt attempt);
    }
}
