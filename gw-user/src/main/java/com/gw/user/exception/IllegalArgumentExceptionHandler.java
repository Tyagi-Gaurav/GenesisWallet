package com.gw.user.exception;

import com.gw.common.domain.ErrorResponse;
import com.gw.common.exception.ErrorResponseHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
@Component
@Order(value = 1)
public class IllegalArgumentExceptionHandler {
    private static final Logger LOG = LogManager.getLogger("APP");

    private final ErrorResponseHelper errorResponseHelper;

    public IllegalArgumentExceptionHandler(ErrorResponseHelper errorResponseHelper) {
        this.errorResponseHelper = errorResponseHelper;
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public Mono<ErrorResponse> handle(IllegalArgumentException exception) {
        if (LOG.isErrorEnabled()) {
            LOG.error(exception.getMessage(), exception);
        }
        return errorResponseHelper.errorResponse(400, exception.getMessage());
    }
}
