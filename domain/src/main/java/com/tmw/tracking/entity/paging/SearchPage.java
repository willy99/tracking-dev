package com.tmw.tracking.entity.paging;


import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.RootEntity;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.entity.enums.RoleEnum;
import com.tmw.tracking.utils.DomainUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.TermMatchingContext;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;

public class SearchPage<T extends RootEntity> extends Page<T> {

    public SearchPage(EntityManager entityManager, PageQuery pageQuery, SearchCriteria searchCriteria) {
        super(entityManager, searchCriteria, "", "", pageQuery);
    }

    public static <T extends RootEntity> Page<T> searchFilter(EntityManager entityManager, PageQuery pageQuery, SearchCriteria searchCriteria) {
        final Page<T> pageContent = new SearchPage<T>(entityManager, pageQuery, searchCriteria);
        pageContent.execute();
        return pageContent;
    }


    @Override
    protected int requestTotalCount() {
        List<T> rows = search(false);
        if (rows == null) {
            return 0;
        }
        return rows.size();

    }


    @Override
    protected List<T> requestContent() {
        return search(true);
    }


    private List<T> search(boolean limited) {

        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();

        if (searchCriteria.getMatcherFields().size() > 0) {

            QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(searchCriteria.getEntitiesForSearch().get(0)).get();

            String firstField = searchCriteria.getMatcherFields().get(0);
            TermMatchingContext termMatchingContext = qb.keyword().wildcard().onField(firstField);

            if (searchCriteria.getMatcherFields().size() > 1) {
                for (int i = 1, searchFieldsSize = searchCriteria.getMatcherFields().size(); i < searchFieldsSize; i++) {
                    String field = searchCriteria.getMatcherFields().get(i);
                    termMatchingContext = termMatchingContext.andField(field);
                }
            }

            org.apache.lucene.search.Query querySearchFields = termMatchingContext.matching(searchCriteria.getMatcher()).createQuery();

            booleanQueryBuilder.add(querySearchFields, BooleanClause.Occur.MUST);
        }

        if (searchCriteria.getAdditionalFilters().size() > 0) {

            for (Map.Entry<String, Object> entry : searchCriteria.getAdditionalFilters().entrySet()) {
                TermQuery termQuery = new TermQuery(new Term(entry.getKey(), entry.getValue().toString()));
                booleanQueryBuilder.add(termQuery, BooleanClause.Occur.MUST);
            }
        }

        //tenant specific
        User authUser = DomainUtils.getAuthenticatedUser();
        if (authUser.getRole() != null && !RoleEnum.SYSTEM_ADMIN.getName().equals(authUser.getRole().getRoleName())) {
            TermQuery termQuery = new TermQuery(new Term("tenant", DomainUtils.getAuthenticatedUser().getTenant().getId().toString()));
            booleanQueryBuilder.add(termQuery, BooleanClause.Occur.MUST);
        }

        //

        BooleanQuery booleanQuery = booleanQueryBuilder.build();
        org.apache.lucene.search.Query querySum = null;
        QueryParser queryParser = new QueryParser("name", new StandardAnalyzer());
        queryParser.setAllowLeadingWildcard(true);

        try {
            querySum = queryParser.parse(booleanQuery.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }


        FullTextQuery persistenceQuery = fullTextEntityManager.createFullTextQuery(querySum, searchCriteria.getEntitiesForSearch().get(0));

        if (limited) {
            persistenceQuery.setFirstResult(getOffset(page));
            persistenceQuery.setMaxResults(ITEMS_ON_PAGE);

            if (isSortable()) {
                Sort sort = new Sort(
                    new SortField(sortColumn, SortField.Type.STRING, true)
                );

                persistenceQuery.setSort(sort);
            }
        }

        return (List<T>) persistenceQuery.getResultList();
    }




}



