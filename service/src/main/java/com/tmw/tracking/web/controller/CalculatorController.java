package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.domain.containercalc.entities.Cargo;
import com.tmw.tracking.domain.containercalc.entities.ContainerCalcRequest;
import com.tmw.tracking.domain.containercalc.entities.ContainerCalcResult;
import com.tmw.tracking.domain.containercalc.entities.PackageType;
import com.tmw.tracking.entity.ContainerType;
import com.tmw.tracking.entity.paging.Page;
import com.tmw.tracking.service.ContainerCalcService;
import com.tmw.tracking.utils.DomainUtils;
import com.tmw.tracking.web.service.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/calculator")
@Singleton
public class CalculatorController extends BaseController {

    private final ContainerCalcService containerCalcService;

    private final static Logger logger = LoggerFactory.getLogger(CalculatorController.class);

    @Inject
    public CalculatorController(
            final ContainerCalcService containerCalcService) {
        this.containerCalcService = containerCalcService;
    }

    @GET
    @Produces("text/html; charset=UTF-8")
    @Path("/containercalc")
    public Viewable getContainerCalcPage() {
        //User authenticatedUser = AuthenticationServiceImpl.getAuthenticatedUser();
        final Map<String, Object> vars = new HashMap<>();
        vars.put("angular", true);
        vars.put("pageSize", Page.ITEMS_ON_PAGE);
        vars.put("environment", environment);
        vars.put("packageTypes", containerCalcService.getPackagetTypes());
        vars.put("containerTypes", containerCalcService.getContainerTypes());
        return new Viewable("/calculator/containercalc", vars);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/packages")
    public Collection<PackageType> getPackageTypes() {
        return containerCalcService.getPackagetTypes();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/containercalc")
    public Collection<ContainerCalcResult> calculateContainer(ContainerCalcRequest containerCalcRequest,
                                                              @Context UriInfo uriInfo,
                                                              @Context HttpServletResponse response) {
        validateCargo(containerCalcRequest.getCargo());
        validateContainerDate(containerCalcRequest.getContainerTypes());
        //TODO validate container types
        return containerCalcService.retrieveCalculation(containerCalcRequest.getCargo(), containerCalcRequest.getContainerTypes());
    }


    private void validateContainerDate(List<ContainerType> containerTypes) {
        if (containerTypes == null) {
            throw new ValidationException(i18NService.getValue("invalid_parameter"));
        }
        for (ContainerType containerType : containerTypes) {
            if (containerType.getWorkload() == null) {
                throw new ValidationException(i18NService.getValue("calculator_validation_invalid_workload"));
            }

            if (containerType.getWorkload() < DomainUtils.getConstantDoubleValue("calculator_parameter_container_min_workload")) {
                throw new ValidationException(i18NService.getValue("calculator_validation_parameter_container_too_small"));
            }

            if (containerType.getWorkload() > containerType.getMaxWorkload()) {
                containerType.setWorkload(containerType.getMaxWorkload());
            }
        }
    }

    private void validateCargo(Cargo cargo) {
        if (cargo.getPackageType() == null) {
            throw new ValidationException(i18NService.getValue("calculator_validation_empty_package_type"));
        }
        logger.debug("Cargo weight " + cargo.getWeight());
        logger.debug("Cargo height " + cargo.getHeight());
        logger.debug("Cargo width " + cargo.getWidth());
        logger.debug("Cargo length " + cargo.getLength());
        if (cargo.getWeight() == null || cargo.getWidth() == null || cargo.getLength() == null || cargo.getHeight() == null) {
            throw new ValidationException(i18NService.getValue("calculator_validation_number_parameter_missing"));
        }
        if (cargo.getWeight() < DomainUtils.getConstantDoubleValue("calculator_parameter_cargo_min_weight") ||
                cargo.getWidth() < DomainUtils.getConstantDoubleValue("calculator_parameter_cargo_min_width") ||
                cargo.getLength() < DomainUtils.getConstantDoubleValue("calculator_parameter_cargo_min_length") ||
                cargo.getHeight() < DomainUtils.getConstantDoubleValue("calculator_parameter_cargo_min_height")) {
            throw new ValidationException(i18NService.getValue("calculator_validation_parameter_too_small"));
        }
        if (cargo.getWeight() > DomainUtils.getConstantDoubleValue("calculator_parameter_cargo_max_weight") ||
                cargo.getWidth() > DomainUtils.getConstantDoubleValue("calculator_parameter_cargo_max_width") ||
                cargo.getLength() > DomainUtils.getConstantDoubleValue("calculator_parameter_cargo_max_length") ||
                cargo.getHeight() > DomainUtils.getConstantDoubleValue("calculator_parameter_cargo_max_height") ) {
            throw new ValidationException(i18NService.getValue("calculator_validation_parameter_too_big"));
        }
    }

}
