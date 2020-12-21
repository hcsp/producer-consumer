package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ProducerConsumer1 {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        AtomicInteger sign = new AtomicInteger(0);
        AtomicInteger val = new AtomicInteger(0);
        Producer producer = new Producer(lock, sign, val);
        Consumer consumer = new Consumer(lock, sign, val);
        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        final Object lock;
        AtomicInteger sign;
        AtomicInteger val;

        public Producer(Object lock, AtomicInteger sign, AtomicInteger val) {
            this.lock = lock;
            this.sign = sign;
            this.val = val;
        }

        @Override
        public void run() {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    while (sign.get() != 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    val.set(new Random().nextInt());
                    System.out.println("Producing " + val);
                    sign.set(1);
                    lock.notify();
                }

            }
        }
    }

    public static class Consumer extends Thread {
        final Object lock;
        AtomicInteger sign;
        AtomicInteger val;

        public Consumer(Object lock, AtomicInteger sign, AtomicInteger val) {
            this.lock = lock;
            this.sign = sign;
            this.val = val;
        }

        @Override
        public void run() {
            synchronized (lock) {
                for (int i = 0; i < 10; i++) {
                    while (sign.get() == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + val);
                    sign.set(0);
                    lock.notify();
                }

            }
        }
    }
}
