package com.example.transactionservice.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.transactionservice.application.port.in.CreateTransactionCommand;
import com.example.transactionservice.application.port.in.CreateTransactionUseCase;
import com.example.transactionservice.application.port.in.GetTransactionUseCase;
import com.example.transactionservice.application.port.in.ListTransactionsQuery;
import com.example.transactionservice.application.port.in.ListTransactionsUseCase;
import com.example.transactionservice.application.port.in.PagedTransactions;
import com.example.transactionservice.domain.exception.TransactionNotFoundException;
import com.example.transactionservice.domain.model.Transaction;
import com.example.transactionservice.domain.model.TransactionStatus;
import com.example.transactionservice.domain.model.TransactionType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Web-layer tests for {@link TransactionController} covering every endpoint.
 */
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateTransactionUseCase createTransactionUseCase;
    @MockBean
    private GetTransactionUseCase getTransactionUseCase;
    @MockBean
    private ListTransactionsUseCase listTransactionsUseCase;

    private Transaction sample(final UUID id) {
        return new Transaction(id, new BigDecimal("150.50"), "EUR", TransactionType.DEBIT,
                TransactionStatus.PENDING, UUID.randomUUID(), "shopping", Instant.parse("2026-06-06T14:30:00Z"));
    }

    @Test
    void createReturns201() throws Exception {
        final UUID id = UUID.randomUUID();
        when(createTransactionUseCase.create(any(CreateTransactionCommand.class))).thenReturn(sample(id));

        mockMvc.perform(post("/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":150.50,\"currency\":\"EUR\",\"type\":\"debit\","
                                + "\"account_id\":\"f47ac10b-58cc-4372-a567-0e02b2c3d479\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.status").value("pending"))
                .andExpect(jsonPath("$.type").value("debit"))
                .andExpect(jsonPath("$.account_id").exists());
    }

    @Test
    void createReturns400OnInvalidPayload() throws Exception {
        mockMvc.perform(post("/v1/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\":0,\"currency\":\"E\",\"type\":\"debit\","
                                + "\"account_id\":\"f47ac10b-58cc-4372-a567-0e02b2c3d479\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }

    @Test
    void getByIdReturns200() throws Exception {
        final UUID id = UUID.randomUUID();
        when(getTransactionUseCase.getById(id)).thenReturn(sample(id));

        mockMvc.perform(get("/v1/transactions/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.currency").value("EUR"));
    }

    @Test
    void getByIdReturns404() throws Exception {
        final UUID id = UUID.randomUUID();
        when(getTransactionUseCase.getById(id)).thenThrow(new TransactionNotFoundException(id));

        mockMvc.perform(get("/v1/transactions/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("NOT_FOUND"));
    }

    @Test
    void listReturns200WithEnvelope() throws Exception {
        when(listTransactionsUseCase.list(any(ListTransactionsQuery.class)))
                .thenReturn(new PagedTransactions(1L, 20, 0, List.of(sample(UUID.randomUUID()))));

        mockMvc.perform(get("/v1/transactions?limit=20&offset=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.limit").value(20))
                .andExpect(jsonPath("$.offset").value(0))
                .andExpect(jsonPath("$.data[0].status").value("pending"));
    }

    @Test
    void listReturns400OnInvalidStatus() throws Exception {
        mockMvc.perform(get("/v1/transactions?status=unknown"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"));
    }
}
