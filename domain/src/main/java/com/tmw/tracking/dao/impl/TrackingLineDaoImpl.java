package com.tmw.tracking.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.TrackingLineDao;
import com.tmw.tracking.entity.TrackingLine;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.paging.PageQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by pzhelnov on 3/29/2017.
 */
@Singleton
public class TrackingLineDaoImpl implements TrackingLineDao {

    private static final Logger logger = LoggerFactory.getLogger(TrackingLineDaoImpl.class);

    private EntityManager entityManager;

    @Inject
    public TrackingLineDaoImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public TrackingLine getById(Long id) {
        TypedQuery<TrackingLine> query = entityManager.createQuery("from TrackingLine where id = :id ", TrackingLine.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transaction
    @Override
    public TrackingLine create(TrackingLine trackingLine){
       entityManager.persist(trackingLine);
       return trackingLine;
    }


    @Override
    public List<TrackingLine> getAll() {
        TypedQuery<TrackingLine> query = entityManager.createQuery("from TrackingLine order by name ", TrackingLine.class);
        return query.getResultList();

    }

    @Override
    public Page<TrackingLine> getByPage(PageQuery pageQuery) {
        return Page.of(entityManager, TrackingLine.class,"from TrackingLine order by name ", pageQuery);
    }

}
