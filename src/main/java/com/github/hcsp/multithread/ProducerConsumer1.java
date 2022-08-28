package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Object lock = new Object();
        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container;
        final Object lock;

        Producer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    try {
                        while (container.getContainer().isPresent()) {
                            lock.wait();
                        }
                        Integer num = new Random().nextInt();
                        System.out.println("Producing " + num);
                        container.setContainer(Optional.of(num));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.notify();
                    }
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;
        Object lock;

        Consumer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    try {
                        while (!container.getContainer().isPresent()) {
                            lock.wait();
                        }
                        System.out.println("Consuming " + container.getContainer().get());
                        container.setContainer(Optional.empty());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.notify();
                    }
                }
            }
        }
    }

    public static class Container {
        private Optional<Integer> container = Optional.empty();

        public Optional<Integer> getContainer() {
            return container;
        }

        public void setContainer(Optional<Integer> container) {
            this.container = container;
        }
    }
}
