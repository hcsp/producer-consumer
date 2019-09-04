package com.github.hcsp.multithread;

import java.util.concurrent.BlockingDeque;

public class Consumer extends Thread {
    private BlockingDeque<Integer> queue;
    private BlockingDeque<Integer> signalQueue;

    Consumer(BlockingDeque<Integer> queue, BlockingDeque<Integer> signalQueue) {
        this.queue = queue;
        this.signalQueue = signalQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("Consuming " + queue.take());
                signalQueue.put(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
