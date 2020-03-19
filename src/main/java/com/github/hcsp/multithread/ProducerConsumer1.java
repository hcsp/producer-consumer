package com.github.hcsp.multithread;

import java.util.Random;

/**
 * Object.wait/notify
 */
public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Container container = new Container(null);

        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);


        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    static class Container {
        Object value;

        Container(Object value) {
            this.value = value;
        }
    }


    public static class Producer extends Thread {
        final Container container;

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
                            e.printStackTrace();
                        }
                    }
                    Object value = container.value = new Random().nextInt();
                    System.out.println("Producing " + value);
                    container.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        final Container container;

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
                            e.printStackTrace();
                        }
                    }

                    Object value = container.value;
                    container.value = null;
                    System.out.println("Consuming " + value);
                    container.notify();
                }
            }
        }
    }
}
