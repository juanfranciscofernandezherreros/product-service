package com.example.vocabularyservice.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.vocabularyservice.application.port.in.ItemCommands.CreateItemCommand;
import com.example.vocabularyservice.application.port.in.ItemCommands.ListItemsQuery;
import com.example.vocabularyservice.application.port.in.ItemCommands.UpdateItemCommand;
import com.example.vocabularyservice.application.port.in.PagedItems;
import com.example.vocabularyservice.application.port.in.VocabularyUseCases;
import com.example.vocabularyservice.domain.exception.DuplicateVocabularyItemException;
import com.example.vocabularyservice.domain.exception.VocabularyItemNotFoundException;
import com.example.vocabularyservice.domain.model.Level;
import com.example.vocabularyservice.domain.model.VocabularyItem;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Web-layer tests for {@link VocabularyItemController}.
 */
@WebMvcTest(VocabularyItemController.class)
class VocabularyItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VocabularyUseCases useCases;

    private VocabularyItem sample() {
        return new VocabularyItem(1L, "book", "libro", "a book", Level.A1);
    }

    @Test
    void createReturns201() throws Exception {
        when(useCases.createItem(any(CreateItemCommand.class))).thenReturn(sample());

        mockMvc.perform(post("/v1/vocabulary/items").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"en\":\"book\",\"es\":\"libro\",\"level\":\"A1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.level").value("A1"));
    }

    @Test
    void createReturns400OnInvalid() throws Exception {
        mockMvc.perform(post("/v1/vocabulary/items").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"en\":\"\",\"es\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void createReturns409OnDuplicate() throws Exception {
        when(useCases.createItem(any(CreateItemCommand.class)))
                .thenThrow(new DuplicateVocabularyItemException("book"));

        mockMvc.perform(post("/v1/vocabulary/items").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"en\":\"book\",\"es\":\"libro\",\"level\":\"A1\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("RESOURCE_ALREADY_EXISTS"));
    }

    @Test
    void getReturns200() throws Exception {
        when(useCases.getItem(1L)).thenReturn(sample());
        mockMvc.perform(get("/v1/vocabulary/items/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.en").value("book"));
    }

    @Test
    void getReturns404() throws Exception {
        when(useCases.getItem(9L)).thenThrow(new VocabularyItemNotFoundException(9L));
        mockMvc.perform(get("/v1/vocabulary/items/{id}", 9))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    void listReturns200() throws Exception {
        when(useCases.listItems(any(ListItemsQuery.class))).thenReturn(new PagedItems(1L, List.of(sample())));
        mockMvc.perform(get("/v1/vocabulary/items?level=A1&limit=20&offset=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1))
                .andExpect(jsonPath("$.items[0].en").value("book"));
    }

    @Test
    void updateReturns200() throws Exception {
        when(useCases.updateItem(eq(1L), any(UpdateItemCommand.class))).thenReturn(sample());
        mockMvc.perform(put("/v1/vocabulary/items/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                        .content("{\"es\":\"libro\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteReturns204() throws Exception {
        doNothing().when(useCases).deleteItem(1L);
        mockMvc.perform(delete("/v1/vocabulary/items/{id}", 1)).andExpect(status().isNoContent());
    }

    @Test
    void deleteReturns404() throws Exception {
        doThrow(new VocabularyItemNotFoundException(9L)).when(useCases).deleteItem(9L);
        mockMvc.perform(delete("/v1/vocabulary/items/{id}", 9)).andExpect(status().isNotFound());
    }
}
