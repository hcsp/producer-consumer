package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    //    public static void main(String[] args) throws InterruptedException {
    //        Producer producer = new Producer();
    //        Consumer consumer = new Consumer();
    //
    //        producer.start();
    //        consumer.start();
    //
    //        producer.join();
    //        producer.join();
    //    }
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Container container = new Container(lock);


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
                    while (container.getContainer().isPresent()) {
                        try {
                            container.notConsumedYet.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    container.setContainer(Optional.of(r));
                    container.getNotProducedYet().signal();
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
                    Integer integer = container.getContainer().get();
                    container.setContainer(Optional.empty());
                    System.out.println("Consuming " + integer);
                    container.notConsumedYet.signal();

                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Container {
        private Condition notConsumedYet;
        private Condition notProducedYet;
        private Optional<Integer> container = Optional.empty();

        public Optional<Integer> getContainer() {
            return container;
        }

        public void setContainer(Optional<Integer> container) {
            this.container = container;
        }

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
    }
}
