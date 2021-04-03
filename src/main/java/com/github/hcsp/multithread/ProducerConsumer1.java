package com.github.hcsp.multithread;

import java.util.Random;

public class ProducerConsumer1 {

    private static Object lock = new Object();
    private static Integer pro;

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
                synchronized (lock) {
                    while (pro != null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int nextInt = new Random().nextInt();
                    System.out.println("Producing " + nextInt);
                    pro = nextInt;
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
                    while (pro == null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + pro);
                    pro = null;
                    lock.notifyAll();
                }
            }
        }
    }
}
