package org.pofay.jcip_example.chapter3.bookexamples;

public class VolatileReaderThread extends Thread {
    private final VolatileVisibilityState state;

    public VolatileReaderThread(VolatileVisibilityState state) {
        this.state = state;
    }

    @Override
    public void run() {
        while (!state.isReady()) {
            Thread.yield();
            System.out.println("Yielding");
        }

        if (state.isReady()) {
            System.out.println("Number: " + this.state.getNumber());
        }
    }
}
