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

    public static class Container {
        private Condition notConsumerYet;
        private Condition notProductYet;
        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }

        public Condition getNotConsumerYet() {
            return notConsumerYet;
        }

        public void setNotConsumerYet(Condition notConsumerYet) {
            this.notConsumerYet = notConsumerYet;
        }

        public Condition getNotProductYet() {
            return notProductYet;
        }

        public void setNotProductYet(Condition notProductYet) {
            this.notProductYet = notProductYet;
        }

        public Container(ReentrantLock lock) {
            this.notConsumerYet = lock.newCondition();
            this.notProductYet = lock.newCondition();
        }


    }

    public static class Producer extends Thread {
        ReentrantLock lock;
        Container container;

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
                            container.getNotConsumerYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    container.setValue(Optional.of(r));
                    container.getNotProductYet().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        ReentrantLock lock;
        Container container;

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
                            container.getNotProductYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer value = container.getValue().get();
                    container.setValue(Optional.empty());
                    System.out.println("Consuming " + value);
                    container.getNotConsumerYet().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
