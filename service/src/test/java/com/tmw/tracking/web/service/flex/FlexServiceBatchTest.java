package com.tmw.tracking.web.service.flex;

import com.tmw.tracking.domain.flex.entities.*;
import com.tmw.tracking.web.TrackingBaseUnitTest;
import com.tmw.tracking.web.controller.FlexController;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;


@Category(TrackingBaseUnitTest.class)
public class FlexServiceBatchTest extends FlexServiceTest {

    private FlexController flexController;
    private String IMP_CONTAINER_NUM_PREFIX = "TSTI000000";
    private String EXP_CONTAINER_NUM_PREFIX = "TSTE000000";


    @Before
    public void setUp() throws Exception {
        super.setUp();
        flexController = injector.getInstance(FlexController.class);
    }



    @Test
    public void testImportBatch_CorrectCase() {
        retrieveImportFrom1c(IMPORT_ORDER_NUM_1);
        importFlexesFromContainer(IMP_CONTAINER_NUM_PREFIX + "1");
        //check
        FlexOrder importOrder = flexOrderDao.getOrderByNumber(IMPORT_ORDER_NUM_1);
        assertEquals(importOrder.getStatus(), FlexStatusEnum.COMPLETED);
    }


    @Test
    public void testExportAndMountBatch_CorrectCase() {
        retrieveImportFrom1c(IMPORT_ORDER_NUM_1);
        importFlexesFromContainer(IMP_CONTAINER_NUM_PREFIX + "1");
        retrieveExportFrom1c(EXPORT_ORDER_NUM_1);

        //EXPORT
        List<String> flexes = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            flexes.add("_TEST_FLEX_" + i);
        }
        attachFlexesToOrder(EXPORT_ORDER_NUM_1, flexes);

        //checking
        FlexOrder exportOrder = flexOrderDao.getOrderByNumber(EXPORT_ORDER_NUM_1);
        assertEquals(exportOrder.getStatus(), FlexStatusEnum.NEW);
        assertEquals(exportOrder.getOrderType(), FlexOrderTypeEnum.MOUNT);


