package com.example.miscservice.domain;

import com.example.miscservice.domain.exception.InvalidMiscException;
import com.example.miscservice.domain.model.LogbookCategory;
import com.example.miscservice.domain.model.LogbookNote;
import com.example.miscservice.domain.model.Suggestion;
import com.example.miscservice.domain.model.SuggestionStatus;
import com.example.miscservice.domain.model.WritingDraft;
import com.example.miscservice.domain.model.WritingType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MiscDomainTest {

    @Test
    void suggestionRejectsShortTitle() {
        assertThatThrownBy(() -> Suggestion.create("abc", "Descripcion larga ok"))
                .isInstanceOf(InvalidMiscException.class);
    }

    @Test
    void suggestionRejectsShortDescription() {
        assertThatThrownBy(() -> Suggestion.create("Titulo valido", "corta"))
                .isInstanceOf(InvalidMiscException.class);
    }

    @Test
    void suggestionWithStatusKeepsData() {
        final Suggestion s = Suggestion.create("Titulo valido", "Descripcion suficiente larga")
                .withStatus(SuggestionStatus.RECHAZADA);
        assertThat(s.estado()).isEqualTo(SuggestionStatus.RECHAZADA);
        assertThat(s.titulo()).isEqualTo("Titulo valido");
    }

    @Test
    void suggestionStatusFromValueInvalid() {
        assertThatThrownBy(() -> SuggestionStatus.fromValue("unknown"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void categoryRejectsBlankName() {
        assertThatThrownBy(() -> LogbookCategory.create(UUID.randomUUID(), "  "))
                .isInstanceOf(InvalidMiscException.class);
    }

    @Test
    void noteRejectsBlankContent() {
        assertThatThrownBy(() -> LogbookNote.create(UUID.randomUUID(), null, ""))
                .isInstanceOf(InvalidMiscException.class);
    }

    @Test
    void draftAppliesDefaultTitle() {
        final WritingDraft d = WritingDraft.create(UUID.randomUUID(), WritingType.STORY, null, null);
        assertThat(d.titulo()).isEqualTo("Mi writing B2");
        assertThat(d.contenido()).isEmpty();
    }

    @Test
    void writingTypeFromValueInvalid() {
        assertThatThrownBy(() -> WritingType.fromValue("poem"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void writingTypeRoundTrip() {
        assertThat(WritingType.fromValue("email_formal")).isEqualTo(WritingType.EMAIL_FORMAL);
        assertThat(WritingType.EMAIL_FORMAL.value()).isEqualTo("email_formal");
    }
}
