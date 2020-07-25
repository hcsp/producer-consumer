package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {

    private static final Lock lock = new ReentrantLock();
    private static Optional<Integer> container = Optional.empty();
    private static final Condition notProducedYet = lock.newCondition();
    private static final Condition notConsumedYet = lock.newCondition();

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
                lock.lock();
                try {
                    while (container.isPresent()) {
                        notProducedYet.await();
                    }
                    int num = new Random().nextInt();
                    container = Optional.of(num);
                    System.out.println("Producing " + num);
                    notConsumedYet.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (!container.isPresent()) {
                        notConsumedYet.await();
                    }
                    System.out.println("Consuming " + container.get());
                    container = Optional.empty();
                    notProducedYet.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
