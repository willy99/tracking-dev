package com.tmw.tracking.web.service.util.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.tmw.tracking.utils.DomainUtils;
import com.tmw.tracking.utils.Utils;
import com.tmw.tracking.web.service.exception.ServiceException;
import com.tmw.tracking.web.service.util.error.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pzhelnov on 2/16/2017.
 */
public class TrackingResponseFilter implements ContainerResponseFilter {
    private final static Logger logger = LoggerFactory.getLogger(TrackingResponseFilter.class);
    private static String remoteAddr;
    private static List<String> allowedOrigins;


    /**
     * {@inheritDoc}
     * @see ContainerResponseFilter#filter(com.sun.jersey.spi.container.ContainerRequest, com.sun.jersey.spi.container.ContainerResponse)
     */
    @Override
    public ContainerResponse filter(final ContainerRequest containerRequest, final ContainerResponse containerResponse) {
        if (allowedOrigins == null) {
            allowedOrigins = Arrays.asList(DomainUtils.getProperties().getProperty("origin.list.allowed").split(","));
        }
        if (containerRequest != null) {
            List<String> originList = containerRequest.getRequestHeaders().get("origin");
            if (originList != null && originList.size()>0) {
                remoteAddr = originList.get(0);
            }
        }
        if(containerResponse != null && (containerResponse.getStatus() == Response.Status.OK.getStatusCode() || containerResponse.getStatus() == Response.Status.NO_CONTENT.getStatusCode()))
            updateResponse(containerResponse);
        else if(containerResponse != null && containerResponse.getStatus() != 200) {
            processError(containerResponse);
        }
        return containerResponse;
    }

    /**
     * Convert service result to {@link ServiceResponse}
     * @param containerResponse the {@code ContainerResponse} object. Cannot be {@code null}.
     */
    private void updateResponse(final ContainerResponse containerResponse){
        if(containerResponse == null || (containerResponse.getStatus() != Response.Status.OK.getStatusCode() && containerResponse.getStatus() != Response.Status.NO_CONTENT.getStatusCode()))return;
        final ObjectMapper objectMapper = Utils.initObjectMapper();
        final StringWriter stringWriter = new StringWriter();
        try {
            objectMapper.writeValue(stringWriter, new ServiceResponse(com.tmw.tracking.web.service.util.response.Status.DONE, containerResponse.getEntity()));
            containerResponse.setEntity(stringWriter.toString());
            containerResponse.setStatusType(Response.Status.OK);
            //if (allowedOrigins.contains(remoteAddr)) {
            //TODO security - remove after test for release
                containerResponse.getHttpHeaders().add("Access-Control-Allow-Origin", "*");
                containerResponse.getHttpHeaders().add("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, locale");
                containerResponse.getHttpHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                containerResponse.getHttpHeaders().add("Access-Control-Max-Age", "600");
                containerResponse.getHttpHeaders().add(HttpHeaders.CONTENT_ENCODING, "UTF-8");

            //}
        }
        catch (Exception e){
            logger.error(Utils.errorToString(e));
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Convert {@link ServiceException} to {@link ServiceResponse}
     * @param containerResponse the {@code ContainerResponse} object. Cannot be {@code null}.
     */
    private void processError(final ContainerResponse containerResponse){
        if(containerResponse == null || containerResponse.getMappedThrowable() == null
                || !(containerResponse.getMappedThrowable() instanceof ServiceException))return;
        final ObjectMapper objectMapper = Utils.initObjectMapper();
        final StringWriter stringWriter = new StringWriter();
        try {
            final ServiceException error = (ServiceException)containerResponse.getMappedThrowable();
            logger.error(Utils.errorToString(error));
            if (ErrorCode.ENTITY_NOT_FOUND.getCode().equals(error.getCode())) {
                containerResponse.setStatusType(Response.Status.NOT_FOUND);
                containerResponse.setStatus(Response.Status.NOT_FOUND.getStatusCode());
            } else {
                containerResponse.setStatusType(Response.Status.INTERNAL_SERVER_ERROR);
            }
            objectMapper.writeValue(stringWriter, new ServiceResponse(com.tmw.tracking.web.service.util.response.Status.ERROR, new com.tmw.tracking.web.service.util.response.Error((String)error.getResponse().getEntity(), error.getCode())));
            containerResponse.setEntity(stringWriter.toString());
            //containerResponse.setStatusType(Response.Status.OK);
        }
        catch (Exception e){
            logger.error(Utils.errorToString(e));
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }


}