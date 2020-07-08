package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Semaphore
 */
public class ProducerConsumer4 {

    static Semaphore emptySlot = new Semaphore(1);
    static Semaphore fullSlot = new Semaphore(0);
    static Integer result;


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
                    emptySlot.acquire();
                    synchronized (ProducerConsumer4.class) {
                        result = new Random().nextInt();
                        System.out.println("Producing " + result);
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
                    synchronized (ProducerConsumer4.class) {
                        System.out.println("Consuming " + result);
                        result = null;
                    }
                    emptySlot.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
