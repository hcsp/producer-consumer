package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static final Condition NOT_EMPTY = LOCK.newCondition();
    private static final List<Integer> PRODUCT_LIST = new ArrayList<>(1);

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                LOCK.lock();
                try {
                    if (PRODUCT_LIST.size() >= 1) {
                        NOT_EMPTY.await();
                    }
                    PRODUCT_LIST.add(new Random().nextInt());
                    System.out.println("Producing " + PRODUCT_LIST.get(0));
                    NOT_EMPTY.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    LOCK.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                LOCK.lock();
                try {
                    if (PRODUCT_LIST.size() < 1) {
                        NOT_EMPTY.await();
                    }
                    System.out.println("Consuming " + PRODUCT_LIST.remove(0));
                    NOT_EMPTY.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    LOCK.unlock();
                }
            }
        }
    }
}
