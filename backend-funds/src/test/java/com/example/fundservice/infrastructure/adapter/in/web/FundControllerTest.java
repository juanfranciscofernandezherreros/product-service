package com.example.fundservice.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.fundservice.application.port.in.CreateFundCommand;
import com.example.fundservice.application.port.in.CreateFundUseCase;
import com.example.fundservice.application.port.in.DeleteFundUseCase;
import com.example.fundservice.application.port.in.GetFundUseCase;
import com.example.fundservice.application.port.in.ListFundsUseCase;
import com.example.fundservice.application.port.in.PagedFunds;
import com.example.fundservice.application.port.in.UpdateFundCommand;
import com.example.fundservice.application.port.in.UpdateFundUseCase;
import com.example.fundservice.domain.exception.FundAlreadyExistsException;
import com.example.fundservice.domain.exception.FundHasActivePositionsException;
import com.example.fundservice.domain.exception.FundNotFoundException;
import com.example.fundservice.domain.model.Fund;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Web-layer tests for {@link FundController}.
 */
@WebMvcTest(FundController.class)
class FundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateFundUseCase createFundUseCase;
    @MockBean
    private GetFundUseCase getFundUseCase;
    @MockBean
    private ListFundsUseCase listFundsUseCase;
    @MockBean
    private UpdateFundUseCase updateFundUseCase;
    @MockBean
    private DeleteFundUseCase deleteFundUseCase;

    private static final String VALID_BODY =
            "{\"isin\":\"ES0123456789\",\"name\":\"Global\",\"category\":\"RV\","
            + "\"currency\":\"EUR\",\"riskLevel\":5,\"managementFee\":1.35,\"depositaryFee\":0.10}";

    private Fund sample() {
        return Fund.register("ES0123456789", "Global", "RV", "EUR", 5,
                new BigDecimal("1.35"), new BigDecimal("0.10"), "desc");
    }

    @Test
    void createReturns201() throws Exception {
        when(createFundUseCase.create(any(CreateFundCommand.class))).thenReturn(sample());

        mockMvc.perform(post("/v1/funds").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isin").value("ES0123456789"))
                .andExpect(jsonPath("$.riskLevel").value(5));
    }

    @Test
    void createReturns400OnInvalid() throws Exception {
        mockMvc.perform(post("/v1/funds").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"isin\":\"\",\"name\":\"\",\"category\":\"\",\"currency\":\"\",\"riskLevel\":9}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void createReturns409OnDuplicate() throws Exception {
        when(createFundUseCase.create(any(CreateFundCommand.class)))
                .thenThrow(new FundAlreadyExistsException("ES0123456789"));

        mockMvc.perform(post("/v1/funds").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("RESOURCE_ALREADY_EXISTS"));
    }

    @Test
    void getReturns200() throws Exception {
        when(getFundUseCase.getByIsin("ES0123456789")).thenReturn(sample());

        mockMvc.perform(get("/v1/funds/{isin}", "ES0123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Global"));
    }

    @Test
    void getReturns404() throws Exception {
        when(getFundUseCase.getByIsin("XX")).thenThrow(new FundNotFoundException("XX"));

        mockMvc.perform(get("/v1/funds/{isin}", "XX"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void listReturns200() throws Exception {
        when(listFundsUseCase.list(any())).thenReturn(new PagedFunds(1L, List.of(sample())));

        mockMvc.perform(get("/v1/funds?currency=EUR&limit=2&offset=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].isin").value("ES0123456789"));
    }

    @Test
    void updateReturns200() throws Exception {
        when(updateFundUseCase.update(eq("ES0123456789"), any(UpdateFundCommand.class))).thenReturn(sample());

        mockMvc.perform(put("/v1/funds/{isin}", "ES0123456789")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"New\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void updateReturns404() throws Exception {
        when(updateFundUseCase.update(eq("XX"), any(UpdateFundCommand.class)))
                .thenThrow(new FundNotFoundException("XX"));

        mockMvc.perform(put("/v1/funds/{isin}", "XX")
                        .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"New\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReturns204() throws Exception {
        doNothing().when(deleteFundUseCase).delete("ES0123456789");

        mockMvc.perform(delete("/v1/funds/{isin}", "ES0123456789"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteReturns404() throws Exception {
        doThrow(new FundNotFoundException("XX")).when(deleteFundUseCase).delete("XX");

        mockMvc.perform(delete("/v1/funds/{isin}", "XX"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReturns422OnActivePositions() throws Exception {
        doThrow(new FundHasActivePositionsException("ES0123456789"))
                .when(deleteFundUseCase).delete("ES0123456789");

        mockMvc.perform(delete("/v1/funds/{isin}", "ES0123456789"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.code").value("FUND_HAS_ACTIVE_POSITIONS"));
    }
}
