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

    private static final Object LOCK = new Object();
    private static final List<Integer> PRODUCT_LIST = new ArrayList<>(1);

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (LOCK) {
                    if (PRODUCT_LIST.size() >= 1) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    PRODUCT_LIST.add(new Random().nextInt());
                    System.out.println("Producing " + PRODUCT_LIST.get(0));
                    LOCK.notifyAll();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (LOCK) {
                    if (PRODUCT_LIST.size() < 1) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + PRODUCT_LIST.remove(0));
                    LOCK.notifyAll();
                }
            }
        }
    }
}
