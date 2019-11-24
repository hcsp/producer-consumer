package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;

public class Producer extends Thread {
    private BlockingQueue<Integer> shareQueue;
    Semaphore emptyState;
    Semaphore fullState;

    public Producer(BlockingQueue<Integer> queue, Semaphore emptyState, Semaphore fullState) {
        this.shareQueue = queue;
        this.emptyState = emptyState;
        this.fullState = fullState;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                emptyState.acquire();
                synchronized (shareQueue) {
                    int value = new Random().nextInt();
                    shareQueue.put(value);
                    System.out.println("Producing " + value);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                fullState.release();
            }
        }

    }
}
