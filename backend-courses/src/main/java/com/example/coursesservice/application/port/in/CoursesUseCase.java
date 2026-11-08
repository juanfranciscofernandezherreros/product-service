package com.example.coursesservice.application.port.in;

import com.example.coursesservice.domain.model.Activity;
import com.example.coursesservice.domain.model.ActivityAttempt;
import com.example.coursesservice.domain.model.ActivityQuestion;
import com.example.coursesservice.domain.model.Course;
import com.example.coursesservice.domain.model.Destreza;
import com.example.coursesservice.domain.model.Unit;

import java.util.List;

/**
 * Inbound port exposing the Courses domain operations.
 */
public interface CoursesUseCase {

    Course createCourse(CoursesCommands.SaveCourse command);

    Course getCourse(Long id);

    List<Course> listCourses(Boolean activo);

    Course updateCourse(CoursesCommands.SaveCourse command);

    void deleteCourse(Long id);

    Unit createUnit(CoursesCommands.CreateUnit command);

    Unit getUnit(Long id);

    List<Unit> listUnits(String nivel, Boolean publicado);

    Activity createActivity(CoursesCommands.CreateActivity command);

    Activity getActivity(Long id);

    List<Activity> listActivities(Destreza destreza, Boolean publicado, int offset, int limit);

    long countActivities(Destreza destreza, Boolean publicado);

    ActivityQuestion addQuestion(CoursesCommands.CreateQuestion command);

    List<ActivityQuestion> listQuestions(Long actividadId);

    ActivityAttempt createAttempt(CoursesCommands.CreateAttempt command);
}
