package org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.util;

public class Event {
    private final String message;

    public Event(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
