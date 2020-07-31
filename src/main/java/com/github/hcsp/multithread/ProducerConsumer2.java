package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {

    public static void main(String[] args) throws InterruptedException {
        Lock lock = new ReentrantLock();
        Container container = new Container(lock);

        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        private Lock lock;
        private Container container;

        public Producer(Container container) {
            this.container = container;
            this.lock = container.getLock();
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (container.getValue() != null) {
                        container.getNotProducedYet().await();
                    }
                    int num = new Random().nextInt();
                    container.setValue(num);
                    System.out.println("Producing " + num);
                    container.getNotConsumedYet().signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private Lock lock;
        private Container container;

        public Consumer(Container container) {
            this.container = container;
            this.lock = container.getLock();
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (container.getValue() == null) {
                        container.getNotConsumedYet().await();
                    }
                    System.out.println("Consuming " + container.getValue());
                    container.setValue(null);
                    container.getNotProducedYet().signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    private static class Container {
        private Lock lock;
        private Integer value;
        private Condition notProducedYet;
        private Condition notConsumedYet;

        Container(Lock lock) {
            this.lock = lock;
            this.notProducedYet = lock.newCondition();
            this.notConsumedYet = lock.newCondition();
        }

        public Lock getLock() {
            return lock;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public Condition getNotProducedYet() {
            return notProducedYet;
        }

        public Condition getNotConsumedYet() {
            return notConsumedYet;
        }
    }
}
