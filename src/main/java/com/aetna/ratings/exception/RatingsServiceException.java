package com.aetna.ratings.exception;

public class RatingsServiceException extends RuntimeException{
    public RatingsServiceException(String message, Throwable cause) {
        super(message, cause);
        cause.printStackTrace();
    }
    public RatingsServiceException(String message) {
        super(message);
    }
}
