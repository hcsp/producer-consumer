package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        Container container = new Container();
        Producer producer = new Producer(lock, container);
        Consumer consumer = new Consumer(lock, container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    //生产者线程
    public static class Producer extends Thread {
        Object lock;
        Container container;

        public Producer(Object lock, Container container) {
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (container.getOptional().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    container.setOptional(Optional.of(r));
                    System.out.println("Producing " + r);
                    lock.notify();
                }
            }
        }
    }

    //消费者线程
    public static class Consumer extends Thread {
        Object lock;
        Container container;

        public Consumer(Object lock, Container container) {
            this.lock = lock;
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!container.getOptional().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer a = container.optional.get();
                    System.out.println("Consuming " + a);
                    container.optional = Optional.empty();
                    lock.notify();
                }
            }
        }
    }

    public static class Container {
        private Optional<Integer> optional = Optional.empty();

        public Optional<Integer> getOptional() {
            return optional;
        }

        public void setOptional(Optional<Integer> optional) {
            this.optional = optional;
        }
    }
}
