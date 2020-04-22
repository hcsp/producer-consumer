package com.github.hcsp.multithread;

import java.util.Random;

/**
 * 使用Object的wait/notify实现生产者消费者模型
 *
 */
public class ProducerConsumer1 {
    static Container container = new Container();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Container {
        private Object value;

        public Container() {
        }
    }

    public static class Producer extends Thread {
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
                    container.value = new Random().nextInt();
                    System.out.println("Producing " + container.value);
                    container.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
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
                    System.out.println("Consuming " + container.value);
                    container.value = null;
                    container.notify();
                }
            }
        }
    }
}
