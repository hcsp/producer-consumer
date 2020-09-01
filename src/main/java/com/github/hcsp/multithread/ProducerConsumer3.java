package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(1);
        LinkedBlockingQueue<Integer> signalQueue = new LinkedBlockingQueue<>(1);

        Producer producer = new Producer(queue, signalQueue);
        Consumer consumer = new Consumer(queue, signalQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        LinkedBlockingQueue<Integer> queue;
        LinkedBlockingQueue<Integer> signalQueue;

        public Producer(LinkedBlockingQueue<Integer> queue, LinkedBlockingQueue<Integer> signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                Integer r = new Random().nextInt();
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
        LinkedBlockingQueue<Integer> queue;
        LinkedBlockingQueue<Integer> signalQueue;

        public Consumer(LinkedBlockingQueue<Integer> queue, LinkedBlockingQueue<Integer> signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }


        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Integer r = queue.take();
                    System.out.println("Consuming " + r);
                    signalQueue.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
