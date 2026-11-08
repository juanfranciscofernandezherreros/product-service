package com.example.fundservice.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.fundservice.application.port.in.GetNavHistoryUseCase;
import com.example.fundservice.application.port.in.NavHistory;
import com.example.fundservice.domain.exception.FundNotFoundException;
import com.example.fundservice.domain.model.NavPoint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Web-layer tests for {@link NavController}.
 */
@WebMvcTest(NavController.class)
class NavControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetNavHistoryUseCase getNavHistoryUseCase;

    @Test
    void returns200WithHistory() throws Exception {
        when(getNavHistoryUseCase.history(any())).thenReturn(new NavHistory("ES01",
                List.of(new NavPoint(LocalDate.of(2026, 11, 6), new BigDecimal("15.40")))));

        mockMvc.perform(get("/v1/funds/{isin}/nav", "ES01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isin").value("ES01"))
                .andExpect(jsonPath("$.history[0].nav").value(15.40));
    }

    @Test
    void returns404WhenFundMissing() throws Exception {
        when(getNavHistoryUseCase.history(any())).thenThrow(new FundNotFoundException("XX"));

        mockMvc.perform(get("/v1/funds/{isin}/nav", "XX"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"));
    }
}
