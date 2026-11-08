package com.example.examsservice.infrastructure.adapter.out.persistence;

import com.example.examsservice.application.port.out.ExamsOutPorts.DiagnosticPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.ExamPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.QuestionPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.ResultPort;
import com.example.examsservice.application.port.out.ExamsOutPorts.SimulationPort;
import com.example.examsservice.domain.model.Diagnostic;
import com.example.examsservice.domain.model.Exam;
import com.example.examsservice.domain.model.Question;
import com.example.examsservice.domain.model.Result;
import com.example.examsservice.domain.model.Simulation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Outbound persistence adapters for the exams domain.
 */
public final class ExamsPersistenceAdapters {

    private ExamsPersistenceAdapters() {
    }

    /** Exam adapter. */
    public static final class ExamAdapter implements ExamPort {
        private final ExamJpaRepository repository;

        /** @param repository repository */
        public ExamAdapter(final ExamJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public Exam save(final Exam exam) {
            return ExamsMapper.toDomain(repository.save(ExamsMapper.toEntity(exam)));
        }

        @Override
        public Optional<Exam> findById(final long id) {
            return repository.findById(id).map(ExamsMapper::toDomain);
        }

        @Override
        public boolean existsById(final long id) {
            return repository.existsById(id);
        }

        @Override
        public boolean existsByCodigo(final String codigo) {
            return repository.existsByCodigo(codigo);
        }

        @Override
        public List<Exam> findByFilter(final String nivel, final Boolean publicado, final int limit,
                                       final int offset) {
            final Pageable pageable = new OffsetBasedPageRequest(offset, limit, Sort.by("id").ascending());
            return repository.findByFilter(nivel, publicado, pageable).stream()
                    .map(ExamsMapper::toDomain).collect(Collectors.toList());
        }

        @Override
        public long countByFilter(final String nivel, final Boolean publicado) {
            return repository.countByFilter(nivel, publicado);
        }

        @Override
        public void deleteById(final long id) {
            repository.deleteById(id);
        }
    }

    /** Question adapter. */
    public static final class QuestionAdapter implements QuestionPort {
        private final QuestionJpaRepository repository;

        /** @param repository repository */
        public QuestionAdapter(final QuestionJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public Question save(final Question question) {
            return ExamsMapper.toDomain(repository.save(ExamsMapper.toEntity(question)));
        }

        @Override
        public List<Question> findByExam(final long examenId) {
            return repository.findByExamenIdOrderByOrden(examenId).stream()
                    .map(ExamsMapper::toDomain).collect(Collectors.toList());
        }

        @Override
        public boolean existsByExamAndOrden(final long examenId, final int orden) {
            return repository.existsByExamenIdAndOrden(examenId, orden);
        }
    }

    /** Result adapter. */
    public static final class ResultAdapter implements ResultPort {
        private final ResultJpaRepository repository;

        /** @param repository repository */
        public ResultAdapter(final ResultJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public Result save(final Result result) {
            return ExamsMapper.toDomain(repository.save(ExamsMapper.toEntity(result)));
        }

        @Override
        public List<Result> findByUser(final UUID usuarioId) {
            return repository.findByUsuarioId(usuarioId).stream()
                    .map(ExamsMapper::toDomain).collect(Collectors.toList());
        }
    }

    /** Diagnostic adapter. */
    public static final class DiagnosticAdapter implements DiagnosticPort {
        private final DiagnosticJpaRepository repository;

        /** @param repository repository */
        public DiagnosticAdapter(final DiagnosticJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public Diagnostic save(final Diagnostic diagnostic) {
            return ExamsMapper.toDomain(repository.save(ExamsMapper.toEntity(diagnostic)));
        }
    }

    /** Simulation adapter. */
    public static final class SimulationAdapter implements SimulationPort {
        private final SimulationJpaRepository repository;

        /** @param repository repository */
        public SimulationAdapter(final SimulationJpaRepository repository) {
            this.repository = repository;
        }

        @Override
        public Simulation save(final Simulation simulation) {
            return ExamsMapper.toDomain(repository.save(ExamsMapper.toEntity(simulation)));
        }
    }
}
