package com.github.hcsp.multithread;

import java.util.concurrent.BlockingQueue;

public class Consumer extends Thread {
    private BlockingQueue<Integer> queue;
    private BlockingQueue<Integer> lock;

    public Consumer(BlockingQueue<Integer> queue, BlockingQueue<Integer> lock) {
        this.queue = queue;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("Consuming " + queue.take());
                lock.put(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
