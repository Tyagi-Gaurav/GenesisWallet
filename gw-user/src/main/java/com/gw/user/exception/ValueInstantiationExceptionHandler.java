package com.gw.user.exception;

import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import com.gw.common.domain.ErrorResponse;
import com.gw.common.exception.ErrorResponseHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

@ControllerAdvice
@Component
@Order(value = 1)
public class ValueInstantiationExceptionHandler {
    private static final Logger LOG = LogManager.getLogger("APP");

    private final ErrorResponseHelper errorResponseHelper;

    public ValueInstantiationExceptionHandler(ErrorResponseHelper errorResponseHelper) {
        this.errorResponseHelper = errorResponseHelper;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {ValueInstantiationException.class})
    public Mono<ErrorResponse> handle(ValueInstantiationException exception) {
        if (LOG.isErrorEnabled()) {
            LOG.error(String.format("Value Instantiation Exception: %s ", exception.getMessage()), exception);
        }
        return errorResponseHelper.errorResponse(400, exception.getMessage());
    }
}
