package com.github.hcsp.multithread;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ProducerConsumer3 {

    // 实例化lock
    private static Lock lock = new ReentrantLock();
    private static Condition producer_con = lock.newCondition();
    private static Condition consumer_con = lock.newCondition();
    private static BlockingDeque<Integer> queue = new LinkedBlockingDeque<>();

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
                    while (queue.size() > 0) {
                        try {
                            producer_con.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    // 否则继续生产
                    int generatorNum = new Random().nextInt();
                    queue.add(generatorNum);
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
                        while (queue.size() == 0) {
                            try {
                                consumer_con.await();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        // 否则继续拿东西
                        System.out.println("Consuming" + queue.remove());
                        //通知
                        producer_con.signalAll();
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
