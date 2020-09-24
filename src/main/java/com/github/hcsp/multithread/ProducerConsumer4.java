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

    private static final List<Integer> productList = new ArrayList<>(1);
    private static final Semaphore mutex = new Semaphore(1);
    private static final Semaphore getSemaphore = new Semaphore(1);
    private static final Semaphore putSemaphore = new Semaphore(1);

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    putSemaphore.acquire();
                    mutex.acquire();
                    int num = new Random().nextInt();
                    productList.add(num);
                    System.out.println("Producing " + num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    getSemaphore.release();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    getSemaphore.acquire();
                    mutex.acquire();
                    System.out.println("Consuming " + productList.remove(0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    mutex.release();
                    putSemaphore.release();
                }
            }
        }
    }
}
