package com.example.studyplanservice.infrastructure.adapter.in.web;

import com.example.studyplanservice.application.port.in.StudyPlanCommands.AddSessionCommand;
import com.example.studyplanservice.application.port.in.StudyPlanCommands.UpsertAiPlanCommand;
import com.example.studyplanservice.application.port.in.StudyPlanCommands.UpsertGoalCommand;
import com.example.studyplanservice.application.port.in.StudyPlanCommands.UpsertPlanCommand;
import com.example.studyplanservice.application.port.in.StudyPlanUseCases;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.AiPlanResponse;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.AiPlanUpsertRequest;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.GoalResponse;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.GoalUpsertRequest;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.PlanResponse;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.PlanUpsertRequest;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.ReportResponse;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.SessionCreateRequest;
import com.example.studyplanservice.infrastructure.adapter.in.web.dto.StudyPlanDtos.SessionResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter for study-plan resources under {@code /v1/study-plan/users/{userId}}.
 */
@RestController
@RequestMapping("/v1/study-plan/users/{userId}")
public class StudyPlanController {

    private final StudyPlanUseCases useCases;

    /**
     * Creates the controller.
     *
     * @param useCases study-plan use cases
     */
    public StudyPlanController(final StudyPlanUseCases useCases) {
        this.useCases = useCases;
    }

    /**
     * Gets the user's plan.
     *
     * @param userId user id
     * @return 200 with the plan
     */
    @GetMapping("/plan")
    public PlanResponse getPlan(@PathVariable final UUID userId) {
        return StudyPlanWebMapper.toResponse(useCases.getPlan(userId));
    }

    /**
     * Upserts the user's plan.
     *
     * @param userId  user id
     * @param request payload
     * @return 200 with the plan
     */
    @PutMapping("/plan")
    public PlanResponse upsertPlan(@PathVariable final UUID userId,
                                   @Valid @RequestBody final PlanUpsertRequest request) {
        return StudyPlanWebMapper.toResponse(useCases.upsertPlan(new UpsertPlanCommand(userId,
                request.fechaExamen(), request.minutosDia(), request.diasSemana(), request.prioridad(),
                request.horaRecordatorio(), request.recordatorioActivo())));
    }

    /**
     * Gets the user's AI plan.
     *
     * @param userId user id
     * @return 200 with the AI plan
     */
    @GetMapping("/ai-plan")
    public AiPlanResponse getAiPlan(@PathVariable final UUID userId) {
        return StudyPlanWebMapper.toResponse(useCases.getAiPlan(userId));
    }

    /**
     * Upserts the user's AI plan.
     *
     * @param userId  user id
     * @param request payload
     * @return 200 with the AI plan
     */
    @PutMapping("/ai-plan")
    public AiPlanResponse upsertAiPlan(@PathVariable final UUID userId,
                                       @Valid @RequestBody final AiPlanUpsertRequest request) {
        return StudyPlanWebMapper.toResponse(useCases.upsertAiPlan(
                new UpsertAiPlanCommand(userId, request.semanas(), request.plan().toString())));
    }

    /**
     * Gets the user's goal.
     *
     * @param userId user id
     * @return 200 with the goal
     */
    @GetMapping("/goal")
    public GoalResponse getGoal(@PathVariable final UUID userId) {
        return StudyPlanWebMapper.toResponse(useCases.getGoal(userId));
    }

    /**
     * Upserts the user's goal.
     *
     * @param userId  user id
     * @param request payload
     * @return 200 with the goal
     */
    @PutMapping("/goal")
    public GoalResponse upsertGoal(@PathVariable final UUID userId,
                                   @Valid @RequestBody final GoalUpsertRequest request) {
        return StudyPlanWebMapper.toResponse(useCases.upsertGoal(
                new UpsertGoalCommand(userId, request.tipo(), request.cantidad())));
    }

    /**
     * Lists the user's sessions.
     *
     * @param userId user id
     * @return 200 with sessions
     */
    @GetMapping("/sessions")
    public List<SessionResponse> listSessions(@PathVariable final UUID userId) {
        return useCases.listSessions(userId).stream()
                .map(StudyPlanWebMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * Adds a session.
     *
     * @param userId  user id
     * @param request payload
     * @return 201 with the session
     */
    @PostMapping("/sessions")
    public ResponseEntity<SessionResponse> addSession(@PathVariable final UUID userId,
                                                      @Valid @RequestBody final SessionCreateRequest request) {
        final SessionResponse response = StudyPlanWebMapper.toResponse(useCases.addSession(
                new AddSessionCommand(userId, request.dia(), request.tarea())));
        return ResponseEntity.created(URI.create("/v1/study-plan/users/" + userId + "/sessions")).body(response);
    }

    /**
     * Lists the user's weekly reports.
     *
     * @param userId user id
     * @return 200 with reports
     */
    @GetMapping("/weekly-reports")
    public List<ReportResponse> listReports(@PathVariable final UUID userId) {
        return useCases.listReports(userId).stream()
                .map(StudyPlanWebMapper::toResponse).collect(Collectors.toList());
    }
}
