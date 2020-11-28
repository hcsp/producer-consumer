package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        // IntegerContainer integerContainer = new IntegerContainer();
        List<Integer> integerContainer = new ArrayList<>();
        Object lock = new Object();
        Thread producing = new Thread(() -> threadProduce(integerContainer, lock), "Producing");
        producing.start();
        Thread consuming = new Thread(() -> threadConsume(integerContainer, lock), "Consuming");
        consuming.start();
        producing.join();
        consuming.join();
    }

    static void threadProduce(List<Integer> integerContainer, Object lock) {
        for (int i = 0; i < 10; i++) {
            synchronized (lock) {
                while (integerContainer.size() != 0) {
                    wait(lock);
                }
                integerContainer.add(new Random().nextInt(100));
                System.out.printf("%s %d%n",
                        Thread.currentThread().getName(),
                        integerContainer.get(0));
                releaseLock(lock);
            }
        }
    }

    static void threadConsume(List<Integer> integerContainer, Object lock) {
        for (int i = 0; i < 10; i++) {
            synchronized (lock) {
                while (integerContainer.size() == 0) {
                    wait(lock);
                }
                System.out.printf("%s %d%n",
                        Thread.currentThread().getName(),
                        integerContainer.get(0));
                integerContainer.remove(0);
                releaseLock(lock);
            }
        }
    }

    static void wait(Object lock) {
        try {
            System.out.printf("%s null%n", Thread.currentThread().getName());
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void releaseLock(Object lock) {
        try {
            // Thread.sleep(100);
            lock.notify();
            lock.wait(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

