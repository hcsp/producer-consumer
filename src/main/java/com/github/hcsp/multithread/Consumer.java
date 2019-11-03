package com.github.hcsp.multithread;

import java.util.concurrent.BlockingQueue;

/**
 * @author wheelchen
 */
public class Consumer extends Thread {
    private final BlockingQueue blockingQueue;
    private final BlockingQueue signal;
    private final int SIZE = 10;

    public Consumer(BlockingQueue blockingQueue, BlockingQueue signal) {
        this.blockingQueue = blockingQueue;
        this.signal = signal;
    }

    @Override
    public void run() {
        for (int i = 0; i < SIZE; i++) {
            try {
                System.out.println("Consuming " + blockingQueue.take());
                //保证控制台输出交替进行
                signal.take();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
