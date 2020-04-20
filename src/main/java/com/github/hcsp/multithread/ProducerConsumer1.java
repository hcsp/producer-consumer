package com.github.hcsp.multithread;

import java.util.Random;

/**
 *  Object.wait/notify
 */
public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    static class Container {
        Object value;
    }

    public static class Producer extends Thread {
        Container container;

        Producer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (container) {
                    while (container.value != null) {
                        try {
                            container.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    int val = new Random().nextInt();
                    container.value = val;
                    System.out.println("Producing " + val);
                    container.notify();
                }
            }

        }
    }

    public static class Consumer extends Thread {
        Container container;

        Consumer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (container) {
                    while (container.value == null) {
                        try {
                            container.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    System.out.println("Consuming " + container.value);
                    container.value = null;
                    container.notify();
                }
            }

        }
    }
}
