package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition notProduced = lock.newCondition();
    private static final Condition notConsumed = lock.newCondition();
    private static final ArrayList<Integer> container = new ArrayList<>(1);

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
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (container.size() == 1) {
                        notProduced.await();
                    }
                    int value = new Random().nextInt();
                    container.add(value);
                    System.out.println("Producing " + value);
                    notConsumed.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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
                    while (container.isEmpty()) {
                        notConsumed.await();
                    }
                    int value = container.get(0);
                    container.clear();
                    System.out.println("Consuming " + value);
                    notProduced.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
