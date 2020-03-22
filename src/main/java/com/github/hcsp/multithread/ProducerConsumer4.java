package com.github.hcsp.multithread;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * 使用Semaphore
 * 使用信号量来代表生产消费的行为，
 * Pv操作，轮流执行
 */
public class ProducerConsumer4 {
    public static void main(String[] args) throws InterruptedException {
        Producer producer = new Producer();
        Consumer consumer = new Consumer();

        producer.start();
        consumer.start();

        producer.join();
        producer.join();
    }


    static Semaphore p = new Semaphore(0);
    static Semaphore c = new Semaphore(1);
    static final  LinkedList<Integer> lists = new LinkedList<>();

    public static class Producer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    c.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int r = new Random().nextInt();
                lists.add(r);
                System.out.println("Producing " + r);
                p.release();
            }
        }
    }

    public static class Consumer extends Thread {
        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    p.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int r = lists.remove(0);
                System.out.println("Consuming " + r);
                c.release();
            }
        }
    }
}
