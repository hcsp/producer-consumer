package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.SynchronousQueue;


public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<Integer> queue = new SynchronousQueue<>();
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private final SynchronousQueue<Integer> queue;

        public Producer(SynchronousQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int rand = new Random().nextInt();
                    queue.put(rand);
                    System.out.println("Producing " + rand);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public static class Consumer extends Thread {
        private final SynchronousQueue<Integer> queue;

        public Consumer(SynchronousQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming " + queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
