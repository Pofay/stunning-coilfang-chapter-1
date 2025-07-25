package org.pofay.commands;

import org.pofay.jcip_example.chapter3.bookexamples.visibilityexample.NoVisibilityState;
import org.pofay.jcip_example.chapter3.bookexamples.visibilityexample.ReaderThread;

import picocli.CommandLine.Command;

@Command(name = "no-visibility")
public class NoVisibilityExample implements Runnable {

    @Override
    public void run() {
        
        for (var i = 0; i < 1_000_000; i++) {
            var state = new NoVisibilityState();
            var thread = new ReaderThread(state);

            var writerThread = new Thread(() -> {
                state.setNumber(42);
                state.setReady(true);
            });

            thread.start();
            writerThread.start();

            try {
                thread.join(1000);
                writerThread.join(1000);
                if (thread.isAlive()) {
                    System.out.println("⚠️ Thread is stuck due to visibility problem");
                    thread.interrupt(); // optional: interrupt the stuck thread
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

    }
}
