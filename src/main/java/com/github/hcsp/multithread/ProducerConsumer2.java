package com.github.hcsp.multithread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {
    public static int condition = 0; //判断盘子是否为空，1为不空，0为空
    public static int apple = 0;  //盘子中存的数
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition notConsumedYet = lock.newCondition();
    private static Condition notProducedYet = lock.newCondition();

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
            for (int i = 0; i < 10; i++) {
                lock.lock();
                try {
                    while (condition == 1) {
                        try {
                            notConsumedYet.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    apple = (int) (1 + Math.random() * 10);
                    System.out.println("Producing " + apple);
                    condition = 1;
                    notProducedYet.signal();
                }finally {
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
                    while (condition == 0) {
                        try {
                            notProducedYet.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Consuming " + apple);
                    condition = 0;
                    notConsumedYet.signal();
                }finally {
                    lock.unlock();
                }
            }
        }
    }
}
