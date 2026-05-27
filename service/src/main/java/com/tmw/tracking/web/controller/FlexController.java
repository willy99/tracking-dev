package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.domain.flex.entities.Flex;
import com.tmw.tracking.domain.flex.entities.FlexContainer;
import com.tmw.tracking.domain.flex.entities.FlexOrder;
import com.tmw.tracking.domain.flex.entities.FlexWarehouse;
import com.tmw.tracking.domain.flex.to.*;
import com.tmw.tracking.entity.User;
import com.tmw.tracking.service.FlexConnectorService;
import com.tmw.tracking.service.FlexService;
import com.tmw.tracking.service.impl.AuthenticationServiceImpl;
import com.tmw.tracking.utils.ValidationUtils;
import com.tmw.tracking.web.aop.MethodCall;
import com.tmw.tracking.web.service.exception.ValidationException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.InputStream;
import java.util.*;

@Path("/flex")
@Singleton
public class FlexController extends BaseController {

    private final FlexService flexService;
    private final FlexConnectorService flexConnectorService;
    private final static Logger logger = LoggerFactory.getLogger(FlexController.class);

    @Inject
    public FlexController(
            final FlexService flexService,
            final FlexConnectorService flexConnectorService
    ) {
        this.flexService = flexService;
        this.flexConnectorService = flexConnectorService;
    }


