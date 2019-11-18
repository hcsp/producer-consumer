package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Producer extends Thread {
    Container container;
    ReentrantLock lock;

    public Producer(Container container, ReentrantLock lock) {
        this.container = container;
        this.lock = lock;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            for (int i = 0; i < 10; i++) {
                while (container.isPresent()) {
                    try {
                        container.getNotConsumedYet().await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                container.put(r);
                container.getNotProducedYet().signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
