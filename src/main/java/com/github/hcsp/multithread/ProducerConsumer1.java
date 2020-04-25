package com.github.hcsp.multithread;

import java.util.Random;

/* Object.wait/notify */
public class ProducerConsumer1 {

    static Object lock = new Object();

    public static class ValueObject {
        public static int value = 0;
    }

    public static void main(String[] args) throws InterruptedException {

        Producer producer = new Producer(lock);
        Consumer consumer = new Consumer(lock);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {

        Object lock;

        Producer(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    while (ValueObject.value != 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    ValueObject.value = new Random().nextInt();
                    System.out.println("Producing " + ValueObject.value);
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {

        Object lock;

        Consumer(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    while (ValueObject.value == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + ValueObject.value);
                    ValueObject.value = 0;
                    lock.notify();
                }
            }
        }
    }
}
