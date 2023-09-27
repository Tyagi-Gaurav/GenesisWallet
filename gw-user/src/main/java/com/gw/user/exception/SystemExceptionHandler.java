package com.gw.user.exception;

import com.gw.common.domain.ErrorResponse;
import com.gw.common.exception.ErrorResponseHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;
import reactor.core.publisher.Mono;

@ControllerAdvice
@Component
public class SystemExceptionHandler {
    private static final Logger LOG = LogManager.getLogger("APP");

    private final ErrorResponseHelper errorResponseHelper;
    private static final String UNEXPECTED_ERROR_OCCURRED = "Unexpected error occurred";

    public SystemExceptionHandler(ErrorResponseHelper errorResponseHelper) {
        this.errorResponseHelper = errorResponseHelper;
    }

    @ResponseStatus(code= HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {Exception.class})
    public Mono<ErrorResponse> handleException(Exception exception) {
        if (LOG.isErrorEnabled()) {
            LOG.error(exception.getMessage(), exception);
        }
        return errorResponseHelper.errorResponse(500, UNEXPECTED_ERROR_OCCURRED);
    }

    @ResponseStatus(code= HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {UnsupportedMediaTypeStatusException.class})
    public Mono<ErrorResponse> handleException(UnsupportedMediaTypeStatusException exception) {
        if (LOG.isErrorEnabled()) {
            LOG.error(exception.getMessage(), exception);
        }
        return errorResponseHelper.errorResponse(400, "No resource Found");
    }
}
