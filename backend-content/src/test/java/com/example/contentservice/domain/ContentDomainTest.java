package com.example.contentservice.domain;

import com.example.contentservice.domain.exception.InvalidContentException;
import com.example.contentservice.domain.model.Article;
import com.example.contentservice.domain.model.Category;
import com.example.contentservice.domain.model.Collection;
import com.example.contentservice.domain.model.CollectionType;
import com.example.contentservice.domain.model.Comment;
import com.example.contentservice.domain.model.Downloadable;
import com.example.contentservice.domain.model.MoviePhrase;
import com.example.contentservice.domain.model.Page;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContentDomainTest {

    @Test
    void articleRejectsBadRuta() {
        assertThatThrownBy(() -> Article.create("bad-ruta", "s", "T", null, "<p>x</p>", "https://u",
                null, null, null)).isInstanceOf(InvalidContentException.class);
    }

    @Test
    void articleCreateDefaultsOrigin() {
        final Article a = Article.create("post/mi-slug.html", "mi-slug", "T", "d", "<p>x</p>",
                "https://u", "cat", null, "B1");
        assertThat(a.origen()).isEqualTo("github");
        assertThat(a.esArticuloIndice()).isFalse();
    }

    @Test
    void articleUpdateKeepsUnchangedFields() {
        final Article a = Article.create("post/mi-slug.html", "mi-slug", "T", "d", "<p>x</p>",
                "https://u", "cat", null, "B1").update(null, null, null, null, null);
        assertThat(a.titulo()).isEqualTo("T");
    }

    @Test
    void commentRejectsEmptyContent() {
        assertThatThrownBy(() -> Comment.create("s", null, null, "  "))
                .isInstanceOf(InvalidContentException.class);
    }

    @Test
    void categoryRequiresName() {
        assertThatThrownBy(() -> Category.create("s", "", null, null))
                .isInstanceOf(InvalidContentException.class);
    }

    @Test
    void pageRequiresHtml() {
        assertThatThrownBy(() -> Page.create("r", "T", null, "", "https://u"))
                .isInstanceOf(InvalidContentException.class);
    }

    @Test
    void collectionRejectsBadFileName() {
        assertThatThrownBy(() -> Collection.create("s", "T", null, CollectionType.ARTICULOS, false,
                "file.txt", null)).isInstanceOf(InvalidContentException.class);
    }

    @Test
    void collectionTypeFromValueInvalid() {
        assertThatThrownBy(() -> CollectionType.fromValue("otros"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void downloadableRequiresUrl() {
        assertThatThrownBy(() -> Downloadable.create("s", "T", null, null, null, "", null))
                .isInstanceOf(InvalidContentException.class);
    }

    @Test
    void downloadableDefaultsCategory() {
        assertThat(Downloadable.create("s", "T", null, null, null, "https://f", null).categoria())
                .isEqualTo("apuntes");
    }

    @Test
    void moviePhraseRejectsNonPositiveNumber() {
        assertThatThrownBy(() -> MoviePhrase.create("m", "Movie", 0, "00:00", "hi", null, null))
                .isInstanceOf(InvalidContentException.class);
    }

    @Test
    void moviePhraseDefaultsLabelsAndNotes() {
        final MoviePhrase m = MoviePhrase.create("m", "Movie", 1, "00:01", "hi", null, null);
        assertThat(m.tenseLabels()).isEmpty();
        assertThat(m.notes()).isEmpty();
    }
}
