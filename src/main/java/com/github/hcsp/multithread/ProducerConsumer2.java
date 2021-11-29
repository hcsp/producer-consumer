package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {

        ReentrantLock object = new ReentrantLock();
        Container container = new Container(object);
        Producer producer = new Producer(object, container);
        Consumer consumer = new Consumer(object, container);

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

                lock.lock();
                try {
                    while (container.getOptional().isPresent()) {
                        container.notEmpty.await();
                    }
                    int r = new Random().nextInt();
                    container.setOptional(Optional.of(r));
                    System.out.println("Producing " + r);
                    container.notFull.signal();
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

                lock.lock();
                try {
                    while (!container.getOptional().isPresent()) {
                        container.notFull.await();
                    }
                    Integer a = container.optional.get();
                    System.out.println("Consuming " + a);
                    container.optional = Optional.empty();
                    container.notEmpty.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Container {
        private Optional<Integer> optional = Optional.empty();
        ReentrantLock lock;
        Condition notFull;
        Condition notEmpty;

        public Container(ReentrantLock lock) {
            this.notEmpty = lock.newCondition();
            this.notFull = lock.newCondition();
        }

        public Optional<Integer> getOptional() {
            return optional;
        }

        public void setOptional(Optional<Integer> optional) {
            this.optional = optional;
        }
    }
}
