package com.github.hcsp.multithread;

import java.util.*;


/**
 * 使用wait(),notifyAll()实现
 * 由于要保证生产者生产一个后，消费者马上去消费，所以原问题是一个缓冲池数量为1且生产消费线程也都为1的简单生产者消费者模型
 * 生产者生产一个后，缓冲池便满了，需要等待，此时去唤醒消费者消费
 * 消费者消费一个后，缓冲池便空了，需要等待，此时去唤醒生产者生产
 */
public class ProducerConsumer1 {
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }

    static Random r = new Random();
    static int count = 0;
    static final Object lock = new Object();

    static final  LinkedList<Integer> lists = new LinkedList<>();

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    if (lists.size() == 1) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = new Random().nextInt();
                    lists.add(r);
                    System.out.println("Producing " + r);
                    lock.notify();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                synchronized (lock) {
                    if (lists.size() == 0) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    int r = lists.remove(0);
                    System.out.println("Consuming " + r);
                    lock.notify();
                }
            }
        }
    }
}
