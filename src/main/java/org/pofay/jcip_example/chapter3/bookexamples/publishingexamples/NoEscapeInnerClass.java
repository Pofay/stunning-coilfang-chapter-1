package org.pofay.jcip_example.chapter3.bookexamples.publishingexamples;

import org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.util.Event;
import org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.util.EventListener;
import org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.util.EventSource;

public class NoEscapeInnerClass {
    private final EventListener listener;
    private final String internalState;

    private NoEscapeInnerClass() {
        listener = new EventListener() {
           public void onEvent(Event e)   {
               doSomething(e);
           }
        };
        internalState = "Initialized";
    }

    public static NoEscapeInnerClass newInstance(EventSource eventSource) {
        // Basically create first, don't do any side-effects in the constructor
        // afterwards when the object is properly created that's when
        // we use it to prevent it from being in an incomplete state.
        final var noEscapeInnerClass = new NoEscapeInnerClass();
        eventSource.registerListener(noEscapeInnerClass.listener);
        return noEscapeInnerClass;
    }

    private void doSomething(Event e) {
        System.out.println("Value is: " + internalState);
    }

    public String getInternalState() {
        return internalState;
    }

}
