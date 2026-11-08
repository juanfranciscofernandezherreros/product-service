package com.example.fundservice.application.usecase;

import com.example.fundservice.application.port.in.CreateFundCommand;
import com.example.fundservice.application.port.in.CreateFundUseCase;
import com.example.fundservice.application.port.in.DeleteFundUseCase;
import com.example.fundservice.application.port.in.GetFundUseCase;
import com.example.fundservice.application.port.in.ListFundsQuery;
import com.example.fundservice.application.port.in.ListFundsUseCase;
import com.example.fundservice.application.port.in.PagedFunds;
import com.example.fundservice.application.port.in.UpdateFundCommand;
import com.example.fundservice.application.port.in.UpdateFundUseCase;
import com.example.fundservice.application.port.out.FundRepositoryPort;
import com.example.fundservice.application.port.out.OrderRepositoryPort;
import com.example.fundservice.domain.exception.FundAlreadyExistsException;
import com.example.fundservice.domain.exception.FundHasActivePositionsException;
import com.example.fundservice.domain.exception.FundNotFoundException;
import com.example.fundservice.domain.model.Fund;
import java.util.List;

/**
 * Application service implementing the fund use cases.
 */
public class FundService implements CreateFundUseCase, GetFundUseCase, ListFundsUseCase,
        UpdateFundUseCase, DeleteFundUseCase {

    private final FundRepositoryPort fundRepository;
    private final OrderRepositoryPort orderRepository;

    /**
     * Creates the service.
     *
     * @param fundRepository  outbound fund persistence port
     * @param orderRepository outbound order persistence port (for active-position checks)
     */
    public FundService(final FundRepositoryPort fundRepository, final OrderRepositoryPort orderRepository) {
        this.fundRepository = fundRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Fund create(final CreateFundCommand command) {
        if (fundRepository.existsByIsin(command.isin())) {
            throw new FundAlreadyExistsException(command.isin());
        }
        final Fund fund = Fund.register(command.isin(), command.name(), command.category(),
                command.currency(), command.riskLevel(), command.managementFee(),
                command.depositaryFee(), command.description());
        return fundRepository.save(fund);
    }

    @Override
    public Fund getByIsin(final String isin) {
        return fundRepository.findByIsin(isin).orElseThrow(() -> new FundNotFoundException(isin));
    }

    @Override
    public PagedFunds list(final ListFundsQuery query) {
        final List<Fund> items = fundRepository.findByFilter(
                query.category(), query.currency(), query.limit(), query.offset());
        final long total = fundRepository.countByFilter(query.category(), query.currency());
        return new PagedFunds(total, items);
    }

    @Override
    public Fund update(final String isin, final UpdateFundCommand command) {
        final Fund existing = fundRepository.findByIsin(isin)
                .orElseThrow(() -> new FundNotFoundException(isin));
        final Fund updated = existing.withUpdatedDetails(command.name(), command.category(),
                command.riskLevel(), command.managementFee(), command.depositaryFee(), command.description());
        return fundRepository.save(updated);
    }

    @Override
    public void delete(final String isin) {
        if (!fundRepository.existsByIsin(isin)) {
            throw new FundNotFoundException(isin);
        }
        if (orderRepository.hasActivePositions(isin)) {
            throw new FundHasActivePositionsException(isin);
        }
        fundRepository.deleteByIsin(isin);
    }
}
