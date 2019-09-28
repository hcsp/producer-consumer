package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Producer extends Thread {
    private BlockingQueue<Integer> queue;
    private BlockingQueue<Integer> lock;

    public Producer(BlockingQueue<Integer> queue, BlockingQueue<Integer> lock) {
        this.queue = queue;
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                int next = new Random().nextInt();
                System.out.println("Producing " + next);
                queue.put(next);
                lock.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
