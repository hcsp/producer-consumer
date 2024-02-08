package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    static final Semaphore toConsume = new Semaphore(0);
    static final Semaphore toProduce = new Semaphore(1);
    static int product = 0;

    static final Object plate = new Object();

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            new Producer().start();
            new Consumer().start();
        }
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            try {
                toProduce.acquire();
                product = new Random().nextInt();
                System.out.println("Producing " + product);
                toConsume.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                toConsume.acquire();
                System.out.println("Consuming " + product);
                toProduce.release();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
