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
        consumer.join();
    }

    public static class Container {
        private Optional<Integer> container = Optional.empty();
        Condition notConsumedYet;
        Condition notProducedYet;

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


        public Optional<Integer> getContainer() {
            return container;
        }

        public void setContainer(Optional<Integer> container) {
            this.container = container;
        }
    }

    public static class Producer extends Thread {
        Container container;
        ReentrantLock lock;

        public Producer(Container container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (container.getContainer().isPresent()) {
                        try {
                            container.notConsumedYet.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer r = new Random().nextInt();
                    container.setContainer(Optional.of(r));
                    System.out.println("Producing " + r);
                    container.notProducedYet.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;
        ReentrantLock lock;

        public Consumer(Container container, ReentrantLock lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (!container.getContainer().isPresent()) {
                        try {
                            container.notProducedYet.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + container.getContainer().get());
                    container.setContainer(Optional.empty());
                    container.notConsumedYet.signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
