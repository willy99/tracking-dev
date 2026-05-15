package com.tmw.tracking.web.service.exception;

import com.tmw.tracking.web.service.util.error.ErrorCode;

import java.util.List;
import java.util.Set;

/**
 * Created by pzhelnov on 2/16/2017.
 */
public class ValidationException extends ServiceException {


    public ValidationException(String message) {
        super(message, ErrorCode.PARAMETER_IS_INVALID);
    }

    public ValidationException(List<String> messageList) {
        super(String.join("\r\n", messageList), ErrorCode.PARAMETER_IS_INVALID);
    }

    public ValidationException(Set<String> messageList) {
        super(String.join("\r\n", messageList), ErrorCode.PARAMETER_IS_INVALID);
    }
}
