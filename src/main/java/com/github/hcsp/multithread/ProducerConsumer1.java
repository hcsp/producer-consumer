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
                    while (container.getContainer().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    System.out.println("producer " + r);
                    container.setContainer(Optional.of(r));
                    lock.notify();
                }
            }
        }
    }

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
                    while (!container.getContainer().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer r = container.getContainer().get();
                    container.setContainer(Optional.empty());
                    System.out.println("Consumer " + r);
                    lock.notify();
                }
            }
        }
    }

    public static class Container {
        Optional<Integer> container = Optional.empty();

        public Optional<Integer> getContainer() {
            return container;
        }

        public void setContainer(Optional<Integer> container) {
            this.container = container;
        }
    }
}
