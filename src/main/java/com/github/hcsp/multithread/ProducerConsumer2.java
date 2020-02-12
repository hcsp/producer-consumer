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
                    while (container.getContainer().isPresent()) {
                        container.getNotConsumerYet().await();
                    }
                    Integer r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    container.setContainer(Optional.of(r));
                    container.getNotProductYet().signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
                    while (!container.getContainer().isPresent()) {
                        container.getNotProductYet().await();
                    }
                    Integer r = container.getContainer().get();
                    System.out.println("Consuming " + r);
                    container.setContainer(Optional.empty());
                    container.getNotConsumerYet().signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Container {
        private Optional<Integer> container = Optional.empty();
        private Condition notProductYet;
        private Condition notConsumerYet;

        public Container(ReentrantLock lock) {
            this.notProductYet = lock.newCondition();
            this.notConsumerYet = lock.newCondition();
        }

        public Optional<Integer> getContainer() {
            return container;
        }

        public void setContainer(Optional container) {
            this.container = container;
        }

        public Condition getNotProductYet() {
            return notProductYet;
        }

        public Condition getNotConsumerYet() {
            return notConsumerYet;
        }
    }

}
