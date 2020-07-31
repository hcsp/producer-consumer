package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> blockQueue = new ArrayBlockingQueue<>(1);
        BlockingQueue<Integer> signalQueue = new ArrayBlockingQueue<>(1);
        Producer producer = new Producer(blockQueue, signalQueue);
        Consumer consumer = new Consumer(blockQueue, signalQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private BlockingQueue<Integer> queue;
        private BlockingQueue<Integer> signalQueue;

        public Producer(BlockingQueue<Integer> blockQueue, BlockingQueue<Integer> signalQueue) {
            this.queue = blockQueue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    int num = new Random().nextInt();
                    System.out.println("Producing " + num);
                    queue.put(num);
                    signalQueue.take();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class Consumer extends Thread {
        private BlockingQueue<Integer> queue;
        private BlockingQueue<Integer> signalQueue;

        public Consumer(BlockingQueue<Integer> blockQueue, BlockingQueue<Integer> signalQueue) {
            this.queue = blockQueue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Consuming " + queue.take());
                    signalQueue.add(-1);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
