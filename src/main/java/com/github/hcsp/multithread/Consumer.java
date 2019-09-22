package com.github.hcsp.multithread;

import java.util.Optional;

public class Consumer extends Thread {
    private final Object lock;
    private final Container container;

    public Consumer(Object lock, Container container) {
        this.lock = lock;
        this.container = container;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            synchronized (lock) {
                while (!container.getValue().isPresent()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Integer random = container.getValue().get();
                System.out.println("Consuming " + random);
                container.setValue(Optional.empty());
                lock.notify();
            }
        }
    }
}
