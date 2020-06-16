package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    private static LinkedBlockingQueue<Integer> blockingQueue  = new LinkedBlockingQueue(1);
    private static LinkedBlockingQueue<Integer> blockingQueue2  = new LinkedBlockingQueue(1);
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
                int nextInt = new Random().nextInt();
                try {
                    blockingQueue.put(nextInt);
                    System.out.println("Producing " +  nextInt);
                    blockingQueue2.take();
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
                    Integer value = blockingQueue.take();
                    System.out.println("Consuming " + value);
                    blockingQueue2.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
