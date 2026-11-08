package com.example.productcrud.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.productcrud.application.port.in.CreateProductUseCase;
import com.example.productcrud.application.port.in.DeleteProductUseCase;
import com.example.productcrud.application.port.in.GetProductUseCase;
import com.example.productcrud.application.port.in.ListProductsUseCase;
import com.example.productcrud.application.port.in.ProductCommand;
import com.example.productcrud.application.port.in.UpdateProductUseCase;
import com.example.productcrud.domain.exception.ProductNotFoundException;
import com.example.productcrud.domain.model.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Web-layer tests for {@link ProductController} covering every endpoint.
 */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateProductUseCase createProductUseCase;
    @MockBean
    private GetProductUseCase getProductUseCase;
    @MockBean
    private ListProductsUseCase listProductsUseCase;
    @MockBean
    private UpdateProductUseCase updateProductUseCase;
    @MockBean
    private DeleteProductUseCase deleteProductUseCase;

    private Product sample(final UUID id) {
        return new Product(id, "PROD-001", "Keyboard", new BigDecimal("99.99"));
    }

    @Test
    void createReturns201() throws Exception {
        final UUID id = UUID.randomUUID();
        when(createProductUseCase.create(any(ProductCommand.class))).thenReturn(sample(id));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sku\":\"PROD-001\",\"name\":\"Keyboard\",\"price\":99.99}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.sku").value("PROD-001"));
    }

    @Test
    void createReturns400OnInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sku\":\"a\",\"name\":\"\",\"price\":0}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByIdReturns200() throws Exception {
        final UUID id = UUID.randomUUID();
        when(getProductUseCase.getById(id)).thenReturn(sample(id));

        mockMvc.perform(get("/api/v1/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void getByIdReturns404() throws Exception {
        final UUID id = UUID.randomUUID();
        when(getProductUseCase.getById(id)).thenThrow(new ProductNotFoundException(id));

        mockMvc.perform(get("/api/v1/products/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllReturns200() throws Exception {
        when(listProductsUseCase.listAll()).thenReturn(List.of(sample(UUID.randomUUID())));

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("PROD-001"));
    }

    @Test
    void updateReturns200() throws Exception {
        final UUID id = UUID.randomUUID();
        when(updateProductUseCase.update(eq(id), any(ProductCommand.class)))
                .thenReturn(new Product(id, "PROD-002", "Mouse", new BigDecimal("49.50")));

        mockMvc.perform(put("/api/v1/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sku\":\"PROD-002\",\"name\":\"Mouse\",\"price\":49.50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Mouse"));
    }

    @Test
    void updateReturns404() throws Exception {
        final UUID id = UUID.randomUUID();
        when(updateProductUseCase.update(eq(id), any(ProductCommand.class)))
                .thenThrow(new ProductNotFoundException(id));

        mockMvc.perform(put("/api/v1/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"sku\":\"PROD-002\",\"name\":\"Mouse\",\"price\":49.50}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReturns204() throws Exception {
        final UUID id = UUID.randomUUID();
        doNothing().when(deleteProductUseCase).delete(id);

        mockMvc.perform(delete("/api/v1/products/{id}", id))
                .andExpect(status().isNoContent());
        verify(deleteProductUseCase).delete(id);
    }

    @Test
    void deleteReturns404() throws Exception {
        final UUID id = UUID.randomUUID();
        doThrow(new ProductNotFoundException(id)).when(deleteProductUseCase).delete(id);

        mockMvc.perform(delete("/api/v1/products/{id}", id))
                .andExpect(status().isNotFound());
    }
}
