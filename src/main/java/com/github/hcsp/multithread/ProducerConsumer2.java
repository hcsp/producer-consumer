package com.github.hcsp.multithread;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    private static Lock lock = new ReentrantLock();
    private static Condition empty = lock.newCondition();
    private static Condition full = lock.newCondition();
    private static ProducerConsumer1.Container container = new ProducerConsumer1.Container();

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
            boolean done = false;
            while (!done) {
                boolean locked = lock.tryLock();
                if (locked) {
                    try {
                        for (int i = 0; i < 10; i++) {
                            lock.lock();
                            try {
                                while (Objects.nonNull(container.value)) {
                                    try {
                                        full.await();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                int value = new Random().nextInt();
                                System.out.println("Producing " + value);
                                container.value = value;
                                empty.signal();
                            } finally {
                                lock.unlock();
                            }
                        }
                        done = true;
                    } finally {
                        lock.unlock();
                    }
                }
            }

        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            boolean done = false;
            while (!done) {
                boolean locked = lock.tryLock();
                if (locked) {
                    try {
                        for (int i = 0; i < 10; i++) {
                            lock.lock();
                            try {
                                while (container.value == null) {
                                    try {
                                        empty.await();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                System.out.println("Consuming " + container.value);
                                container.value = null;
                                full.signal();
                            } finally {
                                lock.unlock();
                            }
                        }
                        done = true;
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
    }
}
