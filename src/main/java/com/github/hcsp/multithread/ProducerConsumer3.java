package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    private static final Semaphore semaphore = new Semaphore(1);
    private static final int MAX_CONSUME_COUNT = 10;
    private static int consumeCount = 0;
    private static Integer randomInt;

    public static class Producer extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    semaphore.acquire(1);
                    if (consumeCount == MAX_CONSUME_COUNT) {
                        break;
                    }
                    randomInt = new Random().nextInt();
                    System.out.println("Producing " + randomInt);
                    semaphore.release(1);
                    Thread.sleep(10);
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
                while (consumeCount != MAX_CONSUME_COUNT) {
                    semaphore.acquire(1);
                    if (randomInt == null) {
                        semaphore.release();
                        continue;
                    }
                    System.out.println("Consuming " + randomInt);
                    consumeCount += 1;
                    randomInt = null;
                    semaphore.release(1);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
