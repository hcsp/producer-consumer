package com.github.hcsp.multithread;

import java.util.concurrent.BlockingQueue;

public class Consumer extends Thread {
    BlockingQueue queue;
    BlockingQueue signal;

    public Consumer(BlockingQueue queue, BlockingQueue signal) {
        this.queue = queue;
        this.signal = signal;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("Consuming " + queue.take());
                signal.put(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
