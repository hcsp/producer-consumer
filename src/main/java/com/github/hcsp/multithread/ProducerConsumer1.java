package com.github.hcsp.multithread;

import java.util.Optional;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        Container container = new Container();
        Producer producer = new Producer(container, lock);
        Consumer consumer = new Consumer(container, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Object lock;
        Container container;

        public Producer(Container container, Object lock) {
            this.lock = lock;
            this.container = container;
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

    public static class Consumer extends Thread {

        Object lock;
        Container container;

        public Consumer(Container container, Object lock) {
            this.lock = lock;
            this.container = container;
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
                    container.setValue(Optional.empty());
                    System.out.println("Consuming " + value);
                    lock.notify();
                }
            }

        }
    }
}
