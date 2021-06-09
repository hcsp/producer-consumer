package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>(1);
        BlockingQueue<Integer> signalQueue = new LinkedBlockingQueue<>(1);
        Producer producer = new Producer(blockingQueue, signalQueue);
        Consumer consumer = new Consumer(blockingQueue, signalQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private BlockingQueue<Integer> blockingQueue;
        private BlockingQueue<Integer> signalQueue;

        public Producer(BlockingQueue<Integer> blockingQueue, BlockingQueue<Integer> signalQueue) {
            this.blockingQueue = blockingQueue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int random = new Random().nextInt();
                System.out.println("Producing " + random);
                try {
                    blockingQueue.put(random);
                    signalQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private BlockingQueue<Integer> blockingQueue;
        private BlockingQueue<Integer> signalQueue;

        public Consumer(BlockingQueue<Integer> blockingQueue, BlockingQueue<Integer> signalQueue) {
            this.blockingQueue = blockingQueue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + blockingQueue.take());
                    signalQueue.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
