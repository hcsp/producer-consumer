package com.github.hcsp.multithread;

import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        /**
         * wait() + notify()
         */
        Object lock = new Object();
        Container container = new Container();
        Producer producer = new Producer(lock, container);
        Consumer consumer = new Consumer(lock, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final Object lock;
        private final Container container;

        public Producer(Object lock, Container container) {
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (container.value != null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    int value = new Random().nextInt();
                    System.out.println("Producing " + value);
                    container.setValue(value);
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private final Object lock;
        private final Container container;

        public Consumer(Object lock, Container container) {
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (container.value == null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }

                    System.out.println("Consuming " + container.getValue());
                    container.setValue(null);
                    lock.notify();
                }
            }
        }
    }
}
