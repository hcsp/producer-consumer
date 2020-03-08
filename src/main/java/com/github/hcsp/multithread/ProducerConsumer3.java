package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class ProducerConsumer3 {
    private static Semaphore fillQueue = new Semaphore(1);
    private static Semaphore emptyQueue = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        Queue<Integer> queue = new LinkedList<>();

        Producer producer = new Producer(queue);
        Consumer consumer = new Consumer(queue);
        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        private Queue<Integer> queue;

        Producer(Queue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    fillQueue.acquire();
                    synchronized (ProducerConsumer3.class) {
                        int sharedNum = new Random().nextInt();
                        queue.add(sharedNum);
                        System.out.println("Producing " + sharedNum);
                    }
                    emptyQueue.release();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Consumer extends Thread {
        private Queue<Integer> queue;

        Consumer(Queue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < 10; i++) {
                    emptyQueue.acquire();
                    synchronized (ProducerConsumer3.class) {
                        Integer sharedNum = queue.remove();
                        System.out.println("Consuming " + sharedNum);
                    }
                    fillQueue.release();
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
