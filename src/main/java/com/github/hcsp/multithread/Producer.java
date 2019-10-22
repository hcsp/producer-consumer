package com.github.hcsp.multithread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

public class Producer extends Thread {
    BlockingQueue<Integer> queue;
    BlockingQueue<Integer> signalQueue;

    public Producer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalQueue) {
        this.queue = queue;
        this.signalQueue = signalQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            int r = ThreadLocalRandom.current().nextInt();
            System.out.println("Producing " + r);
            try {
                queue.put(r);
                signalQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
