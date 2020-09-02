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

    private static final List<Integer> list = new ArrayList<>(1);
    private static final Semaphore semaphore = new Semaphore(1, true);

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphore.acquire();
                    while (!list.isEmpty()) {
                        semaphore.release();
                    }
                    int num = new Random().nextInt();
                    System.out.println("Producing " + num);
                    list.add(num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    semaphore.acquire();
                    while (list.isEmpty()) {
                        semaphore.release();
                    }
                    System.out.println("Consuming " + list.remove(0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }
            }
        }
    }
}