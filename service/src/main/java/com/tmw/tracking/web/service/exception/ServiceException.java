package com.tmw.tracking.web.service.exception;

import com.tmw.tracking.web.service.util.error.ErrorCode;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by pzhelnov on 11/11/2016.
 */
public class ServiceException extends WebApplicationException {
    private final String code;
    private boolean important;

    public ServiceException(final String message, final ErrorCode code) {
        this(message, code, true);
    }

    public ServiceException(final String message, final ErrorCode code, final boolean important) {
        this(message, code, new Exception(message));
        this.important = important;
    }

    public ServiceException(final String message, final ErrorCode code, Throwable throwable) {
        super(throwable,Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(message).type(MediaType.TEXT_PLAIN).build());
        this.code = code.getCode();
    }


    public String getCode() {
        return code;
    }

    public boolean isImportant() {
        return important;
    }
}