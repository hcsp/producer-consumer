package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    static public int sign;
    static public int val;

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        Producer producer = new Producer(lock, condition);
        Consumer consumer = new Consumer(lock, condition);
        sign = 0;

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    public static class Producer extends Thread {
        private ReentrantLock lock;
        private Condition condition;

        public Producer(ReentrantLock lock, Condition condition) {
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (ProducerConsumer2.sign != 0) {
                        condition.await();
                    }
                    val = new Random().nextInt();
                    System.out.println("Producing " + val);
                    ProducerConsumer2.sign = 1;
                    condition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        private ReentrantLock lock;
        private Condition condition;

        public Consumer(ReentrantLock lock, Condition condition) {
            this.lock = lock;
            this.condition = condition;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (ProducerConsumer2.sign != 1) {
                        condition.await();
                    }
                    System.out.println("Consuming " + val);
                    ProducerConsumer2.sign = 0;
                    condition.signal();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
