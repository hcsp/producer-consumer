package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    private static final Object lock = new Object();
    private static final List<Integer> list = new ArrayList<>(1);

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    if (list.size() >= 1) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    list.add(new Random().nextInt());
                    System.out.println("Producing " + list.get(0));
                    lock.notifyAll();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    if (list.size() < 1) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + list.remove(0));
                    lock.notifyAll();
                }
            }
        }
    }
}