package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock1 = new ReentrantLock();
        Container container = new Container(lock1);
        Producer producer = new Producer(lock1, container);
        Consumer consumer = new Consumer(lock1, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        ReentrantLock lock1;
        Container container;

        public Producer(ReentrantLock lock1, Container container) {
            this.lock1 = lock1;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock1.lock();
                    while (container.getContainer().isPresent()) {
                        try {
                            container.getNotConsumedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    System.out.println("Producer " + r);
                    container.getNotProducedYet().signal();
                    container.setContainer(Optional.of(r));
                } finally {
                    lock1.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        ReentrantLock lock1;
        Container container;

        public Consumer(ReentrantLock lock1, Container container) {
            this.lock1 = lock1;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock1.lock();
                    while (!container.getContainer().isPresent()) {
                        try {
                            container.getNotProducedYet().await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer value = container.getContainer().get();
                    container.setContainer(Optional.empty());
                    System.out.println("Consuming " + value);
                    container.getNotConsumedYet().signal();
                } finally {
                    lock1.unlock();
                }
            }

        }
    }
}
