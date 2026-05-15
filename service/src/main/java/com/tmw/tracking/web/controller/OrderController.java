package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.dao.OrderDao;
import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.*;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.entity.support.OrderWorkflowComparator;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.web.aop.MethodCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/order")
@Singleton
public class OrderController extends BaseController {

    private final OrderDao orderDao;
    private final static Logger logger = LoggerFactory.getLogger(DictController.class);

    @Inject
    public OrderController(
            final OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable getUserManagement() {
        User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("user", authenticatedUser);
        vars.put("environment", environment);
        vars.put("pageSize", Page.ITEMS_ON_PAGE);
        return new Viewable("/userstore/orders", vars);
    }



    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAll")
    @MethodCall(requiredPermission = PermissionType.ORDER)
    public List<Order> getAllOrders(@QueryParam("searchFor") @NotNull String matcher,
                                    @Context UriInfo uriInfo,
                                    @Context HttpServletResponse response) {

        SearchCriteria searchCriteria = new SearchCriteria(matcher, getPageQuery(uriInfo));
        searchCriteria.setUser(AuthenticationServiceImpl.getAuthenticatedUser());

        List<Order> orders = orderDao.find(searchCriteria);
        Comparator<OrderWorkflow> comparator = new OrderWorkflowComparator();

        for (Order order : orders) {
            order.setContainerQty( (order.getOrderDetails() != null) ? order.getOrderDetails().size() : 0);
            Collections.sort(order.getWorkflow(), comparator);
        }

        int count = 0;
        if (!orders.isEmpty()) {
            count = orderDao.count();
        }

        final Page<Order> page = Page.of(orders, count, searchCriteria.getPage());
        setPageHeaders(page, response);

        return page.getContent();
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getCurrentLocation")
    @MethodCall(requiredPermission = PermissionType.ORDER_DETAILS_SHOW)
    public ContainerLocation getCurrentLocation(@QueryParam("detailId") @NotNull final Long detailId) {
        return orderDao.getCurrentLocation(detailId);
    }

}
