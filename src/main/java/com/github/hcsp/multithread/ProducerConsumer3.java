package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 使用BlockingQueue完成生产者消费者模型
 */
public class ProducerConsumer3 {
    static BlockingQueue container = new LinkedBlockingQueue();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                try {
                    while (true) {
                        int value = new Random().nextInt();
                        container.put(value);
                        Thread.sleep(300);
                        System.out.println("Producing " + value);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                int value = 0;
                try {
                    while (true) {
                        value = (int) container.take();
                        Thread.sleep(300);
                        System.out.println("Consuming " + value);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
