package com.tmw.tracking.dao.impl;

import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.OrderDao;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.domain.SortOrder;
import com.tmw.tracking.entity.ContainerLocation;
import com.tmw.tracking.entity.Order;
import com.tmw.tracking.entity.paging.Page;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

/**
 * Created by pzhelnov on 2/8/2017.
 */
public class OrderDaoImpl implements OrderDao {


    private final static Logger logger = LoggerFactory.getLogger(OrderDaoImpl.class);

    private EntityManager entityManager;


    @Inject
    public OrderDaoImpl(
            final EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    @Override
    public Order getById(Long id){
        final TypedQuery<Order> query = entityManager.createQuery("from Order where id = :id", Order.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    @Transaction
    @Override
    public Order create(Order order) {
        order.setOrderDate(new Date());
        entityManager.persist(order);
        return order;
    }

    @Transaction
    @Override
    public void update(Order order){
        entityManager.merge(order);
    }


    @Override
    public List<Order> find(SearchCriteria searchCriteria) {
        return search(searchCriteria, false);
    }

    @Override
    public int count() {
        return search(new SearchCriteria(), true).size();
    }

    private List<Order> search(SearchCriteria searchCriteria, boolean all) {
        if (searchCriteria.getMatcher() == null) {
            searchCriteria.setMatcher("");
        }
        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);

        QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Order.class).get();

        Query filterClientQuery;
        if (searchCriteria.getUser() == null) {
            filterClientQuery = qb.all().createQuery();
        } else {
            // TODO //filterClientQuery = qb.keyword().onField("client.id").matching(searchCriteria.getUser().getId()).createQuery();
            filterClientQuery = qb.all().createQuery();
        }

        org.apache.lucene.search.Query fullTextQuery = qb.
                bool().
                should(qb.keyword().wildcard().onField("order1c").matching("*" + searchCriteria.getMatcher().toLowerCase() + "*").createQuery()).
                should(qb.keyword().wildcard().onField("orderDetails.containerNumber").matching("*" + searchCriteria.getMatcher() + "*").createQuery()).
                should(qb.keyword().wildcard().onField("orderDetails.driver.lastName").matching("*" + searchCriteria.getMatcher().toLowerCase() + "*").createQuery()).
                should(qb.keyword().wildcard().onField("orderDetails.driver.firstName").matching("*" + searchCriteria.getMatcher().toLowerCase() + "*").createQuery()).
                should(qb.keyword().wildcard().onField("orderDetails.driver.tractorNumber").matching("*" + searchCriteria.getMatcher() + "*").createQuery() ).
                should(qb.keyword().wildcard().onField("orderDetails.driver.trailerNumber").matching("*" + searchCriteria.getMatcher() + "*").createQuery() ).
                should(qb.keyword().wildcard().onField("terminal.name").matching("*" + searchCriteria.getMatcher().toLowerCase() + "*").createQuery()).
                should(qb.keyword().wildcard().onField("trackingLine.name").matching("*" + searchCriteria.getMatcher().toLowerCase() + "*").createQuery()).
                createQuery();

        Query finalQuery = qb
                .bool()
                .must( filterClientQuery )
                .must( fullTextQuery )
                .createQuery();

        FullTextQuery persistenceQuery =
                fullTextEntityManager.createFullTextQuery(finalQuery, Order.class);

        if (!all) {
            persistenceQuery.setFirstResult(Page.getOffset(searchCriteria.getPage()));
            persistenceQuery.setMaxResults(Page.ITEMS_ON_PAGE);
        }

        if (searchCriteria.isSortable()) {
            Sort sort = new Sort(
                    new SortField(searchCriteria.getSortColumn(), SortField.Type.STRING, searchCriteria.getSortOrder() == SortOrder.DESC)
            );

            persistenceQuery.setSort(sort);
        }

        @SuppressWarnings("unchecked")
        List<Order> results = persistenceQuery.getResultList();
        return results;
    }


    @Override
    public ContainerLocation getCurrentLocation(Long detailId) {
        final TypedQuery<ContainerLocation> query = entityManager.createQuery(
                "select cl from ContainerLocation cl " +
                "where cl.orderDetails.id = :detailId order by cl.locationDate desc", ContainerLocation.class);
        query.setParameter("detailId", detailId);
        query.setMaxResults(1);
        try {
            return query.getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }
}
