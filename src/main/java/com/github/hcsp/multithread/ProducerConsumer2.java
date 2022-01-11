package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Container container = new Container(lock);
        Producer producer = new Producer(lock, container);
        Consumer consumer = new Consumer(lock, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        public Producer(ReentrantLock lock, Container container) {
            this.lock = lock;
            this.container = container;
        }

        private final ReentrantLock lock;
        private final Container container;


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (this.container.getValue().isPresent()) {
                        try {
                            container.getNotConsumed().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer value = new Random().nextInt();
                    this.container.setValue(Optional.of(value));
                    System.out.println("produce " + value);
                    this.container.getNotProduced().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private final ReentrantLock lock;
        private final Container container;

        public Consumer(ReentrantLock lock, Container container) {
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (!this.container.getValue().isPresent()) {
                        try {
                            container.getNotProduced().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("consume " + this.container.getValue().get());
                    this.container.setValue(Optional.empty());
                    this.container.getNotConsumed().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private static class Container {
        public Container(ReentrantLock lock) {
            this.notConsumed = lock.newCondition();
            this.notProduced = lock.newCondition();
        }

        public Condition getNotConsumed() {
            return notConsumed;
        }

        public Condition getNotProduced() {
            return notProduced;
        }

        private final Condition notConsumed;
        private final Condition notProduced;
        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }
}
