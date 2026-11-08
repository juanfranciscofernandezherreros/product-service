package com.example.fundservice.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.fundservice.domain.exception.InvalidFundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link NavPoint} value object.
 */
class NavPointTest {

    @Test
    void createsValidNavPoint() {
        final NavPoint point = new NavPoint(LocalDate.of(2026, 11, 6), new BigDecimal("15.40"));
        assertThat(point.nav()).isEqualByComparingTo("15.40");
    }

    @Test
    void rejectsNullDate() {
        assertThatThrownBy(() -> new NavPoint(null, BigDecimal.ONE))
                .isInstanceOf(InvalidFundException.class);
    }

    @Test
    void rejectsNegativeNav() {
        assertThatThrownBy(() -> new NavPoint(LocalDate.now(), new BigDecimal("-1")))
                .isInstanceOf(InvalidFundException.class);
    }
}
