package com.example.socialservice.infrastructure.config;

import com.example.socialservice.application.port.in.SocialUseCases;
import com.example.socialservice.application.port.out.SocialOutPorts.FollowPort;
import com.example.socialservice.application.port.out.SocialOutPorts.MessagePort;
import com.example.socialservice.application.port.out.SocialOutPorts.ProfilePort;
import com.example.socialservice.application.port.out.SocialOutPorts.RequestPort;
import com.example.socialservice.application.usecase.SocialService;
import com.example.socialservice.infrastructure.adapter.out.persistence.FollowJpaRepository;
import com.example.socialservice.infrastructure.adapter.out.persistence.MessageJpaRepository;
import com.example.socialservice.infrastructure.adapter.out.persistence.ProfileJpaRepository;
import com.example.socialservice.infrastructure.adapter.out.persistence.RequestJpaRepository;
import com.example.socialservice.infrastructure.adapter.out.persistence.SocialPersistenceAdapters.FollowAdapter;
import com.example.socialservice.infrastructure.adapter.out.persistence.SocialPersistenceAdapters.MessageAdapter;
import com.example.socialservice.infrastructure.adapter.out.persistence.SocialPersistenceAdapters.ProfileAdapter;
import com.example.socialservice.infrastructure.adapter.out.persistence.SocialPersistenceAdapters.RequestAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit wiring of hexagonal beans.
 */
@Configuration
public class BeanConfiguration {

    /** @param r repository @return port */
    @Bean
    public ProfilePort profilePort(final ProfileJpaRepository r) {
        return new ProfileAdapter(r);
    }

    /** @param r repository @return port */
    @Bean
    public FollowPort followPort(final FollowJpaRepository r) {
        return new FollowAdapter(r);
    }

    /** @param r repository @return port */
    @Bean
    public MessagePort messagePort(final MessageJpaRepository r) {
        return new MessageAdapter(r);
    }

    /** @param r repository @return port */
    @Bean
    public RequestPort requestPort(final RequestJpaRepository r) {
        return new RequestAdapter(r);
    }

    /**
     * Social use cases.
     *
     * @param profilePort profile port
     * @param followPort  follow port
     * @param messagePort message port
     * @param requestPort request port
     * @return the use cases
     */
    @Bean
    public SocialUseCases socialUseCases(final ProfilePort profilePort, final FollowPort followPort,
                                         final MessagePort messagePort, final RequestPort requestPort) {
        return new SocialService(profilePort, followPort, messagePort, requestPort);
    }
}
