package com.github.hcsp.multithread;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Consumer extends Thread {
    private ArrayBlockingQueue<Integer> store;
    private BlockingQueue<Integer> signalQueue;

    public Consumer(ArrayBlockingQueue<Integer> store, BlockingQueue<Integer> signalQueue) {
        this.store = store;
        this.signalQueue = signalQueue;
    }

    @Override

    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("Consuming " + store.take());
                signalQueue.put(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
