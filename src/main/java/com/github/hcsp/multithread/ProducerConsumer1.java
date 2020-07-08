package com.github.hcsp.multithread;

import java.util.Random;

/**
 * wait和notify
 */
public class ProducerConsumer1 {

    static Integer result;

    public static void main(String[] args) throws InterruptedException {

        Object lock = new Object();
        Integer result = null;

        Producer producer = new Producer(result, lock);
        Consumer consumer = new Consumer(result, lock);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Object lock;

        public Producer(Integer result, Object lock) {
            this.lock = lock;

        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (result != null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    result = new Random().nextInt();
                    System.out.println("Producing " + result);
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Object lock;

        public Consumer(Integer result, Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    while (result == null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + result);
                    result = null;
                    lock.notify();
                }
            }
        }
    }
}
