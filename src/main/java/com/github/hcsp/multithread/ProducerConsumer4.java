package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * Semaphore
 */
public class ProducerConsumer4 {
    private static Semaphore isEmpty = new Semaphore(1);
    private static Semaphore isFull = new Semaphore(0);
    private static final Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {

        Queue<Integer> queue = new LinkedList();
        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        Queue<Integer> queue;

        Producer(Queue queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    isEmpty.acquire();
                    synchronized (obj) {
                        int val = new Random().nextInt();
                        System.out.println("Producing " + val);
                        queue.offer(val);
                    }
                    isFull.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static class Consumer extends Thread {
        Queue<Integer> queue;

        Consumer(Queue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    isFull.acquire();
                    synchronized (obj) {
                        System.out.println("Producing " + queue.poll());
                    }
                    isEmpty.release();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
