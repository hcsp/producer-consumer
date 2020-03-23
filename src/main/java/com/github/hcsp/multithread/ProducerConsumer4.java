package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    private static Semaphore semaphore = new Semaphore(1);
    private static ProducerConsumer1.Container container = new ProducerConsumer1.Container();

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
                    while (container.value != null) {

                    }
                    semaphore.acquire();
                    int value = new Random().nextInt();
                    System.out.println("Producing " + value);
                    container.value = value;
                    semaphore.release();
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
                    while (container.value == null) {

                    }
                    semaphore.acquire();
                    System.out.println("Consuming " + container.value);
                    container.value = null;
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
