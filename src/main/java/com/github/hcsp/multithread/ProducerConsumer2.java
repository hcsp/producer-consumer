package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *  Lock/Condition
 */
public class ProducerConsumer2 {
    private static Lock lock = new ReentrantLock();
    private static Condition isFull = lock.newCondition();
    private static Condition isEmpty = lock.newCondition();

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
                lock.lock();
                try {
                    while (!queue.isEmpty()) {
                        isEmpty.await();
                    }
                    int val = new Random().nextInt();
                    System.out.println("Producing " + val);
                    queue.offer(val);
                    isFull.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
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
                lock.lock();
                try {
                    while (queue.isEmpty()) {
                        isFull.await();
                    }
                    int val = queue.poll();
                    System.out.println("Consuming " + val);
                    isEmpty.signal();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
