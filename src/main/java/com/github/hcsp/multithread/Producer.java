package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.stream.IntStream;

public class Producer extends Thread {
    private BlockingQueue<Integer> shareQueue;

    public Producer(BlockingQueue<Integer> queue) {
        this.shareQueue = queue;
    }

    @Override
    public void run() {
        IntStream.range(1, 11)
                .boxed()
                .map(i -> new Random().nextInt())
                .forEach(i -> {
                    System.out.println("Producing " + i);
                    try {
                        shareQueue.put(i);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
