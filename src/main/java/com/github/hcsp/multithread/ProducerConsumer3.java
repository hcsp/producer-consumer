package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer3 {
    static Lock lock = new ReentrantLock();
    static Condition condition = lock.newCondition();
    static Integer num;

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            int count = 0;
            while (true) {
                if (count == 10) {
                    break;
                }
                lock.lock();
                if (num == null) {
                    num = new Random().nextInt();
                    System.out.println("Producing " + num);
                    count++;
                    condition.signal();
                } else {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                lock.unlock();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            int count = 0;
            while (true) {
                if (count == 10) {
                    break;
                }
                lock.lock();
                if (num != null) {
                    System.out.println("Consuming " + num);
                    num = null;
                    count++;
                    condition.signal();
                } else {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                lock.unlock();
            }
        }
    }
}
