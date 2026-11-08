package com.example.productcrud.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.productcrud.domain.exception.InvalidProductException;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Product} domain invariants.
 */
class ProductTest {

    @Test
    void createsValidProductAndGeneratesId() {
        final Product product = Product.create("PROD-001", "Keyboard", new BigDecimal("99.99"));

        assertThat(product.getId()).isNotNull();
        assertThat(product.getSku()).isEqualTo("PROD-001");
    }

    @Test
    void rejectsShortSku() {
        assertThatThrownBy(() -> Product.create("ab", "Keyboard", new BigDecimal("99.99")))
                .isInstanceOf(InvalidProductException.class);
    }

    @Test
    void rejectsShortName() {
        assertThatThrownBy(() -> Product.create("PROD-001", "a", new BigDecimal("99.99")))
                .isInstanceOf(InvalidProductException.class);
    }

    @Test
    void rejectsPriceBelowMinimum() {
        assertThatThrownBy(() -> Product.create("PROD-001", "Keyboard", new BigDecimal("0.00")))
                .isInstanceOf(InvalidProductException.class);
    }

    @Test
    void withUpdatedDetailsKeepsId() {
        final UUID id = UUID.randomUUID();
        final Product product = new Product(id, "PROD-001", "Keyboard", new BigDecimal("99.99"));

        final Product updated = product.withUpdatedDetails("PROD-002", "Mouse", new BigDecimal("49.50"));

        assertThat(updated.getId()).isEqualTo(id);
        assertThat(updated.getName()).isEqualTo("Mouse");
    }

    @Test
    void equalsBasedOnId() {
        final UUID id = UUID.randomUUID();
        final Product one = new Product(id, "PROD-001", "Keyboard", new BigDecimal("99.99"));
        final Product two = new Product(id, "PROD-999", "Other", new BigDecimal("10.00"));

        assertThat(one).isEqualTo(two);
    }
}
