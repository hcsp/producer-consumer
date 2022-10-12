package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    static Semaphore consumerPermitCount = new Semaphore(0);
    static Semaphore producerPermitCount = new Semaphore(1);

    public static void main(String[] args) throws InterruptedException {
        Container container = new Container();
        Producer producer = new Producer(container);
        Consumer consumer = new Consumer(container);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Container container;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    container.put();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public Producer(Container container) {
            this.container = container;
        }
    }

    public static class Consumer extends Thread {
        Container container;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    container.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        public Consumer(Container container) {
            this.container = container;
        }
    }

    public static class Container {
        private Integer value;

        public void put() throws InterruptedException {
            try {
                producerPermitCount.acquire();
                value = new Random().nextInt();
                System.out.println("Producing " + value);
            } finally {
                consumerPermitCount.release();
                Thread.sleep(100);
            }
        }

        public void take() throws InterruptedException {
            try {
                consumerPermitCount.acquire();
                Integer num = value;
                System.out.println("Consuming " + num);
                value = null;
            } finally {
                producerPermitCount.release();
            }
        }
    }
}
