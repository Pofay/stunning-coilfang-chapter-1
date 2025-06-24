package org.pofay.jcip_example.chapter3.bookexamples.visibilityexample;

public class ReaderThread extends Thread {
    private final NoVisibilityState state;

    public ReaderThread(NoVisibilityState state) {
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
