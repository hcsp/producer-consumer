package com.github.hcsp.multithread;

import java.util.concurrent.BlockingQueue;

public class Consumer extends Thread {
    private BlockingQueue<Integer> shareQueue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.shareQueue = queue;
    }

    @Override
    public void run() {
        while (true) {
            Integer item = null;
            try {
                item = shareQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Consuming " + item);
        }
    }
}
