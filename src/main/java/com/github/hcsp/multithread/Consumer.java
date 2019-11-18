package com.github.hcsp.multithread;

import java.util.concurrent.locks.ReentrantLock;

public class Consumer extends Thread {
    Container container;
    ReentrantLock lock;

    public Consumer(Container container, ReentrantLock lock) {
        this.container = container;
        this.lock = lock;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            for (int i = 0; i < 10; i++) {
                while (!container.isPresent()) {
                    try {
                        container.getNotProducedYet().await();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("Consuming " + container.take());
                container.getNotConsumedYet().signal();
            }
        } finally {
            lock.unlock();
        }
    }
}
