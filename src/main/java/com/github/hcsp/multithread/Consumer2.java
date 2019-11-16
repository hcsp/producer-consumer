package com.github.hcsp.multithread;

import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Consumer2 extends Thread {
    private Queue<Integer> queue;
    private Lock lock;
    private Condition queueEmpty;
    private Condition queueFull;

    public Consumer2(Queue<Integer> queue, Lock lock, Condition queueEmpty, Condition queueFull) {
        this.queue = queue;
        this.lock = lock;
        this.queueEmpty = queueEmpty;
        this.queueFull = queueFull;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            lock.lock();
            try {
                while (queue.isEmpty()) {
                    queueEmpty.await();
                }
                System.out.println("Consuming " + queue.remove());
                queueFull.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
