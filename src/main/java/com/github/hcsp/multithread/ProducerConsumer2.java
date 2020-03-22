package com.github.hcsp.multithread;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 使用Lock/Condition实现
 * 使用syn锁定的话遇到异常，jvm会自动释放锁，但是lock必须手动释放锁，因此经常在finally中进行锁的释放
 */
public class ProducerConsumer2 {
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }


    static final  LinkedList<Integer> lists = new LinkedList<>();

    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    if (lists.size() == 1) {
                        condition.await();
                    }
                    int r = new Random().nextInt();
                    lists.add(r);
                    System.out.println("Producing " + r);
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
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    lock.lock();
                    if (lists.size() == 0) {
                        condition.await();
                    }
                    int r = lists.remove(0);
                    System.out.println("Consuming " + r);
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
