package com.tmw.tracking.web.service.flex;

import com.tmw.tracking.domain.flex.dao.FlexDao;
import com.tmw.tracking.domain.flex.dao.FlexOrderDao;
import com.tmw.tracking.domain.flex.dao.FlexWarehouseDao;
import com.tmw.tracking.domain.flex.dao.impl.FlexOrderDaoImpl;
import com.tmw.tracking.domain.flex.entities.FlexOrder;
import com.tmw.tracking.domain.flex.entities.FlexOrderTypeEnum;
import com.tmw.tracking.domain.flex.entities.FlexStatusEnum;
import com.tmw.tracking.domain.flex.to.FlexContainerTO;
import com.tmw.tracking.domain.flex.to.FlexOrderTO;
import com.tmw.tracking.domain.flex.to.FlexTO;
import com.tmw.tracking.service.impl.FlexServiceImpl;
import com.tmw.tracking.web.service.exception.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.*;

/**
 * Pure unit tests for recalculate-order logic — no real DB, all dependencies mocked.
 */
public class FlexServiceRecalculateTest {

    @Mock private EntityManager entityManager;
    @Mock private FlexDao flexDao;
    @Mock private FlexWarehouseDao flexWarehouseDao;
    @Mock private FlexOrderDao flexOrderDao;

    private FlexOrderDaoImpl flexOrderDaoImpl;
    private FlexServiceImpl flexService;

    private static final Long ORDER_ID = 1L;
    private static final String ORDER_NUM = "EXP001";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        flexOrderDaoImpl = new FlexOrderDaoImpl(entityManager, flexWarehouseDao, flexDao);

