package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {

    public static void main(String[] args) throws InterruptedException {
            BlockingQueue queue = new LinkedBlockingQueue();
            BlockingQueue signalQueue = new LinkedBlockingQueue();

            Producer producer = new Producer(queue, signalQueue);
            Consumer consumer = new Consumer(queue, signalQueue);

            producer.start();
            consumer.start();

            producer.join();
            producer.join();
        }

    public static class Producer extends Thread {
        BlockingQueue queue;
        BlockingQueue siginalQueue;

        public Producer(BlockingQueue queue, BlockingQueue siginalQueue) {
            this.queue = queue;
            this.siginalQueue = siginalQueue;
        }

        public Producer(BlockingQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int r = new Random().nextInt();
                System.out.println("Producing " + r);
                try {
                    queue.put(r);
                    siginalQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static class Consumer extends Thread {
        BlockingQueue queue;
        BlockingQueue signalQueue;

        public Consumer(BlockingQueue queue, BlockingQueue signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        public Consumer(BlockingQueue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + queue.take());
                    signalQueue.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
