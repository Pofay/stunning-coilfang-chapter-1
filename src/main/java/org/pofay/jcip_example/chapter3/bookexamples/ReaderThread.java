package org.pofay.jcip_example.chapter3.bookexamples;

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
            System.out.println("Number: " + this.state.getNumber());
        }

        var num = this.state.getNumber();
        if (num != 42) {
            System.out.println("❌ Visibility broken! Saw: " + num);
        }
        // System.out.println(this.state.getNumber());
    }

}
