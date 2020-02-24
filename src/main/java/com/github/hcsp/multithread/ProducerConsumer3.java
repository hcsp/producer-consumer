package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumer3 {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(1);
        BlockingQueue<Integer> control = new ArrayBlockingQueue<>(1);
        Producer producer = new Producer(queue, control);
        Consumer consumer = new Consumer(queue, control);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        BlockingQueue<Integer> queue;
        BlockingQueue<Integer> control;

        public Producer(BlockingQueue<Integer> queue, BlockingQueue<Integer> control) {
            this.queue = queue;
            this.control = control;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int r = new Random().nextInt();
                    System.out.println("Producing" + r);
                    queue.put(r);
                    control.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static class Consumer extends Thread {
        BlockingQueue<Integer> queue;
        BlockingQueue<Integer> control;

        public Consumer(BlockingQueue<Integer> queue, BlockingQueue<Integer> control) {
            this.queue = queue;
            this.control = control;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    System.out.println("Consuming" + queue.take());
                    control.put(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
