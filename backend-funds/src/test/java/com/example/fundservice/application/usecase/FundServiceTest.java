package com.example.fundservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.fundservice.application.port.in.CreateFundCommand;
import com.example.fundservice.application.port.in.ListFundsQuery;
import com.example.fundservice.application.port.in.PagedFunds;
import com.example.fundservice.application.port.in.UpdateFundCommand;
import com.example.fundservice.application.port.out.FundRepositoryPort;
import com.example.fundservice.application.port.out.OrderRepositoryPort;
import com.example.fundservice.domain.exception.FundAlreadyExistsException;
import com.example.fundservice.domain.exception.FundHasActivePositionsException;
import com.example.fundservice.domain.exception.FundNotFoundException;
import com.example.fundservice.domain.model.Fund;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link FundService}.
 */
@ExtendWith(MockitoExtension.class)
class FundServiceTest {

    @Mock
    private FundRepositoryPort fundRepository;
    @Mock
    private OrderRepositoryPort orderRepository;

    private FundService service;

    @BeforeEach
    void setUp() {
        service = new FundService(fundRepository, orderRepository);
    }

    private CreateFundCommand createCommand() {
        return new CreateFundCommand("ES01", "Global", "RV", "EUR", 5,
                new BigDecimal("1.35"), new BigDecimal("0.10"), "desc");
    }

    @Test
    void createPersistsWhenNew() {
        when(fundRepository.existsByIsin("ES01")).thenReturn(false);
        when(fundRepository.save(any(Fund.class))).thenAnswer(inv -> inv.getArgument(0));

        final Fund result = service.create(createCommand());

        assertThat(result.getIsin()).isEqualTo("ES01");
    }

    @Test
    void createThrowsWhenDuplicate() {
        when(fundRepository.existsByIsin("ES01")).thenReturn(true);

        assertThatThrownBy(() -> service.create(createCommand()))
                .isInstanceOf(FundAlreadyExistsException.class);
        verify(fundRepository, never()).save(any(Fund.class));
    }

    @Test
    void getByIsinReturnsWhenPresent() {
        final Fund fund = Fund.register("ES01", "G", "RV", "EUR", 5, null, null, null);
        when(fundRepository.findByIsin("ES01")).thenReturn(Optional.of(fund));

        assertThat(service.getByIsin("ES01")).isEqualTo(fund);
    }

    @Test
    void getByIsinThrowsWhenMissing() {
        when(fundRepository.findByIsin("ES01")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByIsin("ES01")).isInstanceOf(FundNotFoundException.class);
    }

    @Test
    void listReturnsPageWithTotal() {
        final Fund fund = Fund.register("ES01", "G", "RV", "EUR", 5, null, null, null);
        when(fundRepository.findByFilter(eq("RV"), eq("EUR"), anyInt(), anyInt())).thenReturn(List.of(fund));
        when(fundRepository.countByFilter("RV", "EUR")).thenReturn(1L);

        final PagedFunds page = service.list(new ListFundsQuery("RV", "EUR", 20, 0));

        assertThat(page.total()).isEqualTo(1L);
        assertThat(page.items()).containsExactly(fund);
    }

    @Test
    void updateMergesWhenPresent() {
        final Fund existing = Fund.register("ES01", "Old", "RV", "EUR", 5, null, null, null);
        when(fundRepository.findByIsin("ES01")).thenReturn(Optional.of(existing));
        when(fundRepository.save(any(Fund.class))).thenAnswer(inv -> inv.getArgument(0));

        final Fund result = service.update("ES01",
                new UpdateFundCommand("New", null, 6, null, null, null));

        assertThat(result.getName()).isEqualTo("New");
        assertThat(result.getRiskLevel()).isEqualTo(6);
    }

    @Test
    void updateThrowsWhenMissing() {
        when(fundRepository.findByIsin("ES01")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update("ES01",
                new UpdateFundCommand("N", null, null, null, null, null)))
                .isInstanceOf(FundNotFoundException.class);
    }

    @Test
    void deleteRemovesWhenNoActivePositions() {
        when(fundRepository.existsByIsin("ES01")).thenReturn(true);
        when(orderRepository.hasActivePositions("ES01")).thenReturn(false);

        service.delete("ES01");

        verify(fundRepository).deleteByIsin("ES01");
    }

    @Test
    void deleteThrowsWhenMissing() {
        when(fundRepository.existsByIsin("ES01")).thenReturn(false);

        assertThatThrownBy(() -> service.delete("ES01")).isInstanceOf(FundNotFoundException.class);
    }

    @Test
    void deleteThrowsWhenActivePositions() {
        when(fundRepository.existsByIsin("ES01")).thenReturn(true);
        when(orderRepository.hasActivePositions("ES01")).thenReturn(true);

        assertThatThrownBy(() -> service.delete("ES01"))
                .isInstanceOf(FundHasActivePositionsException.class);
        verify(fundRepository, never()).deleteByIsin("ES01");
    }
}
