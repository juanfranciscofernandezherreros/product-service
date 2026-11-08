package com.example.studyplanservice.infrastructure.adapter.in.web;

import com.example.studyplanservice.domain.model.AiPlan;
import com.example.studyplanservice.domain.model.StudyPlan;
import com.example.studyplanservice.domain.model.StudySession;
import com.example.studyplanservice.domain.model.WeeklyGoal;
import com.example.studyplanservice.domain.model.WeeklyReport;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.AiPlanResponse;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.GoalResponse;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.PlanResponse;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.ReportResponse;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.SessionResponse;

/**
 * Maps domain models to web responses.
 */
public final class StudyPlanWebMapper {

    private StudyPlanWebMapper() {
    }

    /** @param p plan @return response */
    public static PlanResponse toResponse(final StudyPlan p) {
        return new PlanResponse(p.getUserId(), p.getFechaExamen(), p.getMinutosDia(), p.getDiasSemana(),
                p.getPrioridad(), p.getHoraRecordatorio(), p.isRecordatorioActivo(), p.getActualizadoEn());
    }

    /** @param p AI plan @return response */
    public static AiPlanResponse toResponse(final AiPlan p) {
        return new AiPlanResponse(p.getUserId(), p.getSemanas(), p.getPlan(), p.getActualizadoEn());
    }

    /** @param g goal @return response */
    public static GoalResponse toResponse(final WeeklyGoal g) {
        return new GoalResponse(g.getUserId(), g.getTipo(), g.getCantidad(), g.getActualizadoEn());
    }

    /** @param s session @return response */
    public static SessionResponse toResponse(final StudySession s) {
        return new SessionResponse(s.getUserId(), s.getDia(), s.getTarea(), s.getCompletadaEn());
    }

    /** @param r report @return response */
    public static ReportResponse toResponse(final WeeklyReport r) {
        return new ReportResponse(r.getUserId(), r.getSemanaInicio(), r.getSemanaFin(), r.getInforme(),
                r.getGeneradoEn());
    }
}
