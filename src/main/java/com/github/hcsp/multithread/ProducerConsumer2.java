package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    private static final ReentrantLock LOCK = new ReentrantLock();
    private static Condition notConsumedYet = LOCK.newCondition();
    private static Condition notProducedYet = LOCK.newCondition();
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
                LOCK.lock();
                try {
                    while (container.isPresent()) {
                        notConsumedYet.await();
                    }
                    int anInt = new Random().nextInt();
                    System.out.println("Producing " + anInt);
                    container = Optional.of(anInt);
                    notProducedYet.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    LOCK.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                LOCK.lock();
                try {
                    while (!container.isPresent()) {
                        notProducedYet.await();
                    }
                    Integer value = container.get();
                    System.out.println("Consuming " + value);
                    container = Optional.empty();
                    notConsumedYet.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    LOCK.unlock();
                }
            }
        }
    }
}
