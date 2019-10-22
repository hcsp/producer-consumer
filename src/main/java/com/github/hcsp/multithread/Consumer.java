package com.github.hcsp.multithread;

import java.util.concurrent.BlockingQueue;

public class Consumer extends Thread {
    BlockingQueue<Integer> queue;
    BlockingQueue<Integer> signalQueue;


    public Consumer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalQueue) {
        this.queue = queue;
        this.signalQueue = signalQueue;
    }

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
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
