package com.tmw.tracking.entity.paging;

import com.google.common.base.Strings;
import com.tmw.tracking.utils.DomainUtils;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.domain.SortOrder;
import com.tmw.tracking.entity.RootEntity;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Page<T extends RootEntity> {

    public static final int ITEMS_ON_PAGE = DomainUtils.getDefaultPageSize();

    protected final EntityManager entityManager;
    private Class<T> entityClass;
    protected List<Class<? extends RootEntity>> entityList = new ArrayList<Class<? extends RootEntity>>();
    protected SearchCriteria searchCriteria = new SearchCriteria();
    private final String query;
    private final String countWhere;

    protected final int page;
    protected final String sortColumn;
    protected final SortOrder sortOrder;

    private int totalPages = 0;
    private List<T> content = Collections.emptyList();

    public static <T extends RootEntity> Page<T> of(List<T> content, int total, int page) {
        final Page<T> pageContent = new Page<T>(page);
        pageContent.content = content;
        pageContent.totalPages = (total + ITEMS_ON_PAGE - 1) / ITEMS_ON_PAGE;

        return pageContent;
    }

    public static <T extends RootEntity> Page<T> of(EntityManager entityManager, Class<T> clazz, String query, PageQuery pageQuery) {
        return of(entityManager, clazz, query, null, pageQuery);
    }

    public static <T extends RootEntity> Page<T> of(EntityManager entityManager, Class<T> clazz, String query, String countWhere, PageQuery pageQuery) {
        final Page<T> pageContent = new Page<T>(entityManager, clazz, query, countWhere, pageQuery);
        pageContent.execute();

        return pageContent;
    }

    protected Page(int page) {
        this.entityManager = null;
        this.entityClass = null;
        this.query = null;
        this.countWhere = null;

        this.page = page;
        this.sortColumn = "";
        this.sortOrder = SortOrder.ASC;
    }

    protected Page(EntityManager entityManager, Class<T> entityClass, String query, String countWhere, PageQuery pageQuery) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
        this.query = query;
        this.countWhere = countWhere;

        this.page = pageQuery.getPage();
        this.sortColumn = pageQuery.getSortColumn();
        this.sortOrder = pageQuery.getSortOrder();
    }


    protected Page (EntityManager entityManager, SearchCriteria searchCriteria, String query, String countWhere, PageQuery pageQuery){
        this.entityManager = entityManager;
        this.searchCriteria = searchCriteria;
        this.query = query;
        this.countWhere = countWhere;
        this.page = pageQuery.getPage();
        this.sortColumn = pageQuery.getSortColumn();
        this.sortOrder = pageQuery.getSortOrder();
    }

    protected void execute() {
        final int rowCount = requestTotalCount();
        if (rowCount <= 0) {
            return;
        }

        totalPages = (rowCount + ITEMS_ON_PAGE - 1) / ITEMS_ON_PAGE;
        content = requestContent();
    }


    protected boolean isSortable() {
        return !Strings.isNullOrEmpty(sortColumn);
    }

    protected int requestTotalCount() {
        return DomainUtils.getRowCount(entityManager, entityClass);
    }

    protected List<T> requestContent() {
        return DomainUtils.getLimitResult(
                entityManager,
                entityClass,
                applySorting(query),
                getOffset(page),
                ITEMS_ON_PAGE
        );
    }

    private String applySorting(String query) {
        return applySorting(query, null, sortColumn, sortOrder);
    }

    public static String applySorting(String query, String tablePrefix, String sortColumn, SortOrder sortOrder) {
        if (Strings.isNullOrEmpty(sortColumn) || sortOrder == null) {
            return query;
        }

        if (Strings.isNullOrEmpty(query)) {
            return query;
        }

        final String rawQuery = getQueryWithoutSorting(query);

        if (!Strings.isNullOrEmpty(tablePrefix)) {
            sortColumn = tablePrefix + "." + sortColumn;
        }

        return rawQuery + " order by " + sortColumn +  " " + sortOrder.getSortOrder();
    }

    private static String getQueryWithoutSorting(String query) {
        if (Strings.isNullOrEmpty(query)) {
            return query;
        }

        query = query.replaceFirst("\\s*;\\s*$", "");
        final Pattern pattern = Pattern.compile("(?i)\\s+order\\s+by\\s+([\\w.]+(\\s+ASC|\\s+DESC)*)(,\\s*[\\w.]+(\\s+ASC|\\s+DESC)*)*\\s*$");
        final Matcher matcher = pattern.matcher(query);

        if (matcher.find()) {
            return query.substring(0, matcher.start());
        }

        return query;
    }

    public Class<T> getEntityClass() {
        return entityClass;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public static int getOffset(int page) {
        page = Math.max(1, page);
        page -= 1;

        return page * ITEMS_ON_PAGE;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Page{");
        sb.append("entityManager=").append(entityManager);
        sb.append(", entityClass=").append(entityClass);
        sb.append(", query='").append(query).append('\'');
        sb.append(", countWhere='").append(countWhere).append('\'');
        sb.append(", page=").append(page);
        sb.append(", totalPages=").append(totalPages);
        sb.append(", content=").append(content);
        sb.append('}');
        return sb.toString();
    }
}
