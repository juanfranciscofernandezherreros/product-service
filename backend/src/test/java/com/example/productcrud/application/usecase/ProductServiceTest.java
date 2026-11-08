package com.example.productcrud.application.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.productcrud.application.port.in.ProductCommand;
import com.example.productcrud.application.port.out.ProductRepositoryPort;
import com.example.productcrud.domain.exception.ProductNotFoundException;
import com.example.productcrud.domain.model.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link ProductService} covering all use cases.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepositoryPort repository;

    private ProductService service;

    @BeforeEach
    void setUp() {
        service = new ProductService(repository);
    }

    @Test
    void createPersistsAndReturnsProduct() {
        final ProductCommand command = new ProductCommand("PROD-001", "Keyboard", new BigDecimal("99.99"));
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Product result = service.create(command);

        final ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(repository).save(captor.capture());
        assertThat(result.getSku()).isEqualTo("PROD-001");
        assertThat(result.getId()).isNotNull();
        assertThat(captor.getValue().getName()).isEqualTo("Keyboard");
    }

    @Test
    void getByIdReturnsProductWhenPresent() {
        final UUID id = UUID.randomUUID();
        final Product product = new Product(id, "PROD-001", "Keyboard", new BigDecimal("99.99"));
        when(repository.findById(id)).thenReturn(Optional.of(product));

        assertThat(service.getById(id)).isEqualTo(product);
    }

    @Test
    void getByIdThrowsWhenMissing() {
        final UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void listAllReturnsEveryProduct() {
        final Product product = new Product(UUID.randomUUID(), "PROD-001", "Keyboard", new BigDecimal("99.99"));
        when(repository.findAll()).thenReturn(List.of(product));

        assertThat(service.listAll()).containsExactly(product);
    }

    @Test
    void updateChangesDetailsWhenPresent() {
        final UUID id = UUID.randomUUID();
        final Product existing = new Product(id, "PROD-001", "Keyboard", new BigDecimal("99.99"));
        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        final Product result = service.update(id, new ProductCommand("PROD-002", "Mouse", new BigDecimal("49.50")));

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getSku()).isEqualTo("PROD-002");
        assertThat(result.getName()).isEqualTo("Mouse");
        assertThat(result.getPrice()).isEqualByComparingTo("49.50");
    }

    @Test
    void updateThrowsWhenMissing() {
        final UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(id, new ProductCommand("PROD-002", "Mouse", new BigDecimal("49.50"))))
                .isInstanceOf(ProductNotFoundException.class);
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void deleteRemovesWhenPresent() {
        final UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        service.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    void deleteThrowsWhenMissing() {
        final UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(id))
                .isInstanceOf(ProductNotFoundException.class);
        verify(repository, never()).deleteById(any(UUID.class));
    }
}
