package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    private static BlockingQueue container = new LinkedBlockingQueue(1);

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    while (container.size() != 0) {

                    }
                    int value = new Random().nextInt();
                    container.put(value);
                    System.out.println("Producing " + value);
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
                    while (container.size() == 0) {

                    }
                    System.out.println("Consuming " + container.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
