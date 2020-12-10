package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition notFull = lock.newCondition();
        Condition notEmpty = lock.newCondition();
        Container container = new Container();

        Producer producer = new Producer(container, lock, notFull, notEmpty);
        Consumer consumer = new Consumer(container, lock, notFull, notEmpty);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container;
        ReentrantLock lock;
        Condition notFull;
        Condition notEmpty;

        public Producer(Container container, ReentrantLock lock, Condition notFull, Condition notEmpty) {
            this.container = container;
            this.lock = lock;
            this.notFull = notFull;
            this.notEmpty = notEmpty;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (container.getValue().isPresent()) {
                        notFull.await();
                    }
                    int tempInt = new Random().nextInt();
                    container.setValue(Optional.of(tempInt));
                    System.out.println("Producing " + tempInt);
                    notEmpty.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;
        ReentrantLock lock;
        Condition notFull;
        Condition notEmpty;

        public Consumer(Container container, ReentrantLock lock, Condition notFull, Condition notEmpty) {
            this.container = container;
            this.lock = lock;
            this.notFull = notFull;
            this.notEmpty = notEmpty;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (!container.getValue().isPresent()) {
                        notEmpty.await();
                    }
                    int tempInt = container.getValue().get();
                    System.out.println("Consuming " + tempInt);
                    container.setValue(Optional.empty());
                    notFull.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
