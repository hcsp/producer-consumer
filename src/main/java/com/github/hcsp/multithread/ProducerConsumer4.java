package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * 使用Semaphore完成生产者消费者模型
 */
public class ProducerConsumer4 {
    static Semaphore emptySlot = new Semaphore(1);
    static Semaphore fullSlot = new Semaphore(0);
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
                try {
                    emptySlot.acquire();
                    synchronized (container) {
                        container.value = new Random().nextInt();
                        System.out.println("Producing " + container.value);
                    }
                    fullSlot.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    fullSlot.acquire();
                    synchronized (container) {
                        System.out.println("Consuming " + container.value);
                    }
                    emptySlot.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
