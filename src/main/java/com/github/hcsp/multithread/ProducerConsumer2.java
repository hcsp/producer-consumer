package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    // Lock/Condition
    static final ReentrantLock lock = new ReentrantLock();
    static final Condition doneProduced = lock.newCondition();
    static final Condition doneConsumed = lock.newCondition();
    static int product;
    static boolean hasContent = Boolean.FALSE;

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            new Producer().start();
            new Consumer().start();
        }
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            lock.lock();
            try {
                while (hasContent) {
                    doneConsumed.await();
                }
                product = new Random().nextInt();
                System.out.println("Producing " + product);
                hasContent = true;
                doneProduced.signalAll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            lock.lock();
            try {
                while (!hasContent) {
                    doneProduced.await();
                }
                System.out.println("Consuming " + product);
                hasContent = false;
                doneConsumed.signalAll();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    }
}
