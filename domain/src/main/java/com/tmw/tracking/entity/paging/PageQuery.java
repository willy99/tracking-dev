package com.tmw.tracking.entity.paging;

import com.google.common.base.Objects;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.tmw.tracking.domain.SortOrder;

import java.util.List;
import java.util.Map;

public class PageQuery {

    private final int page;
    private final String sortColumn;
    private final SortOrder sortOrder;


    public static PageQuery of(Map<String, List<String>> parameters) {
        final int page = parsePage(getFirstValues(parameters.get("page")));
        final String sortColumn = parseSortColumn(getFirstValues(parameters.get("sort")));
        final SortOrder sortOrder = parseSortOrder(getFirstValues(parameters.get("sort")));

        return of(page, sortColumn, sortOrder);
    }

    private static String getFirstValues(List<String> values) {
        if (values == null || values.isEmpty()) {
            return null;
        }

        return values.get(0);
    }

    private static int parsePage(String pageValue) {
        if (Strings.isNullOrEmpty(pageValue)) {
            return 1;
        }

        try {
            return Integer.parseInt(pageValue);
        } catch (Exception ignore) {
            return 1;
        }
    }

    private static String parseSortColumn(String sortValue) {
        String value = getSortValue(sortValue, 0);
        if (Strings.isNullOrEmpty(value)) {
            return "";
        }

        return value;
    }

    private static String getSortValue(String text, int index) {
        if (Strings.isNullOrEmpty(text) || !text.contains(",")) {
            return null;
        }

        List<String> parts = Lists.newArrayList(
                Splitter.on(',')
                        .trimResults()
                        .omitEmptyStrings()
                        .split(text)
        );

        if (parts.size() <= index) {
            return null;
        }

        return parts.get(index);
    }

    private static SortOrder parseSortOrder(String sortValue) {
        String value = getSortValue(sortValue, 1);
        if (Strings.isNullOrEmpty(value)) {
            return SortOrder.ASC;
        }

        return SortOrder.of(value, SortOrder.ASC);
    }

    public static PageQuery of(int page) {
        return of(page, "", SortOrder.ASC);
    }

    public static PageQuery of(int page, String sortColumn, SortOrder sortOrder) {
        return new PageQuery(page, sortColumn, sortOrder);
    }

    private PageQuery(int page, String sortColumn, SortOrder sortOrder) {
        this.page = page;
        this.sortColumn = sortColumn;
        this.sortOrder = sortOrder;
    }

    public int getPage() {
        return page;
    }

    public boolean isSortable() {
        return !Strings.isNullOrEmpty(sortColumn);
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("page", page)
                .add("sortColumn", sortColumn)
                .add("sortOrder", sortOrder)
                .toString();
    }
}
