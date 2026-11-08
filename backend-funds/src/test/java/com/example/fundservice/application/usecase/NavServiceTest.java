package com.example.fundservice.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.fundservice.application.port.in.NavHistory;
import com.example.fundservice.application.port.in.NavHistoryQuery;
import com.example.fundservice.application.port.out.FundRepositoryPort;
import com.example.fundservice.application.port.out.NavRepositoryPort;
import com.example.fundservice.domain.exception.FundNotFoundException;
import com.example.fundservice.domain.model.NavPoint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link NavService}.
 */
@ExtendWith(MockitoExtension.class)
class NavServiceTest {

    @Mock
    private FundRepositoryPort fundRepository;
    @Mock
    private NavRepositoryPort navRepository;

    private NavService service;

    @BeforeEach
    void setUp() {
        service = new NavService(fundRepository, navRepository);
    }

    @Test
    void returnsHistoryWhenFundExists() {
        when(fundRepository.existsByIsin("ES01")).thenReturn(true);
        when(navRepository.findHistory(any(), any(), any()))
                .thenReturn(List.of(new NavPoint(LocalDate.of(2026, 11, 6), new BigDecimal("15.40"))));

        final NavHistory result = service.history(new NavHistoryQuery("ES01", null, null));

        assertThat(result.isin()).isEqualTo("ES01");
        assertThat(result.history()).hasSize(1);
    }

    @Test
    void throwsWhenFundMissing() {
        when(fundRepository.existsByIsin("ES01")).thenReturn(false);

        assertThatThrownBy(() -> service.history(new NavHistoryQuery("ES01", null, null)))
                .isInstanceOf(FundNotFoundException.class);
    }
}
