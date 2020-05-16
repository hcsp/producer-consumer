package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Integer> arrayBlockingQueue = new ArrayBlockingQueue<>(1);
        final Integer TARGET_NUM = 10;
        Producer producer = new Producer(arrayBlockingQueue, TARGET_NUM);
        Consumer consumer = new Consumer(arrayBlockingQueue, TARGET_NUM);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        BlockingQueue<Integer> blockingQueue;
        final Integer TARGET_NUM;

        public Producer(BlockingQueue<Integer> blockingQueue, Integer TARGET_NUM) {
            this.blockingQueue = blockingQueue;
            this.TARGET_NUM = TARGET_NUM;
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
        final Integer TARGET_NUM;

        public Consumer(BlockingQueue<Integer> blockingQueue, Integer TARGET_NUM) {
            this.blockingQueue = blockingQueue;
            this.TARGET_NUM = TARGET_NUM;
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
