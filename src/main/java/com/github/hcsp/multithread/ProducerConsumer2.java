package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    private static Lock lock = new ReentrantLock();
    private static Condition queueEmpty = lock.newCondition();
    private static Condition queueFull = lock.newCondition();
    private static boolean flag = true;

    public static void main(String[] args) throws InterruptedException {

        Queue<Integer> queue = new LinkedList<>();

        Producer producer = new Producer(queue, 10);
        Consumer consumer = new Consumer(queue);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    public static class Producer extends Thread {
        private Queue<Integer> queue;
        private int max;

        public Producer(Queue<Integer> queue, int max) {
            this.queue = queue;
            this.max = max;
        }

        @Override
        public void run() {
            Random r = new Random();
            for (int i = 0; i < max; i++) {
                lock.lock();
                try {
                    if (!queue.isEmpty()) {
                        queueEmpty.await();
                    }
                    int rInt = r.nextInt();
                    System.out.println("Producing " + rInt);
                    queue.add(rInt);
                    queueFull.signal();
                    if (i >= max - 1) {
                        flag = false;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }

            }
        }
    }

    public static class Consumer extends Thread {
        private Queue<Integer> queue;

        public Consumer(Queue<Integer> queue) {
            this.queue = queue;
        }
        @Override
        public void run() {
            while (flag) {
                lock.lock();
                if (queue.isEmpty()) {
                    try {
                        queueFull.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                int val = queue.remove();
                System.out.println("Consuming " + val);
                queueEmpty.signal();
                lock.unlock();
            }
        }
    }
}
