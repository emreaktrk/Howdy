package com.codify.howdy.exception;

public class ImplementationMissingException extends RuntimeException {

    public ImplementationMissingException() {
        super("Make sure you implement it..");
    }
}
