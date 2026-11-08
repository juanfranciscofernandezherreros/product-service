package com.example.transactionservice.infrastructure.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Unit tests for {@link OffsetBasedPageRequest}.
 */
class OffsetBasedPageRequestTest {

    @Test
    void exposesOffsetLimitAndSort() {
        final OffsetBasedPageRequest request = new OffsetBasedPageRequest(10, 5, Sort.by("createdAt"));

        assertThat(request.getOffset()).isEqualTo(10);
        assertThat(request.getPageSize()).isEqualTo(5);
        assertThat(request.getPageNumber()).isEqualTo(2);
        assertThat(request.getSort().isSorted()).isTrue();
    }

    @Test
    void navigatesBetweenPages() {
        final OffsetBasedPageRequest request = new OffsetBasedPageRequest(10, 5, Sort.unsorted());

        final Pageable next = request.next();
        final Pageable previous = request.previous();
        final Pageable first = request.first();
        final Pageable withPage = request.withPage(4);

        assertThat(next.getOffset()).isEqualTo(15);
        assertThat(previous.getOffset()).isEqualTo(5);
        assertThat(first.getOffset()).isZero();
        assertThat(withPage.getOffset()).isEqualTo(20);
    }

    @Test
    void clampsNegativeOffsetAndDefaultsSort() {
        final OffsetBasedPageRequest negative = new OffsetBasedPageRequest(-5, 10, null);

        assertThat(negative.getOffset()).isZero();
        assertThat(negative.getSort().isUnsorted()).isTrue();
        assertThat(negative.previous().getOffset()).isZero();
    }
}
