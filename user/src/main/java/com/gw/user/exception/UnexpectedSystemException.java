package com.gw.user.exception;

public class UnexpectedSystemException extends RuntimeException {
    public UnexpectedSystemException() {
        super();
    }

    public UnexpectedSystemException(Exception e) {
        super(e);
    }
}
