package com.tmw.tracking.domain.flex.dao.impl;

import com.google.inject.Singleton;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.domain.flex.dao.FlexContainerDao;
import com.tmw.tracking.domain.flex.entities.FlexContainer;
import com.tmw.tracking.domain.flex.entities.FlexOrderTypeEnum;
import com.tmw.tracking.domain.flex.entities.FlexStatusEnum;
import com.tmw.tracking.domain.flex.to.FlexContainerTO;
import com.tmw.tracking.utils.DomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

@Singleton
public class FlexContainerDaoImpl implements FlexContainerDao {

    private static final Logger logger = LoggerFactory.getLogger(FlexContainerDaoImpl.class);
    private EntityManager entityManager;

    @Inject
    public FlexContainerDaoImpl(
            final EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Transaction
    @Override
    public FlexContainer add(FlexContainer flexContainer) {
        entityManager.persist(flexContainer);
        return flexContainer;
    }

    @Transaction
    @Override
    public void upsertAll(List<FlexContainer> flexContainers) {
        if (flexContainers.isEmpty()) {
            return;
        }
        for (FlexContainer container: flexContainers) {
            if (container.getId() == null) {
                entityManager.persist(container);
            } else {
                entityManager.merge(container);
            }
        }
    }



    @Override
    public FlexContainer update(FlexContainer flexContainer) {
        return entityManager.merge(flexContainer);
    }

    @Override
    public FlexContainer getContainerByNumber(String containerNum) {
        TypedQuery<FlexContainer> query = entityManager.createQuery("from FlexContainer where containerNumber = :containerNum and tenant = :tenant", FlexContainer.class);
        query.setParameter("containerNum", containerNum);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<FlexContainer> getContainerListByOrder(String orderNum) {
        TypedQuery<FlexContainer> query = entityManager.createQuery("from FlexContainer where importOrder.orderNumber = :orderNum and tenant = :tenant", FlexContainer.class);
        query.setParameter("orderNum", orderNum);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<FlexContainer> getContainersByNumbers(List<String> containerNums) {
        if (containerNums.isEmpty()) {
            return new ArrayList<>();
        }
        TypedQuery<FlexContainer> query = entityManager.createQuery("from FlexContainer where containerNumber in :containerNums and tenant = :tenant", FlexContainer.class);
        query.setParameter("containerNums", containerNums);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<FlexContainerTO> getContainersForImport(String searchString, Date lastUpdated) {
        String searchClause = (searchString != null && !searchString.isEmpty())? " and c.conatinerNumber = :searchString": "";
        String lastUpdatedClause = (lastUpdated != null)? " and c.importOrder.lastUpdated > :lastUpdated ": "";

        String hqlQuery = "SELECT c.containerNumber, c.importFlexQty, c.status, c.lastUpdated, count(f) " +
                "from FlexContainer c " +
                "left join c.importFlexes f " +
                "left join c.importOrder o " +
                "where (f.deleted is null or f.deleted = false) and c.importOrder is not null and o.orderType = :orderType and c.tenant = :tenant " + lastUpdatedClause + searchClause + "" +
                " group by c.containerNumber, c.importFlexQty, c.status, c.lastUpdated";
        Query query = entityManager.createQuery(hqlQuery);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        if (lastUpdated != null) {
            query.setParameter("lastUpdated", lastUpdated);
        }
        if (!searchClause.isEmpty()) {
            query.setParameter("searchString", searchString);
        }
        query.setParameter("orderType", FlexOrderTypeEnum.IMPORT);
        List<FlexContainerTO> flexContainerTOS = new ArrayList<>();

        List<Object[]> resultList = query.getResultList();
        for (Object[] obj: resultList) {
            FlexContainerTO containerTO = new FlexContainerTO();
            containerTO.setContainerNumber((String)obj[0]);
            containerTO.setFlexQty( (Integer)obj[1]);
            containerTO.setStatus((FlexStatusEnum)obj[2]);
            containerTO.setUpdatedDate((Date)obj[3]);
            containerTO.setProcessedFlexQty(((Long)obj[4]).intValue());
            flexContainerTOS.add(containerTO);
        }
        return flexContainerTOS;
    }

}
