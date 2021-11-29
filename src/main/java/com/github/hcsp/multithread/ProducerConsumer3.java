package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {

        BlockingDeque blockingDeque = new LinkedBlockingDeque(1);
        BlockingDeque blockingDeque0 = new LinkedBlockingDeque(1);

        Producer producer = new Producer(blockingDeque,blockingDeque0);
        Consumer consumer = new Consumer(blockingDeque,blockingDeque0);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        BlockingDeque<Integer> blockingDeque;
        BlockingDeque<Integer> blockingDeque0;

        public Producer(BlockingDeque<Integer> blockingDeque,BlockingDeque<Integer> blockingDeque0) {
            this.blockingDeque = blockingDeque;
            this.blockingDeque0 = blockingDeque0;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                try {
                    blockingDeque.put(r);
                    //停在这里
                    blockingDeque0.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        BlockingDeque<Integer> blockingDeque;
        BlockingDeque<Integer> blockingDeque0;

        public Consumer(BlockingDeque<Integer> blockingDeque,BlockingDeque<Integer> blockingDeque0) {
            this.blockingDeque = blockingDeque;
            this.blockingDeque0 = blockingDeque0;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    //停在这里
                    Integer a = blockingDeque.take();
                    System.out.println("Consuming " + a);
                    blockingDeque0.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
