package com.tmw.tracking.domain.flex.to;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchFilterTO {

    // ---- text / date filters ----
    private String searchQuery;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateTo;

    /** Optional entity-type filter (e.g. "IMPORT", "EXPORT", "MOUNT" for orders). */
    private String typeFilter;

    // ---- paging ----
    /** 1-based page number. Defaults to 1. */
    private int page = 1;

    /**
     * Items per page. 0 means "use the system default"
     * (page.default.size property, falls back to 10).
     */
    private int pageSize = 0;

    // ---- sorting ----
    /** Field name to sort by (matches DTO field names). */
    private String sortField;

    /** "asc" or "desc". Defaults to "desc". */
    private String sortDir = "desc";

    // ---- getters / setters ----

    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }

    public Date getDateFrom() { return dateFrom; }
    public void setDateFrom(Date dateFrom) { this.dateFrom = dateFrom; }

    public Date getDateTo() { return dateTo; }
    public void setDateTo(Date dateTo) { this.dateTo = dateTo; }

    public String getTypeFilter() { return typeFilter; }
    public void setTypeFilter(String typeFilter) { this.typeFilter = typeFilter; }

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }

    public String getSortField() { return sortField; }
    public void setSortField(String sortField) { this.sortField = sortField; }

    public String getSortDir() { return sortDir; }
    public void setSortDir(String sortDir) { this.sortDir = sortDir; }
}
