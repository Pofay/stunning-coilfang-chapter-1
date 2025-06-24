package org.pofay.jcip_example.chapter3.bookexamples.publishingexamples;

public class UnsafeStates {
    private String[] states = new String[] {
        "HI", "LO", "PWM"
    };

    public String[] getStates() { return states; }
}
