package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    private static final BlockingQueue<Integer> q = new ArrayBlockingQueue<>(1);
    private static final BlockingQueue<Integer> notifyQ = new ArrayBlockingQueue<>(1);
    private static final int MAX_CONSUME_COUNT = 10;
    private static int consumeCount = 0;

    public static class Producer extends Thread {
        @Override
        public void run() {
            try {
                while (consumeCount != MAX_CONSUME_COUNT) {
                    int randomInt = new Random().nextInt();
                    System.out.println("Producing " + randomInt);
                    q.put(randomInt);
                    notifyQ.take();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                do {
                    int randomInt = q.take();
                    System.out.println("Consuming " + randomInt);
                    consumeCount += 1;
                    notifyQ.put(1);
                } while (consumeCount != MAX_CONSUME_COUNT);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
