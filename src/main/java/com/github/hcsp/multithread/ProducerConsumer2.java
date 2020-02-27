package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock / Condition
 */
public class ProducerConsumer2 {
    static Lock lock = new ReentrantLock();
    static Condition empty = lock.newCondition();
    static Condition full = lock.newCondition();
    static AtomicReference<Integer> integerReference = new AtomicReference<>();

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
            for (int i = 0; i < 10; ++i) {
                lock.lock();
                try {
                    while (integerReference.get() != null) {
                        try {
                            empty.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    integerReference.set(new Random().nextInt());
                    System.out.println("Producing " + integerReference.get());

                    full.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; ++i) {
                lock.lock();
                try {
                    while (integerReference.get() == null) {
                        try {
                            full.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    System.out.println("Consuming " + integerReference.get());
                    integerReference.set(null);

                    empty.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
