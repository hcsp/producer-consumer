package com.github.hcsp.multithread;

import java.util.concurrent.BlockingQueue;

public class Consumer extends Thread {
    private BlockingQueue<Integer> que;

    public Consumer(BlockingQueue<Integer> que) {
        this.que = que;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("Consuming " + que.take());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
