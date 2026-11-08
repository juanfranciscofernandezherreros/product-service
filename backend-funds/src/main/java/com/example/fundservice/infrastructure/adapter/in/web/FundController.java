package com.example.fundservice.infrastructure.adapter.in.web;

import com.example.fundservice.application.port.in.CreateFundUseCase;
import com.example.fundservice.application.port.in.DeleteFundUseCase;
import com.example.fundservice.application.port.in.GetFundUseCase;
import com.example.fundservice.application.port.in.ListFundsQuery;
import com.example.fundservice.application.port.in.ListFundsUseCase;
import com.example.fundservice.application.port.in.PagedFunds;
import com.example.fundservice.application.port.in.UpdateFundUseCase;
import com.example.fundservice.infrastructure.adapter.in.web.dto.FundCreateRequest;
import com.example.fundservice.infrastructure.adapter.in.web.dto.FundDetailResponse;
import com.example.fundservice.infrastructure.adapter.in.web.dto.FundSummaryResponse;
import com.example.fundservice.infrastructure.adapter.in.web.dto.FundUpdateRequest;
import com.example.fundservice.infrastructure.adapter.in.web.dto.PagedFundsResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter exposing the fund CRUD endpoints under {@code /v1/funds}.
 */
@RestController
@RequestMapping("/v1/funds")
@Validated
public class FundController {

    private final CreateFundUseCase createFundUseCase;
    private final GetFundUseCase getFundUseCase;
    private final ListFundsUseCase listFundsUseCase;
    private final UpdateFundUseCase updateFundUseCase;
    private final DeleteFundUseCase deleteFundUseCase;

    /**
     * Creates the controller.
     *
     * @param createFundUseCase create use case
     * @param getFundUseCase    get use case
     * @param listFundsUseCase  list use case
     * @param updateFundUseCase update use case
     * @param deleteFundUseCase delete use case
     */
    public FundController(final CreateFundUseCase createFundUseCase, final GetFundUseCase getFundUseCase,
                          final ListFundsUseCase listFundsUseCase, final UpdateFundUseCase updateFundUseCase,
                          final DeleteFundUseCase deleteFundUseCase) {
        this.createFundUseCase = createFundUseCase;
        this.getFundUseCase = getFundUseCase;
        this.listFundsUseCase = listFundsUseCase;
        this.updateFundUseCase = updateFundUseCase;
        this.deleteFundUseCase = deleteFundUseCase;
    }

    /**
     * Creates a new fund.
     *
     * @param request validated fund payload
     * @return 201 Created with the fund detail
     */
    @PostMapping
    public ResponseEntity<FundDetailResponse> create(@Valid @RequestBody final FundCreateRequest request) {
        final FundDetailResponse response = FundWebMapper.toDetail(
                createFundUseCase.create(FundWebMapper.toCreateCommand(request)));
        return ResponseEntity.created(URI.create("/v1/funds/" + response.isin())).body(response);
    }

    /**
     * Lists funds with pagination and optional filters.
     *
     * @param category optional category filter
     * @param currency optional currency filter
     * @param limit    maximum number of results
     * @param offset   number of results to skip
     * @return 200 OK with a paginated envelope
     */
    @GetMapping
    public PagedFundsResponse list(
            @RequestParam(required = false) final String category,
            @RequestParam(required = false) final String currency,
            @RequestParam(defaultValue = "20") @Min(1) final int limit,
            @RequestParam(defaultValue = "0") @Min(0) final int offset) {
        final PagedFunds page = listFundsUseCase.list(new ListFundsQuery(category, currency, limit, offset));
        final List<FundSummaryResponse> items = page.items().stream()
                .map(FundWebMapper::toSummary)
                .collect(Collectors.toList());
        return new PagedFundsResponse(page.total(), items);
    }

    /**
     * Returns a fund detail by ISIN.
     *
     * @param isin fund ISIN
     * @return 200 OK with the fund detail
     */
    @GetMapping("/{isin}")
    public FundDetailResponse getByIsin(@PathVariable final String isin) {
        return FundWebMapper.toDetail(getFundUseCase.getByIsin(isin));
    }

    /**
     * Updates an existing fund.
     *
     * @param isin    fund ISIN
     * @param request validated update payload
     * @return 200 OK with the updated fund detail
     */
    @PutMapping("/{isin}")
    public FundDetailResponse update(@PathVariable final String isin,
                                     @Valid @RequestBody final FundUpdateRequest request) {
        return FundWebMapper.toDetail(updateFundUseCase.update(isin, FundWebMapper.toUpdateCommand(request)));
    }

    /**
     * Deletes a fund.
     *
     * @param isin fund ISIN
     */
    @DeleteMapping("/{isin}")
    @ResponseStatus(org.springframework.http.HttpStatus.NO_CONTENT)
    public void delete(@PathVariable final String isin) {
        deleteFundUseCase.delete(isin);
    }
}
