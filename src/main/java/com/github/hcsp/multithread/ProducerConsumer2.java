package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer2 {
        public static void main(String[] args) throws InterruptedException {
            Container container = new Container();
            Object object = new Object();
            Producer producer = new Producer(container, object);
            Consumer consumer = new Consumer(container, object);

            producer.start();
            consumer.start();

            producer.join();
            producer.join();
        }

    public static class Producer extends Thread {
        Container container;
        Object lock;

        public Producer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (container.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    System.out.println("Producing " + r);
                    container.setValue(Optional.of(r));
                    lock.notify();
                }
            }

        }
    }

    public static class Container {
        private Optional<Integer> value = Optional.empty();

        public Optional<Integer> getValue() {
            return value;
        }

        public void setValue(Optional<Integer> value) {
            this.value = value;
        }
    }

    public static class Consumer extends Thread {
        Container container;
        Object lock;

        public Consumer(Container container, Object lock) {
            this.container = container;
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (!container.getValue().isPresent()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer value = container.getValue().get();
                    System.out.println("Consuming " + value);
                    container.setValue(Optional.empty());
                    lock.notify();
                }
            }
        }
    }
}
