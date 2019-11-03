package com.github.hcsp.multithread;

import java.util.concurrent.BlockingQueue;

/**
 * @author wheelchen
 */
public class Consumer extends Thread {
    private final BlockingQueue blockingQueue;

    public Consumer(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                int random = (int)blockingQueue.take();
                System.out.println("Consuming " + random);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
