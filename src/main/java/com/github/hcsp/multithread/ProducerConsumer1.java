package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        final Object LOCK = new Object();
        Container container = new Container();

        Producer producer = new Producer(LOCK, container);
        Consumer consumer = new Consumer(LOCK, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        final Object LOCK;
        Container container;

        public Producer(Object LOCK, Container container) {
            this.LOCK = LOCK;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (LOCK) {
                    while (container.getValue().isPresent()) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    container.setValue(Optional.of(r));
                    LOCK.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        final Object LOCK;
        Container container;

        public Consumer(Object LOCK, Container container) {
            this.LOCK = LOCK;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (LOCK) {
                    while (!container.getValue().isPresent()) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = container.getValue().get();
                    System.out.println("Consuming " + r);
                    container.setValue(Optional.empty());
                    LOCK.notify();
                }
            }
        }
    }

    public static class Container {
        Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }
}
