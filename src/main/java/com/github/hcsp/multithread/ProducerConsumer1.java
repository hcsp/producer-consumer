package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

// lock and condition
public class ProducerConsumer1 {
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
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (container.getValue().isPresent()) {
                        try {
                            container.getNotConsumer().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    container.setValue(Optional.of(r));
                    container.getNotProduce().signal();
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
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {

            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    while (!container.getValue().isPresent()) {
                        try {
                            container.getNotProduce().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Integer value = container.getValue().get();
                    System.out.println("Consuming " + value);
                    container.setValue(Optional.empty());

                    container.getNotConsumer().signal();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}


