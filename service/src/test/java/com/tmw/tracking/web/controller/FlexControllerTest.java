package com.tmw.tracking.web.controller;

import com.tmw.tracking.domain.flex.entities.*;
import com.tmw.tracking.domain.flex.to.FlexSearchFilterTO;
import com.tmw.tracking.domain.flex.to.FlexTO;
import com.tmw.tracking.entity.Company;
import com.tmw.tracking.utils.DomainUtils;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import com.tmw.tracking.web.hibernate.EntityManagerProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Exercises {@link FlexController#searchFlexes} the way the Flex Management page uses it,
 * against the disposable H2 test database (see TrackingBaseUnitTest / tracking-tests.properties)
 * — nothing here touches the real DB.
 */
@Category(TrackingBaseUnitTest.class)
public class FlexControllerTest extends TrackingBaseUnitTest {

    private FlexController flexController;
    private EntityManager entityManager;
    private Company tenant;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        flexController = injector.getInstance(FlexController.class);
        entityManager = injector.getInstance(EntityManagerProvider.class).getEntityManager();
        tenant = DomainUtils.getCurrentUser().getTenant();
    }

    @Test
    public void searchFlexes_onBalance_excludesExportedFlexes() {
        String balanceSerial = "_TEST_BAL_" + System.nanoTime();
        String exportedSerial = "_TEST_EXP_" + System.nanoTime();

        FlexWarehouse warehouse = createWarehouse();
        createFlex(balanceSerial, warehouse, null);

        FlexOrder exportOrder = createExportOrder("_TEST_ORDNUM_" + System.nanoTime());
        createFlex(exportedSerial, warehouse, exportOrder);

        FlexSearchFilterTO filter = new FlexSearchFilterTO();
        filter.setOnBalance(true);
        List<FlexTO> result = flexController.searchFlexes(filter);

        assertTrue("on-balance flex should be in the result", containsSerial(result, balanceSerial));
        assertFalse("exported flex must not show up as on-balance", containsSerial(result, exportedSerial));
    }

    @Test
    public void searchFlexes_hasExportOrder_returnsOnlyExportedFlexes() {
        String balanceSerial = "_TEST_BAL_" + System.nanoTime();
        String exportedSerial = "_TEST_EXP_" + System.nanoTime();

        FlexWarehouse warehouse = createWarehouse();
        createFlex(balanceSerial, warehouse, null);

        FlexOrder exportOrder = createExportOrder("_TEST_ORDNUM_" + System.nanoTime());
        createFlex(exportedSerial, warehouse, exportOrder);

        FlexSearchFilterTO filter = new FlexSearchFilterTO();
        filter.setHasExportOrder(true);
        List<FlexTO> result = flexController.searchFlexes(filter);

        assertTrue("exported flex should be in the result", containsSerial(result, exportedSerial));
        assertFalse("on-balance flex must not show up as exported", containsSerial(result, balanceSerial));
    }

    @Test
    public void searchFlexes_bySerialNumber_matchesPartialCaseInsensitive() {
        String serial = "_TEST_SN_" + System.nanoTime();
        createFlex(serial, createWarehouse(), null);

        FlexSearchFilterTO filter = new FlexSearchFilterTO();
        filter.setSerialNum(serial.substring(0, serial.length() - 4).toLowerCase());
        List<FlexTO> result = flexController.searchFlexes(filter);

        assertTrue("serial number filter should find the flex by partial match", containsSerial(result, serial));
    }

    // ------------------------------------------------------------------------

    private boolean containsSerial(List<FlexTO> result, String serial) {
        for (FlexTO flexTO : result) {
            if (serial.equals(flexTO.getSerialNumber())) {
                return true;
            }
        }
        return false;
    }

    private FlexWarehouse createWarehouse() {
        FlexWarehouse warehouse = new FlexWarehouse();
        warehouse.setTenant(tenant);
        warehouse.setName("_TEST_WH_" + System.nanoTime());
        warehouse.setWarehouseType(FlexWarehouseTypeEnum.BASE);
        entityManager.getTransaction().begin();
        entityManager.persist(warehouse);
        entityManager.getTransaction().commit();
        return warehouse;
    }

    private FlexOrder createExportOrder(String orderNumber) {
        FlexOrder order = new FlexOrder();
        order.setTenant(tenant);
        order.setOrderNumber(orderNumber);
        order.setOrderType(FlexOrderTypeEnum.EXPORT);
        order.setExportContainerQty(0);
        order.setExportFlexQty(0);
        order.setStatus(FlexStatusEnum.NEW);
        entityManager.getTransaction().begin();
        entityManager.persist(order);
        entityManager.getTransaction().commit();
        return order;
    }

    private Flex createFlex(String serialNumber, FlexWarehouse warehouse, FlexOrder exportOrder) {
        Flex flex = new Flex();
        flex.setTenant(tenant);
        flex.setSerialNumber(serialNumber);
        flex.setWarehouse(warehouse);
        flex.setExportOrder(exportOrder);
        flex.setImportDate(new Date());
        entityManager.getTransaction().begin();
        entityManager.persist(flex);
        entityManager.getTransaction().commit();
        return flex;
    }
}
