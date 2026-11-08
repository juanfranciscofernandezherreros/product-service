package com.example.transactionservice.infrastructure.adapter.out.persistence;

import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * {@link Pageable} implementation addressing results by absolute offset and limit,
 * instead of page number, to honour the {@code offset}/{@code limit} contract.
 */
public class OffsetBasedPageRequest extends AbstractPageRequest {

    private static final long serialVersionUID = 1L;

    private final long offset;
    private final Sort sort;

    /**
     * Creates the page request.
     *
     * @param offset number of items to skip (&ge; 0)
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
        final long newOffset = offset - getPageSize();
        return new OffsetBasedPageRequest(Math.max(newOffset, 0), getPageSize(), sort);
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
