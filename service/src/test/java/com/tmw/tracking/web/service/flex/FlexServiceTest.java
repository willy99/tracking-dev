package com.tmw.tracking.web.service.flex;

import com.tmw.tracking.domain.flex.dao.FlexDao;
import com.tmw.tracking.domain.flex.dao.FlexOrderDao;
import com.tmw.tracking.domain.flex.dao.FlexWarehouseDao;
import com.tmw.tracking.domain.flex.entities.*;
import com.tmw.tracking.domain.flex.to.FlexContainerTO;
import com.tmw.tracking.domain.flex.to.FlexOrderTO;
import com.tmw.tracking.service.FlexService;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import com.tmw.tracking.web.service.exception.ValidationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;


@Category(TrackingBaseUnitTest.class)
public class FlexServiceTest extends TrackingBaseUnitTest {

    protected FlexService flexService;
    protected FlexWarehouseDao flexWarehouseDao;
    protected FlexOrderDao flexOrderDao;
    protected FlexDao flexDao;
    protected static final String IMPORT_ORDER_NUM_1 = "_TEST_IMP1";
    protected static final String IMPORT_ORDER_NUM_2 = "_TEST_IMP2";
    protected static final String EXPORT_ORDER_NUM_1 = "_TEST_EXP1";
    protected static final String EXPORT_ORDER_NUM_2 = "_TEST_EXP2";
    protected static final Integer EXPORT_FLEXES_QTY_PER_CONTAINER = 2;
    protected static final Integer IMPORT_FLEXES_QTY_PER_CONTAINER = 4;

