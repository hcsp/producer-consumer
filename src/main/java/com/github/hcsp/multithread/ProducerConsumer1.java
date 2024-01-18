package com.github.hcsp.multithread;

import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    private static final Object LOCK = new Object();
    private static final int MAX_CONSUME_COUNT = 10;
    private static Integer randomInt;
    private static int consumeCount = 0;

    public static class Producer extends Thread {
        @Override
        public void run() {
            synchronized (LOCK) {
                try {
                    while (true) {
                        if (randomInt != null) {
                            System.out.println("Consuming " + randomInt);
                            randomInt = null;
                            consumeCount += 1;
                            if (consumeCount == MAX_CONSUME_COUNT) {
                                break;
                            }
                        }
                        LOCK.notify();
                        LOCK.wait();
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            synchronized (LOCK) {
                try {
                    while (true) {
                        randomInt = new Random().nextInt();
                        System.out.println("Producing " + randomInt);
                        LOCK.notify();
                        if (consumeCount == MAX_CONSUME_COUNT - 1) {
                            break;
                        } else {
                            LOCK.wait();
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
