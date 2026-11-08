package com.example.studyplanservice.infrastructure.adapter.out.persistence;

import com.example.studyplanservice.domain.model.AiPlan;
import com.example.studyplanservice.domain.model.GoalType;
import com.example.studyplanservice.domain.model.Prioridad;
import com.example.studyplanservice.domain.model.StudyPlan;
import com.example.studyplanservice.domain.model.StudySession;
import com.example.studyplanservice.domain.model.Tarea;
import com.example.studyplanservice.domain.model.WeeklyGoal;
import com.example.studyplanservice.domain.model.WeeklyReport;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Explicit mapper between domain models and JPA entities.
 */
public final class StudyPlanMapper {

    private StudyPlanMapper() {
    }

    private static String daysToCsv(final List<Integer> days) {
        return days.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

    private static List<Integer> csvToDays(final String csv) {
        return Arrays.stream(csv.split(",")).map(String::trim).filter(s -> !s.isEmpty())
                .map(Integer::valueOf).collect(Collectors.toList());
    }

    /**
     * @param p domain plan
     * @return entity
     */
    public static StudyPlanEntity toEntity(final StudyPlan p) {
        return new StudyPlanEntity(p.getUserId(), p.getFechaExamen(), p.getMinutosDia(),
                daysToCsv(p.getDiasSemana()), p.getPrioridad().name().toLowerCase(Locale.ROOT),
                p.getHoraRecordatorio(), p.isRecordatorioActivo(), p.getActualizadoEn());
    }

    /**
     * @param e entity
     * @return domain plan
     */
    public static StudyPlan toDomain(final StudyPlanEntity e) {
        return new StudyPlan(e.getUserId(), e.getFechaExamen(), e.getMinutosDia(), csvToDays(e.getDiasSemana()),
                Prioridad.valueOf(e.getPrioridad().toUpperCase(Locale.ROOT)), e.getHoraRecordatorio(),
                e.isRecordatorioActivo(), e.getActualizadoEn());
    }

    /**
     * @param p domain AI plan
     * @return entity
     */
    public static AiPlanEntity toEntity(final AiPlan p) {
        return new AiPlanEntity(p.getUserId(), p.getSemanas(), p.getPlan(), p.getActualizadoEn());
    }

    /**
     * @param e entity
     * @return domain AI plan
     */
    public static AiPlan toDomain(final AiPlanEntity e) {
        return new AiPlan(e.getUserId(), e.getSemanas(), e.getPlan(), e.getActualizadoEn());
    }

    /**
     * @param g domain goal
     * @return entity
     */
    public static GoalEntity toEntity(final WeeklyGoal g) {
        return new GoalEntity(g.getUserId(), g.getTipo().name().toLowerCase(Locale.ROOT),
                g.getCantidad(), g.getActualizadoEn());
    }

    /**
     * @param e entity
     * @return domain goal
     */
    public static WeeklyGoal toDomain(final GoalEntity e) {
        return new WeeklyGoal(e.getUserId(), GoalType.valueOf(e.getTipo().toUpperCase(Locale.ROOT)),
                e.getCantidad(), e.getActualizadoEn());
    }

    /**
     * @param s domain session
     * @return entity
     */
    public static SessionEntity toEntity(final StudySession s) {
        return new SessionEntity(s.getUserId(), s.getDia(), s.getTarea().name().toLowerCase(Locale.ROOT),
                s.getCompletadaEn());
    }

    /**
     * @param e entity
     * @return domain session
     */
    public static StudySession toDomain(final SessionEntity e) {
        return new StudySession(e.getUserId(), e.getDia(),
                Tarea.valueOf(e.getTarea().toUpperCase(Locale.ROOT)), e.getCompletadaEn());
    }

    /**
     * @param e entity
     * @return domain report
     */
    public static WeeklyReport toDomain(final ReportEntity e) {
        return new WeeklyReport(e.getUserId(), e.getSemanaInicio(), e.getSemanaFin(), e.getInforme(),
                e.getGeneradoEn());
    }
}
