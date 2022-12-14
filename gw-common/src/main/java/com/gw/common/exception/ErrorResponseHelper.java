package com.gw.common.exception;

import com.gw.common.domain.ErrorResponse;
import com.gw.common.metrics.ExceptionCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ErrorResponseHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ErrorResponseHelper.class);

    private final ExceptionCounter exceptionCounter;

    public ErrorResponseHelper(ExceptionCounter exceptionCounter) {
        this.exceptionCounter = exceptionCounter;
    }

    public Mono<ErrorResponse> errorResponse(int statusCode, String message) {
        ErrorResponse errorResponse = new ErrorResponse(statusCode, message);

        LOG.error("Response with status code {}", statusCode);

        exceptionCounter.increment(statusCode);

        return Mono.just(errorResponse);
    }
}
