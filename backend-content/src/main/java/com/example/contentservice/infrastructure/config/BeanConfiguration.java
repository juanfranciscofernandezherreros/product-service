package com.example.contentservice.infrastructure.config;

import com.example.contentservice.application.port.in.ContentUseCase;
import com.example.contentservice.application.port.out.ContentOutPorts.ArticlePort;
import com.example.contentservice.application.port.out.ContentOutPorts.CategoryPort;
import com.example.contentservice.application.port.out.ContentOutPorts.CollectionPort;
import com.example.contentservice.application.port.out.ContentOutPorts.CommentPort;
import com.example.contentservice.application.port.out.ContentOutPorts.DownloadablePort;
import com.example.contentservice.application.port.out.ContentOutPorts.MoviePhrasePort;
import com.example.contentservice.application.port.out.ContentOutPorts.PagePort;
import com.example.contentservice.application.usecase.ContentService;
import com.example.contentservice.infrastructure.adapter.out.persistence.ArticleJpaRepository;
import com.example.contentservice.infrastructure.adapter.out.persistence.ArticlePersistenceAdapter;
import com.example.contentservice.infrastructure.adapter.out.persistence.CategoryJpaRepository;
import com.example.contentservice.infrastructure.adapter.out.persistence.CategoryPersistenceAdapter;
import com.example.contentservice.infrastructure.adapter.out.persistence.CollectionJpaRepository;
import com.example.contentservice.infrastructure.adapter.out.persistence.CollectionPersistenceAdapter;
import com.example.contentservice.infrastructure.adapter.out.persistence.CommentJpaRepository;
import com.example.contentservice.infrastructure.adapter.out.persistence.CommentPersistenceAdapter;
import com.example.contentservice.infrastructure.adapter.out.persistence.DownloadableJpaRepository;
import com.example.contentservice.infrastructure.adapter.out.persistence.DownloadablePersistenceAdapter;
import com.example.contentservice.infrastructure.adapter.out.persistence.MoviePhraseJpaRepository;
import com.example.contentservice.infrastructure.adapter.out.persistence.MoviePhrasePersistenceAdapter;
import com.example.contentservice.infrastructure.adapter.out.persistence.PageJpaRepository;
import com.example.contentservice.infrastructure.adapter.out.persistence.PagePersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit bean wiring for the hexagonal components.
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public ArticlePort articlePort(final ArticleJpaRepository r) {
        return new ArticlePersistenceAdapter(r);
    }

    @Bean
    public CommentPort commentPort(final CommentJpaRepository r) {
        return new CommentPersistenceAdapter(r);
    }

    @Bean
    public CategoryPort categoryPort(final CategoryJpaRepository r) {
        return new CategoryPersistenceAdapter(r);
    }

    @Bean
    public PagePort pagePort(final PageJpaRepository r) {
        return new PagePersistenceAdapter(r);
    }

    @Bean
    public CollectionPort collectionPort(final CollectionJpaRepository r) {
        return new CollectionPersistenceAdapter(r);
    }

    @Bean
    public DownloadablePort downloadablePort(final DownloadableJpaRepository r) {
        return new DownloadablePersistenceAdapter(r);
    }

    @Bean
    public MoviePhrasePort moviePhrasePort(final MoviePhraseJpaRepository r) {
        return new MoviePhrasePersistenceAdapter(r);
    }

    @Bean
    public ContentUseCase contentUseCase(final ArticlePort articlePort, final CommentPort commentPort,
                                         final CategoryPort categoryPort, final PagePort pagePort,
                                         final CollectionPort collectionPort,
                                         final DownloadablePort downloadablePort,
                                         final MoviePhrasePort moviePhrasePort) {
        return new ContentService(articlePort, commentPort, categoryPort, pagePort,
                collectionPort, downloadablePort, moviePhrasePort);
    }
}
