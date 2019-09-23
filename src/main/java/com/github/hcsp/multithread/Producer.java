package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Producer extends Thread {
    private ArrayBlockingQueue<Integer> store;
    private BlockingQueue<Integer> signalQueue;

    public Producer(ArrayBlockingQueue<Integer> store, BlockingQueue<Integer> signalQueue) {
        this.store = store;
        this.signalQueue = signalQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                int number = new Random().nextInt();
                store.put(number);
                System.out.println("Producing " + number);
                sleep(500);
                signalQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
