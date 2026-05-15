package com.tmw.tracking.web.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.tmw.tracking.service.TrackingService;
import com.tmw.tracking.web.service.exception.ValidationException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/auction")
@Singleton
public class AuctionController extends BaseController {

    private final TrackingService trackingService;
    private final static Logger logger = LoggerFactory.getLogger(TrackingController.class);

    @Inject
    public AuctionController(final TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/find")
    public String getAuctionInfo(@QueryParam("lot") final String lot, @QueryParam("vin") final String vin, @QueryParam("auction") final String auction) {
        if (StringUtils.isBlank(lot) && StringUtils.isBlank(vin)) {
            throw new ValidationException("Incorrect auction Lot/Vin");
        }
        return trackingService.trackAuction(lot, vin, auction);
    }
}
