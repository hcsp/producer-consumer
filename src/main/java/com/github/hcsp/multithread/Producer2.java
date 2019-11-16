package com.github.hcsp.multithread;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Producer2 extends Thread {
    private Queue<Integer> queue;
    private Lock lock;
    private Condition queueFull;
    private Condition queueEmpty;

    public Producer2(Queue<Integer> queue, Lock lock, Condition queueEmpty, Condition queueFull) {
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
                while (!queue.isEmpty()) {
                    queueFull.await();
                }
                Integer value = new Random().nextInt();
                System.out.println("Producing " + value);
                queue.add(value);
                queueEmpty.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
