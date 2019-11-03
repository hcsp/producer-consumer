package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * @author wheelchen
 */
public class Producer extends Thread {
    private final BlockingQueue blockingQueue;
    private final BlockingQueue signal;
    private final int SIZE = 10;

    public Producer(BlockingQueue blockingQueue, BlockingQueue signal) {
        this.blockingQueue = blockingQueue;
        this.signal = signal;
    }

    @Override
    public void run() {
        for (int i = 0; i < SIZE; i++) {
            int random = getRandomInt();
            try {
                //保证消费者控制台完后再进行下一次输出
                signal.put(0);
                System.out.println("Producing " + random);
                blockingQueue.put(random);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    int getRandomInt(){
        return new Random().nextInt();
    }
}
