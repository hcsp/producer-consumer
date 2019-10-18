package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Producer extends Thread {

    private final Lock lock;

    private final Condition full;

    private final Condition empty;

    private final LinkedList<Integer> buffer;

    private final Random random = new Random();

    public Producer(Lock lock, LinkedList<Integer> buffer, Condition full, Condition empty) {
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
                int data = random.nextInt();
                while (isFull()) {
                    try {
                        empty.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                buffer.addLast(data);
                full.signal();
                System.out.println("Producing " + data);
            } finally {
                lock.unlock();
            }
        }
    }

    private boolean isFull() {
        return 1 == buffer.size();
    }
}
