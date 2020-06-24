package com.github.hcsp.multithread;

import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {

        Object lock = new Object();
        Container container = new Container();

        Producer producer = new Producer(lock, container);
        Consumer consumer = new Consumer(lock, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }


    static class Container {
        public Integer random;
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
                    while (container.random != null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    container.random = new Random().nextInt();
                    System.out.println("Producing " + container.random);
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
                    while (container.random == null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + container.random);
                    container.random = null;
                    lock.notify();
                }
            }
        }
    }
}