    //1c
    @GET
    @Path("/getImportedOrders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_1C_OPERATOR)
    public List<FlexOrderTO> getImportOrders(
                                    @QueryParam("token") final String token,
                                    @Context UriInfo uriInfo,
                                    @Context HttpServletResponse response) {
        return flexService.getImportedOrders();
    }

    //1c
    @GET
    @Path("/getMountedOrders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_1C_OPERATOR)
    public List<FlexOrderTO> getMountedOrders(
                                    @QueryParam("token") final String token,
                                    @Context UriInfo uriInfo,
                                    @Context HttpServletResponse response) {
        return flexService.getMountedOrders();
    }

    //1c
    @GET
    @Path("/getRenamedFlexList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_1C_OPERATOR)
    public List<FlexRenameTO> getRenamedFlexList(
                                    @QueryParam("token") final String token,
                                    @Context UriInfo uriInfo,
                                    @Context HttpServletResponse response) {
        return flexService.getRenamedFlexList();
    }

    //app
    @GET
    @Path("/getRemovedFlexList")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<String> getRemovedFlexList(
                                    @QueryParam("token") final String token,
                                    @Context UriInfo uriInfo,
                                    @Context HttpServletResponse response) {
        return flexService.getRemovedFlexList();
    }

    //app
    @GET
    @Path("/flexListForWarehouse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<Flex> getFlexListForWarehouse(
            @QueryParam("warehouseName") final String warehouseName,
            @QueryParam("token") final String token) {
        if (StringUtils.isBlank(warehouseName)) {
            throw new ValidationException("Warehouse Name should be provided");
        }
        return flexService.getFlexListForWarehouse(warehouseName);
    }

    @GET
    @Path("/flexListWrittenOff")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<FlexTO> getFlexListWrittenOff(
            @QueryParam("token") final String token) {
        return flexService.getFlexListWrittenOff();
    }

    //app
    @GET
    @Path("/flexListForImportContainer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public Map<String, List<String>> getFlexListForImportContainer(
            @QueryParam("containerNum") final String containerNum,
            @QueryParam("token") final String token) {
        if (StringUtils.isBlank(containerNum)) {
            throw new ValidationException("Container Number should be provided");
        }
        return flexService.getFlexListForImportContainer(ValidationUtils.validateFlexContainerNum(containerNum));
    }

    //app
    @GET
    @Path("/getImportOrders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<FlexOrder> getImportOrders(@QueryParam("searchString") String searchString,
                                    @QueryParam("token") final String token,
                                    @Context UriInfo uriInfo,
                                    @Context HttpServletResponse response) {
        return flexService.getAvailableOrdersForImport(searchString != null? searchString.toUpperCase(): null);
    }

    //app
    @GET
    @Path("/getImportContainers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<FlexContainerTO> getImportContainers(@QueryParam("searchString") String searchString,
                                    @QueryParam("token") final String token,
                                    @Context UriInfo uriInfo,
                                    @Context HttpServletResponse response) {
        return flexService.getAvailableContainersForImport(searchString != null? searchString.toUpperCase(): null);
    }

    //app
    @GET
    @Path("/getExportOrdersWithStatistic")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<FlexOrderTO> getExportOrdersWithStatistic(@QueryParam("searchString") String searchString,
                                    @QueryParam("token") final String token,
                                    @Context UriInfo uriInfo,
                                    @Context HttpServletResponse response) {
        return flexService.getExportOrdersWithStatistic(searchString != null? searchString.toUpperCase(): null);
    }

    //app
    @GET
    @Path("/getMountedOrdersWithStatistic")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<FlexOrderTO> getMountedOrdersWithStatistic(@QueryParam("searchString") String searchString,
                                    @QueryParam("token") final String token,
                                    @Context UriInfo uriInfo,
                                    @Context HttpServletResponse response) {
        return flexService.getMountedOrdersWithStatistic(searchString != null? searchString.toUpperCase(): null);
    }


    //app
    @GET
    @Path("/getImportContainersForOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<FlexContainer> getImportContainersForOrder(@QueryParam("orderNum") @NotNull String orderNum,
                                                             @QueryParam("token") final String token,
                                                             @Context UriInfo uriInfo,
                                                             @Context HttpServletResponse response) {
        return flexService.getContainerListByOrder(ValidationUtils.validateFlexOrderNum(orderNum));
    }

    //app
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/importFlex/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public void importFlex(ContainerFlexParams importFlexParams,
                             @Context HttpServletResponse servletResponse) {

        Flex flex = new Flex();
        flex.setSerialNumber(ValidationUtils.validateFlexSerialNum(importFlexParams.getSerialNumber()));
        FlexContainer flexContainer = flexService.getContainerByNumber(ValidationUtils.validateFlexContainerNum(importFlexParams.getСontainerNum()));
        flex.setImportContainer(flexContainer);
        flexService.importFlex(flex);
    }

    //app
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/removeFlex/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public void removeFlex(FlexParams params,
                           @Context HttpServletResponse servletResponse) {

        flexService.removeFlex(ValidationUtils.validateFlexSerialNum(params.getSerialNumber()));
    }

    //app
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/removeFlexBatch/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public void removeFlexBatch(FlexBatchParams params,
                           @Context HttpServletResponse servletResponse) {
        Set<String> validatedSerialNumbers = new HashSet<>();
        for (String inputFlexNumber: params.getSerialNumbers()) {
            validatedSerialNumbers.add(ValidationUtils.validateFlexSerialNum(inputFlexNumber));
        }
        params.setSerialNumbers(new ArrayList<>(validatedSerialNumbers));
        flexService.removeFlexBatch(params.getSerialNumbers());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/importFlexBatch/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public void importFlexBatch(ContainerFlexBatchParams importFlexBatchParams,
                             @Context HttpServletResponse servletResponse) {
        FlexContainer flexContainer = flexService.getContainerByNumber(ValidationUtils.validateFlexContainerNum(importFlexBatchParams.getСontainerNum()));
        Set<String> validatedSerialNumbers = new HashSet<>();
        for (String inputFlexNumber: importFlexBatchParams.getSerialNumbers()) {
            validatedSerialNumbers.add(ValidationUtils.validateFlexSerialNum(inputFlexNumber));
        }
        flexService.importBatchFlex(flexContainer, new ArrayList<>(validatedSerialNumbers));
    }

    //APP
    @POST
    @Path("/retrieveImportPackageFrom1c/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public void retrieveImportPackageFrom1c(
                                    @Context UriInfo uriInfo,
                                    @Context HttpServletResponse response) {
        flexService.importStageFrom1c(flexConnectorService.retrieveImportPackage());
    }





    //* *** * * * ** **** ** EXPORT

    //app
    @POST
    @Path("/retrieveExportPackageFrom1c/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public void retrieveExportPackageFrom1c(@Context UriInfo uriInfo,
                                            @Context HttpServletResponse response) {
        flexService.exportStageFrom1c(flexConnectorService.retrieveExportPackage());
    }

    //app
    @GET
    @Path("/getExportOrders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<FlexOrder> getExportOrders(@QueryParam("searchString") String searchString,
                                    @QueryParam("token") final String token,
                                    @Context UriInfo uriInfo,
                                    @Context HttpServletResponse response) {
        return flexService.getAvailableOrdersForExport(searchString != null? searchString.toUpperCase(): null);
    }

    @GET
    @Path("/flexListForExportOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public Map<String, List<String>> getFlexListForExportOrder(
            @QueryParam("orderNum") final String orderNum,
            @QueryParam("token") final String token) {
        return flexService.getFlexListForExportOrder(orderNum != null? orderNum.toUpperCase(): null);
    }

    @GET
    @Path("/flexListForReservedWarehouse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<String> getFlexListForReservedWarehouse(
            @QueryParam("token") final String token) {
        return flexService.getFlexListForReservedWarehouse();
    }

    @GET
    @Path("/flexListForBaseWarehouse")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<String> getFlexListForBaseWarehouse(
            @QueryParam("token") final String token) {
        return flexService.getFlexListForBaseWarehouse();
    }

    @GET
    @Path("/flexEntitiesForExportOrder")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<FlexTO> getFlexEntitiesForExportOrder(
            @QueryParam("orderNum") final String orderNum,
            @QueryParam("token") final String token) {
        return flexService.getFlexEntitiesForExportOrder(orderNum != null? orderNum.toUpperCase(): null);
    }

    @GET
    @Path("/flexListMountedToContainers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public Map<String, List<FlexTO>> flexListMountedToContainers(
            @QueryParam("orderNum") final String orderNum,
            @QueryParam("token") final String token) {
        return flexService.flexListMountedToContainers(orderNum != null ? orderNum.toUpperCase(): null);
    }

    //app
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/attachFlexToOrder/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public void attachFlexToOrder(OrderFlexParams flexParams,
                             @Context HttpServletResponse servletResponse) {
        logger.debug(">> attachFlexToOrder " + flexParams);
        flexService.attachFlexToOrder(ValidationUtils.validateFlexSerialNum(flexParams.getSerialNumber()), ValidationUtils.validateFlexOrderNum(flexParams.getOrderNum()));
    }

    //app
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/attachFlexToOrderBatch/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public void attachFlexToOrderBatch(OrderFlexBatchParams orderFlexBatchParams,
                             @Context HttpServletResponse servletResponse) {

        logger.debug(">> attachFlexToOrderBatch " + orderFlexBatchParams);
        List<String> validatedSerialNums = new ArrayList<>();
        for (String param: orderFlexBatchParams.getSerialNumbers()) {
            validatedSerialNums.add(ValidationUtils.validateFlexSerialNum(param));
        }
        orderFlexBatchParams.setSerialNumbers(validatedSerialNums);
        orderFlexBatchParams.setOrderNum(ValidationUtils.validateFlexOrderNum(orderFlexBatchParams.getOrderNum()));
        flexService.attachFlexToOrderBatch(orderFlexBatchParams);
    }

    @GET
    @Path("/getMountOrders")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<FlexOrder> getMountOrders(@QueryParam("searchString") String searchString,
                                    @QueryParam("token") final String token,
                                    @Context UriInfo uriInfo,
                                    @Context HttpServletResponse response) {
        return flexService.getAvailableOrdersForMount(searchString != null? searchString.toUpperCase(): null);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/attachFlexToContainer/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_MOUNTER)
    public void attachFlexToContainer(ContainerFlexParams flexParams,
                             @Context HttpServletResponse servletResponse) {
        logger.debug(">> attachFlexToContainer " + flexParams);
        flexService.attachFlexToContainer(ValidationUtils.validateFlexSerialNum(flexParams.getSerialNumber()), ValidationUtils.validateFlexContainerNum(flexParams.getСontainerNum()));
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/attachFlexToContainerBatch/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_MOUNTER)
    public void attachFlexToContainerBatch(FlexInContainerBatchParams params,
                             @Context HttpServletResponse servletResponse) {

        logger.debug(">> attachFlexToContainerBatch " + params);
        for (ContainerFlexParams param: params.getFlexInContainerList()) {
            param.setSerialNumber(ValidationUtils.validateFlexSerialNum(param.getSerialNumber()));
            param.setСontainerNum(ValidationUtils.validateFlexContainerNum(param.getСontainerNum()));
        }
        flexService.attachFlexToContainerBatch(params);
    }

    //app
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/detachFlexFromContainer/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public void detachFlexFromContainer(FlexParams flexParams,
                             @Context HttpServletResponse servletResponse) {
        logger.debug(">> detachFlexFromContainer " + flexParams);
        flexService.detachFlexFromContainer(ValidationUtils.validateFlexSerialNum(flexParams.getSerialNumber()));
    }

    //app
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/detachFlexFromContainerBatch/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public void detachFlexFromContainerBatch(FlexBatchParams flexBatchParams,
                             @Context HttpServletResponse servletResponse) {
        logger.debug(">> detachFlexFromContainerBatch " + flexBatchParams);
        List<String> validatedSerialNumbers = new ArrayList<>();
        if (flexBatchParams == null || flexBatchParams.getSerialNumbers().isEmpty()) {
            throw new ValidationException("Empty serial number list");
        }
        for (String serialNumber: flexBatchParams.getSerialNumbers()) {
            validatedSerialNumbers.add(ValidationUtils.validateFlexSerialNum(serialNumber));
        }
        flexService.detachFlexFromContainerBatch(validatedSerialNumbers);
    }

    //app
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/detachFlexFromOrder/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public void detachFlexFromOrder(FlexParams flexParams,
                             @Context HttpServletResponse servletResponse) {
        logger.debug(">> detachFlexFromOrder " + flexParams);
        flexService.detachFlexFromOrder(ValidationUtils.validateFlexSerialNum(flexParams.getSerialNumber()));
    }

    //app
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/detachFlexFromOrderBatch/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public void detachFlexFromOrderBatch(FlexBatchParams flexBatchParams,
                             @Context HttpServletResponse servletResponse) {
        logger.debug(">> detachFlexFromOrderBatch " + flexBatchParams);
        List<String> validatedSerialNumbers = new ArrayList<>();
        if (flexBatchParams == null || flexBatchParams.getSerialNumbers().isEmpty()) {
            throw new ValidationException("Empty serial number list");
        }
        for (String serialNumber: flexBatchParams.getSerialNumbers()) {
            validatedSerialNumbers.add(ValidationUtils.validateFlexSerialNum(serialNumber));
        }
        flexService.detachFlexFromOrderBatch(validatedSerialNumbers);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/renameFlex/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public void renameFlex(RenameFlexParams renameFlexParams,
                             @Context HttpServletResponse servletResponse) {
        logger.debug(">> renameFlex " + renameFlexParams);
        flexService.renameFlex(ValidationUtils.validateFlexSerialNum(renameFlexParams.getOldSerialNumber()), ValidationUtils.validateFlexSerialNum(renameFlexParams.getNewSerialNumber()));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/writeOffFlex/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_MOUNTER)
    public void writeOffFlex(FlexParams flexParams,
                             @Context HttpServletResponse servletResponse) {
        logger.debug(">> writeOffFlex " + flexParams);
        flexService.writeOffFlex(ValidationUtils.validateFlexSerialNum(flexParams.getSerialNumber()));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cancelWriteOffFlex/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_MOUNTER)
    public void cancelWriteOffFlex(FlexParams flexParams,
                                   @Context HttpServletResponse servletResponse) {
        logger.debug(">> cancelWriteOffFlex " + flexParams);
        flexService.cancelWriteOffFlex(ValidationUtils.validateFlexSerialNum(flexParams.getSerialNumber()));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reserveFlex/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_UPDATE)
    public void reserveFlex(FlexParams flexParams,
                            @Context HttpServletResponse servletResponse) {
        logger.debug(">> reserveFlex " + flexParams);
        flexService.reserveFlex(ValidationUtils.validateFlexSerialNum(flexParams.getSerialNumber()));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/unReserveFlex/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_UPDATE)
    public void unReserveFlex(FlexParams flexParams,
                            @Context HttpServletResponse servletResponse) {
        logger.debug(">> unReserveFlex " + flexParams);
        flexService.unReserveFlex(ValidationUtils.validateFlexSerialNum(flexParams.getSerialNumber()));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/reserveFlexBatch/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_UPDATE)
    public void reserveFlexBatch(FlexBatchParams flexBatchParams,
                            @Context HttpServletResponse servletResponse) {
        logger.debug(">> reserveFlexBatch " + flexBatchParams);
        Set<String> validatedSerialNums = new HashSet<>();
        for (String serialNum: flexBatchParams.getSerialNumbers()) {
            validatedSerialNums.add(ValidationUtils.validateFlexSerialNum(serialNum));
        }
        flexService.reserveFlexBatch(new ArrayList<>(validatedSerialNums));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/unReserveFlexBatch/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_UPDATE)
    public void unReserveFlexBatch(FlexBatchParams flexBatchParams,
                            @Context HttpServletResponse servletResponse) {
        logger.debug(">> unReserveFlexBatch " + flexBatchParams);
        Set<String> validatedSerialNums = new HashSet<>();
        for (String serialNum: flexBatchParams.getSerialNumbers()) {
            validatedSerialNums.add(ValidationUtils.validateFlexSerialNum(serialNum));
        }
        flexService.unReserveFlexBatch(new ArrayList<>(validatedSerialNums));
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/setReservedFlexToOrder/{token}")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_MOUNTER)
    public void setReservedFlexToOrder(OrderFlexParams orderFlexParams,
                             @Context HttpServletResponse servletResponse) {
        logger.debug(">> setReservedFlexToOrder " + orderFlexParams);
        flexService.setReservedFlexToOrder(ValidationUtils.validateFlexSerialNum(orderFlexParams.getSerialNumber()), ValidationUtils.validateFlexOrderNum(orderFlexParams.getOrderNum()));
    }

    @GET
    @Path("/getStatistic")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public StatisticTO getStatistic(@QueryParam("token") final String token,
                                      @Context UriInfo uriInfo,
                                      @Context HttpServletResponse response) {
        flexService.importStageFrom1c(flexConnectorService.retrieveImportPackage());
        flexService.exportStageFrom1c(flexConnectorService.retrieveExportPackage());
        return flexService.getStatistic();
    }


    @GET
    @Path("/getWarehouses")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<FlexWarehouse> getWarehouses(@QueryParam("token") final String token,
                                       @Context UriInfo uriInfo,
                                       @Context HttpServletResponse response) {
        return flexService.getWarehouses();
    }


    @GET
    @Path("/getFlexWarehouseMovement")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_READ)
    public List<FlexHistoryTO> getFlexWarehouseMovement(@QueryParam("searchString") String searchString,
                                    @QueryParam("token") final String token,
                                    @Context UriInfo uriInfo,
                                    @Context HttpServletResponse response) {
        return flexService.getFlexWarehouseMovement(searchString != null? searchString.toUpperCase(): null);
    }



    // FLEX WEB - BATCH OPERATIONS

    @GET
    @Produces(MediaType.TEXT_HTML + ";charset=utf-8")
    @Path("/batchImportFlex")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC)
    public Viewable getBatchImportFlex() {
        User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("user", authenticatedUser);
        vars.put("environment", environment);
        return new Viewable("/flex/batchImportFlex", vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML + ";charset=utf-8")
    @Path("/batchExportFlex")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC)
    public Viewable getBatchExportFlex() {
        User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("user", authenticatedUser);
        vars.put("environment", environment);
        return new Viewable("/flex/batchExportFlex", vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML + ";charset=utf-8")
    @Path("/batchMountFlex")
    @MethodCall(requiredPermission = PermissionType.LOGISTIC)
    public Viewable getBatchMountFlex() {
        User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("user", authenticatedUser);
        vars.put("environment", environment);
        return new Viewable("/flex/batchMountFlex", vars);
    }

    @POST
    @Path("/executeBatchImportFlex")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public String executeBatchImportFlex(
            @FormDataParam("batchfile") InputStream uploadedInputStream,
            @FormDataParam("batchfile") FormDataContentDisposition fileDetails) {
        try {
            List<List<String>> params = parseExcel(uploadedInputStream);
            if (params.isEmpty()) {
                throw new ValidationException("Empty content in an excel file");
            }
            ContainerFlexBatchParams importFlexBatchParams = new ContainerFlexBatchParams();
            importFlexBatchParams.serialNumbers = new ArrayList<String>();
            // get order num
            for (int i = 0; i < params.size(); i++) {
                if (i == 0) {
                    importFlexBatchParams.сontainerNum = params.get(i).get(0);
                } else {
                    importFlexBatchParams.serialNumbers.add(params.get(i).get(0));
                }
            }
            System.out.println(">>> Sending export batch");
            System.out.println(importFlexBatchParams);
            FlexContainer flexContainer = flexService.getContainerByNumber(ValidationUtils.validateFlexContainerNum(importFlexBatchParams.getСontainerNum()));
            Set<String> validatedSerialNumbers = new HashSet<>();
            for (String inputFlexNumber: importFlexBatchParams.getSerialNumbers()) {
                validatedSerialNumbers.add(ValidationUtils.validateFlexSerialNum(inputFlexNumber));
            }
            flexService.importBatchFlex(flexContainer, new ArrayList<>(validatedSerialNumbers));

        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "ok";
    }

    @POST
    @Path("/executeBatchExportFlex")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public String executeBatchExportFlex(
            @FormDataParam("batchfile") InputStream uploadedInputStream,
            @FormDataParam("batchfile") FormDataContentDisposition fileDetails) {
        try {
            List<List<String>> params = parseExcel(uploadedInputStream);
            if (params.isEmpty()) {
                throw new ValidationException("Empty content in an excel file");
            }
            OrderFlexBatchParams flexBatchParams = new OrderFlexBatchParams();
            flexBatchParams.serialNumbers = new ArrayList<String>();
            // get order num
            for (int i = 0; i < params.size(); i++) {
                if (i == 0) {
                    flexBatchParams.orderNum = params.get(i).get(0);
                } else {
                    flexBatchParams.serialNumbers.add(params.get(i).get(0));
                }
            }
            System.out.println(">>> Sending export batch");
            System.out.println(flexBatchParams);
            List<String> validatedSerialNums = new ArrayList<>();
            for (String param: flexBatchParams.getSerialNumbers()) {
                validatedSerialNums.add(ValidationUtils.validateFlexSerialNum(param));
            }
            flexBatchParams.setSerialNumbers(validatedSerialNums);
            flexBatchParams.setOrderNum(ValidationUtils.validateFlexOrderNum(flexBatchParams.getOrderNum()));
            flexService.attachFlexToOrderBatch(flexBatchParams);
        }
        catch (Exception e) {
            return e.getMessage();
        }
        return "ok";
    }

    @POST
    @Path("/executeBatchMountFlex")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @MethodCall(requiredPermission = PermissionType.LOGISTIC_WAREHOUSE)
    public String executeBatchMountFlex(
            @FormDataParam("batchfile") InputStream uploadedInputStream,
            @FormDataParam("batchfile") FormDataContentDisposition fileDetails) {
        try {
            List<List<String>> params = parseExcel(uploadedInputStream);
            if (params.isEmpty()) {
                throw new ValidationException("Empty content in an excel file");
            }
            FlexInContainerBatchParams flexBatchParams = new FlexInContainerBatchParams();
            flexBatchParams.flexInContainerList = new ArrayList<ContainerFlexParams>();
            for (int i = 0; i < params.size(); i++) {
                ContainerFlexParams containerFlexParams = new ContainerFlexParams();
                containerFlexParams.serialNumber = params.get(i).get(0);
                containerFlexParams.сontainerNum = params.get(i).get(1);
                flexBatchParams.flexInContainerList.add(containerFlexParams);
            }
            System.out.println(">>> Sending mount batch");
            System.out.println(flexBatchParams);
            for (ContainerFlexParams param: flexBatchParams.getFlexInContainerList()) {
                param.setSerialNumber(ValidationUtils.validateFlexSerialNum(param.getSerialNumber()));
                param.setСontainerNum(ValidationUtils.validateFlexContainerNum(param.getСontainerNum()));
            }
            flexService.attachFlexToContainerBatch(flexBatchParams);
        }
        catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "ok";
    }

    private List<List<String>> parseExcel(InputStream uploadedInputStream) throws Exception {
        List<List<String>> params = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(uploadedInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        // Find number of rows in excel file
        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        // Create a loop over all the rows of excel file to read it
        for (int i = 0; i < rowCount + 1; i++) {
            Row row = sheet.getRow(i);
            System.out.print(i + 1 + " -> ");
            // Create a loop to print cell values in a row
            List<String> rowList = new ArrayList<>();
            if (row == null) {
                continue;
            }
            for (int j = 0; j < row.getLastCellNum(); j++) {
                // Print Excel data in console
                System.out.print(row.getCell(j) + " || ");
                String cellvalue = "" + row.getCell(j);
                if (!cellvalue.trim().isEmpty()) {
                    rowList.add(cellvalue);
                }
            }
            if (!rowList.isEmpty()) {
                params.add(rowList);
            }
        }
        if (params.isEmpty()) {
            throw new ValidationException("Invalid Excel, sheet 1 should contain information");
        }
        return params;
    }



    public static class ContainerFlexBatchParams {
        private List<String> serialNumbers;
        private String сontainerNum;

        public List<String> getSerialNumbers() {
            return serialNumbers;
        }

        public void setSerialNumber(List<String> serialNumbers) {
            this.serialNumbers = serialNumbers;
        }

        public String getСontainerNum() {
            return сontainerNum;
        }

        public void setСontainerNum(String сontainerNum) {
            this.сontainerNum = сontainerNum;
        }
    }

    public static class FlexInContainerBatchParams {
        private List<ContainerFlexParams> flexInContainerList;

        public List<ContainerFlexParams> getFlexInContainerList() {
            return flexInContainerList;
        }

        public void setFlexInContainerList(List<ContainerFlexParams> flexInContainerList) {
            this.flexInContainerList = flexInContainerList;
        }
    }

    public static class ContainerFlexParams {
        private String serialNumber;
        private String сontainerNum;

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getСontainerNum() {
            return сontainerNum;
        }

        public void setСontainerNum(String сontainerNum) {
            this.сontainerNum = сontainerNum;
        }
    }

    public static class OrderFlexParams {
        private String serialNumber;
        private String orderNum;

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }
    }

    public static class OrderFlexBatchParams {
        private String orderNum;
        private List<String> serialNumbers;

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public List<String> getSerialNumbers() {
            return serialNumbers;
        }

        public void setSerialNumbers(List<String> serialNumbers) {
            this.serialNumbers = serialNumbers;
        }
    }

    public static class RenameFlexParams {
        private String oldSerialNumber;
        private String newSerialNumber;

        public String getOldSerialNumber() {
            return oldSerialNumber;
        }

        public void setOldSerialNumber(String oldSerialNumber) {
            this.oldSerialNumber = oldSerialNumber;
        }

        public String getNewSerialNumber() {
            return newSerialNumber;
        }

        public void setNewSerialNumber(String newSerialNumber) {
            this.newSerialNumber = newSerialNumber;
        }
    }

    public static class FlexParams {
        private String serialNumber;

        public String getSerialNumber() {
            return serialNumber;
        }

        public void setSerialNumber(String serialNumber) {
            this.serialNumber = serialNumber;
        }
    }

    public static class FlexBatchParams {
        private List<String> serialNumbers;

        public List<String> getSerialNumbers() {
            return serialNumbers;
        }

        public void setSerialNumbers(List<String> serialNumbers) {
            this.serialNumbers = serialNumbers;
        }
    }



}
