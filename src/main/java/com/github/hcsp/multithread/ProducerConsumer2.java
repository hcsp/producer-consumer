package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    private static final Lock lock = new ReentrantLock();

    private static final Container container = new Container(lock);
    private static final Integer CYCLE_INDEX = 10;

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer(lock, container, CYCLE_INDEX);
        Consumer consumer = new Consumer(lock, container, CYCLE_INDEX);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Container {
        private Condition notConsumeYet;
        private Condition notProduceYet;
        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }

        public Container(Lock lock) {
            this.notConsumeYet = lock.newCondition();
            this.notProduceYet = lock.newCondition();
        }

        public Condition getNotConsumeYet() {
            return notConsumeYet;
        }

        public void setNotConsumeYet(Condition notConsumeYet) {
            this.notConsumeYet = notConsumeYet;
        }

        public Condition getNotProduceYet() {
            return notProduceYet;
        }

        public void setNotProduceYet(Condition notProduceYet) {
            this.notProduceYet = notProduceYet;
        }
    }

    public static class Producer extends Thread {
        private Lock lock;
        private Container container;
        private Integer cycleIndex;

        public Producer(Lock lock, Container container, Integer cycleIndex) {
            this.lock = lock;
            this.container = container;
            this.cycleIndex = cycleIndex;
        }

        @Override
        public void run() {
            for (int i = 0; i < cycleIndex; i++) {
                lock.lock();
                try {
                    while (container.getValue().isPresent()) {
                        container.getNotConsumeYet().await();
                    }
                    Integer randomInt = new Random().nextInt();
                    System.out.println("Producing " + randomInt);
                    container.setValue(Optional.of(randomInt));
                    container.getNotProduceYet().signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private final Lock lock;
        private final Container container;
        private final Integer cycleIndex;

        public Consumer(Lock lock, Container container, Integer cycleIndex) {
            this.lock = lock;
            this.container = container;
            this.cycleIndex = cycleIndex;
        }

        @Override
        public void run() {
            for (int i = 0; i < cycleIndex; i++) {
                lock.lock();
                try {
                    while (!container.getValue().isPresent()) {
                        container.getNotProduceYet().await();
                    }
                    Integer value = container.getValue().get();
                    System.out.println("Consuming " + value);
                    container.setValue(Optional.empty());
                    container.getNotConsumeYet().signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
