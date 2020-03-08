package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer2 {
    private static BlockingQueue bq = new ArrayBlockingQueue(1);

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer(bq);
        Consumer consumer = new Consumer(bq);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();

    }

    public static class Producer extends Thread {
        private BlockingQueue bq;

        Producer(BlockingQueue<Integer> bq) {
            this.bq = bq;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    int randomNum = new Random().nextInt();
                    bq.put(randomNum);
                    System.out.println("Producing " + randomNum);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Consumer extends Thread {
        private BlockingQueue bq;

        Consumer(BlockingQueue<Integer> bq) {
            this.bq = bq;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    int randomNum = (int) bq.take();
                    System.out.println("Consuming " + randomNum);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
