package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    private static final BlockingQueue<Integer> BLOCKING_QUEUE = new ArrayBlockingQueue<>(1);
    private static final BlockingQueue<Integer> TEMP_QUEUE = new ArrayBlockingQueue<>(1);

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int num = new Random().nextInt();
                    BLOCKING_QUEUE.put(num);
                    System.out.println("Producing " + num);
                    TEMP_QUEUE.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + BLOCKING_QUEUE.take());
                    TEMP_QUEUE.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
