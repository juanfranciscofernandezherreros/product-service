package com.example.fundservice.infrastructure.adapter.in.web;

import com.example.fundservice.application.port.in.CreateFundCommand;
import com.example.fundservice.application.port.in.UpdateFundCommand;
import com.example.fundservice.domain.model.Fund;
import com.example.fundservice.infrastructure.adapter.in.web.dto.FundCreateRequest;
import com.example.fundservice.infrastructure.adapter.in.web.dto.FundDetailResponse;
import com.example.fundservice.infrastructure.adapter.in.web.dto.FundSummaryResponse;
import com.example.fundservice.infrastructure.adapter.in.web.dto.FundUpdateRequest;

/**
 * Explicit mapper between fund web DTOs and application/domain types.
 */
public final class FundWebMapper {

    private FundWebMapper() {
    }

    /**
     * Converts a create request into a command.
     *
     * @param request inbound request
     * @return the create command
     */
    public static CreateFundCommand toCreateCommand(final FundCreateRequest request) {
        return new CreateFundCommand(request.isin(), request.name(), request.category(), request.currency(),
                request.riskLevel(), request.managementFee(), request.depositaryFee(), request.description());
    }

    /**
     * Converts an update request into a command.
     *
     * @param request inbound request
     * @return the update command
     */
    public static UpdateFundCommand toUpdateCommand(final FundUpdateRequest request) {
        return new UpdateFundCommand(request.name(), request.category(), request.riskLevel(),
                request.managementFee(), request.depositaryFee(), request.description());
    }

    /**
     * Converts a domain fund into a summary response.
     *
     * @param fund domain fund
     * @return the summary DTO
     */
    public static FundSummaryResponse toSummary(final Fund fund) {
        return new FundSummaryResponse(fund.getIsin(), fund.getName(), fund.getCategory(),
                fund.getCurrency(), fund.getCurrentNav());
    }

    /**
     * Converts a domain fund into a detail response.
     *
     * @param fund domain fund
     * @return the detail DTO
     */
    public static FundDetailResponse toDetail(final Fund fund) {
        return new FundDetailResponse(fund.getIsin(), fund.getName(), fund.getCategory(), fund.getCurrency(),
                fund.getCurrentNav(), fund.getRiskLevel(), fund.getManagementFee(), fund.getDepositaryFee(),
                fund.getAssetsUnderManagement(), fund.getDescription());
    }
}
