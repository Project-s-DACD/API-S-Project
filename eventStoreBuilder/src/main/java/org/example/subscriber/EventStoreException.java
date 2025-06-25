package org.example.subscriber;

public class EventStoreException extends Exception {
    public EventStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
