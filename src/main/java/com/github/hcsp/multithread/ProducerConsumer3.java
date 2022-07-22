package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        /**
         * Semaphore
         */
        Container container = new Container();
        Object lock = new Object();
        Semaphore emptySlot = new Semaphore(10);
        Semaphore fullSlot = new Semaphore(10);

        Producer producer = new Producer(container, lock, emptySlot, fullSlot);
        Consumer consumer = new Consumer(container, lock, emptySlot, fullSlot);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final Container container;
        private final Object lock;
        private final Semaphore emptySlot;
        private final Semaphore fullSlot;

        public Producer(Container container, Object lock, Semaphore emptySlot, Semaphore fullSlot) {
            this.container = container;
            this.lock = lock;
            this.emptySlot = emptySlot;
            this.fullSlot = fullSlot;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (container.value != null) {
                    try {
                        emptySlot.acquire();
                    } catch (InterruptedException e) {
                        currentThread().interrupt();
                    }
                }

                synchronized (lock) {
                    int value = new Random().nextInt();
                    System.out.println("Producing " + value);
                    container.setValue(value);
                    fullSlot.release();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private final Container container;
        private final Object lock;
        private final Semaphore emptySlot;
        private final Semaphore fullSlot;

        public Consumer(Container container, Object lock, Semaphore emptySlot, Semaphore fullSlot) {
            this.container = container;
            this.lock = lock;
            this.emptySlot = emptySlot;
            this.fullSlot = fullSlot;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                while (container.value == null) {
                    try {
                        fullSlot.acquire();
                    } catch (InterruptedException e) {
                        currentThread().interrupt();
                    }
                }

                synchronized (lock) {
                    System.out.println("Consuming " + container.getValue());
                    container.setValue(null);
                    emptySlot.release();
                }
            }
        }
    }
}