        //MOUNT
        List<FlexController.ContainerFlexParams> containerFlexParams = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            FlexController.ContainerFlexParams param = new FlexController.ContainerFlexParams();
            param.setSerialNumber("_TEST_FLEX_" + i);
            param.setСontainerNum(EXP_CONTAINER_NUM_PREFIX + i);
            containerFlexParams.add(param);
        }
        attachFlexesToContainer(containerFlexParams);

        //checking
        FlexOrder mountOrder = flexOrderDao.getOrderByNumber(EXPORT_ORDER_NUM_1);
        assertEquals(mountOrder.getStatus(), FlexStatusEnum.COMPLETED);
        assertEquals(mountOrder.getOrderType(), FlexOrderTypeEnum.MOUNT);

    }

    @Test
    public void testMountUnmount() {
        retrieveImportFrom1c(IMPORT_ORDER_NUM_1);
        importFlexesFromContainer(IMP_CONTAINER_NUM_PREFIX + "1");
        retrieveExportFrom1c(EXPORT_ORDER_NUM_1);

        //EXPORT
        List<String> flexes = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            flexes.add("_TEST_FLEX_" + i);
        }
        attachFlexesToOrder(EXPORT_ORDER_NUM_1, flexes);

        //MOUNT
        List<FlexController.ContainerFlexParams> containerFlexParams = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            FlexController.ContainerFlexParams param = new FlexController.ContainerFlexParams();
            param.setSerialNumber("_TEST_FLEX_" + i);
            param.setСontainerNum(EXP_CONTAINER_NUM_PREFIX + i);
            containerFlexParams.add(param);
        }
        attachFlexesToContainer(containerFlexParams);


        FlexController.FlexBatchParams detachParams = new FlexController.FlexBatchParams();
        detachParams.setSerialNumbers(flexes);
        flexController.detachFlexFromContainerBatch(detachParams, null);

        //checking
        FlexOrder mountOrder = flexOrderDao.getOrderByNumber(EXPORT_ORDER_NUM_1);
        assertEquals(mountOrder.getStatus(), FlexStatusEnum.IN_PROGRESS);
        assertEquals(mountOrder.getOrderType(), FlexOrderTypeEnum.MOUNT);

    }

    @Test
    public void testDetachMountedFromOrder() {
        retrieveImportFrom1c(IMPORT_ORDER_NUM_1);
        importFlexesFromContainer(IMP_CONTAINER_NUM_PREFIX + "1");
        retrieveExportFrom1c(EXPORT_ORDER_NUM_1);

        //EXPORT
        List<String> flexes = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            flexes.add("_TEST_FLEX_" + i);
        }
        attachFlexesToOrder(EXPORT_ORDER_NUM_1, flexes);

        //MOUNT
        List<FlexController.ContainerFlexParams> containerFlexParams = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            FlexController.ContainerFlexParams param = new FlexController.ContainerFlexParams();
            param.setSerialNumber("_TEST_FLEX_" + i);
            param.setСontainerNum(EXP_CONTAINER_NUM_PREFIX + i);
            containerFlexParams.add(param);
        }
        attachFlexesToContainer(containerFlexParams);


        FlexController.FlexBatchParams detachParams = new FlexController.FlexBatchParams();
        detachParams.setSerialNumbers(flexes);
        flexController.detachFlexFromOrderBatch(detachParams, null);

        //checking
        FlexOrder mountOrder = flexOrderDao.getOrderByNumber(EXPORT_ORDER_NUM_1);
        assertEquals(mountOrder.getStatus(), FlexStatusEnum.IN_PROGRESS);
        assertEquals(mountOrder.getOrderType(), FlexOrderTypeEnum.EXPORT);

        Flex flex = flexDao.getBySerialNumber("_TEST_FLEX_1");
        assertNull(flex.getExportOrder());
        assertNull(flex.getExportDate());
        assertNull(flex.getMountContainer());
        assertNull(flex.getMountDate());
        assertNotNull(flex.getImportContainer());
        assertNotNull(flex.getImportDate());
    }

    private void attachFlexesToContainer(List<FlexController.ContainerFlexParams> containerParams) {
        FlexController.FlexInContainerBatchParams params = new FlexController.FlexInContainerBatchParams();
        params.setFlexInContainerList(containerParams);
        flexController.attachFlexToContainerBatch(params, null);
    }

    private void attachFlexesToOrder(String orderNum, List<String> flexes) {
        FlexController.OrderFlexBatchParams params = new FlexController.OrderFlexBatchParams();
        params.setOrderNum(orderNum);
        params.setSerialNumbers(flexes);
        flexController.attachFlexToOrderBatch(params, null);
    }

    private void importFlexesFromContainer(String containerNum) {
        List<String> flexesToImport = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            flexesToImport.add("_TEST_FLEX_" + i);
        }

        FlexController.ContainerFlexBatchParams batchParams = new FlexController.ContainerFlexBatchParams();
        batchParams.setSerialNumber(flexesToImport);
        batchParams.setСontainerNum(containerNum);

        flexController.importFlexBatch(batchParams, null);

    }


    private void retrieveExportFrom1c(String orderNum) {
        FlexExportPackage flexExportPackage = new FlexExportPackage();
        FlexExportPackage.Order exportOrder = new FlexExportPackage.Order();
        exportOrder.orderNum = orderNum;
        exportOrder.exportContainerQty = EXPORT_FLEXES_QTY_PER_CONTAINER;
        flexExportPackage.orders = new HashSet<>();
        flexExportPackage.orders.add(exportOrder);
        flexService.exportStageFrom1c(flexExportPackage);
    }


    private void retrieveImportFrom1c(String orderNum) {
        FlexImportPackage importPackage = new FlexImportPackage();

        FlexImportPackage.Order order = new FlexImportPackage.Order();
        order.orderNum = orderNum;
        order.containers = new HashSet<>();

        order.containers.add(createPackageContainer(  IMP_CONTAINER_NUM_PREFIX + "1", IMPORT_FLEXES_QTY_PER_CONTAINER));
        importPackage.orders = new HashSet<>();
        importPackage.orders.add(order);
        //importing from 1c - Back (emulation)
        flexService.importStageFrom1c(importPackage);


    }

}
