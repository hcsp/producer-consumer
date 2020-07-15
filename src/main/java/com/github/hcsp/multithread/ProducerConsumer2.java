package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer2 {

    private static int MAX_NUM = 10;
    private static List<Integer> container = new ArrayList<>();

    // 实例化lock
    private static Lock lock = new ReentrantLock();
    private static Condition producer_con = lock.newCondition();
    private static Condition consumer_con = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
//        producer.join();
    }

    public static class Producer extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.lock();
                try {
                    // 如果容器为空，则等待通知
                    while (container.size() == MAX_NUM) {
                        try {
                            producer_con.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 否则继续生产
                    int generatorNum = new Random().nextInt();
                    container.add(generatorNum);
                    System.out.println("Producing" + generatorNum);
                    //通知
                    consumer_con.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.lock();
                try {
                    // 如果容器为空，则等待通知
                    while (container.size() == 0) {
                        try {
                            consumer_con.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 否则继续拿东西
                    int getNum = container.get(0);
                    container.remove(0);
                    System.out.println("Consuming" + getNum);
                    //通知
                    producer_con.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
