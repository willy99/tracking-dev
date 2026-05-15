package com.tmw.tracking.guice;

import com.google.inject.AbstractModule;
import com.tmw.tracking.web.controller.*;

public class ControllerModule extends AbstractModule {

    /**
     * {@inheritDoc}
     * @see com.google.inject.AbstractModule#configure()
     */
    @Override
    protected void configure() {
        bind(MainController.class);
        bind(MonitoringController.class);
        bind(UserController.class);
        bind(TrackingController.class);
        bind(AuctionController.class);
        bind(AnonController.class);
        bind(DictController.class);
        bind(OrderController.class);
        bind(LogisticController.class);
        bind(CompanyController.class);
        bind(CalculatorController.class);
        bind(FlexController.class);
    }
}
