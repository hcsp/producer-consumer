package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProducerConsumer1 {
    private static final Object lock = new Object();
    private static List<Integer> basket = new ArrayList<>(1);
    private static int index = 0;

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
            while (index < 10) {
                synchronized (lock) {
                    if (basket.isEmpty()) {
                        int random = new Random().nextInt();
                        basket.add(random);
                        System.out.println("Producing " + basket.get(0));
                        lock.notify();
                    } else {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            while (index < 10) {
                synchronized (lock) {
                    if (basket.isEmpty()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        System.out.println("Consuming " + basket.get(0));
                        basket.remove(0);
                        index++;
                        lock.notify();
                    }
                }
            }
        }
    }
}
