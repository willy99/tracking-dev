package com.tmw.tracking.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.tmw.tracking.service.TrackingService;
import com.tmw.tracking.web.service.exception.ServiceException;
import com.tmw.tracking.web.service.util.error.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;

@Singleton
public class TrackingServiceImpl implements TrackingService {

    private final static Logger logger = LoggerFactory.getLogger(TrackingServiceImpl.class);
    private final String trackingServerLogin;
    private final String trackingServerPassword;
    private final String trackingServerPort;
    private final String trackingServerTrackApiUrl;
    private final String trackingServerAuctionApiUrl;
    private final String trackingServerUrl;


    @Inject
    public TrackingServiceImpl(
            @Named("tracking.server.login") final String trackingServerLogin,
            @Named("tracking.server.password") final String trackingServerPassword,
            @Named("tracking.server.port") final String trackingServerPort,
            @Named("tracking.server.track.api.url") final String trackingServerTrackApiUrl,
            @Named("tracking.server.auction.api.url") final String trackingServerAuctionApiUrl,
            @Named("tracking.server.url") final String trackingServerUrl) {
        this.trackingServerLogin = trackingServerLogin;
        this.trackingServerPassword = trackingServerPassword;
        this.trackingServerPort = trackingServerPort;
        this.trackingServerTrackApiUrl = trackingServerTrackApiUrl;
        this.trackingServerAuctionApiUrl = trackingServerAuctionApiUrl;
        this.trackingServerUrl = trackingServerUrl;

    }

    @Override
    public String trackContainer(String containerNumber) {
        try {
            return getRefId(containerNumber, trackingServerTrackApiUrl);
        }
        catch (Exception e) {
            throw new ServiceException(e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public String trackAuction(String lot, String vin, String auction) {
        try {
            return getAuctionRefId(lot, vin, auction, trackingServerAuctionApiUrl);
        }
        catch (Exception e) {
            throw new ServiceException(e.getMessage(), ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String getAuctionRefId(String lot, String vin, String auction, String api) {
        Client client = Client.create();

        client.addFilter(new HTTPBasicAuthFilter(trackingServerLogin, trackingServerPassword));
        String lotInput =  (lot != null? "lot=" + lot + "&": "");
        String vinInput =  (vin != null? "vin=" + vin + "&": "");
        String auctionInput =  "auction=" + (auction != null? auction: "");
        String input = lotInput + vinInput + auctionInput;
        WebResource webResource = client
                .resource( trackingServerUrl + (trackingServerPort!=null?trackingServerPort:"") + "/" + api + "?" + input);

        return getRefIdByWebResource(webResource);
    }

    private String getRefId(String container, String api) {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter(trackingServerLogin, trackingServerPassword));
        WebResource webResource = client
                .resource( trackingServerUrl + (trackingServerPort!=null?trackingServerPort:"") + "/" + api + "/" + container);
        return getRefIdByWebResource(webResource);
    }

    private String getRefIdByWebResource(WebResource webResource) {
        ClientResponse response = webResource
                .accept("application/json")
                .type("application/json")
                .get(ClientResponse.class);

        logger.debug("Status "+ response.getStatus());
        if (response.getStatus() == 204) {
            return response.getEntity(String.class);
        }
        if (response.getStatus() != 200) {
            throw new ServiceException("Failed to get tracking number", ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return response.getEntity(String.class);
    }

}
