package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        final Object lock = new Object();
        Container container = new Container();
        final Integer CYCLE_INDEX = 10;
        Producer producer = new Producer(container, lock, CYCLE_INDEX);
        Consumer consumer = new Consumer(container, lock, CYCLE_INDEX);
        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Container {
        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }

    public static class Producer extends Thread {
        private final Container container;
        private final Object lock;
        private final Integer cycleIndex;

        public Producer(Container container, Object lock, Integer cycleIndex) {
            this.container = container;
            this.lock = lock;
            this.cycleIndex = cycleIndex;
        }

        @Override
        public void run() {
            for (int i = 0; i < cycleIndex; i++) {
                synchronized (lock) {
                    while (container.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    int randomInt = new Random().nextInt();
                    container.setValue(Optional.of(randomInt));
                    System.out.println("Producing " + randomInt);
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private final Container container;
        private final Object lock;
        private final Integer cycleIndex;

        public Consumer(Container container, Object lock, Integer cycleIndex) {
            this.container = container;
            this.lock = lock;
            this.cycleIndex = cycleIndex;
        }

        @Override
        public void run() {
            for (int i = 0; i < cycleIndex; i++) {
                synchronized (lock) {
                    while (!container.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Integer integer = container.getValue().get();
                    System.out.println("Consuming " + integer);
                    container.setValue(Optional.empty());
                    lock.notify();
                }
            }
        }
    }
}

