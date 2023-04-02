package com.realstaq.configuration.error;
/**
 * Used for throwing Exceptions to a client side.
 *
 */
public class BadRequestException extends IllegalArgumentException{

    public BadRequestException(String message) {
        super(message);
    }
}
