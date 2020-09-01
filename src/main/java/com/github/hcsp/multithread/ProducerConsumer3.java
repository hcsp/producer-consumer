package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

public class ProducerConsumer3 {
    public static void main(String[] args) throws InterruptedException {
        LinkedBlockingDeque<Integer> queue = new LinkedBlockingDeque<>(1);
        LinkedBlockingDeque<Integer> signalQueue = new LinkedBlockingDeque<>(1);

        Producer producer = new Producer(queue, signalQueue);
        Consumer consumer = new Consumer(queue, signalQueue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        LinkedBlockingDeque<Integer> signalQueue;
        LinkedBlockingDeque<Integer> queue;

        public Producer(LinkedBlockingDeque<Integer> signalQueue, LinkedBlockingDeque<Integer> queue) {
            this.signalQueue = signalQueue;
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                int r = new Random().nextInt();
                System.out.println("Producing" + r);
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
        LinkedBlockingDeque<Integer> queue;
        LinkedBlockingDeque<Integer> signalQueue;

        public Consumer(LinkedBlockingDeque<Integer> queue, LinkedBlockingDeque<Integer> signalQueue) {
            this.queue = queue;
            this.signalQueue = signalQueue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Integer r = queue.take();
                    System.out.println("Consuming" + r);
                    signalQueue.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
