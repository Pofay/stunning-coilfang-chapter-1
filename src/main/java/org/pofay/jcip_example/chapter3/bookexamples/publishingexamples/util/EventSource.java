package org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.util;

import java.util.ArrayList;
import java.util.List;

public class EventSource {
    private final List<EventListener> listeners = new ArrayList<>();

    public void registerListener(EventListener listener) {
        listeners.add(listener);
    }

    public void fireEvent(String msg) {
        Event event = new Event(msg);
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
