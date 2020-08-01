package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new LinkedBlockingQueue(1);
        BlockingQueue<Integer> signalqueue = new LinkedBlockingQueue<>(1);
        Producer producer = new Producer(queue, signalqueue);
        Consumer consumer = new Consumer(queue, signalqueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        BlockingQueue<Integer> queue;
        BlockingQueue<Integer> signalqueue;

        public Producer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalqueue) {
            this.queue = queue;
            this.signalqueue = signalqueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int randomNumber = new Random().nextInt();
                try {
                    System.out.println("Producing " + randomNumber);
                    queue.put(randomNumber);
                    signalqueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        BlockingQueue<Integer> queue;
        BlockingQueue<Integer> signalqueue;

        public Consumer(BlockingQueue<Integer> queue, BlockingQueue<Integer> signalqueue) {
            this.queue = queue;
            this.signalqueue = signalqueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + queue.take());
                    signalqueue.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
