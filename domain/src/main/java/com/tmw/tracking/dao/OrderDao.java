package com.tmw.tracking.dao;

import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.ContainerLocation;
import com.tmw.tracking.entity.Order;

import java.util.List;

/**
 * Created by pzhelnov on 1/25/2017.
 */
public interface OrderDao {

    Order getById(Long id);

    Order create(Order order);

    //List<Order> search(SearchCriteria searchCriteria);

    void update(Order order);

    List<Order> find(SearchCriteria searchCriteria);

    int count();

    ContainerLocation getCurrentLocation(Long detailId);


}
