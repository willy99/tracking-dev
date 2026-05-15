package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.dao.ContainerTypeDao;
import com.tmw.tracking.dao.DriverDao;
import com.tmw.tracking.dao.TrackingLineDao;
import com.tmw.tracking.domain.PermissionType;
import com.tmw.tracking.entity.ContainerType;
import com.tmw.tracking.entity.Driver;
import com.tmw.tracking.entity.TrackingLine;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.aop.MethodCall;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/dict")
@Singleton
public class DictController extends BaseController {

    private final ContainerTypeDao containerTypeDao;
    private final DriverDao driverDao;
    private final TrackingLineDao trackingLineDao;
    private final static Logger logger = LoggerFactory.getLogger(DictController.class);

    @Inject
    public DictController(
            final ContainerTypeDao containerTypeDao,
            final DriverDao driverDao,
            final TrackingLineDao trackingLineDao) {
        this.containerTypeDao = containerTypeDao;
        this.driverDao = driverDao;
        this.trackingLineDao = trackingLineDao;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/containerTypes")
    @MethodCall(requiredPermission = PermissionType.DICT_CONT_TYPES_SHOW)
    public Viewable getContainerTypes() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("containers", containerTypeDao.getAll());
        vars.put("environment", environment);
        vars.put("pageSize", Page.ITEMS_ON_PAGE);
        return new Viewable("/dict/containerTypes", vars);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllContainerTypes")
    @MethodCall(requiredPermission = PermissionType.DICT_CONT_TYPES_SHOW)
    public List<ContainerType> getAllContainerTypes() {
        return containerTypeDao.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getContainerTypes")
    @MethodCall(requiredPermission = PermissionType.DICT_CONT_TYPES_SHOW)
    public List<ContainerType> getContainerTypesByPage(@Context UriInfo uriInfo,
                                                       @Context HttpServletResponse response) {

        final Page<ContainerType> page = containerTypeDao.getByPage(getPageQuery(uriInfo));
        setPageHeaders(page, response);
        return page.getContent();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getContainerType")
    @MethodCall(requiredPermission = PermissionType.DICT_CONT_TYPES_SHOW)
    public ContainerType getRole(@QueryParam("id") final Long id) {
        return containerTypeDao.getById(id);
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/saveContainerType")
    @MethodCall(requiredPermission = PermissionType.DICT_CONT_TYPES_UPDATE)
    public String saveContainterType(ContainerType containerType) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        try {
            containerTypeDao.update(containerType);
        } catch (Exception e) {
            logger.error("", e);
            vars.put("errorMessage", "Error during saving Container Type" + e.getMessage());
        }
        return Utils.toJson(vars);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteContainerType")
    @MethodCall(requiredPermission = PermissionType.DICT_CONT_TYPES_UPDATE)
    public String deleteContainterType(ContainerType containerType) {
        final Map<String, Object> vars = new HashMap<String, Object>();
        if (containerType == null) {
            vars.put("errorMessage", "Container Type is empty");
            return Utils.toJson(vars);
        }

        ContainerType ct = containerTypeDao.getById(containerType.getId());
        if (ct == null) {
            vars.put("errorMessage", "Container Type is not found by id");
            return Utils.toJson(vars);
        }
        try {
            containerTypeDao.delete(containerType);
        } catch (Exception e) {
            logger.error("", e);
            vars.put("errorMessage", "Error during deleting Container Type" + e.getMessage());
        }
        return Utils.toJson(vars);
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/drivers")
    @MethodCall(requiredPermission = PermissionType.DICT_DRIVERS_SHOW)
    public Viewable getDriversPage() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("drivers", driverDao.getAll());
        vars.put("environment", environment);
        vars.put("pageSize", Page.ITEMS_ON_PAGE);
        return new Viewable("/dict/drivers", vars);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllDrivers")
    @MethodCall(requiredPermission = PermissionType.DICT_DRIVERS_SHOW)
    public List<Driver> getAllDrivers() {
        return driverDao.getAll();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getDrivers")
    @MethodCall(requiredPermission = PermissionType.DICT_DRIVERS_SHOW)
    public List<Driver> getDrivers(@Context UriInfo uriInfo,
                                   @Context HttpServletResponse response) {

        final Page<Driver> page = driverDao.getByPage(getPageQuery(uriInfo));
        setPageHeaders(page, response);
        return page.getContent();
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/lines")
    @MethodCall(requiredPermission = PermissionType.DICT_CONT_LINES_SHOW)
    public Viewable getTrackingLinesPage() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("lines", trackingLineDao.getAll());
        vars.put("environment", environment);
        vars.put("pageSize", Page.ITEMS_ON_PAGE);
        return new Viewable("/dict/lines", vars);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getAllLines")
    @MethodCall(requiredPermission = PermissionType.DICT_CONT_LINES_SHOW)
    public List<TrackingLine> getAllTrackingLines() {
        return trackingLineDao.getAll();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getLines")
    @MethodCall(requiredPermission = PermissionType.DICT_CONT_LINES_SHOW)
    public List<TrackingLine> getTrackingLines(@Context UriInfo uriInfo,
                                               @Context HttpServletResponse response) {

        final Page<TrackingLine> page = trackingLineDao.getByPage(getPageQuery(uriInfo));

        setPageHeaders(page, response);
        return page.getContent();
    }

}
