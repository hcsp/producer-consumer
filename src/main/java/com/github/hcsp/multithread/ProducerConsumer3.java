package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<>(1);
        Producer producer = new Producer(blockingQueue);
        Consumer consumer = new Consumer(blockingQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final BlockingQueue queue;

        Producer(BlockingQueue q) {
            queue = q;
        }
        @Override
        public void run() {

            try {
                for (int i = 0; i < 100; i++) {
                    int value = new Random().nextInt();
                    queue.put(value);
                    System.out.println("Producing " + value);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Consumer extends Thread {
        private final BlockingQueue queue;

        Consumer(BlockingQueue q) {
            queue = q;
        }
        @Override
        public void run() {

            try {
                for (int i = 0; i < 100; i++) {
                    Integer value = (Integer) queue.take();
                    System.out.println("Consuming " + value);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
