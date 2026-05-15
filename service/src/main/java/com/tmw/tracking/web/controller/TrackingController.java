package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.view.Viewable;
import com.tmw.tracking.service.TrackingService;
import com.tmw.tracking.web.service.exception.ValidationException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pzhelnov on 11/18/2016.
 */
@Path("/tracking")
@Singleton
public class TrackingController extends BaseController {

    private final TrackingService trackingService;
    private final static Logger logger = LoggerFactory.getLogger(TrackingController.class);

    @Inject
    public TrackingController(final TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @Path("/trackContainer")
    public Viewable getTrackContainer() {
        final Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("angular", true);
        vars.put("environment", environment);
        return new Viewable("/tracking/trackContainer", vars);
    }


    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/find")
    public String getTrackingInfo(@QueryParam("container") final String container) {
        if (StringUtils.isBlank(container)) {
            throw new ValidationException("Incorrect tracking number");
        }
        return trackingService.trackContainer(container);
    }
}
