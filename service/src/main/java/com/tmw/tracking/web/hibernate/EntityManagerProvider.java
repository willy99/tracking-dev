package com.tmw.tracking.web.hibernate;

import com.tmw.tracking.entity.User;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.utils.ApplicationInitialization;
import com.tmw.tracking.utils.Utils;
import org.apache.shiro.SecurityUtils;
import org.hibernate.Session;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Singleton
public class EntityManagerProvider {

    private EntityManagerFactory entityManagerFactory;
    private final ThreadLocal<EntityManager> threadLocalEntityManager;
    private final static Logger logger = LoggerFactory.getLogger(EntityManagerProvider.class);
    private static final int BATCH_SIZE = 250000;
    private static final long OBJECTS_TO_INDEX = 25000000;

    public EntityManagerProvider(String unitName) {
        threadLocalEntityManager = new ThreadLocal<EntityManager>();
        entityManagerFactory = Persistence.createEntityManagerFactory(unitName, Utils.getProperties());
    }

    public EntityManager getEntityManager() {
        EntityManager entityManager = threadLocalEntityManager.get();
        if (entityManager == null) {
            create();
            entityManager = threadLocalEntityManager.get();
            if (entityManager == null) {
                throw new IllegalStateException("Thread local entityManager is empty, creating new");
            }
        }
        return entityManager;
    }

    public boolean isInitialized() {
        return entityManagerFactory != null;
    }

    public void destroy() {
        entityManagerFactory.close();
    }

    public void create() {
        EntityManager entityManager = threadLocalEntityManager.get();
        if (entityManager != null && entityManager.isOpen()) {
            entityManager.close();
        }
        entityManager = entityManagerFactory.createEntityManager();
        threadLocalEntityManager.set(entityManager);

        if (!ApplicationInitialization.LUCENE_INDEXED) {
            createLuceneIndex();
            ApplicationInitialization.LUCENE_INDEXED = true;
        }
    }

    private void createLuceneIndex() {
        logger.info("Initializing lucene index");
        EntityManager entityManager = threadLocalEntityManager.get();
        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory.createEntityManager();
            threadLocalEntityManager.set(entityManager);
        }

        FullTextEntityManager fullTextEntityManager = Search
                .getFullTextEntityManager(entityManager);

        try {
            fullTextEntityManager.
                    createIndexer().
                    purgeAllOnStart(true).
                    batchSizeToLoadObjects(BATCH_SIZE).
                    limitIndexedObjectsTo(OBJECTS_TO_INDEX).

                    startAndWait();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.info("---Thread interrupted");
        }
        logger.info("---Initialized lucene index");

    }


    public void close() {
        final EntityManager entityManager = threadLocalEntityManager.get();
        if (entityManager != null) {
            if (entityManager.isOpen())
                entityManager.close();
            threadLocalEntityManager.remove();
        }
    }

    void applyGlobalFilters(final User user) {
        if (user == null || getEntityManager() == null) return;
        final Session session = getEntityManager().unwrap(Session.class);
    }

    public void applyGlobalFilters() {
        if (SecurityUtils.getSubject().isAuthenticated())
            applyGlobalFilters(AuthenticationServiceImpl.getAuthenticatedUser());
    }


}
