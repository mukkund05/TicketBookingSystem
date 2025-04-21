package com.hexaware.exception;

/**
 * Custom exception for when an event is not found.
 */
public class EventNotFoundException extends Exception {
    public EventNotFoundException(String message) {
        super(message);
    }
}