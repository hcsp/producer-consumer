package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    private static final Object LOCK = new Object();

    public static void main(String[] args) throws InterruptedException {
        Optional<Integer> value = Optional.empty();
        Container container = new Container(value);
        Producer producer = new Producer(LOCK, container);
        Consumer consumer = new Consumer(LOCK, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        final Object lock;
        Container container;

        public Producer(Object lock, Container container) {
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (container.value.isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    int r = new Random().nextInt();
                    container.value = Optional.of(r);
                    System.out.println("Producing " + r);
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        final Object lock;
        Container container;

        public Consumer(Object lock, Container container) {
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!container.value.isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("Consuming " + container.value.get());
                    container.value = Optional.empty();
                    lock.notify();
                }
            }
        }
    }

    public static class Container {
        Optional<Integer> value;

        public Container(Optional<Integer> value) {
            this.value = value;
        }
    }
}
