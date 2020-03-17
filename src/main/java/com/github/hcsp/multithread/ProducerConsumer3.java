package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        BlockingDeque<Integer> queue = new LinkedBlockingDeque<>(1);
        BlockingDeque<Integer> signalQueue = new LinkedBlockingDeque<>(1);

        Producer producer = new Producer(queue, signalQueue);
        Consumer consumer = new Consumer(queue, signalQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        BlockingDeque<Integer> queue;
        BlockingDeque<Integer> signalQueue;

        public Producer(BlockingDeque<Integer> queue, BlockingDeque<Integer> signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                try {
                    queue.put(r);
                    signalQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static class Consumer extends Thread {
        BlockingDeque<Integer> queue;
        BlockingDeque<Integer> signalQueue;

        public Consumer(BlockingDeque<Integer> queue, BlockingDeque<Integer> signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + queue.take());
                    signalQueue.put(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
