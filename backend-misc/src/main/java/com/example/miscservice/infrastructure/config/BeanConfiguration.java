package com.example.miscservice.infrastructure.config;

import com.example.miscservice.application.port.in.MiscUseCase;
import com.example.miscservice.application.port.out.MiscOutPorts.LogbookCategoryPort;
import com.example.miscservice.application.port.out.MiscOutPorts.LogbookNotePort;
import com.example.miscservice.application.port.out.MiscOutPorts.SuggestionPort;
import com.example.miscservice.application.port.out.MiscOutPorts.WritingDraftPort;
import com.example.miscservice.application.usecase.MiscService;
import com.example.miscservice.infrastructure.adapter.out.persistence.LogbookCategoryJpaRepository;
import com.example.miscservice.infrastructure.adapter.out.persistence.LogbookCategoryPersistenceAdapter;
import com.example.miscservice.infrastructure.adapter.out.persistence.LogbookNoteJpaRepository;
import com.example.miscservice.infrastructure.adapter.out.persistence.LogbookNotePersistenceAdapter;
import com.example.miscservice.infrastructure.adapter.out.persistence.SuggestionJpaRepository;
import com.example.miscservice.infrastructure.adapter.out.persistence.SuggestionPersistenceAdapter;
import com.example.miscservice.infrastructure.adapter.out.persistence.WritingDraftJpaRepository;
import com.example.miscservice.infrastructure.adapter.out.persistence.WritingDraftPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit bean wiring for the hexagonal components.
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public SuggestionPort suggestionPort(final SuggestionJpaRepository repository) {
        return new SuggestionPersistenceAdapter(repository);
    }

    @Bean
    public LogbookCategoryPort logbookCategoryPort(final LogbookCategoryJpaRepository repository) {
        return new LogbookCategoryPersistenceAdapter(repository);
    }

    @Bean
    public LogbookNotePort logbookNotePort(final LogbookNoteJpaRepository repository) {
        return new LogbookNotePersistenceAdapter(repository);
    }

    @Bean
    public WritingDraftPort writingDraftPort(final WritingDraftJpaRepository repository) {
        return new WritingDraftPersistenceAdapter(repository);
    }

    @Bean
    public MiscUseCase miscUseCase(final SuggestionPort suggestionPort,
                                   final LogbookCategoryPort categoryPort,
                                   final LogbookNotePort notePort,
                                   final WritingDraftPort draftPort) {
        return new MiscService(suggestionPort, categoryPort, notePort, draftPort);
    }
}
