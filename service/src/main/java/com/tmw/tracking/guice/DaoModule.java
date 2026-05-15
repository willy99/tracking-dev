package com.tmw.tracking.guice;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.tmw.tracking.Transaction;
import com.tmw.tracking.dao.*;
import com.tmw.tracking.dao.impl.*;
import com.tmw.tracking.domain.flex.dao.*;
import com.tmw.tracking.domain.flex.dao.impl.*;
import com.tmw.tracking.service.PermissionService;
import com.tmw.tracking.service.impl.PermissionServiceImpl;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.aop.TransactionalInterceptor;
import com.tmw.tracking.web.hibernate.EntityManagerProvider;
import com.tmw.tracking.web.hibernate.EntityManagerWrapper;

import javax.persistence.EntityManager;

public class DaoModule extends AbstractModule {

    /**
     * {@inheritDoc}
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {
        final EntityManagerProvider entityManagerProvider = new EntityManagerProvider(Utils.PERSIST_MODULE_NAME);

        bind(EntityManagerProvider.class).toInstance(entityManagerProvider);
        final TransactionalInterceptor interceptor = new TransactionalInterceptor(entityManagerProvider);
        bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transaction.class), interceptor);
        bind(EntityManager.class).to(EntityManagerWrapper.class);
        // dao
        bind(AuthenticatedUserDao.class).to(AuthenticatedUserDaoImpl.class);
        bind(JobStatusInfoDao.class).to(JobStatusInfoDaoImpl.class);
        bind(UserDao.class).to(UserDaoImpl.class);
        bind(PermissionService.class).to(PermissionServiceImpl.class);
        bind(RoleDao.class).to(RoleDaoImpl.class);
        bind(TrackingSiteDao.class).to(TrackingSiteDaoImpl.class);
        bind(DriverDao.class).to(DriverDaoImpl.class);
        bind(OrderDao.class).to(OrderDaoImpl.class);
        bind(PermissionDao.class).to(PermissionDaoImpl.class);
        bind(ContainerTypeDao.class).to(ContainerTypeDaoImpl.class);
        bind(TrackingLineDao.class).to(TrackingLineDaoImpl.class);
        bind(TerminalDao.class).to(TerminalDaoImpl.class);
        bind(CompanyDao.class).to(CompanyDaoImpl.class);
        bind(ConfigurationDao.class).to(ConfiguarationDaoImpl.class);

        //flex
        bind(FlexDao.class).to(FlexDaoImpl.class);
        bind(FlexContainerDao.class).to(FlexContainerDaoImpl.class);
        bind(FlexWarehouseDao.class).to(FlexWarehouseDaoImpl.class);
        bind(FlexOrderDao.class).to(FlexOrderDaoImpl.class);
        bind(FlexHistoryDao.class).to(FlexHistoryDaoImpl.class);
    }
}
