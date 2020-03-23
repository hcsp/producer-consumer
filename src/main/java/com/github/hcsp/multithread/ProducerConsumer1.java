package com.github.hcsp.multithread;

import java.util.Objects;
import java.util.Random;

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
        private Container container;

        public Producer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            synchronized (container) {
                for (int i = 0; i < 10; i++) {
                    while (Objects.nonNull(container.value)) {
                        try {
                            container.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Integer value = new Random().nextInt();
                    container.value = value;
                    System.out.println("Producing " + value);
                    container.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private Container container;

        public Consumer(Container container) {
            this.container = container;
        }

        @Override
        public void run() {
            synchronized (container) {
                for (int i = 0; i < 10; i++) {
                    while (Objects.isNull(container.value)) {
                        try {
                            container.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
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
