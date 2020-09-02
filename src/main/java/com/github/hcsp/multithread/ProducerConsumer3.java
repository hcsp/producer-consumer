package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    private static final BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);
    private static final BlockingQueue<Integer> signalQueue = new ArrayBlockingQueue<>(1);

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int num = new Random().nextInt();
                    queue.put(num);
                    System.out.println("Producing " + num);
                    signalQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + queue.take());
                    signalQueue.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}