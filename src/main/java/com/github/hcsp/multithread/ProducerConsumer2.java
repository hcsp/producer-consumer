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
        private ReentrantLock lock;
        private Container container;

        public Producer(ReentrantLock lock, Container container) {
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (container.getValue().isPresent()) {
                        container.getNotEmpty().await();
                    }
                    int number = new Random().nextInt();
                    System.out.println("生产：" + number);
                    container.setValue(Optional.of(number));
                    container.getEmpty().signal();

                } catch (InterruptedException e) {
                    e.printStackTrace();
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
                lock.lock();
                try {
                    while (!container.getValue().isPresent()) {
                        container.getEmpty().await();
                    }
                    System.out.println("消费：" + container.getValue().get());
                    container.setValue(Optional.empty());
                    container.getNotEmpty().signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Container {
        private Condition empty;
        private Condition notEmpty;
        private Optional<Integer> value = Optional.empty();


        public Container(ReentrantLock lock) {
            this.empty = lock.newCondition();
            this.notEmpty = lock.newCondition();
        }

        public Condition getEmpty() {
            return empty;
        }

        public Condition getNotEmpty() {
            return notEmpty;
        }

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }
}
