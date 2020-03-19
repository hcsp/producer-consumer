package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * BlockingQueue
 */
public class ProducerConsumer3 {

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Object> queue = new SynchronousQueue<Object>();

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);


        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }


    public static class Producer extends Thread {
        final BlockingQueue<Object> queue;

        Producer(BlockingQueue<Object> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    int r = new Random().nextInt();
                    queue.put(r);
                    System.out.println("Producing " + r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        final BlockingQueue<Object> queue;

        Consumer(BlockingQueue<Object> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Object r = queue.take();
                    System.out.println("Producing " + r);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
