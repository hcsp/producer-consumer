package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Container container = new Container(lock, Optional.empty());
        Producer producer = new Producer(lock, container);
        Consumer consumer = new Consumer(lock, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Container {
        private Condition notConsumedYet;
        private Condition notProducedYet;
        private Optional<Integer> optional;

        public Container(ReentrantLock lock, Optional<Integer> optional) {
            this.notConsumedYet = lock.newCondition();
            this.notProducedYet = lock.newCondition();
            this.optional = optional;
        }


        public Condition getNotConsumedYet() {
            return notConsumedYet;
        }

        public Condition getNotProducedYet() {
            return notProducedYet;
        }

        public Optional<Integer> getOptional() {
            return optional;
        }

        public void setOptional(Optional<Integer> optional) {
            this.optional = optional;
        }
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
                    while (container.getOptional().isPresent()) {
                        try {
                            container.getNotConsumedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    int randomInteger = new Random().nextInt();
                    container.setOptional(Optional.of(randomInteger));
                    System.out.println("Producing " + container.getOptional().get());
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
                    while (!container.getOptional().isPresent()) {
                        try {
                            container.getNotProducedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + container.getOptional().get());
                    container.setOptional(Optional.empty());
                    container.getNotConsumedYet().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
