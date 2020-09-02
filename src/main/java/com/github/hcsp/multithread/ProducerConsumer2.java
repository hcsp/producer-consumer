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

    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition notEmpty = lock.newCondition();
    private static final List<Integer> list = new ArrayList<>(1);

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    if (list.size() >= 1) {
                        notEmpty.await();
                    }
                    list.add(new Random().nextInt());
                    System.out.println("Producing " + list.get(0));
                    notEmpty.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    if (list.size() < 1) {
                        notEmpty.await();
                    }
                    System.out.println("Consuming " + list.remove(0));
                    notEmpty.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}