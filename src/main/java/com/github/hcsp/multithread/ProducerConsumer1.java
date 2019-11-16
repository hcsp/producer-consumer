package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Object.wait()/notify()/notifyAll()
 */
public class ProducerConsumer1 {

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Queue<Integer> queue = new LinkedList<>();

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    static class Producer extends Thread {
        Queue<Integer> queue;
        Producer(Queue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    try {
                        while (!queue.isEmpty()) {
                            lock.wait();
                        }
                        int value = new Random().nextInt();
                        System.out.println("Producing " + value);
                        queue.add(value);
                        lock.notify();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class Consumer extends Thread {
        Queue<Integer> queue;

        Consumer(Queue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    try {
                        while (queue.isEmpty()) {
                            lock.wait();
                        }
                        System.out.println("Consumer " + queue.remove());
                        lock.notify();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
