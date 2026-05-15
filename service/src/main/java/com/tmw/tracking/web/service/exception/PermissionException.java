package com.tmw.tracking.web.service.exception;
import com.tmw.tracking.web.service.util.error.ErrorCode;

/**
 * Created by pzhelnov on 2/16/2017.
 */
public class PermissionException extends ServiceException {

    public PermissionException(String message) {
        super(message, ErrorCode.AUTH_ERROR_PERMISSION_DENIED);
    }
}
