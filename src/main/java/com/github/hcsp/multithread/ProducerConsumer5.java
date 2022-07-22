package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer5 {
    public static void main(String[] args) throws InterruptedException {
        /**
         * BlockingQueue
         */
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(1);

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final BlockingQueue<Integer> queue;

        public Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int value = new Random().nextInt();
                    System.out.println("Producing " + value);
                    queue.put(value);
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private final BlockingQueue<Integer> queue;

        public Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + queue.take());
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                }
            }
        }
    }
}