    /**
     * {@inheritDoc}
     * @see TrackingBaseUnitTest#setUp()
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        flexService = injector.getInstance(FlexService.class);
        flexWarehouseDao = injector.getInstance(FlexWarehouseDao.class);
        flexOrderDao = injector.getInstance(FlexOrderDao.class);
        flexDao = injector.getInstance(FlexDao.class);
        deleteOrders(new ArrayList<String>(){{add(IMPORT_ORDER_NUM_1);add(IMPORT_ORDER_NUM_2);add(EXPORT_ORDER_NUM_1);add(EXPORT_ORDER_NUM_2);}});
    }

    protected void deleteOrders(List<String> orderNumbers) {
        List<FlexOrder> ordersToDelete = flexService.getOrdersByNumber(orderNumbers);
        flexOrderDao.deleteOrderBatch(ordersToDelete);
    }


    private void testImport(String orderNum, String containerNum) {
        FlexImportPackage importPackage = new FlexImportPackage();

        FlexImportPackage.Order order = new FlexImportPackage.Order();
        order.orderNum = orderNum;
        order.containers = new HashSet<>();
        //String CONTAINER_NUMBER = "IMPR1234567";

        order.containers.add(createPackageContainer(containerNum + "1", IMPORT_FLEXES_QTY_PER_CONTAINER));
        order.containers.add(createPackageContainer(containerNum + "2", IMPORT_FLEXES_QTY_PER_CONTAINER));
        importPackage.orders = new HashSet<>();
        importPackage.orders.add(order);

        //importing from 1c - Back (emulation)
        flexService.importStageFrom1c(importPackage);

        for (int i = 1; i < 3; i++) {
            final String CONTAINER_NUMBER = containerNum + i;
            FlexContainer container = flexService.getContainerByNumber(CONTAINER_NUMBER);
            assertEquals(FlexStatusEnum.NEW, container.getStatus());

            //add flex - APP->Back
            flexService.importFlex(createFlex(CONTAINER_NUMBER + "->1", CONTAINER_NUMBER));

            //Checking
            container = flexService.getContainerByNumber(CONTAINER_NUMBER);
            assertEquals(FlexStatusEnum.IN_PROGRESS, container.getStatus());

            //validation for the same flex
            try {
                flexService.importFlex(createFlex(CONTAINER_NUMBER + "->1", CONTAINER_NUMBER));
                fail("Should be error, flex already exists");
            } catch (ValidationException ex) {
                assertTrue(ex.getMessage().contains("Flex already exists"));
            }

            //add second flex - complete container
            flexService.importFlex(createFlex(CONTAINER_NUMBER + "->bad", CONTAINER_NUMBER)); //for writing-off
            flexService.importFlex(createFlex(CONTAINER_NUMBER + "->reserve", CONTAINER_NUMBER)); //for reserve
            flexService.importFlex(createFlex(CONTAINER_NUMBER + "->2", CONTAINER_NUMBER));

            container = flexService.getContainerByNumber(CONTAINER_NUMBER);
            assertEquals(FlexStatusEnum.COMPLETED, container.getStatus());

            //check order
            FlexOrder updatedOrder = flexService.getOrderByNumber(orderNum);
            assertEquals(FlexStatusEnum.COMPLETED, updatedOrder.getStatus());

            //validation for the completed state container
            try {
                flexService.importFlex(createFlex(CONTAINER_NUMBER + "->3", CONTAINER_NUMBER));
                fail("Should be error, Trying to add flex from completed container");
            }
            catch (ValidationException ex) {
                assertTrue(ex.getMessage().contains("Trying to add flex from completed container"));
            }


        }

    }

    private void testExport(String orderNum, String containerNum) {
        List<FlexOrder> exportOrderList = flexService.getAvailableOrdersForExport("");
        assertEquals(0, exportOrderList.size());

        FlexExportPackage flexExportPackage = new FlexExportPackage();
        FlexExportPackage.Order exportOrder = new FlexExportPackage.Order();
        exportOrder.orderNum = orderNum;
        exportOrder.exportContainerQty = EXPORT_FLEXES_QTY_PER_CONTAINER;
        flexExportPackage.orders = new HashSet<>();
        flexExportPackage.orders.add(exportOrder);

        flexService.exportStageFrom1c(flexExportPackage);

        //checking
        exportOrderList = flexService.getAvailableOrdersForExport("");
        assertEquals(1, exportOrderList.size());

        FlexOrder flexExportOrder = flexService.getOrderByNumber(orderNum);
        assertEquals(FlexStatusEnum.NEW, flexExportOrder.getStatus());

        //add flex - local UI->backend operation

        flexService.attachFlexToOrder(containerNum + "->1", orderNum);
        flexExportOrder = flexService.getOrderByNumber(orderNum);
        assertEquals(FlexStatusEnum.IN_PROGRESS, flexExportOrder.getStatus());

        flexService.attachFlexToOrder(containerNum + "->2", orderNum);
        flexExportOrder = flexService.getOrderByNumber(orderNum);
        assertEquals(FlexStatusEnum.NEW, flexExportOrder.getStatus());
        //orders for export should be empty, the order comes to mount list
        exportOrderList = flexService.getAvailableOrdersForExport("");
        assertEquals(0, exportOrderList.size());
        exportOrderList = flexService.getAvailableOrdersForMount("");
        assertEquals(1, exportOrderList.size());

        //validation for the completed state order
        try {
            flexService.attachFlexToOrder(containerNum + "->reserve", orderNum);
            fail("Should be error, Trying to attach flex to completed order");
        }
        catch (ValidationException ex) {
            assertTrue(ex.getMessage().contains("Trying to attach flex to completed order"));
        }

    }

    private void testMount(String orderNum, String containerNum) {

        //String CONTAINER_NUMBER = "IMPR1234567"; //import container - serial num
        FlexOrder flexExportOrder = flexService.getOrderByNumber(orderNum);

        flexService.attachFlexToContainer(containerNum + "->1", "EXPR0000001");
        flexExportOrder = flexService.getOrderByNumber(orderNum);
        assertEquals(FlexStatusEnum.IN_PROGRESS, flexExportOrder.getStatus());
        try {
            flexService.attachFlexToContainer(containerNum + "->1", "EXPR0000001");
            fail("Should be error, Trying to attach to container, which is already in use");
        }
        catch (ValidationException ex) {
            assertTrue(ex.getMessage().contains("is already attached to container"));
        }

        //FlexContainer updatedContainer = flexService.getContainerByNumber("EXPR0000001");
        //assertEquals(flexExportOrder, updatedContainer);

        flexService.attachFlexToContainer(containerNum + "->2", "EXPR0000002");
        flexExportOrder = flexService.getOrderByNumber(orderNum);
        assertEquals(FlexStatusEnum.COMPLETED, flexExportOrder.getStatus());
        //orders for export should be empty, the order comes to mount list
        List<FlexOrder> exportOrderList = flexService.getAvailableOrdersForExport("");
        assertEquals(0, exportOrderList.size());
        exportOrderList = flexService.getAvailableOrdersForMount("");
        assertEquals(1, exportOrderList.size());
        //validation for the completed state order
        try {
            flexService.attachFlexToContainer(containerNum + "->reserve", "EXPR0000003");
            fail("Should be error, Trying to attach flex to completed order");
        }
        catch (ValidationException ex) {
            assertTrue(ex.getMessage().contains("does not have an export order assigned"));
        }

        List<FlexOrderTO> mountedOrders = flexService.getMountedOrders();
        assertEquals(1, mountedOrders.size());
        assertEquals(2, mountedOrders.get(0).getContainers().size());
        FlexContainerTO mountedContainer = mountedOrders.get(0).getContainers().iterator().next();
        assertEquals(1, mountedContainer.getFlexes().size());
    }

    //@Test
    public void testImportFlexFromContainerToWarehouse() {

        //STAGE 1 :: IMPORT
        testImport(IMPORT_ORDER_NUM_1, "IMPR111111");
        testImport(IMPORT_ORDER_NUM_2,"IMPR111112");


        //check imported orders
        List<FlexOrderTO> importedOrders = flexService.getImportedOrders();
        assertEquals(2, importedOrders.size());
        assertEquals(2, importedOrders.get(0).getContainers().size());
        FlexContainerTO importedContainer = importedOrders.get(0).getContainers().iterator().next();
        assertEquals(IMPORT_FLEXES_QTY_PER_CONTAINER, new Integer(importedContainer.getFlexes().size()));


        //STAGE 2 :: EXPORT
        testExport(EXPORT_ORDER_NUM_1, "IMPR1111111");
        //testExport(EXPORT_ORDER_NUM_2, "IMPR1111112");

        //STAGE 3:: MOUNT
        testMount(EXPORT_ORDER_NUM_1, "IMPR1111111");
        //testMount(EXPORT_ORDER_NUM_2, "IMPR1111112");

        //UNMOUNT/WRITEOFF


    }

    @Test
    public void testAddUpdateContainer() {
        FlexImportPackage importPackage = new FlexImportPackage();

        FlexImportPackage.Order order = new FlexImportPackage.Order();
        order.orderNum = IMPORT_ORDER_NUM_1;
        order.containers = new HashSet<>();
        order.containers.add(createPackageContainer("NNNN1234567", 5));

        importPackage.orders = new HashSet<>();
        importPackage.orders.add(order);

        flexService.importStageFrom1c(importPackage);

        FlexOrder flexOrder = flexService.getOrderByNumber(IMPORT_ORDER_NUM_1);
        assertNotNull(flexOrder);

        List<FlexContainer> flexContainersList = flexService.getContainerListByOrder(IMPORT_ORDER_NUM_1);
        assertEquals(1, flexContainersList.size());

        //second try, updating the first container, adding the second
        order.containers.clear();
        order.containers.add(createPackageContainer("NNNN1234567", 4));
        order.containers.add(createPackageContainer("NNNN1234568", 6));

        flexService.importStageFrom1c(importPackage);

        flexContainersList = flexService.getContainerListByOrder(IMPORT_ORDER_NUM_1);
        assertEquals(2, flexContainersList.size());
        assertEquals(new Integer(4), flexContainersList.get(0).getImportFlexQty());
        assertEquals(FlexStatusEnum.NEW, flexContainersList.get(0).getStatus());
        assertEquals(new Integer(6), flexContainersList.get(1).getImportFlexQty());
        assertEquals(FlexStatusEnum.NEW, flexContainersList.get(1).getStatus());

    }

    @Test
    public void testValidationForContainers() {
        createOrder(IMPORT_ORDER_NUM_1);
        FlexOrder order = flexService.getOrderByNumber(IMPORT_ORDER_NUM_1);
        assertNotNull(order);
        FlexContainer invalidNumFlexContainer = createFlexContainer("NNNN123456", order, 5);
        FlexContainer invalidNum2FlexContainer = createFlexContainer("3948jfkdj3j", order, 5);
        FlexContainer invalidQtyFlexContainer = createFlexContainer("NNNN1234567", order, 0);
        try {
            flexService.upsertContainers(new ArrayList<FlexContainer>(){{add(invalidNumFlexContainer);}});
            fail("Incorrect Container Number");
        }
        catch (ValidationException ex) {
            assertTrue(ex.getMessage().contains("Incorrect Container Number"));
        }
        try {
            flexService.upsertContainers(new ArrayList<FlexContainer>(){{add(invalidNum2FlexContainer);}});
            fail("Incorrect Container Number");
        }
        catch (ValidationException ex) {
            assertTrue(ex.getMessage().contains("Incorrect Container Number"));
        }
        try {
            flexService.upsertContainers(new ArrayList<FlexContainer>(){{add(invalidQtyFlexContainer);}});
            fail("Container should have at least one flex");
        }
        catch (ValidationException ex) {
            assertTrue(ex.getMessage().contains("Container should have at least one flex"));
        }
    }

    @Test
    public void testDefaultWarehouse() {
        FlexWarehouse warehouse = flexWarehouseDao.getBaseWarehouse();
        assertNotNull(warehouse);
        assertEquals(warehouse.getName(), FlexWarehouseDao.BASE_WAREHOUSE);
    }

    @Test
    public void testFlexOrderHashEquals() {
        createOrder(IMPORT_ORDER_NUM_1);
        FlexOrder newOrder = new FlexOrder();
        newOrder.setOrderNumber(IMPORT_ORDER_NUM_1);
        FlexOrder createdOrder = flexService.getOrderByNumber(IMPORT_ORDER_NUM_1);
        assertEquals(createdOrder, newOrder);

        List<FlexOrder> orders = new ArrayList<>();
        orders.add(createdOrder);

        assertTrue(orders.contains(newOrder));
    }

    protected void createOrder(String orderNum) {
        FlexOrder order = new FlexOrder();
        order.setOrderNumber(orderNum);
        order.setOrderType(FlexOrderTypeEnum.IMPORT);
        order.setStatus(FlexStatusEnum.NEW);
        flexService.addOrder(order);
    }

    protected FlexContainer createFlexContainer(String containerNumber, FlexOrder order, Integer flexQty) {
        FlexContainer container = new FlexContainer();
        container.setContainerNumber(containerNumber);
        container.setImportOrder(order);
        container.setImportFlexQty(flexQty);
        return container;
    }

    protected FlexImportPackage.Container createPackageContainer(String number, Integer qty) {
        FlexImportPackage.Container container = new FlexImportPackage.Container();
        container.containerNumber = number;
        container.flexQty = qty;
        return container;
    }

    protected Flex createFlex(String serialNumber, String containerNumber) {
        Flex flex = new Flex();
        flex.setImportContainer(flexService.getContainerByNumber(containerNumber));
        flex.setSerialNumber(serialNumber);
        return flex;
    }



}