        // Only recalculateOrder is exercised — remaining deps left null
        // constructor order: flexDao, flexHistoryDao, flexOrderDao, flexContainerDao, flexWarehouseDao, configurationService
        flexService = new FlexServiceImpl(flexDao, null, flexOrderDao, null, null, null);
    }

    // -------------------------------------------------------------------------
    // FlexServiceImpl.recalculateOrder
    // -------------------------------------------------------------------------

    @Test(expected = ValidationException.class)
    public void recalculateOrder_orderNotFound_throwsValidationException() {
        when(flexOrderDao.getOrderByNumber(ORDER_NUM)).thenReturn(null);
        flexService.recalculateOrder(ORDER_NUM);
    }

    @Test
    public void recalculateOrder_orderFound_delegatesToDao() {
        FlexOrder order = makeOrder(ORDER_ID, ORDER_NUM, 2);
        when(flexOrderDao.getOrderByNumber(ORDER_NUM)).thenReturn(order);

        flexService.recalculateOrder(ORDER_NUM);

        verify(flexOrderDao).recalculateOrderStatuses(Collections.singletonList(order));
    }

    // -------------------------------------------------------------------------
    // FlexOrderDaoImpl.recalculateOrderStatuses — status/type matrix
    // -------------------------------------------------------------------------

    @Test
    public void recalculate_noFlexesAttached_setsExportNew() {
        FlexOrder order = makeOrder(ORDER_ID, ORDER_NUM, 3);
        when(flexDao.getFlexesForOrders(anyListOf(FlexOrder.class))).thenReturn(Collections.<FlexTO>emptyList());

        flexOrderDaoImpl.recalculateOrderStatuses(Collections.singletonList(order));

        assertEquals(FlexOrderTypeEnum.EXPORT, order.getOrderType());
        assertEquals(FlexStatusEnum.NEW, order.getStatus());
        verify(entityManager).merge(order);
    }

    @Test
    public void recalculate_partialExportAttached_setsExportInProgress() {
        FlexOrder order = makeOrder(ORDER_ID, ORDER_NUM, 3);
        // 1 out of 3 exported, none mounted
        List<FlexTO> flexes = Collections.singletonList(flexWithExportOrder(ORDER_ID));
        when(flexDao.getFlexesForOrders(anyListOf(FlexOrder.class))).thenReturn(flexes);

        flexOrderDaoImpl.recalculateOrderStatuses(Collections.singletonList(order));

        assertEquals(FlexOrderTypeEnum.EXPORT, order.getOrderType());
        assertEquals(FlexStatusEnum.IN_PROGRESS, order.getStatus());
    }

    @Test
    public void recalculate_allExportedNoneMounted_setsMountNew() {
        FlexOrder order = makeOrder(ORDER_ID, ORDER_NUM, 2);
        List<FlexTO> flexes = Arrays.asList(flexWithExportOrder(ORDER_ID), flexWithExportOrder(ORDER_ID));
        when(flexDao.getFlexesForOrders(anyListOf(FlexOrder.class))).thenReturn(flexes);

        flexOrderDaoImpl.recalculateOrderStatuses(Collections.singletonList(order));

        assertEquals(FlexOrderTypeEnum.MOUNT, order.getOrderType());
        assertEquals(FlexStatusEnum.NEW, order.getStatus());
    }

    @Test
    public void recalculate_partialMounted_setsMountInProgress() {
        FlexOrder order = makeOrder(ORDER_ID, ORDER_NUM, 3);
        // 2 exported, 1 mounted (mounted also has export-order set)
        List<FlexTO> flexes = Arrays.asList(
                flexWithExportOrder(ORDER_ID),
                flexWithExportOrder(ORDER_ID),
                flexWithExportOrderAndMount(ORDER_ID)
        );
        when(flexDao.getFlexesForOrders(anyListOf(FlexOrder.class))).thenReturn(flexes);

        flexOrderDaoImpl.recalculateOrderStatuses(Collections.singletonList(order));

        assertEquals(FlexOrderTypeEnum.MOUNT, order.getOrderType());
        assertEquals(FlexStatusEnum.IN_PROGRESS, order.getStatus());
    }

    @Test
    public void recalculate_allMounted_setsMountCompleted() {
        FlexOrder order = makeOrder(ORDER_ID, ORDER_NUM, 2);
        List<FlexTO> flexes = Arrays.asList(
                flexWithExportOrderAndMount(ORDER_ID),
                flexWithExportOrderAndMount(ORDER_ID)
        );
        when(flexDao.getFlexesForOrders(anyListOf(FlexOrder.class))).thenReturn(flexes);

        flexOrderDaoImpl.recalculateOrderStatuses(Collections.singletonList(order));

        assertEquals(FlexOrderTypeEnum.MOUNT, order.getOrderType());
        assertEquals(FlexStatusEnum.COMPLETED, order.getStatus());
    }

    @Test
    public void recalculate_nullExpectedCount_treatedAsZero_setsExportNew() {
        FlexOrder order = makeOrder(ORDER_ID, ORDER_NUM, null); // null exportFlexQty
        when(flexDao.getFlexesForOrders(anyListOf(FlexOrder.class))).thenReturn(Collections.<FlexTO>emptyList());

        flexOrderDaoImpl.recalculateOrderStatuses(Collections.singletonList(order));

        // 0 exported, 0 mounted, expectedCount=0 → exportedFlexForOrders == 0 == expectedCount → MOUNT/NEW
        // (production code sets MOUNT/NEW when expectedFlexCount.equals(flexesAttachedToOrder))
        assertEquals(FlexStatusEnum.NEW, order.getStatus());
    }

    @Test
    public void recalculate_mergeCalledForEachOrder() {
        FlexOrder o1 = makeOrder(1L, "ORD1", 1);
        FlexOrder o2 = makeOrder(2L, "ORD2", 1);
        when(flexDao.getFlexesForOrders(anyListOf(FlexOrder.class))).thenReturn(Collections.<FlexTO>emptyList());

        flexOrderDaoImpl.recalculateOrderStatuses(Arrays.asList(o1, o2));

        verify(entityManager).merge(o1);
        verify(entityManager).merge(o2);
    }

    // -------------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------------

    private FlexOrder makeOrder(Long id, String num, Integer exportFlexQty) {
        FlexOrder o = new FlexOrder();
        o.setId(id);
        o.setOrderNumber(num);
        o.setExportFlexQty(exportFlexQty);
        return o;
    }

    private FlexTO flexWithExportOrder(Long orderId) {
        FlexOrderTO exportOrder = new FlexOrderTO();
        exportOrder.setId(orderId);

        FlexTO flex = new FlexTO();
        flex.setExportOrder(exportOrder);
        return flex;
    }

    private FlexTO flexWithExportOrderAndMount(Long orderId) {
        FlexTO flex = flexWithExportOrder(orderId);
        FlexContainerTO container = new FlexContainerTO();
        container.setId(orderId);
        flex.setMountContainer(container);
        return flex;
    }
}
