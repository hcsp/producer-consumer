package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static void main(String[] args) {
        // IntegerContainer integerContainer = new IntegerContainer();
        List<Integer> integerContainer = new ArrayList<>();
        final Lock lock = new ReentrantLock();
        final Condition notFull = lock.newCondition();
        final Condition notEmpty = lock.newCondition();
        new Thread(() -> threadProduce(integerContainer, lock, notFull, notEmpty), "Producing").start();
        new Thread(() -> threadConsume(integerContainer, lock, notFull, notEmpty), "Consuming").start();
    }

    static void threadProduce(List<Integer> integerContainer, Lock lock, Condition notFull, Condition notEmpty) {
        for (int i = 0; i < 10; i++) {
            lock.lock();
            try {
                while (integerContainer.size() != 0) {
                    // System.out.printf("%s null%n", Thread.currentThread().getName());
                    notFull.await();
                }
                integerContainer.add(new Random().nextInt(100));
                System.out.printf("%s %d%n",
                        Thread.currentThread().getName(),
                        integerContainer.get(0));
                notEmpty.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    static void threadConsume(List<Integer> integerContainer, Lock lock, Condition notFull, Condition notEmpty) {
        for (int i = 0; i < 10; i++) {
            lock.lock();
            try {
                while (integerContainer.size() == 0) {
                    // System.out.printf("%s null%n", Thread.currentThread().getName());
                    notEmpty.await();
                }
                System.out.printf("%s %d%n",
                        Thread.currentThread().getName(),
                        integerContainer.get(0));
                integerContainer.remove(0);
                notFull.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }
}
