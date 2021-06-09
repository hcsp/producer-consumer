package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Container container = new Container(lock);
        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final Container container;
        private final ReentrantLock lock;

        public Producer(Container container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (container.getSharedData().isPresent()) {
                        try {
                            container.getNotConsumedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int random = new Random().nextInt();
                    System.out.println("Producing " + random);
                    container.setSharedData(Optional.of(random));
                    container.getNotProducedYet().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private final Container container;
        private final ReentrantLock lock;

        public Consumer(Container container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (!container.getSharedData().isPresent()) {
                        container.getNotProducedYet().await();
                    }
                    int random = container.getSharedData().get();
                    System.out.println("Consuming " + random);
                    container.setSharedData(Optional.empty());
                    container.getNotConsumedYet().signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Container {
        private Condition notConsumedYet;
        private Condition notProducedYet;
        private Optional<Integer> sharedData = Optional.empty();

        public Container(ReentrantLock lock) {
            this.notConsumedYet = lock.newCondition();
            this.notProducedYet = lock.newCondition();
        }

        public Condition getNotConsumedYet() {
            return notConsumedYet;
        }

        public void setNotConsumedYet(Condition notConsumedYet) {
            this.notConsumedYet = notConsumedYet;
        }

        public Condition getNotProducedYet() {
            return notProducedYet;
        }

        public void setNotProducedYet(Condition notProducedYet) {
            this.notProducedYet = notProducedYet;
        }

        public Optional<Integer> getSharedData() {
            return sharedData;
        }

        public void setSharedData(Optional<Integer> sharedData) {
            this.sharedData = sharedData;
        }
    }
}
