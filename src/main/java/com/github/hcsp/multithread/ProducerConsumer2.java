package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    private static final Lock lock = new ReentrantLock();
    private static final Condition notProducing = lock.newCondition();
    private static final Condition notConsuming = lock.newCondition();
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
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    if (container.isPresent()) {
                        notProducing.await();
                    }
                    int product = new Random().nextInt();
                    container = Optional.of(product);
                    System.out.println("Producing " + product);
                    notConsuming.signal();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    if (!container.isPresent()) {
                        notConsuming.await();
                    }
                    int result = container.get();
                    System.out.println("Consuming " + result);
                    container = Optional.empty();
                    notProducing.signal();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }
}
