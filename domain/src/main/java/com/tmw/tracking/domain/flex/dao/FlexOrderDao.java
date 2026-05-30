package com.tmw.tracking.domain.flex.dao;

import com.tmw.tracking.domain.flex.entities.FlexOrder;
import com.tmw.tracking.domain.flex.to.FlexOrderTO;
import com.tmw.tracking.domain.flex.to.SearchFilterTO;

import java.util.Date;
import java.util.List;

public interface FlexOrderDao {

    FlexOrder add(FlexOrder order);

    void upsertOrders(List<FlexOrder> orders);

    FlexOrder update(FlexOrder order);

    FlexOrder getOrderByNumber(String number);

    List<FlexOrder> getOrdersByNumbers(List<String> numbers);

    void deleteOrder(FlexOrder order);
    void deleteOrderBatch(List<FlexOrder> orderList);

    List<FlexOrder> getOrdersForExport(String searchString);

    List<FlexOrder> getOrdersForMount(String searchString);

    List<FlexOrder> getOrdersForImport(String searchString);

    List<FlexOrderTO> getExportOrdersWithStatistic(String searchString, Date lastUpdated);

    List<FlexOrderTO> getMountedOrdersWithStatistic(String searchString, Date lastUpdated);

    List<FlexOrderTO> getAllOrdersWithStatistic(SearchFilterTO filter);

}
