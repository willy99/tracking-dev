package com.tmw.tracking.domain.flex.dao.impl;

import com.tmw.tracking.Transaction;
import com.tmw.tracking.domain.flex.dao.FlexHistoryDao;
import com.tmw.tracking.domain.flex.entities.FlexHistory;
import com.tmw.tracking.domain.flex.entities.FlexWarehouseTypeEnum;
import com.tmw.tracking.domain.flex.to.FlexHistoryTO;
import com.tmw.tracking.utils.DomainUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlexHistoryDaoImpl implements FlexHistoryDao {

    private static final Logger logger = LoggerFactory.getLogger(FlexDaoImpl.class);
    private EntityManager entityManager;

    @Inject
    public FlexHistoryDaoImpl (
            final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transaction
    public void batchAdd(List<FlexHistory> historyList) {
        for (FlexHistory history: historyList) {
            entityManager.persist(history);
        }
    }

    @Override
    public List<FlexHistoryTO> getHistory(String searchString, Date lastUpdated) {
        String searchClause = (searchString != null && !searchString.isEmpty())? " and f.serialNumber = :searchString": "";
        String lastUpdatedClause = (lastUpdated != null)? " and h.lastUpdated > :lastUpdated ": "";

        String hqlQuery = "SELECT f.serialNumber, w.name, w.warehouseType, h.actionDate " +
                " from FlexHistory h " +
                " left join h.warehouse w " +
                " left join h.flex f " +
                " where  h.tenant = :tenant and f.deleted is not true " + lastUpdatedClause + searchClause +
                " order by h.actionDate asc ";
        Query query = entityManager.createQuery(hqlQuery);
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        if (lastUpdated != null) {
            query.setParameter("lastUpdated", lastUpdated);
        }
        if (!searchClause.isEmpty()) {
            query.setParameter("searchString", searchString);
        }
        List<FlexHistoryTO> flexHistoryTOS = new ArrayList<>();

        List<Object[]> resultList = query.getResultList();
        for (Object[] obj: resultList) {
            FlexHistoryTO historyTO = new FlexHistoryTO();
            historyTO.setFlex((String)obj[0]);
            historyTO.setWarehouseName((String)obj[1]);
            historyTO.setWarehouseType((FlexWarehouseTypeEnum)obj[2]);
            historyTO.setDate((Date)obj[3]);
            flexHistoryTOS.add(historyTO);
        }
        return flexHistoryTOS;
    }

}
