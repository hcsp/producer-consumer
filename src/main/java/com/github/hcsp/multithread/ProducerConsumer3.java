package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        BlockingQueue<Integer> lockQueue = new LinkedBlockingQueue<>(1);
        Producer producer = new Producer(queue, lockQueue);
        Consumer consumer = new Consumer(queue, lockQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        BlockingQueue<Integer> queue;
        BlockingQueue<Integer> lockQueue;

        public Producer(BlockingQueue<Integer> queue, BlockingQueue<Integer> lockQueue) {
            this.queue = queue;
            this.lockQueue = lockQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                Integer value = new Random().nextInt();
                try {
                    this.queue.put(value);
                    System.out.println("produce " + value);
                    this.lockQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        public Consumer(BlockingQueue<Integer> queue, BlockingQueue<Integer> lockQueue) {
            this.queue = queue;
            this.lockQueue = lockQueue;
        }

        BlockingQueue<Integer> queue;
        BlockingQueue<Integer> lockQueue;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Object value = this.queue.take();
                    System.out.println("consume " + value);
                    this.lockQueue.put(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
