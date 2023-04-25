package com.github.hcsp.multithread;

import java.util.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer1 {
    public static final BlockingQueue<Integer> sharedQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer(sharedQueue);
        Consumer consumer = new Consumer(sharedQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final BlockingQueue<Integer> sharedQueue;

        public Producer(BlockingQueue<Integer> sharedQueue) {
            this.sharedQueue = sharedQueue;
        }

        @Override
        public void run() {
            try {
                for (int i = 1; i <= 10; i++) {
                    int num = new Random().nextInt();
                    sharedQueue.put(num);
                    System.out.println("Producing " + num);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static class Consumer extends Thread {
        private final BlockingQueue<Integer> sharedQueue;

        public Consumer(BlockingQueue<Integer> sharedQueue) {
            this.sharedQueue = sharedQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + this.sharedQueue.take());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}