package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

// Object.wait / notify
public class ProducerConsumer1 {
    static Queue<Integer> queue = new LinkedList<>();
    static final Object LOCK = new Object();

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
            synchronized (LOCK) {
                Random random = new Random();
                for (int i = 0; i < 10; i++) {
                    while (!queue.isEmpty()) {
                        safeWait();
                    }
                    int intProduced = random.nextInt();
                    queue.offer(intProduced);
                    System.out.println("Producing " + intProduced);
                    LOCK.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            synchronized (LOCK) {
                for (int i = 0; i < 10; i++) {
                    while (queue.isEmpty()) {
                        safeWait();
                    }
                    Integer intConsumed = queue.poll();
                    System.out.println("Consuming " + intConsumed);
                    LOCK.notify();
                }
            }
        }
    }

    private static void safeWait() {
        try {
            LOCK.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
