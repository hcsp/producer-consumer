package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Object lock = new Object();
        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container;
        final Object lock;

        public Producer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (container.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer value = new Random().nextInt();
                    container.setValue(Optional.of(value));
                    System.out.println("Producing " + value);
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;
        final Object lock;

        public Consumer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!container.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer value = container.getValue().get();
                    System.out.println("Consuming " + value);
                    container.setValue(Optional.empty());
                    lock.notify();
                }
            }
        }
    }

    private static class Container {
        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }

        private Optional<Integer> value = Optional.empty();


    }
}
