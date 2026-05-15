package com.tmw.tracking.dao;

import com.tmw.tracking.dao.impl.OrderDaoImpl;
import com.tmw.tracking.domain.SearchCriteria;
import com.tmw.tracking.entity.Order;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


@Category(TrackingBaseUnitTest.class)
public class OrderDaoTest extends TrackingBaseUnitTest {

    private EntityManager entityManager;
    private OrderDao orderDao;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        entityManager = injector.getInstance(EntityManager.class);
        orderDao = injector.getInstance(OrderDaoImpl.class);
    }

    @Test(expected = Exception.class)
    public void testCreateOrderWithNullTerminal(){
        Order order = createOrder();
        order.setTerminal(null);
        orderDao.create(order);
    }

    @Test
    public void testCreateOrderWithAllFields(){
        Order order = createOrder();
        orderDao.create(order);
        entitiesToBeRemoved.add(order);
        Order orderDaoById = orderDao.getById(order.getId());
        assertEquals(order, orderDaoById);
    }

    @Test
    public void testCreateWithOrderDetailsAndWorkflow(){
        Order order = setOrderDetailsAndWorklof(createOrder());
        orderDao.create(order);
        entitiesToBeRemoved.add(order);
        Order orderDaoById = orderDao.getById(order.getId());
        assertEquals(order, orderDaoById );
        assertEquals(order.getOrderDetails(), orderDaoById.getOrderDetails());
        assertEquals(order.getWorkflow(), orderDaoById.getWorkflow());
    }

    @Test
    public void testFindByUser(){
        List<Order> orders = createCollectionOrders();
        for (int i = 0; i < orders.size() ; i++) {
            SearchCriteria searchCriteria = new SearchCriteria();
            searchCriteria.setUser(orders.get(i).getClient());
            Order orderDao = this.orderDao.find(searchCriteria).get(0);
            assertEquals(orders.get(i),orderDao);
        }
    }


    @Test
    public void testFindByContainerNumber(){
        List<Order> orders = createCollectionOrders();
        List<Order> ordersforMockTest = createCollectionOrders();
        for (int i = 0; i < orders.size() ; i++) {
            SearchCriteria searchCriteria = new SearchCriteria();
            String containerNumber = orders.get(i).getOrderDetails().get(0).getContainerNumber();
            searchCriteria.setMatcher(containerNumber);
            List<Order> orderFromDbList = orderDao.find(searchCriteria);
            assertEquals(1, orderFromDbList.size());

            assertEquals(orders.get(i), orderFromDbList.get(0));
            assertFalse(ordersforMockTest.contains(orderFromDbList.get(0)));
        }
    }


    public List<Order> createCollectionOrders(){
        List<Order> orders = new ArrayList<Order>();
        for(int i = 0; i<10; i++) {
            Order order;
            order = setOrderDetailsAndWorklof(createOrder());
            orders.add(order);
            entitiesToBeRemoved.add(order);
            orderDao.create(order);
        }
        return orders;
    }
}


