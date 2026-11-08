package com.example.vocabularyservice.infrastructure.adapter.in.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.vocabularyservice.application.port.in.UpsertProgressCommand;
import com.example.vocabularyservice.application.port.in.VocabularyUseCases;
import com.example.vocabularyservice.domain.exception.VocabularyItemNotFoundException;
import com.example.vocabularyservice.domain.model.VocabularyProgress;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Web-layer tests for {@link VocabularyProgressController}.
 */
@WebMvcTest(VocabularyProgressController.class)
class VocabularyProgressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VocabularyUseCases useCases;

    private static final String USER = "11111111-1111-1111-1111-111111111111";

    @Test
    void listReturns200() throws Exception {
        when(useCases.listProgress(any(UUID.class)))
                .thenReturn(List.of(new VocabularyProgress(UUID.fromString(USER), 1L, 2, 1, null, null)));
        mockMvc.perform(get("/v1/vocabulary/users/{userId}/progress", USER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemId").value(1));
    }

    @Test
    void upsertReturns200() throws Exception {
        when(useCases.upsertProgress(any(UpsertProgressCommand.class)))
                .thenReturn(new VocabularyProgress(UUID.fromString(USER), 1L, 2, 1, null, null));
        mockMvc.perform(put("/v1/vocabulary/users/{userId}/progress", USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":1,\"streak\":2,\"lastGrade\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.streak").value(2));
    }

    @Test
    void upsertReturns400OnInvalidGrade() throws Exception {
        mockMvc.perform(put("/v1/vocabulary/users/{userId}/progress", USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":1,\"lastGrade\":3}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("BAD_REQUEST"));
    }

    @Test
    void upsertReturns404WhenItemMissing() throws Exception {
        when(useCases.upsertProgress(any(UpsertProgressCommand.class)))
                .thenThrow(new VocabularyItemNotFoundException(9L));
        mockMvc.perform(put("/v1/vocabulary/users/{userId}/progress", USER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":9,\"lastGrade\":1}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("RESOURCE_NOT_FOUND"));
    }
}
