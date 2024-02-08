package com.github.hcsp.multithread;

import java.util.Random;

public class ProducerConsumer1 {
    //wait/notify
    static final Object plate = new Object();
    static boolean hasContent = Boolean.FALSE;
    static int product;

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            new Producer().start();
            new Consumer().start();
        }
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            synchronized (plate) {
                while (hasContent) {
                    try {
                        plate.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                product = new Random().nextInt();
                System.out.println("Producing " + product);
                hasContent = true;
                plate.notifyAll();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            synchronized (plate) {
                while (!hasContent) {
                    try {
                        plate.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println("Consuming " + product);
                hasContent = false;
                plate.notifyAll();
            }
        }
    }
}
