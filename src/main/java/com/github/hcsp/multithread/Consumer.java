package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Consumer extends Thread {

    private final Lock lock;

    private final Condition full;

    private final Condition empty;

    private final LinkedList<Integer> buffer;

    public Consumer(Lock lock, LinkedList<Integer> buffer, Condition full, Condition empty) {
        this.lock = lock;
        this.buffer = buffer;
        this.full = full;
        this.empty = empty;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            lock.lock();
            try {
                while (isEmpty()) {
                    try {
                        full.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Consuming " + buffer.removeFirst());
                empty.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    private boolean isEmpty() {
        return 0 == buffer.size();
    }
}
