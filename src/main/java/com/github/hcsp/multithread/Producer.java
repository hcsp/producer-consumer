package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Producer extends Thread {
    BlockingQueue queue;
    BlockingQueue signal;

    public Producer(BlockingQueue queue, BlockingQueue signal) {
        this.queue = queue;
        this.signal = signal;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                queue.put(r);
                signal.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
