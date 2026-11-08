package com.example.fundservice.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.fundservice.application.port.in.PlaceOrderCommand;
import com.example.fundservice.application.port.in.PlaceOrderUseCase;
import com.example.fundservice.domain.exception.FundNotFoundException;
import com.example.fundservice.domain.model.OperationType;
import com.example.fundservice.domain.model.Order;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Web-layer tests for {@link OrderController}.
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaceOrderUseCase placeOrderUseCase;

    private static final String VALID_BODY =
            "{\"accountId\":\"ACC-1\",\"isin\":\"ES0123456789\","
            + "\"operationType\":\"SUBSCRIPTION\",\"amount\":1000.00,\"currency\":\"EUR\"}";

    @Test
    void returns201() throws Exception {
        when(placeOrderUseCase.place(any(PlaceOrderCommand.class))).thenReturn(
                Order.place("ACC-1", "ES0123456789", OperationType.SUBSCRIPTION, new BigDecimal("1000.00"), "EUR"));

        mockMvc.perform(post("/v1/orders").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void returns400OnInvalid() throws Exception {
        mockMvc.perform(post("/v1/orders").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountId\":\"\",\"isin\":\"\",\"amount\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void returns404WhenFundMissing() throws Exception {
        when(placeOrderUseCase.place(any(PlaceOrderCommand.class)))
                .thenThrow(new FundNotFoundException("ES0123456789"));

        mockMvc.perform(post("/v1/orders").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"));
    }
}
