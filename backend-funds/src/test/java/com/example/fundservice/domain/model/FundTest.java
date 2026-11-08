package com.example.fundservice.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.fundservice.domain.exception.InvalidFundException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Fund} domain invariants.
 */
class FundTest {

    @Test
    void registersValidFund() {
        final Fund fund = Fund.register("ES0123456789", "Global Growth FI", "RV", "EUR", 5,
                new BigDecimal("1.35"), new BigDecimal("0.10"), "desc");

        assertThat(fund.getIsin()).isEqualTo("ES0123456789");
        assertThat(fund.getRiskLevel()).isEqualTo(5);
        assertThat(fund.getCurrentNav()).isNull();
    }

    @Test
    void rejectsBlankIsin() {
        assertThatThrownBy(() -> Fund.register(" ", "n", "c", "EUR", 5, null, null, null))
                .isInstanceOf(InvalidFundException.class);
    }

    @Test
    void rejectsRiskLevelOutOfRange() {
        assertThatThrownBy(() -> Fund.register("ES01", "n", "c", "EUR", 9, null, null, null))
                .isInstanceOf(InvalidFundException.class);
    }

    @Test
    void rejectsNegativeFee() {
        assertThatThrownBy(() -> Fund.register("ES01", "n", "c", "EUR", 5, new BigDecimal("-1"), null, null))
                .isInstanceOf(InvalidFundException.class);
    }

    @Test
    void updateKeepsIsinAndMergesNulls() {
        final Fund fund = Fund.register("ES01", "Old", "RV", "EUR", 5, new BigDecimal("1.0"), null, "d");

        final Fund updated = fund.withUpdatedDetails("New", null, 6, null, new BigDecimal("0.2"), null);

        assertThat(updated.getIsin()).isEqualTo("ES01");
        assertThat(updated.getName()).isEqualTo("New");
        assertThat(updated.getCategory()).isEqualTo("RV");
        assertThat(updated.getRiskLevel()).isEqualTo(6);
        assertThat(updated.getManagementFee()).isEqualByComparingTo("1.0");
        assertThat(updated.getDepositaryFee()).isEqualByComparingTo("0.2");
    }

    @Test
    void equalsBasedOnIsin() {
        final Fund a = Fund.register("ES01", "A", "c", "EUR", 1, null, null, null);
        final Fund b = Fund.register("ES01", "B", "d", "USD", 7, null, null, null);
        assertThat(a).isEqualTo(b).hasSameHashCodeAs(b);
    }
}
