package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Optional<Integer> value = Optional.empty();
        Container container = new Container(value, lock);
        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
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
                    while (container.value.isPresent()) {
                        container.notFull.await();
                    }
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    container.value = Optional.of(r);
                    container.notEmpty.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
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
                    while (!container.value.isPresent()) {
                        container.notEmpty.await();
                    }
                    System.out.println("Consuming " + container.value.get());
                    container.value = Optional.empty();
                    container.notFull.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }

        }
    }

    public static class Container {
        Optional<Integer> value;
        Condition notFull;
        Condition notEmpty;

        public Container(Optional<Integer> value, ReentrantLock lock) {
            this.value = value;
            this.notFull = lock.newCondition();
            this.notEmpty = lock.newCondition();
        }
    }
}
