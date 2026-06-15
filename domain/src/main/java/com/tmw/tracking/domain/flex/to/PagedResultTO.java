package com.tmw.tracking.domain.flex.to;

import java.util.List;

/**
 * Generic paged-response wrapper returned by list endpoints.
 * Carries the current page of items plus pagination metadata.
 */
public class PagedResultTO<T> {

    private List<T> items;
    private int totalItems;
    private int totalPages;
    private int pageSize;
    private int currentPage;

    public PagedResultTO() {}

    public PagedResultTO(List<T> items, int totalItems, int pageSize, int currentPage) {
        this.items = items;
        this.totalItems = totalItems;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.totalPages = (pageSize > 0) ? (int) Math.ceil((double) totalItems / pageSize) : 1;
    }

    public List<T> getItems() { return items; }
    public void setItems(List<T> items) { this.items = items; }

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }
}
