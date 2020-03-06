package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock2 = new ReentrantLock();
        Container container2 = new Container(lock2);
        Producer producer2 = new Producer(container2, lock2);
        Consumer consumer2 = new Consumer(container2, lock2);

        producer2.start();
        consumer2.start();

        producer2.join();
        producer2.join();
    }

    public static class Producer extends Thread {
        Container container2;
        ReentrantLock lock2;

        public Producer(Container container, ReentrantLock lock) {
            this.container2 = container;
            this.lock2 = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock2.lock();
                    while (container2.getValue().isPresent()) {
                        try {
                            container2.getNotConsumerYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int value2 = new Random().nextInt();
                    System.out.println("Producing " + value2);
                    container2.setValue(Optional.of(value2));
                    container2.getNotProducedYet().signal();
                } finally {
                    lock2.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container container2;
        ReentrantLock lock2;

        public Consumer(Container container, ReentrantLock lock) {
            this.container2 = container;
            this.lock2 = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock2.lock();
                    while (!container2.getValue().isPresent()) {
                        try {
                            container2.getNotProducedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer value2 = container2.getValue().get();
                    container2.setValue(Optional.empty());
                    System.out.println("Consuming" + value2);

                    container2.getNotConsumerYet().signal();
                } finally {
                    lock2.unlock();
                }
            }
        }
    }

    static class Container {
        private Condition notConsumerYed2;
        private Condition notProducedYed2;

        Container(ReentrantLock lock) {
            this.notConsumerYed2 = lock.newCondition();
            this.notProducedYed2 = lock.newCondition();
        }

        public Condition getNotConsumerYet() {
            return notConsumerYed2;
        }

        public Condition getNotProducedYet() {
            return notProducedYed2;
        }

        Optional<Integer> value2 = Optional.empty();

        public Optional<Integer> getValue() {
            return value2;
        }

        public void setValue(Optional<Integer> value2) {
            this.value2 = value2;
        }
    }
}
