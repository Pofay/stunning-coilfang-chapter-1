package org.pofay.jcip_example.chapter3.bookexamples.publishingexamples;

import java.util.concurrent.CountDownLatch;

import org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.util.Event;
import org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.util.EventListener;
import org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.util.EventSource;
import org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.util.ImmediateEventSource;

public class ThisEscapeInnerClass {
    private String internalState;

    public ThisEscapeInnerClass(EventSource source, CountDownLatch latch) {
        source.registerListener(
                new EventListener() {
                    public void onEvent(Event e) {
                        doSomething(e);
                    }
                });
        latch.countDown();
        this.internalState = "Initialized";
    }

    public ThisEscapeInnerClass(ImmediateEventSource source, CountDownLatch latch) {
        source.registerListener(
                new EventListener() {
                    public void onEvent(Event e) {
                        doSomething(e);
                    }
                });
        latch.countDown();
        this.internalState = "Initialized";
    }

    private void doSomething(Event e) {
        System.out.println("Value is: " + internalState);
    }

    public String getInternalState() {
        return internalState;
    }
}
