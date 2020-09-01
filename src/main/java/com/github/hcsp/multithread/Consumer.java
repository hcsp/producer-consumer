package com.github.hcsp.multithread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Consumer extends Thread {
    private BlockingQueue<Integer> shareQueue;
    Semaphore emptyState;
    Semaphore fullState;

    public Consumer(BlockingQueue<Integer> queue, Semaphore emptyState, Semaphore fullState) {
        this.shareQueue = queue;
        this.emptyState = emptyState;
        this.fullState = fullState;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                fullState.acquire();
                synchronized (shareQueue) {
                    Integer item = shareQueue.take();
                    System.out.println("Consuming " + item);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                emptyState.release();
            }
        }
    }
}
