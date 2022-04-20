package com.github.hcsp.multithread;

import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;

// Semaphore
public class ProducerConsumer4 {
    static Queue<Integer> queue = new ConcurrentLinkedQueue<>();
    static final Semaphore SEMAPHORE = new Semaphore(1);

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
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                while (!queue.isEmpty()) {
                    SEMAPHORE.release();
                }
                safeAcquire();
                int intProduced = random.nextInt();
                queue.offer(intProduced);
                System.out.println("Producing " + intProduced);
                SEMAPHORE.release();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (queue.isEmpty()) {
                    SEMAPHORE.release();
                }
                safeAcquire();
                Integer intConsumed = queue.poll();
                System.out.println("Consuming " + intConsumed);
                SEMAPHORE.release();
            }
        }
    }

    private static void safeAcquire() {
        try {
            SEMAPHORE.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
