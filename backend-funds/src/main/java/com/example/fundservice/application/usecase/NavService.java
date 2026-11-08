package com.example.fundservice.application.usecase;

import com.example.fundservice.application.port.in.GetNavHistoryUseCase;
import com.example.fundservice.application.port.in.NavHistory;
import com.example.fundservice.application.port.in.NavHistoryQuery;
import com.example.fundservice.application.port.out.FundRepositoryPort;
import com.example.fundservice.application.port.out.NavRepositoryPort;
import com.example.fundservice.domain.exception.FundNotFoundException;
import com.example.fundservice.domain.model.NavPoint;
import java.util.List;

/**
 * Application service implementing NAV history retrieval.
 */
public class NavService implements GetNavHistoryUseCase {

    private final FundRepositoryPort fundRepository;
    private final NavRepositoryPort navRepository;

    /**
     * Creates the service.
     *
     * @param fundRepository outbound fund persistence port
     * @param navRepository  outbound NAV persistence port
     */
    public NavService(final FundRepositoryPort fundRepository, final NavRepositoryPort navRepository) {
        this.fundRepository = fundRepository;
        this.navRepository = navRepository;
    }

    @Override
    public NavHistory history(final NavHistoryQuery query) {
        if (!fundRepository.existsByIsin(query.isin())) {
            throw new FundNotFoundException(query.isin());
        }
        final List<NavPoint> history = navRepository.findHistory(
                query.isin(), query.startDate(), query.endDate());
        return new NavHistory(query.isin(), history);
    }
}
