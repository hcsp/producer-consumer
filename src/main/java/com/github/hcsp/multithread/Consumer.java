package com.github.hcsp.multithread;

import java.util.LinkedList;

public class Consumer extends Thread {

    private final Object lock;

    private final LinkedList<Integer> buffer;

    public Consumer(Object lock, LinkedList<Integer> buffer) {
        this.lock = lock;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for (int i = 0;i<10;i++) {
            synchronized (lock) {
                while (isEmpty()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Consuming "+buffer.removeFirst());
                lock.notifyAll();
            }
        }
    }

    private boolean isEmpty() {
        return 0 == buffer.size();
    }
}
