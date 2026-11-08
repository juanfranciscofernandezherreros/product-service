package com.example.miscservice.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Pageable implementation driven by an absolute offset rather than a page index.
 */
public final class OffsetBasedPageRequest extends AbstractPageRequest {

    private final long offset;
    private final Sort sort;

    /**
     * Creates an offset-based page request.
     *
     * @param offset zero-based offset of the first element
     * @param limit  maximum number of elements (at least 1)
     * @param sort   sort specification
     */
    public OffsetBasedPageRequest(final long offset, final int limit, final Sort sort) {
        super(0, Math.max(limit, 1));
        this.offset = Math.max(offset, 0);
        this.sort = sort;
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
        final long newOffset = Math.max(offset - getPageSize(), 0);
        return new OffsetBasedPageRequest(newOffset, getPageSize(), sort);
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
