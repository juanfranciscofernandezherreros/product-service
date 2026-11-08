package com.example.examsservice.infrastructure.adapter.in.web;

import com.example.examsservice.application.port.in.ExamsCommands.CreateDiagnosticCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateResultCommand;
import com.example.examsservice.application.port.in.ExamsCommands.CreateSimulationCommand;
import com.example.examsservice.application.port.in.ExamsUseCases;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.DiagnosticRequest;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.DiagnosticResponse;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.ResultRequest;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.ResultResponse;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.SimulationRequest;
import com.example.examsservice.infrastructure.adapter.in.web.dto.ExamsDtos.SimulationResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter for results, diagnostics and simulations under {@code /v1/exams}.
 */
@RestController
@RequestMapping("/v1/exams")
public class AssessmentController {

    private final ExamsUseCases useCases;

    /**
     * Creates the controller.
     *
     * @param useCases exams use cases
     */
    public AssessmentController(final ExamsUseCases useCases) {
        this.useCases = useCases;
    }

    private static String json(final com.fasterxml.jackson.databind.JsonNode node) {
        return node == null ? null : node.toString();
    }

    /**
     * Registers an exam result.
     *
     * @param request payload
     * @return 201 with the result
     */
    @PostMapping("/results")
    public ResponseEntity<ResultResponse> createResult(@Valid @RequestBody final ResultRequest request) {
        final ResultResponse response = ExamsWebMapper.toResponse(useCases.createResult(new CreateResultCommand(
                request.usuarioId(), request.examenId(), json(request.respuestas()), request.aciertos(),
                request.total(), request.nota())));
        return ResponseEntity.created(java.net.URI.create("/v1/exams/results/" + response.id())).body(response);
    }

    /**
     * Lists a user's results.
     *
     * @param userId user id
     * @return 200 with results
     */
    @GetMapping("/results/users/{userId}")
    public List<ResultResponse> listUserResults(@PathVariable final UUID userId) {
        return useCases.listUserResults(userId).stream()
                .map(ExamsWebMapper::toResponse).collect(Collectors.toList());
    }

    /**
     * Registers a diagnostic.
     *
     * @param request payload
     * @return 201 with the diagnostic
     */
    @PostMapping("/diagnostics")
    public ResponseEntity<DiagnosticResponse> createDiagnostic(
            @Valid @RequestBody final DiagnosticRequest request) {
        final DiagnosticResponse response = ExamsWebMapper.toResponse(useCases.createDiagnostic(
                new CreateDiagnosticCommand(request.usuarioId(), request.aciertos(), request.total(),
                        json(request.desglose()))));
        return ResponseEntity.created(java.net.URI.create("/v1/exams/diagnostics/" + response.id()))
                .body(response);
    }

    /**
     * Registers a simulation.
     *
     * @param request payload
     * @return 201 with the simulation
     */
    @PostMapping("/simulations")
    public ResponseEntity<SimulationResponse> createSimulation(
            @Valid @RequestBody final SimulationRequest request) {
        final SimulationResponse response = ExamsWebMapper.toResponse(useCases.createSimulation(
                new CreateSimulationCommand(request.usuarioId(), request.modalidad(), request.aciertos(),
                        request.total(), json(request.respuestas()), request.duracionSegundos())));
        return ResponseEntity.created(java.net.URI.create("/v1/exams/simulations/" + response.id()))
                .body(response);
    }
}
