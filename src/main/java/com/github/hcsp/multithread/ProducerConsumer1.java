package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container(Optional.empty());
        Object lock = new Object();

        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Container {
        private Optional<Integer> optional;

        public Container(Optional<Integer> optional) {
            this.optional = optional;
        }

        public Optional<Integer> getOptional() {
            return optional;
        }

        public void setOptional(Optional<Integer> optional) {
            this.optional = optional;
        }
    }

    public static class Producer extends Thread {

        private Container container;
        private Object lock;

        public Producer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
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
                    int randomInteger = new Random().nextInt();
                    container.setOptional(Optional.of(randomInteger));
                    System.out.println("Producing " + container.getOptional().get());
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private Container container;
        private Object lock;

        public Consumer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
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
                    System.out.println("Consuming " + container.getOptional().get());
                    container.setOptional(Optional.empty());
                    lock.notify();
                }
            }
        }
    }
}
