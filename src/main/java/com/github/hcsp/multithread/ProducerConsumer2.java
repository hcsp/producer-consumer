package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* Lock/Condition */
public class ProducerConsumer2 {

    static Lock lock = new ReentrantLock();
    static Condition notEmpty = lock.newCondition();
    static Condition notFull = lock.newCondition();

    public static class ValueObject {
        public static int value = 0;
    }

    public static void main(String[] args) throws InterruptedException {

        Producer producer = new Producer(lock);
        Consumer consumer = new Consumer(lock);

        producer.start();
        consumer.start();
    }

    public static class Producer extends Thread {

        Lock lock;

        Producer(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while (ValueObject.value != 0) {
                        try {
                            notFull.await();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    ValueObject.value = new Random().nextInt();
                    System.out.println("Producing " + ValueObject.value);
                    notEmpty.signal();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public static class Consumer extends Thread {

        Lock lock;

        Consumer(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                for (int i = 0; i < 10; i++) {
                    while (ValueObject.value == 0) {
                        try {
                            notEmpty.await();
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + ValueObject.value);
                    ValueObject.value = 0;
                    notFull.signal();
                }
            } finally {
                lock.unlock();
            }
        }
    }
}
