package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        /**
         * Lock + Condition
         */
        ReentrantLock lock = new ReentrantLock();
        Condition fullCondition = lock.newCondition();
        Condition emptyCondition = lock.newCondition();
        Container container = new Container();
        Producer producer = new Producer(container, lock, fullCondition, emptyCondition);
        Consumer consumer = new Consumer(container, lock, fullCondition, emptyCondition);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final Container container;
        private final ReentrantLock lock;
        private final Condition fullCondition;
        private final Condition emptyCondition;

        public Producer(Container container, ReentrantLock lock, Condition fullCondition, Condition emptyCondition) {
            this.container = container;
            this.lock = lock;
            this.fullCondition = fullCondition;
            this.emptyCondition = emptyCondition;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (container.value != null) {
                        emptyCondition.await();
                    }
                    int value = new Random().nextInt();
                    System.out.println("Producing " + value);
                    container.setValue(value);
                    fullCondition.signal();
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private final Container container;
        private final ReentrantLock lock;
        private final Condition fullCondition;
        private final Condition emptyCondition;

        public Consumer(Container container, ReentrantLock lock, Condition fullCondition, Condition emptyCondition) {
            this.container = container;
            this.lock = lock;
            this.fullCondition = fullCondition;
            this.emptyCondition = emptyCondition;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (container.value == null) {
                        fullCondition.await();
                    }
                    System.out.println("Consuming " + container.getValue());
                    container.setValue(null);
                    emptyCondition.signal();
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
