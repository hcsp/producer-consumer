package com.github.hcsp.multithread;


import java.util.concurrent.ArrayBlockingQueue;

public class Consumer extends Thread {
    private ArrayBlockingQueue<Integer> store;


    public Consumer(ArrayBlockingQueue<Integer> store) {
        this.store = store;
    }

    @Override

    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                System.out.println("Consuming " + store.take());
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
