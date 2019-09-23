package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;


public class Producer extends Thread {
    private ArrayBlockingQueue<Integer> store;

    public Producer(ArrayBlockingQueue<Integer> store) {
        this.store = store;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            try {
                int number = new Random().nextInt();
                store.put(number);
                System.out.println("Producing " + number);
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
