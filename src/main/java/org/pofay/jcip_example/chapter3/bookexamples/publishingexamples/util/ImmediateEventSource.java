package org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.util;

public class ImmediateEventSource {
    public void registerListener(EventListener listener) {
        listener.onEvent(new Event("Immediate"));
    }
}
