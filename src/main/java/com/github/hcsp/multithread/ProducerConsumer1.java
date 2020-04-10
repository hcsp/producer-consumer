package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    private static final Object LOCK = new Object();
    private static Optional<Integer> container = Optional.empty();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (LOCK) {
                    while (container.isPresent()) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int anInt = new Random().nextInt();
                    System.out.println("Producing " + anInt);
                    container = Optional.of(anInt);
                    LOCK.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (LOCK) {
                    while (!container.isPresent()) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("Consuming " + container.get());
                    container = Optional.empty();
                    LOCK.notify();
                }
            }
        }
    }
}
