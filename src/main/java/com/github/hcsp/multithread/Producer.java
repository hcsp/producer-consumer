package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * @author wheelchen
 */
public class Producer extends Thread {
    private final BlockingQueue blockingQueue;

    public Producer(BlockingQueue blockingQueue) {
        this.blockingQueue = blockingQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            int random = getRandomInt();
            try {
                blockingQueue.put(random);
                System.out.println("Producing " + random);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    int getRandomInt(){
        return new Random().nextInt();
    }
}
