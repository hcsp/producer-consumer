package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer1 {
    static final Integer TARGET_NUM = 10;

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<>(1);
        Producer producer = new Producer(arrayBlockingQueue);
        Consumer consumer = new Consumer(arrayBlockingQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        BlockingQueue<Integer> blockingQueue;

        public Producer(BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < TARGET_NUM; i++) {
                try {
                    Integer randomProduct = getRandomProduct();
                    blockingQueue.put(randomProduct);
                    System.out.println("Producing " + randomProduct);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Integer getRandomProduct() {
            return new Random().nextInt();
        }
    }

    public static class Consumer extends Thread {
        BlockingQueue<Integer> blockingQueue;

        public Consumer(BlockingQueue<Integer> blockingQueue) {
            this.blockingQueue = blockingQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < TARGET_NUM; i++) {
                try {
                    Integer product = blockingQueue.take();
                    System.out.println("Consuming " + product);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
