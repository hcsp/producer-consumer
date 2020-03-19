package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Lock/Condition
 */
public class ProducerConsumer2 {
    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Container container = new Container(null);

        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);


        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    static class Container {
        Object value;

        Container(Object value) {
            this.value = value;
        }
    }


    public static class Producer extends Thread {
        final Container container;

        Producer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (container.value != null) {
                        try {
                            condition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Object value = container.value = new Random().nextInt();
                    System.out.println("Producing " + value);
                    condition.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        final Container container;

        Consumer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (container.value == null) {
                        try {
                            condition.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Object value = container.value;
                    container.value = null;
                    System.out.println("Consuming " + value);
                    condition.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
