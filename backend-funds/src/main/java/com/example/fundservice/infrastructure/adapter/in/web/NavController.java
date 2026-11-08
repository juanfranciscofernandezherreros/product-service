package com.example.fundservice.infrastructure.adapter.in.web;

import com.example.fundservice.application.port.in.GetNavHistoryUseCase;
import com.example.fundservice.application.port.in.NavHistory;
import com.example.fundservice.application.port.in.NavHistoryQuery;
import com.example.fundservice.infrastructure.adapter.in.web.dto.NavHistoryResponse;
import com.example.fundservice.infrastructure.adapter.in.web.dto.NavPointResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter exposing the NAV history endpoint.
 */
@RestController
@RequestMapping("/v1/funds/{isin}/nav")
public class NavController {

    private final GetNavHistoryUseCase getNavHistoryUseCase;

    /**
     * Creates the controller.
     *
     * @param getNavHistoryUseCase NAV history use case
     */
    public NavController(final GetNavHistoryUseCase getNavHistoryUseCase) {
        this.getNavHistoryUseCase = getNavHistoryUseCase;
    }

    /**
     * Returns the NAV history of a fund within an optional date range.
     *
     * @param isin      fund ISIN
     * @param startDate optional inclusive start date
     * @param endDate   optional inclusive end date
     * @return 200 OK with the NAV history
     */
    @GetMapping
    public NavHistoryResponse history(
            @PathVariable final String isin,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate endDate) {
        final NavHistory history = getNavHistoryUseCase.history(new NavHistoryQuery(isin, startDate, endDate));
        final List<NavPointResponse> points = history.history().stream()
                .map(point -> new NavPointResponse(point.date(), point.nav()))
                .collect(Collectors.toList());
        return new NavHistoryResponse(history.isin(), points);
    }
}
