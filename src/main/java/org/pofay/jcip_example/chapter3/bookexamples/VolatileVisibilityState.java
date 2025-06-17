package org.pofay.jcip_example.chapter3.bookexamples;

public class VolatileVisibilityState {
    private volatile boolean ready;
    private volatile int number;

    public VolatileVisibilityState() {
        number = 0;
        ready = false;
    }

    public synchronized boolean isReady() {
        return ready;
    }

    public synchronized void setReady(boolean ready) {
        this.ready = ready;
    }

    public synchronized int getNumber() {
        return number;
    }

    public synchronized void setNumber(int number) {
        this.number = number;
    }
}
