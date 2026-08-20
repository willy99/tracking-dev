package com.tmw.tracking.web.controller;

import com.tmw.tracking.dao.OrderDao;
import com.tmw.tracking.dao.impl.OrderDaoImpl;
import com.tmw.tracking.entity.ContainerLocation;
import com.tmw.tracking.entity.Order;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Exercises {@link OrderController}'s search/lookup endpoints against the disposable H2 test
 * database (see TrackingBaseUnitTest / tracking-tests.properties) — nothing here touches the
 * real DB.
 */
@Category(TrackingBaseUnitTest.class)
public class OrderControllerTest extends TrackingBaseUnitTest {

    private OrderController orderController;
    private OrderDao orderDao;
    private UriInfo uriInfo;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        orderController = injector.getInstance(OrderController.class);
        orderDao = injector.getInstance(OrderDaoImpl.class);

        uriInfo = mock(UriInfo.class);
        @SuppressWarnings("unchecked")
        MultivaluedMap<String, String> emptyParams = mock(MultivaluedMap.class);
        when(uriInfo.getQueryParameters()).thenReturn(emptyParams);
    }

    @Test
    public void getAllOrders_byContainerNumber_findsTheMatchingOrder() {
        Order order = setOrderDetailsAndWorklof(createOrder());
        orderDao.create(order);
        entitiesToBeRemoved.add(order);

        String containerNumber = order.getOrderDetails().get(0).getContainerNumber();
        List<Order> result = orderController.getAllOrders(containerNumber, uriInfo, null);

        assertTrue("should find the order by its container number", containsOrderId(result, order.getId()));
    }

    @Test
    public void getCurrentLocation_forUnknownDetail_returnsNull() {
        ContainerLocation location = orderController.getCurrentLocation(-1L);
        assertNull("no order detail should exist for id -1", location);
    }

    // ------------------------------------------------------------------------

    private boolean containsOrderId(List<Order> orders, Long id) {
        for (Order o : orders) {
            if (id.equals(o.getId())) {
                return true;
            }
        }
        return false;
    }
}
