package com.example.fundservice.infrastructure.config;

import com.example.fundservice.application.port.in.CreateFundUseCase;
import com.example.fundservice.application.port.in.DeleteFundUseCase;
import com.example.fundservice.application.port.in.GetFundUseCase;
import com.example.fundservice.application.port.in.GetNavHistoryUseCase;
import com.example.fundservice.application.port.in.ListFundsUseCase;
import com.example.fundservice.application.port.in.PlaceOrderUseCase;
import com.example.fundservice.application.port.in.UpdateFundUseCase;
import com.example.fundservice.application.port.out.FundRepositoryPort;
import com.example.fundservice.application.port.out.NavRepositoryPort;
import com.example.fundservice.application.port.out.OrderRepositoryPort;
import com.example.fundservice.application.usecase.FundService;
import com.example.fundservice.application.usecase.NavService;
import com.example.fundservice.application.usecase.OrderService;
import com.example.fundservice.infrastructure.adapter.out.persistence.FundJpaRepository;
import com.example.fundservice.infrastructure.adapter.out.persistence.FundPersistenceAdapter;
import com.example.fundservice.infrastructure.adapter.out.persistence.NavPersistenceAdapter;
import com.example.fundservice.infrastructure.adapter.out.persistence.NavPointJpaRepository;
import com.example.fundservice.infrastructure.adapter.out.persistence.OrderJpaRepository;
import com.example.fundservice.infrastructure.adapter.out.persistence.OrderPersistenceAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Explicit wiring of hexagonal beans so every dependency is visible in one place.
 */
@Configuration
public class BeanConfiguration {

    /**
     * Provides the fund persistence adapter.
     *
     * @param repository Spring Data repository
     * @return the fund repository port
     */
    @Bean
    public FundRepositoryPort fundRepositoryPort(final FundJpaRepository repository) {
        return new FundPersistenceAdapter(repository);
    }

    /**
     * Provides the NAV persistence adapter.
     *
     * @param repository Spring Data repository
     * @return the NAV repository port
     */
    @Bean
    public NavRepositoryPort navRepositoryPort(final NavPointJpaRepository repository) {
        return new NavPersistenceAdapter(repository);
    }

    /**
     * Provides the order persistence adapter.
     *
     * @param repository Spring Data repository
     * @return the order repository port
     */
    @Bean
    public OrderRepositoryPort orderRepositoryPort(final OrderJpaRepository repository) {
        return new OrderPersistenceAdapter(repository);
    }

    /**
     * Provides the fund application service.
     *
     * @param fundRepositoryPort  fund port
     * @param orderRepositoryPort order port
     * @return the fund service
     */
    @Bean
    public FundService fundService(final FundRepositoryPort fundRepositoryPort,
                                   final OrderRepositoryPort orderRepositoryPort) {
        return new FundService(fundRepositoryPort, orderRepositoryPort);
    }

    /**
     * Provides the NAV application service.
     *
     * @param fundRepositoryPort fund port
     * @param navRepositoryPort  NAV port
     * @return the NAV service
     */
    @Bean
    public NavService navService(final FundRepositoryPort fundRepositoryPort,
                                 final NavRepositoryPort navRepositoryPort) {
        return new NavService(fundRepositoryPort, navRepositoryPort);
    }

    /**
     * Provides the order application service.
     *
     * @param orderRepositoryPort order port
     * @param fundRepositoryPort  fund port
     * @return the order service
     */
    @Bean
    public OrderService orderService(final OrderRepositoryPort orderRepositoryPort,
                                     final FundRepositoryPort fundRepositoryPort) {
        return new OrderService(orderRepositoryPort, fundRepositoryPort);
    }

    /** @param fundService service @return create use case */
    @Bean
    public CreateFundUseCase createFundUseCase(final FundService fundService) {
        return fundService;
    }

    /** @param fundService service @return get use case */
    @Bean
    public GetFundUseCase getFundUseCase(final FundService fundService) {
        return fundService;
    }

    /** @param fundService service @return list use case */
    @Bean
    public ListFundsUseCase listFundsUseCase(final FundService fundService) {
        return fundService;
    }

    /** @param fundService service @return update use case */
    @Bean
    public UpdateFundUseCase updateFundUseCase(final FundService fundService) {
        return fundService;
    }

    /** @param fundService service @return delete use case */
    @Bean
    public DeleteFundUseCase deleteFundUseCase(final FundService fundService) {
        return fundService;
    }

    /** @param navService service @return NAV history use case */
    @Bean
    public GetNavHistoryUseCase getNavHistoryUseCase(final NavService navService) {
        return navService;
    }

    /** @param orderService service @return place-order use case */
    @Bean
    public PlaceOrderUseCase placeOrderUseCase(final OrderService orderService) {
        return orderService;
    }
}
