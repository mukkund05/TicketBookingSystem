package com.hexaware.exception;

/**
 * Custom exception for invalid booking IDs.
 */
public class InvalidBookingIDException extends Exception {
    public InvalidBookingIDException(String message) {
        super(message);
    }
}