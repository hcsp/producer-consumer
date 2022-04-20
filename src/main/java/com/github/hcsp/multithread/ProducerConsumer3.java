package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

// BlockingQueue
public class ProducerConsumer3 {
    static BlockingQueue<Integer> blockingQueue = new LinkedBlockingDeque<>(1);

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
                int intProduced = random.nextInt();
                try {
                    blockingQueue.put(intProduced);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Producing " + intProduced);
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                Integer intConsumed;
                try {
                    intConsumed = blockingQueue.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Consuming " + intConsumed);
            }
        }
    }
}
