package org.pofay.jcip_example.chapter3.bookexamples.visibilityexample;

public class NoVisibilityState {
    private boolean ready;
    private int number;

    public NoVisibilityState() {
        number = 0;
        ready = false;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
