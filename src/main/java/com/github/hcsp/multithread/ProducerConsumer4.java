package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    private static final List<Integer> PRODUCT_LIST = new ArrayList<>(1);
    private static final Semaphore SEMAPHORE = new Semaphore(1);
    private static final Semaphore GET_SEMAPHORE = new Semaphore(1);
    private static final Semaphore PUT_SEMAPHORE = new Semaphore(1);

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    PUT_SEMAPHORE.acquire();
                    SEMAPHORE.acquire();
                    int num = new Random().nextInt();
                    PRODUCT_LIST.add(num);
                    System.out.println("Producing " + num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    SEMAPHORE.release();
                    GET_SEMAPHORE.release();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    GET_SEMAPHORE.acquire();
                    SEMAPHORE.acquire();
                    System.out.println("Consuming " + PRODUCT_LIST.remove(0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    SEMAPHORE.release();
                    PUT_SEMAPHORE.release();
                }
            }
        }
    }
}
