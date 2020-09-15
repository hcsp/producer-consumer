package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer2 {
    static LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(1);

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
            int count = 0;
            while (true) {
                if (count == 10) {
                    break;
                }
                int num = new Random().nextInt();
                System.out.println("Producing " + num);
                count++;
                try {
                    queue.put(num);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            int count = 0;
            while (true) {
                if (count == 10) {
                    break;
                }
                try {
                    count++;
                    System.out.println("Consuming " + queue.take());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
