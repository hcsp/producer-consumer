package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Random;

public class ProducerConsumer1 {
    static final Object lock = new Object();
    static LinkedList<Integer> list = new LinkedList<>();
    static final int CAPACITY = 10;

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    static class Producer extends Thread {
        @Override
        public void run() {
            try {
                int i = 0;
                while (i++ < CAPACITY) {
                    synchronized (lock) {
                        if (!list.isEmpty()) {
                            lock.wait();
                        }

                        int value = new Random().nextInt();
                        list.add(value);
                        System.out.println("Producing " + value);

                        lock.notify();
                        Thread.sleep(1000);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                int i = 0;
                while (i++ < CAPACITY) {
                    synchronized (lock) {
                        if (list.isEmpty()) {
                            lock.wait();
                        }

                        int value = list.removeFirst();
                        System.out.println("Consuming " + value);

                        lock.notify();
                        Thread.sleep(1000);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
