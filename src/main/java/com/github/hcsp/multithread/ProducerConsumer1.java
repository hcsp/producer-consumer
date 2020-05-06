package com.github.hcsp.multithread;

import java.util.Random;

/**
 * Object.wait/notify
 *
 * @author kwer
 * @date 2020/5/6 22:33
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

    /**
     * 容器
     */
    static class Container {
        Object value;
    }

    public static class Producer extends Thread {
        Container container;

        public Producer(Container container) {
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
                    int j = new Random().nextInt();
                    System.out.println("Producing " + j);
                    container.value = j;
                    container.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Container container;

        public Consumer(Container container) {
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
                    System.out.println("Consuming " + container.value);
                    container.value = null;
                    container.notify();
                }
            }
        }
    }
}
