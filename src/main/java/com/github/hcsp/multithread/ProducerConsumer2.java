package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Lock / Condition
public class ProducerConsumer2 {
    static Queue<Integer> queue = new LinkedList<>();
    static final Lock LOCK = new ReentrantLock();
    private static final Condition CONDITION = LOCK.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            LOCK.lock();
            try {
                Random random = new Random();
                for (int i = 0; i < 10; i++) {
                    while (!queue.isEmpty()) {
                        safeAwait();
                    }
                    int intProduced = random.nextInt();
                    queue.offer(intProduced);
                    System.out.println("Producing " + intProduced);
                    CONDITION.signal();
                }
            } finally {
                LOCK.unlock();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            LOCK.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while (queue.isEmpty()) {
                        safeAwait();
                    }
                    Integer intConsumed = queue.poll();
                    System.out.println("Consuming " + intConsumed);
                    CONDITION.signal();
                }
            } finally {
                LOCK.unlock();
            }
        }
    }

    private static void safeAwait() {
        try {
            CONDITION.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
