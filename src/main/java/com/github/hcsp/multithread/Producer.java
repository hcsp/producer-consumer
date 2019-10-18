package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Random;

public class Producer extends Thread {

    private final Object lock;

    private final LinkedList<Integer> buffer;

    private final Random random = new Random();

    public Producer(Object lock, LinkedList<Integer> buffer) {
        this.lock = lock;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            synchronized (lock) {
                int data = random.nextInt();
                while (isFull()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                buffer.addLast(data);
                System.out.println("Producing " + data);
                lock.notifyAll();
            }
        }
    }

    private boolean isFull() {
        return 1 == buffer.size();
    }
}
