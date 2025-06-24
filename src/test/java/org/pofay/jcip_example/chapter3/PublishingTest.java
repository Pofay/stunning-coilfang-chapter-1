package org.pofay.jcip_example.chapter3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.EncapsulatedStates;
import org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.ThisEscapeInnerClass;
import org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.UnsafeStates;
import org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.util.EventSource;
import org.pofay.jcip_example.chapter3.bookexamples.publishingexamples.util.ImmediateEventSource;

public class PublishingTest {

    @Test
    public void unsafeStatesAllowInternalModification() {
        final var sut = new UnsafeStates();
        final var expected = new String[] {
                "HI", "LO", "UP"
        };

        // Breaks encapsulation
        // as it allows external modification
        // of its internal state.
        final var internalState = sut.getStates();
        internalState[2] = "UP";

        assertArrayEquals(expected, internalState);
    }

    @Test
    public void encapsulatedStatesCannotHaveItsInternalStateModified() {
        final var sut = new EncapsulatedStates();
        final var expected = sut.getStates();

        // Can safely have different variables
        // that can reference sut.getStates
        // since it returns a copy
        // thereby preserving encapsulation.
        final var actualState = sut.getStates();
        actualState[2] = "UP";

        assertNotEquals(expected, actualState);
    }

    @Test
    @Disabled("Fixing")
    public void testThisEscape() throws InterruptedException {
        final var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        final var expected = "Value is: null";
        final var eventSource = new EventSource();

        // Latch to coordinate threads
        final var latch = new CountDownLatch(1);

        // Thread to fire the event after listener is registered, but before
        // internalState is set
        Thread t = new Thread(() -> {
            try {
                latch.await(); // Wait until listener is registered
                eventSource.fireEvent("FIRE");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        t.start();

        // You need to modify ThisEscapeInnerClass to accept a latch and count down
        // after registering the listener but before setting internalState
        final var sut = new ThisEscapeInnerClass(eventSource, latch);

        
        t.join();

        final var actual = out.toString().trim();

        assertEquals(expected, actual);

        System.setOut(System.out);
    }
}
