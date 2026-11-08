package com.example.vocabularyservice.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * {@link Pageable} addressing results by absolute offset and limit.
 */
public class OffsetBasedPageRequest extends AbstractPageRequest {

    private static final long serialVersionUID = 1L;

    private final long offset;
    private final Sort sort;

    /**
     * Creates the page request.
     *
     * @param offset results to skip (&ge; 0)
     * @param limit  page size (&ge; 1)
     * @param sort   sort order
     */
    public OffsetBasedPageRequest(final long offset, final int limit, final Sort sort) {
        super(limit <= 0 ? 0 : (int) (offset / limit), limit);
        this.offset = Math.max(offset, 0);
        this.sort = sort == null ? Sort.unsorted() : sort;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new OffsetBasedPageRequest(offset + getPageSize(), getPageSize(), sort);
    }

    @Override
    public Pageable previous() {
        return new OffsetBasedPageRequest(Math.max(offset - getPageSize(), 0), getPageSize(), sort);
    }

    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(0, getPageSize(), sort);
    }

    @Override
    public Pageable withPage(final int pageNumber) {
        return new OffsetBasedPageRequest((long) pageNumber * getPageSize(), getPageSize(), sort);
    }
}
