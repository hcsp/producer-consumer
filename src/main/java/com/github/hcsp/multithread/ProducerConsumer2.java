package com.github.hcsp.multithread;

import javax.swing.*;
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
        private ReentrantLock lock;
        private Container container;

        public Producer(ReentrantLock lock, Container container) {
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (container.getValue().isPresent()) {
                        try {
                            container.getNotConsumedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int randomNumber = new Random().nextInt();
                    container.setValue(Optional.of(randomNumber));
                    System.out.println("Producing " + randomNumber);
                    container.getNotProducedYet().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private ReentrantLock lock;
        private Container container;

        public Consumer(ReentrantLock lock, Container container) {
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (!container.getValue().isPresent()) {
                        try {
                            container.getNotProducedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + container.getValue().get());
                    container.setValue(Optional.empty());
                    container.getNotConsumedYet().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Container {
        private Condition notConsumedYet;
        private Condition notProducedYet;
        private Optional<Integer> value = Optional.empty();

        public Container(ReentrantLock lock) {
            this.notConsumedYet = lock.newCondition();
            this.notProducedYet = lock.newCondition();
        }

        public Condition getNotConsumedYet() {
            return notConsumedYet;
        }

        public Condition getNotProducedYet() {
            return notProducedYet;
        }

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }
}
