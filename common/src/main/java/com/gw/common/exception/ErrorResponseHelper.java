package com.gw.common.exception;

import com.gw.common.domain.ErrorResponse;
import com.gw.common.metrics.ExceptionCounter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ErrorResponseHelper {
    private static final Logger LOG = LogManager.getLogger("APP");

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
