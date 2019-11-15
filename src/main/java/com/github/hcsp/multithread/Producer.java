package com.github.hcsp.multithread;

import java.util.Random;

public class Producer extends Thread {

    private Container container;

    public Producer(Container container) {
        this.container = container;
    }

    @Override
    public void run() {
        synchronized (container) {
            while (container.value != null) {
                try {
                    container.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            int random = new Random().nextInt();
            System.out.println("Producing " + random);
            container.value = random;
            container.notifyAll();
        }
    }
}
