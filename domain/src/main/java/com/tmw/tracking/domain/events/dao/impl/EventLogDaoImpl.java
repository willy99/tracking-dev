package com.tmw.tracking.domain.events.dao.impl;

import com.tmw.tracking.Transaction;
import com.tmw.tracking.domain.events.dao.EventLogDao;
import com.tmw.tracking.domain.events.entities.EventLog;
import com.tmw.tracking.domain.events.to.EventLogSearchFilterTO;
import com.tmw.tracking.domain.events.to.EventLogTO;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.utils.DomainUtils;
import org.apache.commons.lang.StringUtils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class EventLogDaoImpl implements EventLogDao {

    private static final int MAX_RESULTS = 1000;

    private final EntityManager entityManager;

    @Inject
    public EventLogDaoImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transaction
    public void log(EventLog event) {
        entityManager.persist(event);
    }

    @Override
    public List<EventLogTO> search(EventLogSearchFilterTO filter) {
        StringBuilder hql = new StringBuilder(
                "SELECT e.id, u.email, u.firstName, u.lastName, e.eventDate, e.action, " +
                        "e.permission, e.httpMethod, e.success, e.errorMessage " +
                        "FROM EventLog e LEFT JOIN e.user u " +
                        "WHERE e.tenant = :tenant");

        if (filter != null && StringUtils.isNotBlank(filter.getUserEmail())) {
            hql.append(" AND u IS NOT NULL AND upper(u.email) like :userEmail");
        }
        if (filter != null && StringUtils.isNotBlank(filter.getAction())) {
            hql.append(" AND upper(e.action) like :action");
        }
        if (filter != null && Boolean.TRUE.equals(filter.getSuccessOnly())) {
            hql.append(" AND e.success = true");
        }
        if (filter != null && filter.getDateFrom() != null) {
            hql.append(" AND e.eventDate >= :dateFrom");
        }
        if (filter != null && filter.getDateTo() != null) {
            hql.append(" AND e.eventDate <= :dateTo");
        }
        hql.append(" ORDER BY e.eventDate DESC");

        Query query = entityManager.createQuery(hql.toString());
        query.setParameter("tenant", DomainUtils.getCurrentUser().getTenant());
        if (filter != null && StringUtils.isNotBlank(filter.getUserEmail())) {
            query.setParameter("userEmail", "%" + filter.getUserEmail().trim().toUpperCase() + "%");
        }
        if (filter != null && StringUtils.isNotBlank(filter.getAction())) {
            query.setParameter("action", "%" + filter.getAction().trim().toUpperCase() + "%");
        }
        if (filter != null && filter.getDateFrom() != null) {
            query.setParameter("dateFrom", filter.getDateFrom());
        }
        if (filter != null && filter.getDateTo() != null) {
            query.setParameter("dateTo", filter.getDateTo());
        }
        query.setMaxResults(MAX_RESULTS);

        List<EventLogTO> result = new ArrayList<>();
        List<Object[]> resultList = query.getResultList();
        for (Object[] obj : resultList) {
            EventLogTO to = new EventLogTO();
            to.setId((Long) obj[0]);
            to.setUserEmail((String) obj[1]);
            String firstName = (String) obj[2];
            String lastName = (String) obj[3];
            to.setUserName(StringUtils.trimToEmpty(firstName) + " " + StringUtils.trimToEmpty(lastName));
            to.setEventDate((java.util.Date) obj[4]);
            to.setAction((String) obj[5]);
            to.setPermission((String) obj[6]);
            to.setHttpMethod((String) obj[7]);
            to.setSuccess(Boolean.TRUE.equals(obj[8]));
            to.setErrorMessage((String) obj[9]);
            result.add(to);
        }
        return result;
    }
}
